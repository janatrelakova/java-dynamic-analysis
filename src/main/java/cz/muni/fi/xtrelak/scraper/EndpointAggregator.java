package cz.muni.fi.xtrelak.scraper;

import com.github.javaparser.utils.SourceRoot;
import cz.muni.fi.xtrelak.scraper.exporter.EndpointOutput;
import cz.muni.fi.xtrelak.scraper.iterator.ClassMetadata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EndpointAggregator {
    public static List<EndpointOutput> aggregate(List<ClassMetadata> classes, SourceRoot sourceRoot) {
        var endpoints = new ArrayList<EndpointOutput>();

        for (var c : classes) {
            if (c == null) {
                continue;
            }
            var urlPrefix = c.endpointPrefix();
            for (var m : c.methods()) {
                List<String> bodyFields = null;
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
        return endpoints;
    }

    private static List<String> joinBodyFields(Map<String, String> q) {
        return q.keySet().stream().map(e -> e + ": " + q.get(e)).toList();
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
