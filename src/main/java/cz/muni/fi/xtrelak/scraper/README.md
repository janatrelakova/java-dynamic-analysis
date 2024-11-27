# Scraper

This project is suited for Spring applications that are using following annotations approach:
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PatchMapping`
  - supported as a method level annotation
  - annotations above support different types of definition:
    - `@GetMapping("/items")`
    - `@GetMapping(value = "/items")`
    - `@GetMapping(value = { "/items" })`
- `@RequestMapping`
  - supported as a class or method level annotation
  - annotations above support different types of definition:
    - `@RequestMapping("/items")`
    - `@RequestMapping(value = "/items")`
    - `@RequestMapping(value = { "/items" })`
    - `@RequestMapping(value = "/items", method = RequestMethod.GET)`
    - `@RequestMapping(value = "/items", method = { RequestMethod.GET })`
- `@RequestParam`
  - supported as a parameter of a method 
- `@PathVariable`
  - supported as a parameter of a method 
- `@RequestBody`
  - supported as a parameter of a method 
- `@ModelAttribute`
  - supported as a parameter of a method
  - ignored as an annotation of a method

For example, for [test project](../../../../../../../test/resources/test-project) the output is:
```yaml
endpoints:
  - url: "/constructs/test"
    httpMethod: null
    body: null
  - url: "/constructs/testAbstract"
    httpMethod: null
    body: null
  - url: "/constructs/testBean"
    httpMethod: null
    body: null
  - url: "/products"
    httpMethod: "GET"
    body: null
  - url: "/products"
    httpMethod: "POST"
    body:
      - "price: double"
      - "name: String"
      - "id: int"
  - url: "/products/valid/{id}"
    httpMethod: "GET"
    body: null
  - url: "/products/{id}"
    httpMethod: "GET"
    body: null
  - url: "/products/{id}"
    httpMethod: "PUT"
    body:
      - "price: double"
      - "name: String"
      - "id: int"
  - url: "/products/{id}"
    httpMethod: "DELETE"
    body: null
  - url: "/users/add"
    httpMethod: "POST"
    body:
      - "name: String"
      - "age: int"
  - url: "/users/greet?name={String}&age={int}"
    httpMethod: "GET"
    body: null
  - url: "/users/hello?name={String}&age={int}"
    httpMethod: "GET"
    body: null
  - url: "/users/list"
    httpMethod: "GET"
    body: null
  - url: "/users/search?username={String}&age={int}"
    httpMethod: "GET"
    body: null
  - url: "/users/updateUserSomehow"
    httpMethod: "PUT"
    body:
      - "name: String"
      - "age: int"
  - url: "/users/updateUserSomehow"
    httpMethod: "PATCH"
    body:
      - "name: String"
      - "age: int"
  - url: "/users/{id}/products"
    httpMethod: "GET"
    body: null
```

## Known limitations
1. The scraper is targeted on Spring projects using annotations listed below.
2. Body and query params imported from outer packages are not recognized.
3. Body and query params imported as aliases may not be recognized.
4. The scraper works strictly statically, so it does not recognize the endpoints 
    that are created dynamically in any way.
5. If there are multiple definitions of body or query params, the result may not be correct.
