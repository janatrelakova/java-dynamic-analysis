package cz.muni.fi.xtrelak.scraper.iterator;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;

import java.util.ArrayList;

public class ClassVisitor extends GenericVisitorAdapter<ClassMetadata, Void> {
    @Override
    public ClassMetadata visit(com.github.javaparser.ast.body.ClassOrInterfaceDeclaration compilationUnit, Void arg) {
        super.visit(compilationUnit, arg);
        var parentNode = compilationUnit.getParentNode().orElseThrow();
        var imports = parentNode.findAll(com.github.javaparser.ast.ImportDeclaration.class).stream().map(NodeWithName::getNameAsString).toList();
        var packageName = parentNode.findFirst(com.github.javaparser.ast.PackageDeclaration.class).orElseThrow().getNameAsString();
        var endpointPrefix = getEndpointPrefix(compilationUnit);
        var methods = new ArrayList<MethodMetadata>();
        var methodVisitor = new MethodVisitor();
        compilationUnit.getMethods().forEach(cu -> methods.add(cu.accept(methodVisitor, null)));
        return new ClassMetadata(compilationUnit.getNameAsString(), packageName, endpointPrefix, imports, methods, null);
    }

    private static String getEndpointPrefix(ClassOrInterfaceDeclaration c) {
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
