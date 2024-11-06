package cz.muni.fi.xtrelak.scraper.iterator;

import com.github.javaparser.ast.body.MethodDeclaration;
import cz.muni.fi.xtrelak.scraper.Endpoint;

import java.util.ArrayList;
import java.util.List;

public interface Annotation {
    List<Endpoint> extractHttpConfiguration(MethodDeclaration method) throws IllegalAccessException;

    // Extract query parameters from method signature
    static List<String> extractQueryParams(MethodDeclaration method) {
        var params = new ArrayList<String>();
        method.getParameters().forEach(parameter -> {
            parameter.getAnnotationByName("RequestParam").ifPresent(_ -> {
                String paramName = parameter.getNameAsString();
                String type = parameter.getType().asString();
                params.add(paramName + "={" + type + "}");
            });
        });
        return params;
    }
}
