package server.models;

import java.io.Serializable;

/**
 *Classe ayant comme attributs des caracteristiques de cours universitaires décrits ci-dessous
 */
public class Course implements Serializable {
    /**
     * Long utilisé dans le fichier serveur
     */
	private long serialVersionUID = 1L;
    /**
     * String représentant le nom du cours
     */
	private String name;
    /**
     * String représentant le code du cours
     */
    private String code;
    /**
     * String représentant la session du cours
     */
    private String session;

    /**
     * Constructeur d'un cours universitaire avec ses attributs principaux
     * @param name nom du cours
     * @param code code du cours
     * @param session session à laquelle le cours est donné
     */
    public Course(String name, String code, String session) {
        this.name = name;
        this.code = code;
        this.session = session;
    }

    /**
     * Fonction qui retourne le nom du cours de l'objet Cours
     * @return nom du cours
     */

    public String getName() {
        return name;
    }

    /**
     * Fonction qui modifie le nom du cours de l'objet Cours
     * @param name Nom du cours modifié
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Fonction qui retourne le code du cours de l'objet Cours
     * @return Code du cours
     */

    public String getCode() {
        return code;
    }

    /**
     * Fonction qui modifie le code du cours de l'objet Cours
     * @param code Code du cours modifié
     */

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Fonction qui retourne la session du cours de l'objet Cours
     * @return Session du cours
     */
    public String getSession() {
        return session;
    }

    /**
     * Fonction qui modifie la session du cours de l'objet Cours
     * @param session Session du cours modifiée
     */

    public void setSession(String session) {
        this.session = session;
    }

    /**
     * Fonction qui retourne un string contenant les informations en lien au cours
     * @return Un string ayant les informations du cours
     */

    @Override
    public String toString() {
        return "Course{" +
                "name=" + name +
                ", code=" + code +
                ", session=" + session +
                '}';
    }
}
