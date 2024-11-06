package cz.muni.fi.xtrelak.scraper;

import java.util.List;

public enum HTTP_METHOD {
    GET,
    POST,
    PUT,
    DELETE;

    public static final List<String> validMethods =
            List.of("GetMapping", "PostMapping", "PutMapping", "DeleteMapping", "RequestMapping");

    // Convert the annotation name to standard HTTP methods (GET, POST, PUT, DELETE)
    public static HTTP_METHOD convertAnnotationToHttpMethod(String annotationName) {
        return switch (annotationName) {
            case "GetMapping", "RequestMethod.GET" -> HTTP_METHOD.GET;
            case "PostMapping", "RequestMethod.POST" -> HTTP_METHOD.POST;
            case "PutMapping", "RequestMethod.PUT" -> HTTP_METHOD.PUT;
            case "DeleteMapping", "RequestMethod.DELETE" -> HTTP_METHOD.DELETE;
            default -> null;
        };
    }
}
