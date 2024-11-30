package cz.muni.fi.xtrelak.scraper.iterator;

import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class RecordVisitor extends GenericVisitorAdapter<ClassMetadata, Void> {
    @Override
    public ClassMetadata visit(com.github.javaparser.ast.body.RecordDeclaration record, Void arg) {
        super.visit(record, arg);

        var parentNode = record.getParentNode().orElseThrow();
        var packageName = parentNode.findFirst(com.github.javaparser.ast.PackageDeclaration.class).orElseThrow().getNameAsString();
        var imports = parentNode.findAll(com.github.javaparser.ast.ImportDeclaration.class).stream().map(NodeWithName::getNameAsString).toList();

        var publicFields = new HashMap<String, String>();
        var parameters = record.getParameters();
        parameters.forEach(f -> publicFields.put(f.getNameAsString(), f.getType().asString()));
        return new ClassMetadata(record.getNameAsString(), packageName, null, imports, new ArrayList<>(), publicFields);
    }
}
