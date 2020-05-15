package Test;

import TestBase.BaseClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import endPoints.ApiEndPoints;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import pojos.Deck;
import utilities.JavaUtil;

import java.io.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

public class CreateNewDeckTest extends BaseClass {

    private static final Logger logger = LogManager.getLogger(CreateNewDeckTest.class);


    /**
     * Positive Test Case
     * <p>
     * Create new deck
     * verify that shuffle is false
     * verify remaining cards are 52 without jokers
     *
     *               expected : status code is 200
     *      *                   shuffled is false
     *      *                   remaining is 52
     *      *           actual: verified successfully
     */

    @Test
    @DisplayName("Creating new deck, joker and shuffled are disabled ")
    public void newDeckWithoutJoker() {

        logger.info(" :: Creating new deck of cards");

       Response response =
                given().
                        accept(ContentType.JSON).
                        basePath(ApiEndPoints.NEW_DECK.getEndPoint())
                        .queryParam("jokers_enabled", "false")
                        .when()
                        .get();
        logger.info(" :: Verifying number of cards in the deck");

        response.
                then()
                .assertThat()
                .statusCode(200)
                .and()
                .assertThat()
                .body("shuffled", is(false))
                .and()
                .assertThat()
                .body("remaining", is(52));

    }

    /**
     * Positive Test Case
     * <p>
     * Create a deck with two jokers
     * verify deck is  shuffled
     * verify remaining cards are 54
     *
     * @param status value true to enable joker, false to disable
     * @param joker  by default joker is disabled
     *               <p>
     *               {@code @CsvSource} is an ArgumentsSource which reads
     *               comma-separated values (CSV) from one or more supplied #value CSV lines
     *            expected : status code is 200
     *                       shuffled is true
     *                      remaining is 54
     *           actual: verified successfully
     */

    @DisplayName("Creating new deck with jokers, verifying the number of Deck  ")
    @ParameterizedTest
    @CsvSource({"jokers_enabled, true"})
    public void newDeckWithJoker(String joker, String status) {

        logger.info(" :: Creating new deck with jokers");

        given().
                accept(ContentType.JSON).
                basePath(ApiEndPoints.NEW_DECK.getEndPoint() + ApiEndPoints.SHUFFLE.getEndPoint())
                .queryParam(joker, status)
                .when()
                .get()
                .prettyPeek()


                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .assertThat().body("shuffled", is(true))
                .and()
                .assertThat().body("remaining", equalTo(54))
                .and()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("CreateNewDeckWithJokerSchema.json")).log().all();


    }

    /**
     * Positive Test Case
     *
     * Create more than 20 Decks
     * verify Error message
     *
     *  comma-separated value (CSV) for this test will be provided from /deck_count.csv file
     *
     * @param deck_count Query argument ./api/deck/new/?deck_count=21
     * @param count      The number of decks to be created
     *
     *               --    Successfully verified
     *
     */
    @DisplayName("Verifying the error when creating more than the allowed 20 decks ")
    @ParameterizedTest
    @CsvFileSource(resources = "/deck_count.csv")
    public void createMoreThanAllowedDeck(String deck_count, int count) {
        logger.info(" Creating more than 20 decks ");

        given().
                accept(ContentType.JSON).
                basePath(ApiEndPoints.NEW_DECK.getEndPoint())
                .queryParam(deck_count, count)
                .when()
                .get().prettyPeek()


                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .assertThat().body("success", is(false))
                .and()
                .assertThat().body("error", equalTo("The max number of Decks is 20."))
                .log().ifError();
    }

    /**
     * Positive Test Case
     *
     * Create new Deck without joker. Deserialize JSON response to the Deck POJO class.
     * writing JSON response in File in the resource to use it in the following test to Post
     *
     *
     *
     */
    @DisplayName("Creating new Deck without jokers and writing response body in new_deck.json file")
    @Test
    public void createNewDeckSaveBodyInJsonFile() throws IOException {
        logger.info("Creating new Deck without joker");
        Response response = given()
                .accept(ContentType.JSON)
                .basePath(ApiEndPoints.NEW_DECK.getEndPoint())
                .when()
                .get();


        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        logger.info(":: deserialize response into POJO CLASS");
        Deck deck = response.jsonPath().getObject("", Deck.class);

        logger.info(":: Writing Body in new_deck.json file");
        FileWriter output = null;
        String path = "src/test/resources/new_deck.json";

        try {
            output = new FileWriter(path);
            gson.toJson(deck, output);
            output.flush();
        } catch (IOException e) {
            logger.info(":: An Exception has occurred " + e);
        } finally {
            JavaUtil.closeQuietly(output);
        }

        logger.info(":: Verifying the file /new_deck.json is created " );

        ObjectMapper mapper = new ObjectMapper();
        InputStream is = Deck.class.getResourceAsStream("/new_deck.json");
        Object testObj = mapper.readValue(is, Deck.class);

    }


    /**
     * Negative Test Case
     *
     * Test will Fail
     *
     * Expected status code 201
     * Actual status code 301 Moved Permanently !
     *
     * Post new deck using new_deck.json file
     *
     * Serialization from POJO class to JSON
     */
    @DisplayName("Post new Deck reading from new_deck.json file")
    @Test
    public void postNewDeck() {

        Gson gson = new Gson();
        logger.info(":: Reading from new_deck.json file" );
        FileReader jsonFIle = null;
        String path = "src/test/resources/new_deck.json";
        try {
            jsonFIle = new FileReader(path);
        } catch (FileNotFoundException e) {
            logger.info(":: An Exception has occurred " + e);
        }

        logger.info(" Serialization of JSON into POJO Deck Class " );

        Deck deck = gson.fromJson(jsonFIle, Deck.class);

        given()
                .accept(ContentType.JSON)
                .body(deck)
                .when()
                .post(ApiEndPoints.NEW_DECK.getEndPoint())
                .prettyPeek()
                .then()
                .assertThat()
                .statusCode(is(201))
                .log().all();
    }

    /**
     * Negative Test Case
     *
     * Test will Fail
     *
     *Create new deck with negative number
     * Expected status code 422
     * Actual status code 200
     *
     */
    @DisplayName("Get new Deck with negative number")
    @Test
    public void createNewDeckWithNegativeNumber() {
        logger.info(" :: Creating new deck with query param -5 ");
        given().
                accept(ContentType.JSON).
                basePath(ApiEndPoints.NEW_DECK.getEndPoint() + ApiEndPoints.SHUFFLE.getEndPoint())
                .when()
                .queryParam("deck_count", -5)
                .get()
                .prettyPeek()


                .then()
                .assertThat()
                .statusCode(is(422))
                .and()
                .assertThat().body("success", is(false))
                .log().ifError();

    }




}