package cz.muni.fi.xtrelak.scraper;

import cz.muni.fi.xtrelak.scraper.exporter.EndpointOutput;
import cz.muni.fi.xtrelak.scraper.iterator.ClassMetadata;
import cz.muni.fi.xtrelak.scraper.iterator.MethodMetadata;

import java.util.*;

public class EndpointAggregator {
    public static List<EndpointOutput> aggregate(List<ClassMetadata> classes) {
        var endpoints = new ArrayList<EndpointOutput>();

        for (var c : classes) {
            if (c == null) {
                continue;
            }

            var urlPrefix = c.endpointPrefix();
            for (var m : c.methods()) {
                var params = analyzeMethodParameters(m, c, classes);

                for (var e : m.endpoints()) {
                    var qm = new ArrayList<String>();
                    if (params.query != null) {
                        qm.addAll(params.query);
                    }
                    if (m.queryParams() != null) {
                        qm.addAll(m.queryParams());
                    }
                    endpoints.add(new EndpointOutput(e.httpMethod(), urlPrefix + e.uri(), qm, params.body));
                }
            }
        }
        return endpoints;
    }

    private record RequestParameter(List<String> body, List<String> query) {}

    private static RequestParameter analyzeMethodParameters(MethodMetadata method, ClassMetadata cm, List<ClassMetadata> classes) {
        var parameters = method.param();
        if (parameters == null) {
            return new RequestParameter(null, null);
        }


        List<String> body = null;
        List<String> query = null;
        for (var p : parameters) {
            var type = p.getTypeAsString();
            var fields = lookForFields(type, classes, cm);

            var annotations = p.getAnnotations();
            if (annotations.isEmpty() && !fields.isEmpty()) {
                query = joinQueryParamsFields(fields);
            }

            for (var a : annotations) {
                var annotation = a.getNameAsString();
                if (annotation.startsWith("ModelAttribute")) {
                    query = joinQueryParamsFields(fields);
                }

                if (annotation.startsWith("RequestBody")) {
                    body = joinBodyFields(fields);
                }
            }

            if (body != null && query != null) {
                break;
            }
        }

        return new RequestParameter(body, query);
    }

    private static Map<String, String> lookForFields(String typeName, List<ClassMetadata> classes, ClassMetadata cm) {
        // case when the type is not imported but located in the same package
        var localPackage = cm.packageName();
        var localType = classes.stream().filter(c -> c != null && c.name().equals(typeName) && c.packageName().equals(localPackage)).toList();
        if (localType.size() == 1) {
            return localType.getFirst().publicFields();
        }

        // case when type is imported
        var importedPackage = cm.imports().stream().filter(i -> i.endsWith(typeName)).findFirst();
        if (importedPackage.isPresent()) {
            var importString = importedPackage.orElseThrow();
            var pkg = importString.substring(0, importString.lastIndexOf('.'));
            var foundClass = classes.stream().filter(c -> c!= null && c.name().equals(typeName) && c.packageName().equals(pkg)).findFirst();
            if (foundClass.isPresent()) {
                return foundClass.get().publicFields();
            }
        }

        return new HashMap<>();
    }

    private static List<String> joinBodyFields(Map<String, String> q) {
        return q.keySet().stream().map(e -> e + ": " + q.get(e)).toList();
    }

    private static List<String> joinQueryParamsFields(Map<String, String> q) {
        return q.keySet().stream().map(e -> e + "={" + q.get(e) + "}").toList();
    }
}
