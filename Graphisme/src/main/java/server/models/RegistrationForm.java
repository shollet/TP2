package server.models;

import java.io.Serializable;

/**
 * Classe permettant de prendre les informations de l'étudiant et de l'inscrire à un cours choisi par lui
 */
public class RegistrationForm implements Serializable {
    /**
     * String représentant le prenom de l'étudiant
     */
    private String prenom;
    /**
     * String représentant le nom de l'étudiant
     */
    private String nom;
    /**
     * String représentant l'émail de l'étudiant
     */
    private String email;
    /**
     * String représentant la matricule de l'étudiant
     */
    private String matricule;
    /**
     * Objet course qui représente le cours inscris par l'étudiant
     */
    private Course course;

    /**
     *  Constructeur créant une inscription d'un étudiant en prenant ses informations et le cours inscris
     * @param prenom Prénom de l'étudiant
     * @param nom nom de l'étudiant
     * @param email émail de l'étudiant
     * @param matricule matricule à 8 chiffres de l'étudiant
     * @param course cours inscris par l'étudiant
     */

    public RegistrationForm(String prenom, String nom, String email, String matricule, Course course) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.matricule = matricule;
        this.course = course;
    }

    /**
     * Fonction qui retourne le prenom de l'étudiant
     * @return prenom de l'etudiant
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Fonction qui modifie le prenom de l'étudiant
     * @param prenom prenom de l'etudiant
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Fonction qui retourne le nom de l'étudiant
     * @return nom de l' etudiant
     */
    public String getNom() {
        return nom;
    }

    /**
     * Fonction qui modifie le nom de l'étudiant
     * @param nom nom de l'étudiant modifié
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Fonction qui retourne l'email de l'étudiant
     * @return email de l'etudiant
     */
    public String getEmail() {
        return email;
    }

    /**
     * Fonction qui modifie l'email de l'étudiant
     * @param email email de l'étudiant modifié
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Fonction qui retourne le matricule de l'étudiant
     * @return Matricule de l'étudiant
     */

    public String getMatricule() {
        return matricule;
    }

    /**
     * Fonction permettant de modifier le matricule de l'étudiant
     * @param matricule matricule de l'étudiant modifié
     */

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    /**
     * Fonction qui retourne le cours inscrit pour l'etudiant
     * @return Cours inscrit pour l'etudiant
     */

    public Course getCourse() {
        return course;
    }

    /**
     * Fonction qui modifie le cours inscrit pour l'etudiant
     * @param course cours modifié
     */

    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Fonction qui retourne un string contenant les informations de l'inscription de l'etudiant
     * @return String contenant les informations de l'étudiant
     */

    @Override
    public String toString() {
        return "InscriptionForm{" + "prenom='" + prenom + '\'' + ", nom='" + nom + '\'' + ", email='" + email + '\'' + ", matricule='" + matricule + '\'' + ", course='" + course + '\'' + '}';
    }
}
