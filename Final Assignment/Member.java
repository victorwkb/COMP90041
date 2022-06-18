/*
 * Student name: Kai Bing Goh
 * Student ID: 981436
 * LMS username: kaibingg
 */

/**
 * Class that stores individual members information, can be updated in the list in DataProvider
 */
public class Member {
    private String idNumber;
    private String name;
    private String email;

    // Setter and getter methods

    public void setIdNumber(String anIdNumber) {
        idNumber = anIdNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setName(String aName) {
        name = aName;
    }

    public String getName() {
        return name;
    }

    // for future use
    public void setEmail(String anEmail) {
        email = anEmail;
    }

    public String getEmail() {
        return email;
    }
}
