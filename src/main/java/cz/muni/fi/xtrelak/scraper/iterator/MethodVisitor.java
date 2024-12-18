package cz.muni.fi.xtrelak.scraper.iterator;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import cz.muni.fi.xtrelak.scraper.Endpoint;
import cz.muni.fi.xtrelak.scraper.HTTP_METHOD;
import cz.muni.fi.xtrelak.scraper.iterator.annotation.MarkerAnnotation;
import cz.muni.fi.xtrelak.scraper.iterator.annotation.NormalAnnotation;
import cz.muni.fi.xtrelak.scraper.iterator.annotation.SimpleAnnotation;

import java.util.ArrayList;
import java.util.List;

public class MethodVisitor extends GenericVisitorAdapter<MethodMetadata, Void> {

    @Override
    public MethodMetadata visit(MethodDeclaration method, Void arg) {
        super.visit(method, arg);

        var methodAnnotations = method.getAnnotations();
        var endpoints = new ArrayList<Endpoint>();
        for (AnnotationExpr annotation : methodAnnotations) {
            if (!HTTP_METHOD.validMethods.contains(annotation.getNameAsString())) {
                continue;
            }
            endpoints.addAll(parseEndpointFromAnnotation(annotation, method));
        }

        if (endpoints.isEmpty()) {
            return new MethodMetadata(endpoints, null, null, null, null);
        }
        var queryParams = extractQueryParams(method);
        var body = extractBodyType(method);
        var bodyString = body == null ? null : body.asString();
        var formBody = extractFormBodyType(method);
        var formBodyString = formBody == null ? null : formBody.asString();
        var parameters = method.getParameters().stream().toList();

        return new MethodMetadata(endpoints, bodyString, formBodyString, queryParams, parameters);
    }

    private static List<Endpoint> parseEndpointFromAnnotation(AnnotationExpr annotation, MethodDeclaration method) {
        try {
            if (isSingleMemberAnnotationExpr(annotation)) {
                var obj = new SimpleAnnotation(annotation);
                return obj.extractHttpConfiguration(method);
            } else if (isNormalAnnotationExpr(annotation)) {
                var obj = new NormalAnnotation(annotation);
                return obj.extractHttpConfiguration(method);
            } else if (isMarkerAnnotationExpr(annotation)) {
                var obj = new MarkerAnnotation(annotation);
                return obj.extractHttpConfiguration(method);
            } else {
                throw new IllegalAccessException("Invalid annotation type");
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> extractQueryParams(MethodDeclaration method) {
        var params = new ArrayList<String>();
        method.getParameters().forEach(parameter -> parameter.getAnnotationByName("RequestParam").ifPresent(_ -> {
            String paramName = parameter.getNameAsString();
            String type = parameter.getType().asString();
            params.add(paramName + "={" + type + "}");
        }));
        return params;
    }

    private static Type extractBodyType(MethodDeclaration method) {
        var requestBodyParameter = method.getParameters().stream().filter(parameter -> parameter.getAnnotationByName("RequestBody").isPresent()).findFirst();
        return requestBodyParameter.map(Parameter::getType).orElse(null);
    }

    static Type extractFormBodyType(MethodDeclaration method) {
        var requestBodyParameter = method.getParameters().stream().filter(parameter -> parameter.getAnnotationByName("ModelAttribute").isPresent()).findFirst();
        return requestBodyParameter.map(Parameter::getType).orElse(null);
    }

    private static boolean isNormalAnnotationExpr(AnnotationExpr annotation) {
        return annotation instanceof NormalAnnotationExpr;
    }

    private static boolean isSingleMemberAnnotationExpr(AnnotationExpr annotation) {
        return annotation instanceof SingleMemberAnnotationExpr;
    }

    private static boolean isMarkerAnnotationExpr(AnnotationExpr annotation) {
        return annotation instanceof MarkerAnnotationExpr;
    }
}
