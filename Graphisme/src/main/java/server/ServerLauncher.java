package server;
/**
 * Classe permettant le lancement de la connexion entre le serveur et le client
 */
public class ServerLauncher {
    /**
     * Port local de la connexion serveur-client
     */
    public final static int PORT = 1337;

    /**
     * Lancement de la connexion et affichage confirmant la connexion dans l'écran
     * @param args argumemt de base de java
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}