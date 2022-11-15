package tech.brito.apiresttexoit.controllers;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import tech.brito.apiresttexoit.core.Constants;
import tech.brito.apiresttexoit.core.DataBuilder;
import tech.brito.apiresttexoit.models.Movie;
import tech.brito.apiresttexoit.services.MovieService;
import tech.brito.apiresttexoit.util.DatabaseCleaner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class PrizeRangeControllerIT {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @LocalServerPort
    private int randomPort;

    @Autowired
    private MovieService movieService;

    @BeforeEach
    public void setUpTest() {
        RestAssured.port = randomPort;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        databaseCleaner.clearTables();
        configureData();
    }

    private void configureData() {
        try {

            var filePath = Constants.getMovieFilePath("src/test/resources/movielist_test.csv");
            var movies = new DataBuilder(filePath, ';').getMovieList();
            movieService.save(movies);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Test
    void whenGetPrizeRangeCheckMinResult() {

        given()
                .when()
                    .get("/prizerange")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("min", hasSize(1))
                    .body("min.producer", hasItems("Joel Silver"))
                    .body("min.interval", hasItems(1))
                    .body("min.previousWin", hasItems(1990))
                    .body("min.followingWin", hasItems(1991));
    }

    @Test
    void whenGetPrizeRangeCheckMaxResult() {

        given()
                .when()
                    .get("/prizerange")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("max", hasSize(1))
                    .body("max.producer", hasItems("Matthew Vaughn"))
                    .body("max.interval", hasItems(13))
                    .body("max.previousWin", hasItems(2002))
                    .body("max.followingWin", hasItems(2015));
    }

    @Test
    void whenGetPrizeRangeCheckMultipleMinResult() {
        var previousWin = movieService.save(new Movie(2020, "Dolittle", "Netflix", "Robert Downey Jr", true));
        var followingWin = movieService.save(new Movie(2021, "Space Jam", "Netflix", "Robert Downey Jr", true));

        given()
                .when()
                    .get("/prizerange")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("min", hasSize(2))
                    .body("min.producer", hasItems("Joel Silver", "Robert Downey Jr"))
                    .body("min.interval", hasItems(1))
                    .body("min.previousWin", hasItems(1990, previousWin.getYear()))
                    .body("min.followingWin", hasItems(1991, followingWin.getYear()));
    }

    @Test
    void whenGetPrizeRangeCheckMultipleMaxResult() {

        var previousWin = movieService.save(new Movie(2009, "Dolittle", "Netflix", "Robert Downey Jr", true));
        var followingWin = movieService.save(new Movie(2022, "Space Jam", "Netflix", "Robert Downey Jr", true));

        given()
                .when()
                    .get("/prizerange")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("max", hasSize(2))
                    .body("max.producer", hasItems("Matthew Vaughn", "Robert Downey Jr"))
                    .body("max.interval", hasItems(13))
                    .body("max.previousWin", hasItems(2002, previousWin.getYear()))
                    .body("max.followingWin", hasItems(2015, followingWin.getYear()));
    }

    @Test
    void whenGetPrizeRangeCheckNewMaxResult() {

        var previousWin = movieService.save(new Movie(2000, "Dolittle", "Netflix", "Robert Downey Jr", true));
        var followingWin = movieService.save(new Movie(2022, "Space Jam", "Netflix", "Robert Downey Jr", true));

        given()
                .when()
                    .get("/prizerange")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("max", hasSize(1))
                    .body("max.producer", hasItems("Robert Downey Jr"))
                    .body("max.interval", hasItems(followingWin.getYear() - previousWin.getYear()))
                    .body("max.previousWin", hasItems(previousWin.getYear()))
                    .body("max.followingWin", hasItems(followingWin.getYear()));
    }

    @Test
    void whenGetPrizeRangeCheckDatabaseNewResult() {
        databaseCleaner.clearTables();

        given()
                .when()
                    .get("/prizerange")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("min", hasSize(0))
                    .body("max", hasSize(0));
    }
}