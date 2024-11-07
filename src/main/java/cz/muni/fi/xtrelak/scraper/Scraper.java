package cz.muni.fi.xtrelak.scraper;

import com.github.javaparser.utils.SourceRoot;
import cz.muni.fi.xtrelak.scraper.exporter.EndpointOutput;
import cz.muni.fi.xtrelak.scraper.exporter.YamlExporter;
import cz.muni.fi.xtrelak.scraper.iterator.ClassType;
import cz.muni.fi.xtrelak.scraper.iterator.ClassVisitor;
import cz.muni.fi.xtrelak.scraper.iterator.MethodMetadata;

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

        sourceRoot.getCompilationUnits().forEach(cu -> classes.add(cu.accept(classVisitor, null)));

        var endpoints = new ArrayList<EndpointOutput>();

        for (var c : classes) {
            var urlPrefix = c.getEndpointPrefix();
            for (var m : c.getMethods()) {
                for (var e : m.endpoints()) {
                    endpoints.add(new EndpointOutput(e.getHttpMethod(), urlPrefix + e.getUri(), m.queryParams()));
                }
            }
        }

        var exporter = new YamlExporter();
        exporter.export(endpoints);
    }
}
