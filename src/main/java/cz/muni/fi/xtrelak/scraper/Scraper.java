package cz.muni.fi.xtrelak.scraper;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.utils.SourceRoot;
import cz.muni.fi.xtrelak.scraper.exporter.EndpointOutput;
import cz.muni.fi.xtrelak.scraper.exporter.YamlExporter;
import cz.muni.fi.xtrelak.scraper.iterator.ClassMetadata;
import cz.muni.fi.xtrelak.scraper.iterator.ClassVisitor;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Scraper {

    public static void main(String[] args) throws Exception {
        // Path to the root of the project
        String projectPath = args[0];
        ParserConfiguration configuration = new ParserConfiguration();
        configuration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_16); // or your target Java version
        SourceRoot sourceRoot = new SourceRoot(Paths.get(projectPath));
        sourceRoot.setParserConfiguration(configuration);

        // Analysis will be done in src/main/java directory only
        List<ParseResult<CompilationUnit>> cus = sourceRoot.tryToParse("src/main/java");

        var classes = new ArrayList<ClassMetadata>();
        var classVisitor = new ClassVisitor();

        cus.forEach(cu -> cu.ifSuccessful(c -> classes.add(c.accept(classVisitor, null))));

        var endpoints = new ArrayList<EndpointOutput>();

        for (var c : classes) {
            if (c == null) {
                continue;
            }
            var urlPrefix = c.endpointPrefix();
            for (var m : c.methods()) {
                String bodyFields = null;
                String bodyFormFields = null;
                if (m.body() != null) {
                    Map<String, String> q = getBodyParameters(m.body(), c.imports(), c.packageName(), sourceRoot);
                    bodyFields = joinBodyFields(q);
                }

                if (m.formBody() != null) {
                    Map<String, String> q = getBodyParameters(m.formBody(), c.imports(), c.packageName(), sourceRoot);
                    bodyFormFields = joinBodyFormFields(q);
                }

                for (var e : m.endpoints()) {
                    endpoints.add(new EndpointOutput(e.httpMethod(), urlPrefix + e.uri(), m.queryParams(), bodyFields, bodyFormFields));
                }
            }
        }

        var exporter = new YamlExporter();
        exporter.export(endpoints);
    }

    private static String joinBodyFields(Map<String, String> q) {
        return q.keySet().stream().map(e -> e + ": " + q.get(e)).collect(Collectors.joining("\n"));
    }

    private static String joinBodyFormFields(Map<String, String> q) {
        return q.keySet().stream().map(e -> e + "=" + q.get(e)).collect(Collectors.joining("&"));
    }

    private static Map<String, String> getBodyParameters(String body, List<String> imports, String packageName, SourceRoot sr) {
        var imported = imports.stream().filter(i -> i.endsWith(body)).findFirst();
        if (imported.isPresent()) {
            var importString = imported.orElseThrow();
            var pkg = importString.substring(0, importString.lastIndexOf('.'));
            return getFieldsOfClass(pkg, body, sr);
        }

        return getFieldsOfClass(packageName, body, sr);
    }

    private static Map<String, String> getFieldsOfClass(String packageName, String body, SourceRoot sr) {
        var location = "src/main/java/" + packageName;
        var result = new HashMap<String, String>();
        try {
            var classOfBody = sr.tryToParse(location, body + ".java");
            if (classOfBody.isSuccessful()) {
                var type = classOfBody.getResult().get().getPrimaryType();
                if (type.get().isRecordDeclaration()) {
                    var record = type.get().asRecordDeclaration();
                    var parameters = record.getParameters();
                    parameters.forEach(e -> {
                        result.put(e.getNameAsString(), e.getType().asString());
                    });
                } else {
                    type.get().asClassOrInterfaceDeclaration().getFields().forEach(e -> {
                        result.put(e.getVariable(0).getNameAsString(), e.getVariable(0).getType().asString());
                    });
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

}
