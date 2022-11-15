package tech.brito.apiresttexoit.services;

import org.springframework.stereotype.Service;
import tech.brito.apiresttexoit.dtos.PrizeRangeResponse;
import tech.brito.apiresttexoit.models.Movie;
import tech.brito.apiresttexoit.repositories.MovieRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    public PrizeRangeResponse getPrizeRange() {
        return movieRepository.getPrizeRange();
    }

    public List<Movie> save(List<Movie> movies) {
        return movieRepository.saveAll(movies);
    }

    public long count(){
        return movieRepository.count();
    }
}
