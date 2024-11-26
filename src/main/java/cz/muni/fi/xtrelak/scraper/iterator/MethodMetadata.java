package cz.muni.fi.xtrelak.scraper.iterator;

import com.github.javaparser.ast.body.Parameter;
import cz.muni.fi.xtrelak.scraper.Endpoint;

import java.util.List;

public record MethodMetadata(
        List<Endpoint> endpoints,
        String body,
        String formBody,
        List<String> queryParams,
        List<Parameter> param) {
}
