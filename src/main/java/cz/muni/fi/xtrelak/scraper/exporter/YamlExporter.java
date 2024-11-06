package cz.muni.fi.xtrelak.scraper.exporter;

import cz.muni.fi.xtrelak.scraper.Endpoint;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class YamlExporter {
    // Export the endpoints to a YAML format
    public void export(List<Endpoint> endpoints) {
        // Configure YAML options
        DumperOptions options = new DumperOptions();
        var outputEndpoints = endpoints.stream().map(endpoint -> new EndpointOutput(endpoint.httpMethod(), endpoint.uri(), endpoint.queryParams())).toList();

        var xd = new ArrayList<>(new HashSet<>(outputEndpoints));

        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // For readable block style YAML
        options.setPrettyFlow(true);
        Representer representer = new Representer(options);
        representer.addClassTag(EndpointOutput.class, Tag.MAP);
        // Create a Yaml instance with the specified options
        Yaml yaml = new Yaml(representer, options);

        // Write the YAML output to stdout
        yaml.dump(xd, new OutputStreamWriter(System.out));
    }
}
