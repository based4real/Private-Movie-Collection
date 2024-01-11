package pmc.dal.database;

import pmc.be.Movie;
import pmc.dal.exception.DataAccessException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Mock implementering af IDAO interface for Movie entitet.<br>
 * Denne klasse bliver brugt til at generere og hente test data for at simulere DAO operationer
 * uden at interagere med det rigtige datalag.<br>
 * <br>
 * Er beregnet til test formål for applikationen.
 */
public class MovieDAO_Mock implements IDAO<Movie> {
    private static final String POSTER_DIR = "data/posters/test/";

    private List<Movie> movies;
    private List<String> titles;
    private Iterator<String> titleIterator;
    private String[] posterFiles;

    /**
     * Konstruerer en nyt MovieDAO_Mock instans med prægeneret test data.
     */
    public MovieDAO_Mock() {
        movies = new ArrayList<>();
        titles = generateTitles();
        titleIterator = titles.iterator();
        posterFiles = new String[] {
                "aquaman_resized.jpg",
                "barbie_resized.jpg",
                "inception_resized.jpg",
                "lotr_resized.jpg",
                "olsenbanden_resized.jpg"
        };

        movies.add(new Movie(-1, "", "FIRST", generateRandomRating(), generateRandomRating(), "", POSTER_DIR + "madmax_resized.jpg", generateRandomDateTime()));

        for (int i = 0; i < 50; i++) {
            movies.add(generateRandomMovie());
        }

        movies.add(new Movie(-1, "", "LAST", generateRandomRating(), generateRandomRating(), "", POSTER_DIR + "fightclub_resized.jpg", generateRandomDateTime()));
    }

    @Override
    public Optional<Movie> get(long id) throws DataAccessException {
        return Optional.empty();
    }

    @Override
    public List<Movie> getAll() throws DataAccessException {
        return movies;
    }

    @Override
    public Movie add(Movie movie) throws DataAccessException {
        return null;
    }

    @Override
    public boolean update(Movie original, Movie updatedData) throws DataAccessException {
        return false;
    }

    @Override
    public boolean delete(Movie movie) throws DataAccessException {
        return false;
    }

    private Movie generateRandomMovie() {
        String name = titleIterator.next();
        float imdbRating = generateRandomRating();
        float personalRating = generateRandomRating();
        String fileLink = "fileLink_" + name.replaceAll("\\s+", "");
        String posterPath = getRandomPosterPath();
        LocalDateTime lastView = generateRandomDateTime();

        return new Movie(-1, "", name, imdbRating, personalRating, fileLink, posterPath, lastView);
    }

    private List<String> generateTitles() {
        String[] adjectives = {
                "Eternal", "Mysterious", "Forbidden", "Lost", "Hidden",
                "Ancient", "Distant", "Last", "Silent", "Shadowy",
                "Enchanted", "Forgotten", "Mystical", "Secret", "Spectral",
                "Crimson", "Fading", "Whispering", "Shattered", "Uncharted"
        };

        String[] nouns = {
                "Journey", "Island", "Secret", "Explorer", "Treasure",
                "Realm", "Voyage", "Chronicle", "Quest", "Legend",
                "Echo", "Dream", "Horizon", "Destiny", "Path",
                "Empire", "Oasis", "Oracle", "Labyrinth", "Phantom"
        };

        List<String> titles = new ArrayList<>();
        for (String adjective : adjectives){
            for (String noun : nouns) {
                titles.add(adjective + " " + noun);
            }
        }

        Collections.shuffle(titles);
        return titles;
    }

    private LocalDateTime generateRandomDateTime() {
        Random rand = new Random();

        long minDay = LocalDateTime.of(2015, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC);
        long maxDay = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long randDay = minDay + ((long) (rand.nextDouble() * (maxDay - minDay)));
        return LocalDateTime.ofEpochSecond(randDay, 0, ZoneOffset.UTC);
    }

    private float generateRandomRating() {
        Random rand = new Random();
        return 1.0f + (float) (Math.round(rand.nextFloat() * 90) / 10.0);
    }

    private String getRandomPosterPath() {
        Random rand = new Random();
        int index = rand.nextInt(posterFiles.length);
        return POSTER_DIR + posterFiles[index];
    }

    /**
     * Til at se test data i terminal.
     */
    public static void main(String[] args) {
        IDAO<Movie> movieIDAO = new MovieDAO_Mock();

        try {
            for (Movie m : movieIDAO.getAll()) {
                System.out.println(m);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}