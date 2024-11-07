package cz.muni.fi.xtrelak.scraper.iterator;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import cz.muni.fi.xtrelak.scraper.Endpoint;
import cz.muni.fi.xtrelak.scraper.HTTP_METHOD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleAnnotation implements Annotation {

    private final SingleMemberAnnotationExpr annotation;

    public SimpleAnnotation(AnnotationExpr genericAnnotation) {
        if (!(genericAnnotation instanceof SingleMemberAnnotationExpr a)) {
            throw new IllegalArgumentException();
        }
        this.annotation = a;
    }

    @Override
    public List<Endpoint> extractHttpConfiguration(MethodDeclaration method) {
        var methodName = method.getNameAsString();
        List<String> uri = extractPath();
        HTTP_METHOD httpMethod = HTTP_METHOD.convertAnnotationToHttpMethod(annotation.getNameAsString());
        return uri.stream().map(u -> new Endpoint(httpMethod, u, methodName)).toList();
    }

    // Extract the path value from the annotation
    private List<String> extractPath() {
        var value = annotation.getMemberValue().toString().describeConstable();
        var path = value.orElseThrow();
        if (path.startsWith("{")) {
            return Arrays.stream(
                    path.substring(1, path.length() - 1)
                            .split(","))
                    .map(this::trim)
                    .toList();
        }
        return new ArrayList<>(List.of(trim(path)));
    }

    private String trim(String value) {
        return value.replace("\"", "").replace(" ", "");
    }
}
