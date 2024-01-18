package pmc.gui.components.dialog.addmovie;

import java.time.LocalDateTime;
import java.util.List;

public record AddMovieData(int tmdbId,
                           String imdbId,
                           String title,
                           float imdbRating,
                           int personalRating,
                           String fileName,
                           String posterPath,
                           LocalDateTime lastSeen,
                           String filePath,
                           String posterUrl,
                           List<Integer> genreIds) { }