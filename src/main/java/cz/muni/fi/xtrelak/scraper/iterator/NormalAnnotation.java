package cz.muni.fi.xtrelak.scraper.iterator;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import cz.muni.fi.xtrelak.scraper.Endpoint;
import cz.muni.fi.xtrelak.scraper.HTTP_METHOD;

import java.util.ArrayList;
import java.util.List;

import static cz.muni.fi.xtrelak.scraper.HTTP_METHOD.convertAnnotationToHttpMethod;


public class NormalAnnotation implements Annotation {

    private final NormalAnnotationExpr annotation;


    public NormalAnnotation(AnnotationExpr genericAnnotation) {
        if (!(genericAnnotation instanceof NormalAnnotationExpr a)) {
            throw new IllegalArgumentException();
        }
        this.annotation = a;
    }


    @Override
    public List<Endpoint> extractHttpConfiguration(MethodDeclaration method) throws IllegalAccessException {
        var paths = extractPath();
        var methods = extractMethods();
        var result = new ArrayList<Endpoint>();
        for (var m : methods) {
            for (var p : paths) {
                result.add(new Endpoint(m, p));
            }
        }
        return result;
    }

    private List<HTTP_METHOD> extractMethods() throws IllegalAccessException {
        var methodPair = annotation.getPairs().stream()
                .filter(pair -> pair.getNameAsString().equals("method"))
                .findFirst()
                .map(MemberValuePair::getValue);


        if (methodPair.isEmpty()) {
            var annotationMethod = HTTP_METHOD.convertAnnotationToHttpMethod(annotation.getNameAsString());
            if (annotationMethod != null) {
                return List.of(annotationMethod);
            }
            throw new IllegalAccessException("Invalid HTTP method");
        }

        var methodDefinition = methodPair.get();
        List<HTTP_METHOD> methods;

        if (methodDefinition.isArrayInitializerExpr()) {
            var values = methodDefinition.asArrayInitializerExpr().getValues();
            methods = values.stream().map(v -> convertAnnotationToHttpMethod(v.toString())).toList();
        } else if (methodDefinition.isFieldAccessExpr()) {
            var value = methodDefinition.toString();

            var method = convertAnnotationToHttpMethod(value);
            if (method == null) {
                throw new IllegalAccessException("Invalid HTTP method");
            }
            methods = List.of(method);
        } else {
            var value = methodDefinition.asNameExpr().getNameAsString();
            var method = convertAnnotationToHttpMethod(value);
            if (method == null) {
                throw new IllegalAccessException("Invalid HTTP method");
            }
            methods = List.of(method);
        }
        return methods;
    }

    private List<String> extractPath() {
        var optionalValues = annotation.getPairs().stream()
                .filter(pair -> pair.getNameAsString().equals("value"))
                .map(MemberValuePair::getValue).toList();

        if (optionalValues.isEmpty()) {
            return new ArrayList<>();
        }

        var value = optionalValues.getFirst();
        if (value.isArrayInitializerExpr()) {
            var values = value.asArrayInitializerExpr().getValues();
            return values.stream().map(v -> trim(v.toString())).toList();
        }
        return List.of(trim(value.toString()));
    }

    private String trim(String value) {
        return value.replaceAll("\"", "");
    }
}
