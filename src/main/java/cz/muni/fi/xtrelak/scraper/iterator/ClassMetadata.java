package cz.muni.fi.xtrelak.scraper.iterator;

import java.util.List;

public record ClassMetadata(String name, String packageName, String endpointPrefix, List<String> imports,
                            List<MethodMetadata> methods) {

}
