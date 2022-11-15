package tech.brito.apiresttexoit.repositories;

import tech.brito.apiresttexoit.dtos.PrizeRangeDTO;
import tech.brito.apiresttexoit.dtos.PrizeRangeResponse;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MovieRepositoryImpl {

    @PersistenceContext
    private EntityManager manager;

    public PrizeRangeResponse getPrizeRange() {

        List<Object[]> result = manager.createNativeQuery(getPrizeRangeSQL()).getResultList();
        if (result.isEmpty()) {
            return new PrizeRangeResponse(Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        }

        List<PrizeRangeDTO> intervals = new ArrayList<>();

        for (var interval : result) {
            intervals.add(new PrizeRangeDTO((String) interval[0],    // producer
                                            (Integer) interval[1],   // interval
                                            (Integer) interval[2],   // previousWin
                                            (Integer) interval[3])); // followingWin
        }

        var intervalMin = intervals.stream().mapToInt(i -> i.interval()).min().getAsInt();
        var intervalMax = intervals.stream().mapToInt(i -> i.interval()).max().getAsInt();

        return new PrizeRangeResponse(intervals.stream().filter(i -> i.interval() == intervalMin).collect(Collectors.toList()),
                                      intervals.stream().filter(i -> i.interval() == intervalMax).collect(Collectors.toList()));
    }

    private String getPrizeRangeSQL() {
        return """
                WITH t_winning_producers(producer, exhibition_year) AS
                (
                	SELECT p.producer,
                	       m.exhibition_year
                	FROM movie m
                	JOIN movie_producer p ON p.movie_id = m.id
                	WHERE winner 	
                ),

                t_producer_rank(producer, exhibition_year, rank) AS
                (
                	SELECT w1.producer,
                	       CAST(w1.exhibition_year AS INTEGER) exhibition_year,
                		   (SELECT COUNT(*)
                		    FROM t_winning_producers w2
                			WHERE w2.producer = w1.producer
                			  AND w2.exhibition_year <= w1.exhibition_year) rank
                	FROM t_winning_producers w1
                    ORDER BY w1.producer ASC, w1.exhibition_year ASC	
                ),

                t_producers_with_multiple_awards(producer, last_award, penultimate_award, interval_between_awards) AS
                (
                	SELECT  r1.producer ,
                			r1.exhibition_year last_award,
                			r2.exhibition_year penultimate_award,
                			r1.exhibition_year - r2.exhibition_year  interval_between_awards
                	FROM t_producer_rank r1
                	JOIN t_producer_rank r2 ON r2.producer = r1.producer
                						   AND r2.rank = r1.rank - 1
                ),

                t_interval(min, max) as
                (
                	SELECT MIN(interval_between_awards) min,
                	       MAX(interval_between_awards) max
                	FROM t_producers_with_multiple_awards
                )

                SELECT p.producer,
                       p.interval_between_awards,
                       p.penultimate_award,
                	   p.last_award                	   
                FROM t_interval i
                JOIN t_producers_with_multiple_awards p ON p.interval_between_awards IN (i.min, i.max)
                ORDER BY p.interval_between_awards, p.penultimate_award                
                """;
    }
}
