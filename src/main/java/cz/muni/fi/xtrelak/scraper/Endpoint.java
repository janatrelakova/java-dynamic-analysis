package cz.muni.fi.xtrelak.scraper;

import java.util.List;

public class Endpoint {

    private final HTTP_METHOD httpMethod;
    private final String uri;

    public Endpoint(HTTP_METHOD httpMethod, String uri) {
        this.httpMethod = httpMethod;
        this.uri = uri;
    }

    public HTTP_METHOD getHttpMethod() {
        return httpMethod;
    }

    public String getUri() {
        return uri;
    }
}
