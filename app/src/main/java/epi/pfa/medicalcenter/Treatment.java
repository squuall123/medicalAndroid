package epi.pfa.medicalcenter;

/**
 * Created by squall on 24/05/2018.
 */

class Treatment {
    String titre;
    String contenu;
    String patientId;
    String doctorid;

    public Treatment() {
        this.titre = titre;
        this.contenu = contenu;
        this.patientId = patientId;
        this.doctorid = doctorid;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }
}
