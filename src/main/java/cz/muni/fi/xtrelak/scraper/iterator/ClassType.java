package cz.muni.fi.xtrelak.scraper.iterator;

import cz.muni.fi.xtrelak.scraper.Endpoint;

import java.util.List;

public class ClassType {
    private final String name;
    private final String packageName;
    private final String endpointPrefix;
    private final List<String> imports;

    public String getEndpointPrefix() {
        return endpointPrefix;
    }

    public List<String> getImports() {
        return imports;
    }

    public List<MethodMetadata> getMethods() {
        return methods;
    }

    private final List<MethodMetadata> methods;


    public ClassType(String name, String packageName, String endpointPrefix, List<String> imports, List<MethodMetadata> methods) {
        this.name = name;
        this.packageName = packageName;
        this.endpointPrefix = endpointPrefix;
        this.imports = imports;
        this.methods = methods;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

}
