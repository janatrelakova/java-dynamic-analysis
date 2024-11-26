package cz.muni.fi.xtrelak.scraper;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import cz.muni.fi.xtrelak.scraper.exporter.YamlExporter;
import cz.muni.fi.xtrelak.scraper.iterator.ClassMetadata;
import cz.muni.fi.xtrelak.scraper.iterator.ClassVisitor;
import cz.muni.fi.xtrelak.scraper.iterator.RecordVisitor;

import java.nio.file.Paths;
import java.util.*;

public class Scraper {

    public static void main(String[] args) throws Exception {
        // Path to the root of the project
        String projectPath = args[0];
        ParserConfiguration configuration = new ParserConfiguration();
        configuration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_16); // or your target Java version
        SourceRoot sourceRoot = new SourceRoot(Paths.get(projectPath));
        sourceRoot.setParserConfiguration(configuration);

        // Analysis will be done in src/main/java directory only
        List<ParseResult<CompilationUnit>> cus = sourceRoot.tryToParse("src/main/java");

        var classes = new ArrayList<ClassMetadata>();
        var classVisitor = new ClassVisitor();
        var recordVisitor = new RecordVisitor();

        cus.forEach(cu -> cu.ifSuccessful(c -> {
            classes.add(c.accept(recordVisitor, null));
            classes.add(c.accept(classVisitor, null));
        }));
        var result = EndpointAggregator.aggregate(classes, sourceRoot);

        var exporter = new YamlExporter();
        exporter.export(result);
    }
}
