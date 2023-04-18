package server;

/**
 * Interface fonctionnelle qui permet de gérer les intéractions avec la méthode handle.
 * L'interface prend un évènement et stocke les paramètres cmd et arg qui permettront de 
 * se faire executer dans la classe Server.
 *
 */

@FunctionalInterface
public interface EventHandler {
    void handle(String cmd, String arg);
}
