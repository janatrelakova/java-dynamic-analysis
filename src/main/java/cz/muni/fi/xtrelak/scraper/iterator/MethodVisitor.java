package cz.muni.fi.xtrelak.scraper.iterator;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import cz.muni.fi.xtrelak.scraper.Endpoint;
import cz.muni.fi.xtrelak.scraper.HTTP_METHOD;

import java.util.List;

// Visitor to extract endpoint information from method declarations
public class MethodVisitor extends VoidVisitorAdapter<Void> {

    private final List<Endpoint> endpoints;

    public MethodVisitor(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public void visit(MethodDeclaration method, Void arg) {
        super.visit(method, arg);

        var methodAnnotations = method.getAnnotations();

        for (AnnotationExpr annotation : methodAnnotations) {
            if (!HTTP_METHOD.validMethods.contains(annotation.getNameAsString())) {
                continue;
            }

            try {
                if (isSingleMemberAnnotationExpr(annotation)) {
                    var obj = new SimpleAnnotation(annotation);
                    endpoints.addAll(obj.extractHttpConfiguration(method));
                } else if (isNormalAnnotationExpr(annotation)) {
                    var obj = new NormalAnnotation(annotation);
                    endpoints.addAll(obj.extractHttpConfiguration(method));
                } else if (isMarkerAnnotationExpr(annotation)) {
                    var obj = new MarkerAnnotation(annotation);
                    endpoints.addAll(obj.extractHttpConfiguration(method));
                } else {
                    throw new IllegalAccessException("Invalid annotation type");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
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
