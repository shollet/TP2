package client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import server.models.*;

/**
 * Cette classe crée un client, qui est connecté au fichier Server et simule l'inscription d'un étudiant
 * à des cours choisis offerts à une session donnée.
 */

public class Client implements AutoCloseable {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private ArrayList<Course> listeDeCours;
    private RegistrationForm formulaire;
    private Scanner scanner;

    /**
     * Ce constructeur prend en compte deux paramètres, mais ne crée pas d'attributs les concernant.
     * Le constructeur construie tout simplement un scanneur utilisé dans les procédures suivantes.
     * @param host String correspondant au nom du host du serveur.
     * @param port Int correspondant au numéro du port du serveur.
     */
    
    public Client(String host, int port) {
    	this.scanner = new Scanner(System.in);
	}
    
    /**
     * Dans ce main, le main crée un client avec les valeurs classiques d'un serveur local et appelle
     * la fonction run() créée ci-dessous.
     * @param args argument mis par defaut par le language de programmation.
     */

	public static void main(String[] args) {
        try (Client client = new Client("localhost", 1337)) {
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
    }
	
	/**
     * Cette abstraction procedurale commence par intier le contact entre le fichier client et le fichier
     * server. Ensuite, la fonction affiche un message de bienvenue au client et lui demande la session
     * où il veut s'inscrire. Ensuite, le programme propose au client s'il veut s'inscrire à des cours pour
     * la session indiquee ou s'il veut voir les cours offerts à une autre session.
     * @throws IOException Exception s'il y a une interruption ou une erreur dans le socket ou dans le stream
     * @throws ClassNotFoundException Exception si la classe qu'on essaie de lire n'est pas repéré
     */


    public void run() throws IOException, ClassNotFoundException {
    	boolean running = true;
    	
    	System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM***");
        while (running) {
        	
            System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours: ");
            System.out.print("1. Automne\n" + "2. Hiver\n" + "3. Ete\n" + "> Choix: ");
            String session = null;
            switch (scanner.nextInt()) {
                case 1 -> session = "Automne";
                case 2 -> session = "Hiver";
                case 3 -> session = "Ete";
                default -> System.out.println("La donnee entree ne corrrespond pas aux choix demandes");
            }
            
            if (session != null) {
            	this.socket = new Socket("localhost", 1337);
                this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            	request(session);
            	while (running) {
            		System.out.println("Les cours offerts pendant la session d'" + session + " sont:");
            		String line;
            		int compteur = 1;
            		for (Course cours : listeDeCours){
            			line = compteur + ". " + cours.getCode() + "\t" + cours.getName();
            			System.out.println(line);
            			compteur++;
            		}
            		System.out.print("> Choix:\n" + "1. Consulter les cours offerts pour une autre session\n" + "2. Inscription à un cours\n" + "> Choix: ");
            		String result = null;
            		switch (scanner.nextInt()) {
            			case 1 -> result = "break";
            			case 2 ->  result = "INSCRIRE";
            			default -> System.out.println("La donnee entree ne corrrespond pas aux choix demandes");
            		};
            		
            		if (result == "INSCRIRE") {
            			this.socket = new Socket("localhost", 1337);
                        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            			inscription(result);
            			if (formulaire != null) {
            				objectOutputStream.writeObject(formulaire);
                			objectOutputStream.flush();
                			running = false;
            			}
            		} else if (result == "break") {
            			break;
            		}
            	}
            }
        } 
    }
    
    /**
     * Cette abstraction procedurale permet à l'étudiant de s'inscrire à un cours et lui demande de
     * rentrer son prenom, son nom, son email, son matricule et le code du cours. Le programme verifie
     * si l'émail et le matricule entré sont valides et si le code du cours est en lien avec un cours
     * dans la session donnée.Le programme finit en validant l'inscription de l'étudiant au cours.
     * @param arg argument qu'on envoie au serveur pour l'inscription
     * @throws IOException Exception s'il y a une interruption ou une erreur dans le stream
     */

    public void inscription(String arg) throws IOException {
    	objectOutputStream.writeObject(arg);
    	objectOutputStream.flush();
    	
    	System.out.print("Veuillez saisir votre prénom: ");
    	String prenom = scanner.next();
    	System.out.print("Veuillez saisir votre nom: ");
    	String nom = scanner.next();
    	String email = null;
    	boolean isValidEmail = false;
    	boolean isValidMatricule = false;
    	while (!isValidEmail) {
    		System.out.print("Veuillez saisir votre email: ");
    		email = scanner.next();
    		String[] email1 = email.split("@");
    		if (email1.length == 2) {
    			String[] email2 = email1[1].split("\\.");
    			if (email2.length == 2) {
    				isValidEmail = true;
    			} else {
    				System.out.println("Erreur email, il manque un \".\"");
    			}
    		} else {
    			System.out.println("Erreur email, il manque un @");
    		}
    	} 

    	String matricule = null;

    	while (!isValidMatricule) {
    		System.out.print("Veuillez saisir votre matricule: ");
    		matricule = scanner.next();
    		char[] matricule1 = matricule.toCharArray();
    		int j = 0;
    		for (int i = 0; i < matricule1.length; i++){
    			if (Character.isDigit(matricule1[i])) {
    				j++;
    			}
    		}

    		if (j != 8) {
    			System.out.println("Erreur matricule");
    		} else {
    			isValidMatricule = true;
    		}
    	}

    	String code = null;
    	Course coursInscrit = null;
    	boolean isValidCode = false;

    	while (!isValidCode) {
    		System.out.print("Veuillez saisir le code du cours: ");
    		code = scanner.next();
    		int compteur = 0;
    		for (Course cours : listeDeCours) {
    			if (code.equals(cours.getCode())) {
    				coursInscrit = new Course(cours.getName(), cours.getCode(), cours.getSession());
    				isValidCode = true;
    			} else {
    				compteur++;
    			}
    		}

    		if (compteur == listeDeCours.toArray().length){
    			System.out.println("Code introuvable dans la session");
    		}
    	}

    	this.formulaire = new RegistrationForm(prenom, nom, email, matricule, coursInscrit);
    	System.out.println("Félicitations! Inscription réussie de " + prenom + " au cours " + code);
    }
    
    /**
     * Cette abstraction procédurale envoie la requete à appliquer par le serveur en utilisant
     * la procedure EventHanlder et processCommandLine.
     * @param session parametre representant la session dont le client est intéressé et est envoyée 
     * à EventHandler dans le fichier Server.               
     * @throws IOException Exception s'il y a une interruption ou une erreur dans le stream
     * @throws ClassNotFoundException Exception si la classe qu'on essaie de lire n'est pas repéré
     */

	public void request(String session) throws IOException, ClassNotFoundException {
        objectOutputStream.writeObject("CHARGER " + session);
        objectOutputStream.flush();
        
        this.listeDeCours = (ArrayList<Course>) objectInputStream.readObject();
      
    }

	/**
     * Cette abstraction procedurale ferme les objets objectInputStream, objectOutputStream et
     * socket et termine le contact entre le client et le serveur.
     * @throws Exception Exception pour tout type d'exceptions lors de la fermeture du stream ou du socket
     */

	@Override
	public void close() throws Exception {
		objectInputStream.close();
		objectOutputStream.close();
		socket.close();
	}
}
