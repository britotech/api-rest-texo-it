package tech.brito.apiresttexoit.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "MOVIE")
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "exhibition_year")
    private int year;
    private String title;
    private String studios;
    @OneToMany(orphanRemoval = true, mappedBy = "movie", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MovieProducer> producers;
    private boolean winner;

    public Movie() {
    }

    public Movie(int year, String title, String studios, String producer, boolean winner) {
        this.year = year;
        this.title = title;
        this.studios = studios;
        this.winner = winner;
        this.producers = List.of(new MovieProducer(this, producer));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<MovieProducer> getProducers() {
        return producers;
    }

    public void setProducers(List<MovieProducer> producers) {
        this.producers = producers;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Movie movie = (Movie) o;
        return id.equals(movie.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
