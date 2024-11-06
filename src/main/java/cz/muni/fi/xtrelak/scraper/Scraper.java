package cz.muni.fi.xtrelak.scraper;

import com.github.javaparser.utils.SourceRoot;
import cz.muni.fi.xtrelak.scraper.exporter.YamlExporter;
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

        var result = new ArrayList<Endpoint>();
        var visitor = new MethodVisitor(result);

        sourceRoot.getCompilationUnits().forEach(cu -> {
            cu.accept(visitor, null);
        });

        var exporter = new YamlExporter();
        exporter.export(result);
    }
}
