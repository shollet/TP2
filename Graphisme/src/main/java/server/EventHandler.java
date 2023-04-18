package server;

/**
 * Interface utilisée dans le fichier principal serveur et participe au travail serveur-client
 */
@FunctionalInterface
public interface EventHandler {
    /**
     * Abstraction procédurale appellant la procedure handle et prend deux parametres
     * @param cmd Parametre décrit plus en profondeur dans le fichier serveur
     * @param arg Parametre décrit plus en profondeur dans le fichier serveur
     */
    void handle(String cmd, String arg);
}
