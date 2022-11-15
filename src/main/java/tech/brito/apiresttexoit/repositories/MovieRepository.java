package tech.brito.apiresttexoit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tech.brito.apiresttexoit.dtos.PrizeRangeResponse;
import tech.brito.apiresttexoit.models.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    PrizeRangeResponse getPrizeRange();
}
