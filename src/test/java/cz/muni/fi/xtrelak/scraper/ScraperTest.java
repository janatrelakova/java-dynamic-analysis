package cz.muni.fi.xtrelak.scraper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScraperTest {

    ByteArrayOutputStream outputStream;
    PrintStream originalOutput = System.out;

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOutput);
    }

    @Test
    public void testMain() throws Exception {
        Scraper.main(new String[]{"src/test/resources/test-project"});
        var expected = """
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
                
                """;

        assertEquals(expected, outputStream.toString());
    }
}
