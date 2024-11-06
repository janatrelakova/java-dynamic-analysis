package cz.muni.fi.xtrelak.scraper.exporter;

import cz.muni.fi.xtrelak.scraper.HTTP_METHOD;

import java.util.List;

public class EndpointOutput
{
    public String url;
    public HTTP_METHOD httpMethod;

    public EndpointOutput(HTTP_METHOD httpMethod, String uri, List<String> queryParams) {
        this.httpMethod = httpMethod;
        if (queryParams.isEmpty()) {
            this.url = uri;
        } else {
            this.url = uri + "?" + String.join("&", queryParams);
        }
    }

}
