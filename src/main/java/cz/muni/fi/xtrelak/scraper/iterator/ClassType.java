package cz.muni.fi.xtrelak.scraper.iterator;

import cz.muni.fi.xtrelak.scraper.Endpoint;

import java.util.List;

public class ClassType {
    private final String name;
    private final String packageName;
    private final List<String> imports;

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    private final List<Endpoint> endpoints;


    public ClassType(String name, String packageName, List<String> imports, List<Endpoint> endpoints) {
        this.name = name;
        this.packageName = packageName;
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
