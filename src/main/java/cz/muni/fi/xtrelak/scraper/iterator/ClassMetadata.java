package cz.muni.fi.xtrelak.scraper.iterator;

import java.util.List;
import java.util.Map;

public record ClassMetadata(String name,
                            String packageName,
                            String endpointPrefix,
                            List<String> imports,
                            List<MethodMetadata> methods,
                            Map<String, String> publicFields) {

}
