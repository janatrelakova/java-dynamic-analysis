package cz.muni.fi.xtrelak.scraper.iterator;

import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import cz.muni.fi.xtrelak.scraper.Endpoint;

import java.util.ArrayList;

public class ClassVisitor extends GenericVisitorAdapter<ClassType, Void> {
    @Override
    public ClassType visit(com.github.javaparser.ast.body.ClassOrInterfaceDeclaration compilationUnit, Void arg) {
        super.visit(compilationUnit, arg);
        var parentNode = compilationUnit.getParentNode().orElseThrow();
        var imports = parentNode.findAll(com.github.javaparser.ast.ImportDeclaration.class).stream().map(NodeWithName::getNameAsString).toList();
        var packageName = parentNode.findFirst(com.github.javaparser.ast.PackageDeclaration.class).orElseThrow().getNameAsString();
        var classEndpoints = new ArrayList<Endpoint>();
        var methodVisitor = new MethodVisitor(classEndpoints);
        compilationUnit.getMethods().forEach(cu -> {
            cu.accept(methodVisitor, null);
        });
        return new ClassType(compilationUnit.getNameAsString(), packageName, imports, classEndpoints);
    }
}
