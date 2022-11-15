package tech.brito.apiresttexoit.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MOVIE_PRODUCER")
public class MovieProducer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    @Id
    @Column(name = "producer")
    private String producer;

    public MovieProducer() {

    }

    public MovieProducer(Movie movie, String producer) {
        this.movie = movie;
        this.producer = producer;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var other = (MovieProducer) o;
        if (!movie.equals(other.movie)) {
            return false;
        }
        return producer.equals(other.producer);
    }

    @Override
    public int hashCode() {
        int result = movie.hashCode();
        result = 31 * result + producer.hashCode();
        return result;
    }
}
