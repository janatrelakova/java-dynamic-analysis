package cz.muni.fi.xtrelak.scraper.iterator;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import cz.muni.fi.xtrelak.scraper.Endpoint;

import java.util.*;

public interface Annotation {
    List<Endpoint> extractHttpConfiguration(MethodDeclaration method) throws IllegalAccessException;

    // Extract query parameters from method signature
    static List<String> extractQueryParams(MethodDeclaration method) {
        var params = new ArrayList<String>();
        method.getParameters().forEach(parameter -> parameter.getAnnotationByName("RequestParam").ifPresent(_ -> {
            String paramName = parameter.getNameAsString();
            String type = parameter.getType().asString();
            params.add(paramName + "={" + type + "}");
        }));
        return params;
    }

    static Map<String, String> extractBody(MethodDeclaration method) {
        var body = new HashMap<String, String>();
        method.getParameters().forEach(parameter -> parameter.getAnnotationByName("RequestBody").ifPresent(_ -> {
            String paramName = parameter.getNameAsString();
            var type = parameter;
            body.put(paramName, "type");
        }));
        return body;
    }

    static String getEndpointPrefix(MethodDeclaration method) {
        var classDeclaration = method.findAncestor(ClassOrInterfaceDeclaration.class).orElseThrow();
        var classAnnotations = classDeclaration.getAnnotations();
        for (AnnotationExpr annotation : classAnnotations) {
            if (annotation.getNameAsString().equals("RequestMapping")) {
                return extractPath(annotation);
            }
        }
        return "";
    }

    private static String extractPath(AnnotationExpr annotation) {
        if (annotation instanceof SingleMemberAnnotationExpr) {
            return annotation.asSingleMemberAnnotationExpr().getMemberValue().asStringLiteralExpr().getValue();
        }
        if (annotation instanceof NormalAnnotationExpr) {
            var value = annotation.asNormalAnnotationExpr().getPairs().stream()
                    .filter(pair -> pair.getNameAsString().equals("value"))
                    .findFirst()
                    .map(MemberValuePair::getValue);

            if (value.isEmpty()) {
                throw new IllegalArgumentException("Invalid annotation type for RequestMapping on class level");
            }

            var potentialEndpoints = value.get();

            if (potentialEndpoints instanceof StringLiteralExpr) {
                return potentialEndpoints.asStringLiteralExpr().getValue();
            }

            // If RequestMapping defines array inside, we take the first element
            if (potentialEndpoints instanceof ArrayInitializerExpr) {
                var paths = potentialEndpoints.asArrayInitializerExpr().getValues();
                if (paths.isEmpty()) {
                    return "";
                }

                var firstPath = paths.getFirst();
                return firstPath.map(
                        expression -> expression.toString().replaceAll("\"", ""))
                        .orElse("");
            }
        }

        throw new IllegalArgumentException("Invalid annotation type for RequestMapping on class level");
    }
}
