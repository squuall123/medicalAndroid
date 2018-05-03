package epi.pfa.medicalcenter;

/**
 * Created by squall on 02/05/2018.
 */

class Doctor {

    int id;
    String name;
    String email;
    String phone;
    String specialite;

    public Doctor() {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.specialite = specialite;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
}
