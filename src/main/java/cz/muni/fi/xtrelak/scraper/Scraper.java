package cz.muni.fi.xtrelak.scraper;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.utils.SourceRoot;
import cz.muni.fi.xtrelak.scraper.exporter.EndpointOutput;
import cz.muni.fi.xtrelak.scraper.exporter.YamlExporter;
import cz.muni.fi.xtrelak.scraper.iterator.ClassType;
import cz.muni.fi.xtrelak.scraper.iterator.ClassVisitor;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Scraper {

    public static void main(String[] args) throws Exception {
        // Define the path to your source code directory (absolute path)
        String projectPath = args[0];
        SourceRoot sourceRoot = new SourceRoot(Paths.get(projectPath));

        // Parse all Java files in the directory
        List<ParseResult<CompilationUnit>> cus = sourceRoot.tryToParse("src/main/java");

        var classes = new ArrayList<ClassType>();
        var classVisitor = new ClassVisitor();

        cus.forEach(cu -> cu.ifSuccessful(c -> classes.add(c.accept(classVisitor, null))));

        var endpoints = new ArrayList<EndpointOutput>();

        for (var c : classes) {
            var urlPrefix = c.getEndpointPrefix();
            for (var m : c.getMethods()) {
                String bodyFields = null;
                if (m.body() != null) {
                    List<FieldDeclaration> q = getBodyParameters(m.body(), c.getImports(), c.getPackageName(), sourceRoot);
                    bodyFields = joinBodyFields(q);
                }
                for (var e : m.endpoints()) {
                    endpoints.add(new EndpointOutput(e.getHttpMethod(), urlPrefix + e.getUri(), m.queryParams(), bodyFields));
                }
            }
        }

        var exporter = new YamlExporter();
        exporter.export(endpoints);
    }

    private static String joinBodyFields(List<FieldDeclaration> q) {
        return q.stream().map(FieldDeclaration::getVariables).flatMap(e -> e.getFirst().stream())
                .map(e -> e.getNameAsString() + ": " + e.getType()).collect(Collectors.joining(", "));
    }

    private static List<FieldDeclaration> getBodyParameters(String body, List<String> imports, String packageName, SourceRoot sr) {
        var imported = imports.stream().filter(i -> i.endsWith(body)).findFirst();
        if (imported.isPresent()) {
            var importString = imported.orElseThrow();
            var pkg = importString.substring(0, importString.lastIndexOf('.'));
            var file = body + ".java";
            return getFieldsOfClass(file, pkg, body, sr);
        }

        return getFieldsOfClass(body, packageName, body, sr);
    }

    private static List<FieldDeclaration> getFieldsOfClass(String filename, String packageName, String body, SourceRoot sr) {
        var location = "src/main/java/" + packageName;
        try {
            var classOfBody = sr.tryToParse(location, filename);
            if (classOfBody.isSuccessful()) {
                var parsedClass = classOfBody.getResult().orElseThrow().getClassByName(body).orElseThrow();
                return parsedClass.getFields();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ArrayList<>();
    }

}
