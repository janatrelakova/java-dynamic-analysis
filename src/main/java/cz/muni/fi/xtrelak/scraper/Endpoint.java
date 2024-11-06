package cz.muni.fi.xtrelak.scraper;

import java.util.List;

public record Endpoint(HTTP_METHOD httpMethod, String uri, String methodName, List<String> queryParams) {

}
