package pmc.be.rest;

public class TMDBCreditEntity {
    private int gender;
    private int id;
    private String knownForDepartment;
    private String name;
    private String originalName;
    private String profileImage;
    private String characterName;
    private int orderID;

    public TMDBCreditEntity(int gender, int id, String knownForDepartment,
                            String name, String originalName, String profileImage,
                            String characterName, int orderID) {
        this.gender = gender;
        this.id = id;
        this.knownForDepartment = knownForDepartment;
        this.name = name;
        this.originalName = originalName;
        this.profileImage = profileImage;
        this.characterName = characterName;
        this.orderID = orderID;
    }

    public int getGender() {
        return gender;
    }

    public int getID() {
        return id;
    }

    public String getDepartment() {
        return knownForDepartment;
    }

    public String getName() {
        return name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getImage() {
        return profileImage;
    }

    public String getCharacterName() {
        return characterName;
    }

    public int getOrderID() {
        return orderID;
    }

}
