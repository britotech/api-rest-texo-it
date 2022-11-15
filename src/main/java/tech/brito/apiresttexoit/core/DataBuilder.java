package tech.brito.apiresttexoit.core;

import com.opencsv.CSVParserBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import tech.brito.apiresttexoit.dtos.MovieDTO;
import tech.brito.apiresttexoit.models.Movie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class DataBuilder {

    private final String fileName;
    private final Character separator;

    public DataBuilder(String fileName, Character separator) {
        this.fileName = fileName;
        this.separator = separator;
    }

    public List<Movie> getMovieList() throws IOException {
        var reader = Files.newBufferedReader(Paths.get(fileName));
        var csvParser = new CSVParserBuilder().withSeparator(separator).build();
        var moviesDTO = new CsvToBeanBuilder<MovieDTO>(reader).withSeparator(separator).withType(MovieDTO.class).build();
        return moviesDTO.stream().map(m -> m.toEntity()).collect(Collectors.toList());
    }
}
