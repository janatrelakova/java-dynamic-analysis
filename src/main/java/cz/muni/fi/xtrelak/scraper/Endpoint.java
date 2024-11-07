package cz.muni.fi.xtrelak.scraper;

import java.util.List;

public class Endpoint {

    private final HTTP_METHOD httpMethod;
    private final String uri;
    private final String methodName;
    private List<String> queryParams;


    public Endpoint(HTTP_METHOD httpMethod, String uri, String methodName) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.methodName = methodName;
    }

    public void setQueryParams(List<String> queryParams) {
        this.queryParams = queryParams;
    }

    public HTTP_METHOD getHttpMethod() {
        return httpMethod;
    }

    public String getUri() {
        return uri;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<String> getQueryParams() {
        return queryParams;
    }
}
