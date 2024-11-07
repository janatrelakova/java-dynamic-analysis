package cz.muni.fi.xtrelak.scraper;

import com.github.javaparser.utils.SourceRoot;
import cz.muni.fi.xtrelak.scraper.exporter.YamlExporter;
import cz.muni.fi.xtrelak.scraper.iterator.ClassType;
import cz.muni.fi.xtrelak.scraper.iterator.ClassVisitor;
import cz.muni.fi.xtrelak.scraper.iterator.MethodVisitor;

import java.nio.file.Paths;
import java.util.*;

public class Scraper {

    public static void main(String[] args) throws Exception {
        // Define the path to your source code directory (absolute path)
        String projectPath = args[0];
        SourceRoot sourceRoot = new SourceRoot(Paths.get(projectPath));

        // Parse all Java files in the directory
        sourceRoot.tryToParse("");

        var classes = new ArrayList<ClassType>();
        var classVisitor = new ClassVisitor();

        sourceRoot.getCompilationUnits().forEach(cu -> {
            classes.add(cu.accept(classVisitor, null));
        });

        var result = classes.stream().map(ClassType::getEndpoints).flatMap(Collection::stream).toList();
        var exporter = new YamlExporter();
        exporter.export(result);
    }
}
