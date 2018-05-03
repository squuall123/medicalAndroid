package epi.pfa.medicalcenter;

/**
 * Created by squall on 27/04/2018.
 */

public class Service {
    int id;
    String name;
    String description;
    String phone;

    public Service() {
        this.id = id;
        this.name = name;
        this.description = description;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
