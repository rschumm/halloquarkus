package ch.schumm.halloquarkus;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class HalloResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hallo")
          .then()
             .statusCode(200)
             .body(is("Hoi zäme uf dä ganze Wält!"));
    }


    @Test
    public void testSaliEndpoint() {
        given()
          .when().get("/hallo/sali/Yvonne")
          .then()
             .statusCode(200)
             .body(is("Hoi, Yvonne"));
    }

}