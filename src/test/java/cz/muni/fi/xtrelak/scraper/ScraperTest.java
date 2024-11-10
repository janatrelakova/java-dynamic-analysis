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
                    formBody: null
                  - url: "/constructs/testAbstract"
                    httpMethod: null
                    body: null
                    formBody: null
                  - url: "/constructs/testBean"
                    httpMethod: null
                    body: null
                    formBody: null
                  - url: "/products"
                    httpMethod: "GET"
                    body: null
                    formBody: null
                  - url: "/products"
                    httpMethod: "POST"
                    body:
                    - "price: double"
                    - "name: String"
                    - "id: int"
                    formBody: null
                  - url: "/products/{id}"
                    httpMethod: "GET"
                    body: null
                    formBody: null
                  - url: "/products/{id}"
                    httpMethod: "PUT"
                    body:
                    - "price: double"
                    - "name: String"
                    - "id: int"
                    formBody: null
                  - url: "/products/{id}"
                    httpMethod: "DELETE"
                    body: null
                    formBody: null
                  - url: "/users/add"
                    httpMethod: "POST"
                    body:
                    - "name: String"
                    - "age: int"
                    formBody: null
                  - url: "/users/greet"
                    httpMethod: "GET"
                    body: null
                    formBody: "name=String&age=int"
                  - url: "/users/hello"
                    httpMethod: "GET"
                    body: null
                    formBody: "name=String&age=int"
                  - url: "/users/list"
                    httpMethod: "GET"
                    body: null
                    formBody: null
                  - url: "/users/search?username={String}&age={int}"
                    httpMethod: "GET"
                    body: null
                    formBody: null
                  - url: "/users/updateUserSomehow"
                    httpMethod: "PUT"
                    body:
                    - "name: String"
                    - "age: int"
                    formBody: null
                  - url: "/users/updateUserSomehow"
                    httpMethod: "PATCH"
                    body:
                    - "name: String"
                    - "age: int"
                    formBody: null
                  - url: "/users/{id}/products"
                    httpMethod: "GET"
                    body: null
                    formBody: null
                
                """;

        assertEquals(expected, outputStream.toString());
    }
}
