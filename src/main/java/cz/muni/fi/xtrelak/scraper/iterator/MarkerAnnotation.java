package cz.muni.fi.xtrelak.scraper.iterator;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import cz.muni.fi.xtrelak.scraper.Endpoint;
import cz.muni.fi.xtrelak.scraper.HTTP_METHOD;

import java.util.List;

public class MarkerAnnotation implements Annotation {
    private final MarkerAnnotationExpr annotation;

    public MarkerAnnotation(AnnotationExpr genericAnnotation) {
        if (!(genericAnnotation instanceof MarkerAnnotationExpr a)) {
            throw new IllegalArgumentException();
        }
        this.annotation = a;
    }

    @Override
    public List<Endpoint> extractHttpConfiguration(MethodDeclaration method) throws IllegalAccessException {
        var path = Annotation.getEndpointPrefix(method);
        var httpMethod = HTTP_METHOD.convertAnnotationToHttpMethod(annotation.getNameAsString());
        return List.of(new Endpoint(httpMethod, path, method.getNameAsString()));
    }
}
