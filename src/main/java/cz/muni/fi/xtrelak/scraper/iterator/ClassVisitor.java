package cz.muni.fi.xtrelak.scraper.iterator;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import cz.muni.fi.xtrelak.scraper.Endpoint;

import java.util.ArrayList;

public class ClassVisitor extends GenericVisitorAdapter<ClassType, Void> {
    @Override
    public ClassType visit(com.github.javaparser.ast.body.ClassOrInterfaceDeclaration compilationUnit, Void arg) {
        super.visit(compilationUnit, arg);
        var parentNode = compilationUnit.getParentNode().orElseThrow();
        var imports = parentNode.findAll(com.github.javaparser.ast.ImportDeclaration.class).stream().map(NodeWithName::getNameAsString).toList();
        var packageName = parentNode.findFirst(com.github.javaparser.ast.PackageDeclaration.class).orElseThrow().getNameAsString();
        var endpointPrefix = getEndpointPrefix(compilationUnit);
        var classEndpoints = new ArrayList<Endpoint>();
        var methodVisitor = new MethodVisitor(classEndpoints);
        compilationUnit.getMethods().forEach(cu -> {
            cu.accept(methodVisitor, null);
        });
        classEndpoints.forEach(endpoint -> endpoint.setUri(endpointPrefix + endpoint.getUri()));
        return new ClassType(compilationUnit.getNameAsString(), packageName, endpointPrefix, imports, classEndpoints);
    }

    static String getEndpointPrefix(ClassOrInterfaceDeclaration c) {
        var classAnnotations = c.getAnnotations();
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
