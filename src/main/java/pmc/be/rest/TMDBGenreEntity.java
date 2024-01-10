package pmc.be.rest;

public class TMDBGenreEntity {
    private int id;
    private String name;

    public TMDBGenreEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }
}
