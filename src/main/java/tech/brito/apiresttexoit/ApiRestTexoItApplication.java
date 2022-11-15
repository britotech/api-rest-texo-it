package tech.brito.apiresttexoit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tech.brito.apiresttexoit.core.Constants;
import tech.brito.apiresttexoit.core.DataBuilder;
import tech.brito.apiresttexoit.services.MovieService;

import java.io.IOException;

@SpringBootApplication
public class ApiRestTexoItApplication implements CommandLineRunner {

    private final MovieService movieService;

    @Value(value = "${movies-file-name}")
    private String moviesFileName;

    public ApiRestTexoItApplication(MovieService movieService) {
        this.movieService = movieService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiRestTexoItApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {

            if (StringUtils.isBlank(moviesFileName) && args.length == 0) {
                return;
            }

            if (args.length == 0) {
                generateDatabaseRecords(Constants.getMovieFilePath(moviesFileName));
                return;
            }

            for (var filePath : args) {
                generateDatabaseRecords(filePath);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void generateDatabaseRecords(String filePath) throws IOException {
        System.out.println("************ filePath -> " + filePath);
        var movies = new DataBuilder(filePath, ';').getMovieList();
        movieService.save(movies);
    }
}
