package cz.muni.fi.xtrelak.scraper.iterator;

import cz.muni.fi.xtrelak.scraper.Endpoint;

import java.util.List;

public class ClassType {
    private final String name;
    private final String packageName;
    private final String endpointPrefix;
    private final List<String> imports;
    private final List<Endpoint> endpoints;

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public ClassType(String name, String packageName, String endpointPrefix, List<String> imports, List<Endpoint> endpoints) {
        this.name = name;
        this.packageName = packageName;
        this.endpointPrefix = endpointPrefix;
        this.imports = imports;
        this.endpoints = endpoints;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

}
