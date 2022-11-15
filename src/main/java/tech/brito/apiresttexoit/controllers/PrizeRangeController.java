package tech.brito.apiresttexoit.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.brito.apiresttexoit.dtos.PrizeRangeResponse;
import tech.brito.apiresttexoit.services.MovieService;

@RestController
@RequestMapping("/prizerange")
public class PrizeRangeController {

    private final MovieService movieService;

    public PrizeRangeController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public PrizeRangeResponse getPrizeRange() {
        var prizeRange = movieService.getPrizeRange();
        return prizeRange;
    }
}
