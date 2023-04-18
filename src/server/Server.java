package server;
import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * La classe Server établit les opérations d'un serveur avec un client pour envoyer et recevoir les informations
 * de celui-ci. Elle est essentielle afin d'enregistrer l'inscription du client en l'ajoutant dans un fichier inscription.txt
 * et permet d'envoyer toutes les informations des cours demandés par le client à partir du fichier cours.txt. Elle contient 
 * 7 attributs, un constructeur et 9 méthodes.
 *
 */

public class Server {
	
	/**
	 * Constante REGISTER_COMMAND est assignée comme un String "INSCRIRE" et qui
     * n'est pas modifiable ni instanciable.
	 */

    public final static String REGISTER_COMMAND = "INSCRIRE";
    
    /** 
     * Constante LOAD_COMMAND est assignée comme un String "CHARGER" et qui
     * n'est pas modifiable ni instanciable.
     */
    
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    /**
     * Constructeur de la classe Server qui a un seul paramètre comme attribut,
     * qui est un int représentant le port. À l'aide de cet attribut,
     * la classe crée ses 3 attributs, qui sont server, qui est un ServerSocket qui prend le port comme
     * paramètre et accueille un seul client. Le second attribut est une ArrayList contenant des objets de
     * la classe EventHandler, qui est une interface créée dans un autre fichier.
     * @param port port dans lequel le serveur et le client se connectent.
     * @throws IOException Exception s'il y a une interruption ou une erreur dans le socket
     */
    
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**Cette abstraction procédurale prend en paramètre un EventHandler et l'ajoute à la ArrayList
     *handlers créée dans Server.
     * @param h Objet de la classe EventHandler
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**Abstraction procédurale qui itère sur tous les éléments de la ArrayList handlers qui contient des
     * EventHandler et les ajoute à l'interface graphique EvenHandler.
     * @param cmd premier paramètre de handle utilisée plus en profondeur dans les autres procedures
     * @param arg deuxieme parametre de handle utilisée plus en profondeur dans les autres procedures
     */

    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Abstraction procédurale qui ne prend aucun paramètre, démarre la connexion entre le client
     * et le serveur. Ensuite, la procédure affiche le client connecté et crée 2 objets:
     * un ObjectInputStream et un ObjectOutputStream. Le premier objet prend en paramètre le input que
     * le client recoit et le second prend en paramètre le output envoyé par le client.
     * Elle termine par appeller la procédure listen() et disconnect().
     */
    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cette abstraction procédurale ne prend aucun parametre. Une variable qui s'appelle line est
     * déclaree. Ensuite, une condition verifie si line est l'input du serveur et la condition verifie
     * si le string line n'est pas vide. Ensuite, un couple nommé parts est créé par la fonction
     * processCommandLine qui prend en parametre line. Ensuite, cmd est assigné à la clé de parts et arg à
     * la valeur de parts.La derniere ligne de la procedure appelle alertHandlers avec les parametres
     * parts et arg.
     * @throws IOException Exception s'il y a une interruption ou une erreur dans le Stream
     * @throws ClassNotFoundException Exception si la classe qu'on essaie de lire n'est pas repéré
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     * Cette fonction cree un tableau de strings nommé parts, qui est créé par le split de l'espace " ".
     * Ensuite, le premier element du tableau sera utilisé pour déclarer la variable cmd.
     * est constituee du string créé par le reste des elements du tableau séparés par " ".
     * @param line String pris en parammetre qui contient un espace
     * @return La fonction termine en retournant un couple avec premier parametre cmd et
     * second parametre args
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Cette abstraction procedurale déconnecte le client en fermant les objets objectOutputStream et
     * objectInputStream créé dans la procedure run(). La procedure termine en fermant la socket client,
     * qui correspondait au socket qui validait le lien client-serveur.
     * @throws IOException Exception s'il y a une interruption ou une erreur dans le stream ou dans le socket
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * Cette abstraction procedurale, appellée handleEvents, comme son nom l'indique, va appliquer la
     * tache appropriée selon l'évènement indiqué. La fonction verifie si les parametres donnés sont
     * donnés dans le but de s'inscrire à un cours à une session donnée ou si le but est de voir les
     * cours est offert pour la session donnée.
     * @param cmd String comparé à la valeur REGISTER_COMMAND qui est egale à "INSCRIRE". Si la valeur
     * est identique, la procedure appelle la fonction handleRegistration et inscrit l'étudiant
     * @param arg String comparé à la valeur LOAD_COMMAND qui est egale à "CHARGER". Si la valeur est
     * identique, la procedure appelle la fonction handleLoadCourses et gère le fichier contenant les cours
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
    	String session = arg;
    	String filePath = String.valueOf(Path.of("src\\server\\data\\cours.txt").toAbsolutePath());
    	List<Course> listeDeCours = new ArrayList<>();

    	try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
    		String line;
    		String[] data;
    		while ((line = br.readLine()) != null) {
    			data = line.split("\t");
    			if (data.length == 3 && data[2].equals(session)) {
    				Course cours = new Course(data[1], data[0], data[2]);
    				listeDeCours.add(cours);
    			}

    		}
    		
    		objectOutputStream.writeObject(listeDeCours);
    		objectOutputStream.flush();
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    	} 
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        
    	String filePath = String.valueOf(Path.of("src\\server\\data\\inscription.txt").toAbsolutePath());
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
    		
    		RegistrationForm inscription = (RegistrationForm) objectInputStream.readObject();
    		
    		String data = inscription.getCourse().getSession() + "\t" + inscription.getCourse().getCode() + "\t"
    				+ inscription.getMatricule() + "\t" + inscription.getNom() + "\t" + inscription.getPrenom() + "\t" 
    				+ inscription.getEmail();
			writer.write(data);
			writer.newLine();
			writer.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
}

