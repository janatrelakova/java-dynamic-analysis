package cz.muni.fi.xtrelak.scraper.exporter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.util.Arrays;
import java.util.List;

public class YamlExporter {
    public void export(List<EndpointOutput> endpoints) {
        endpoints.sort((a, b) -> {
            if (a.url.equals(b.url)) {
                return a.httpMethod.compareTo(b.httpMethod);
            }
            return a.url.compareTo(b.url);
        });
        printAsYAML(endpoints);

    }

    private static void printAsYAML(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.findAndRegisterModules();

            String yamlOutput = mapper.writeValueAsString(object);

            var builder = new StringBuilder();
            builder.append("endpoints:\n");
            var lines = Arrays.stream(yamlOutput.split("\n")).toList();
            lines = lines.subList(1, lines.size());
            for (String line : lines) {
                builder.append("  ").append(line).append("\n");
            }
            System.out.println(builder);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
