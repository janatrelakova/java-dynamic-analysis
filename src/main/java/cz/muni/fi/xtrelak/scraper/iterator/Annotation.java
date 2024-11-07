package cz.muni.fi.xtrelak.scraper.iterator;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import cz.muni.fi.xtrelak.scraper.Endpoint;

import java.util.*;

public interface Annotation {
    List<Endpoint> extractHttpConfiguration(MethodDeclaration method) throws IllegalAccessException;

    // Extract query parameters from method signature
    static List<String> extractQueryParams(MethodDeclaration method) {
        var params = new ArrayList<String>();
        method.getParameters().forEach(parameter -> parameter.getAnnotationByName("RequestParam").ifPresent(_ -> {
            String paramName = parameter.getNameAsString();
            String type = parameter.getType().asString();
            params.add(paramName + "={" + type + "}");
        }));
        return params;
    }

    static Map<String, String> extractBody(MethodDeclaration method) {
        var body = new HashMap<String, String>();
        method.getParameters().forEach(parameter -> parameter.getAnnotationByName("RequestBody").ifPresent(_ -> {
            String paramName = parameter.getNameAsString();
            var type = parameter;
            body.put(paramName, "type");
        }));
        return body;
    }


}
