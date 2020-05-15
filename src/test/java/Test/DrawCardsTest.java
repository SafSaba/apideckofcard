package Test;

import TestBase.BaseClass;

import endPoints.ApiEndPoints;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojos.Draw;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;

public class DrawCardsTest extends BaseClass {

    private static final Logger logger = LogManager.getLogger(DrawCardsTest.class);


    /**
     * Positive Test Case
     * <p>
     * Draw 5 cards
     * verify that Success is True
     * verify remaining cards are 48
     */

    @Test
    @DisplayName("Draw 5 cards , Validate remaining are 47 ")
    public void drawFiveCards() {

        logger.info(":: Drawing 5 cards");
        Response response =
                given().log().all()
                        .accept(ContentType.JSON)
                        .basePath(ApiEndPoints.NEW_DECK.getEndPoint() + ApiEndPoints.DRAW_CARD.getEndPoint())
                        .queryParam("count", 5)
                        .when()
                        .get();


        logger.info(":: Deserialize JSON into POJO");

        Draw draw = response.jsonPath().getObject("", Draw.class);

//        Assert.assertEquals(response.statusCode(), 200);
//        Assert.assertTrue(draw.isSuccess());
//        Assert.assertEquals(draw.getRemaining(), 47);
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertTrue(draw.isSuccess());
        Assertions.assertEquals(draw.getRemaining(), 47);

    }

    /**
     * Positive Test Case
     * <p>
     * Draw more than  52 cards without jokers
     * verify that Success  is False
     * verify Error message is Not enough cards remaining to draw {int} additional
     */
    @Test
    @DisplayName("Draw more than 52 without jokers, validate error message")
    public void drawFiftyThreeCards() {
        logger.info(":: Drawing 53 cards");
        Response response =
                given().log().all()
                        .accept(ContentType.JSON).
                        basePath(ApiEndPoints.NEW_DECK.getEndPoint() + ApiEndPoints.DRAW_CARD.getEndPoint())
                        .queryParam("count", 53)
                        .when()
                        .get();


        Draw draw = response.jsonPath().getObject("", Draw.class);
        logger.info(":: Validating response");
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertFalse(draw.isSuccess());
        Assertions.assertEquals(draw.getRemaining(), 0);
        Assertions.assertEquals(draw.getError(), "Not enough cards remaining to draw 53 additional");
    }

    /**
     * Positive Test Case
     * <p>
     * Schema JSON Validation
     *
     */
    @Test
    @DisplayName("Validating JSON Schema")
    public void SchemaValidation() {
        given().
                basePath(ApiEndPoints.NEW_DECK.getEndPoint() + ApiEndPoints.DRAW_CARD.getEndPoint())
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(is(200))
                .body(matchesJsonSchemaInClasspath("JsonSchemaDrawDeck.json")).log().all();
    }
    /**
     * Negative Test Case
     * <p>
     * passing decimal value in query Parameters 5.5
     * test Will Fail
     */
    @Test
    @DisplayName("Drawing 5,5 cards from the deck")
    public void DoubleValueQueyParam() {

        logger.info(":: Drawing 5.5 cards ");
        try {
            Response response =
                    given().log().all()
                            .accept(ContentType.JSON)
                            .basePath(ApiEndPoints.NEW_DECK.getEndPoint() + ApiEndPoints.DRAW_CARD.getEndPoint())
                            .queryParam("count", 5.5)
                            .when()
                            .get();
        } catch (Exception e) {
            logger.info(":: An Exception has occurred " + e);
            Assertions.fail("Incorrect query parameters");
        }
    }

}
