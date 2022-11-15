package tech.brito.apiresttexoit.dtos;

import tech.brito.apiresttexoit.models.Movie;
import tech.brito.apiresttexoit.models.MovieProducer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MovieDTO {

    private int year;
    private String title;
    private String studios;
    private String producers;
    private boolean winner;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStudios() {
        return studios;
    }

    public void setStudios(String studios) {
        this.studios = studios;
    }

    public String getProducers() {
        return producers;
    }

    public void setProducers(String producers) {
        this.producers = producers;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public Movie toEntity() {
        var movie = new Movie();
        movie.setYear(year);
        movie.setTitle(title);
        movie.setStudios(studios);
        movie.setWinner(winner);
        movie.setProducers(extractProducerNames().stream()
                                                 .map(n -> new MovieProducer(movie, n.trim()))
                                                 .collect(Collectors.toList()));

        return movie;
    }

    private List<String> extractProducerNames() {
        producers = producers.replace(", and ", ",");
        producers = producers.replace(" and ", ",");

        return Arrays.asList(producers.split(","));
    }
}
