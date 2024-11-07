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
                """;

        assertEquals(expected, outputStream.toString());
    }
}
