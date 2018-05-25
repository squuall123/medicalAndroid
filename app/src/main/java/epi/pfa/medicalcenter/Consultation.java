package epi.pfa.medicalcenter;

/**
 * Created by squall on 24/05/2018.
 */

public class Consultation {

    String date;
    String patient;
    String time;
    String description;

    public Consultation() {
        this.date = date;
        this.patient = patient;
        this.time = time;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
