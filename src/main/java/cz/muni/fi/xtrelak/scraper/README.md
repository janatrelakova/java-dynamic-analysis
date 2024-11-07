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
- body: null
  formBody: null
  httpMethod: GET
  url: /products
- body: |-
    name: String
    price: double
  formBody: null
  httpMethod: POST
  url: /products
- body: null
  formBody: null
  httpMethod: GET
  url: /products/{id}
- body: |-
    name: String
    price: double
  formBody: null
  httpMethod: PUT
  url: /products/{id}
- body: null
  formBody: null
  httpMethod: DELETE
  url: /products/{id}
- body: |-
    name: String
    age: int
  formBody: null
  httpMethod: POST
  url: /users/add
- body: null
  formBody: name=String&age=int
  httpMethod: GET
  url: /users/greet
- body: null
  formBody: name=String&age=int
  httpMethod: GET
  url: /users/hello
- body: null
  formBody: null
  httpMethod: GET
  url: /users/list
- body: null
  formBody: null
  httpMethod: GET
  url: /users/search?username={String}&age={int}
- body: |-
    name: String
    age: int
  formBody: null
  httpMethod: PUT
  url: /users/updateUserSomehow
- body: |-
    name: String
    age: int
  formBody: null
  httpMethod: PATCH
  url: /users/updateUserSomehow
```