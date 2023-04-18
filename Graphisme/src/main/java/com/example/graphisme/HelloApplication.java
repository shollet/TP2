package com.example.graphisme;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import server.models.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * La classe HelloApplication hérite de la classe Application et implémente l'interface AutoCloseable.
 * Elle contient 3 attributs, un objet et 7 méthodes. Elle permet de lancer l'interface graphique pour le client et
 * gère l'interaction du client sous forme de programmmation évènementielle. Lors d'un événement, elle sert de tunnel (stream)
 * entre le serveur et le client et affiche au client les réponses du serveur.
 *
 */
public class HelloApplication extends Application implements AutoCloseable {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    /**
     * La classe Cours est l'objet qui va être manipulée dans l'interface graphique afin
     * de bien saisir et récolter toutes les informations demandées sur les cours. Elle contient 3 attributs,
     * un constructeur et 3 getters.
     *
     */
    public class Cours {
        private String code;
        private String cours;
        private String session;

        /**
         * Le constructeur de Cours est appelée lorsqu'on veut créer un nouveau cours de type Cours.
         * Elle change les champs des attributs lorsqu;on appelle celui-ci.
         * @param code Le code du cours
         * @param cours Le nom du cours
         * @param session La session à laquelle le cours se trouve
         */

        public Cours(String code, String cours, String session) {
            this.code = code;
            this.cours = cours;
            this.session = session;
        }

        /**
         * Un getter du champs code qui renvoie la valeur de celle-ci
         * @return La valeur du code
         */

        public String getCode() {
            return code;
        }

        /**
         * Un getter du champs cours qui renvoie la valeur de celle-ci
         * @return La valeur du cours
         */

        public String getCours() {
            return cours;
        }

        /**
         * Un getter du champs session qui renvoie la valeur de celle-ci
         * @return La valeur du session
         */

        public String getSession() {
            return session;
        }
    }

    /**
     * La méthode main prend un paramètre qui est un tableau de String. Ici, nous n'avons pas utilisé le paramètre.
     * Donc la méthode main s'occupe uniquement de lancer l'application pour que l'interface graphique s'affiche.
     * @param args Arguments passés par l'utilisateur
     */

    public static void main(String[] args) {
        try {
            launch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {

        AnchorPane partieGauche = new AnchorPane();
        AnchorPane partieDroite = new AnchorPane();

        BackgroundFill backgroundCouleur = new BackgroundFill(Color.BEIGE, null, null);
        Background background = new Background(backgroundCouleur);
        partieDroite.setBackground(background);
        partieGauche.setBackground(background);

        Label labelDroite = new Label("Formulaire d'inscription");
        labelDroite.setStyle("-fx-font-size: 25;");
        partieDroite.getChildren().add(labelDroite);
        AnchorPane.setTopAnchor(labelDroite, 20.0);
        AnchorPane.setLeftAnchor(labelDroite, 20.0);

        Label labelGauche = new Label("Liste des cours");
        labelGauche.setStyle("-fx-font-size: 25;");
        partieGauche.getChildren().add(labelGauche);
        AnchorPane.setTopAnchor(labelGauche, 20.0);
        AnchorPane.setLeftAnchor(labelGauche, 20.0);

        Separator separator = new Separator();
        separator.setMinWidth(10);
        separator.setMaxWidth(100);
        separator.setStyle("-fx-background-color: black;");

        HBox hbox = new HBox();
        hbox.getChildren().addAll(partieGauche, separator, partieDroite);
        hbox.setStyle("-fx-border-width: 6; -fx-border-color: white;");
        partieGauche.prefWidthProperty().bind(hbox.widthProperty().divide(2));
        partieDroite.prefWidthProperty().bind(hbox.widthProperty().divide(2));

        Scene scene = new Scene(hbox, 600, 400);

        String[] etudiant = new String[] {"Prenom", "Nom", "Email", "Matricule"};

        for (int i = 0 ; i < 4 ; i++) {
            Label label = new Label(etudiant[i]);
            label.setStyle("-fx-font-size: 11;");
            partieDroite.getChildren().add(label);
            AnchorPane.setTopAnchor(label, 60.0 + i*40);
            AnchorPane.setLeftAnchor(label, 15.0);
        }

        AtomicReference<ArrayList<TextField>> data = new AtomicReference<>(buildChoices(partieDroite));

        primaryStage.setTitle("Inscription UdeM");
        primaryStage.setScene(scene);
        primaryStage.show();


        ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(
                "Automne", "Hiver", "Ete"));

        choiceBox.setValue("Hiver");
        partieGauche.getChildren().add(choiceBox);
        AnchorPane.setBottomAnchor(choiceBox, 20.0);
        AnchorPane.setLeftAnchor(choiceBox, 40.0);

        TableView<Cours> tableView = new TableView<>();
        TableColumn<Cours,String> codeCours = new TableColumn<>("Code");
        codeCours.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<Cours,String> nomCours = new TableColumn<>("Cours");
        nomCours.setCellValueFactory(new PropertyValueFactory<>("cours"));

        tableView.getColumns().add(codeCours);
        tableView.getColumns().add(nomCours);

        Button buttonCharger = new Button("charger");
        partieGauche.getChildren().add(buttonCharger);
        AnchorPane.setBottomAnchor(buttonCharger, 20.0);
        AnchorPane.setLeftAnchor(buttonCharger, 170.0);
        buttonCharger.setOnAction((event) -> {
            ArrayList<Course> listeRecue = request(choiceBox.getValue());
            afficher(listeRecue, tableView);
        });

        Button buttonEnvoyer = new Button("envoyer");
        partieDroite.getChildren().add(buttonEnvoyer);
        AnchorPane.setBottomAnchor(buttonEnvoyer, 150.0);
        AnchorPane.setRightAnchor(buttonEnvoyer, 100.0);
        buttonEnvoyer.setOnAction((event) -> {
            Cours coursChoisi = tableView.getSelectionModel().getSelectedItem();
            if (coursChoisi != null) {
                tableView.setStyle("");
                inscrire(data, coursChoisi.getCours(), coursChoisi.getCode(), coursChoisi.getSession(), partieDroite);

            } else {
                tableView.setStyle("-fx-border-color: red;");
                Alert fenetreErreur = new Alert(AlertType.ERROR);
                fenetreErreur.setTitle("Error");
                fenetreErreur.setHeaderText("Error");
                fenetreErreur.setContentText("Le formulaire est invalide\n" + "Vous devez sélectionner un cours!");
                fenetreErreur.showAndWait();
            }
        });


        codeCours.prefWidthProperty().bind(tableView.widthProperty().divide(2));
        nomCours.prefWidthProperty().bind(tableView.widthProperty().divide(2));
        partieGauche.getChildren().add(tableView);

        AnchorPane.setTopAnchor(tableView, 50.0);
        AnchorPane.setLeftAnchor(tableView, 15.0);
        AnchorPane.setRightAnchor(tableView, 15.0);
        AnchorPane.setBottomAnchor(tableView, 50.0);
    }

    /**
     * La fonction buildChoices permet de construire les champs de texte que le client
     * pourra entrer. Elle prend en parametre un AnchorPane utilisé pour constuire ces champs.
     * @param partieDroite Puisqu'on a besoin de le construire au côté droit de l'interface, on spécifie la localisation
     * de la construction de ces champs.
     * @return Une liste de champs de texte
     */
    private ArrayList<TextField> buildChoices(AnchorPane partieDroite) {
        ArrayList<TextField> data = new ArrayList<>();

        for (int j = 0; j < 4; j++) {
            TextField textField = new TextField();
            textField.setPrefHeight(5);
            partieDroite.getChildren().add(textField);
            AnchorPane.setTopAnchor(textField, 60.0 + j*40);
            AnchorPane.setLeftAnchor(textField, 100.0 );
            AnchorPane.setRightAnchor(textField, 40.0);
            data.add(textField);
        }

        return data;

    }

    /**
     * Cette méthode inscrit un étudiant en vérifiant les champs du formulaire donné, puis en envoyant le formulaire
     * via un socket au serveur. Si le formulaire est valide et l'inscription réussie, un message de réussite sera affiché.
     * Sinon, un message d'erreur sera affiché avec les champs invalides soulignés en rouge.
     *
     * @param data Une référence atomique à une liste de champs de texte qui contient les données du formulaire
     * @param cours Le nom du cours pour lequel l'étudiant s'inscrit.
     * @param code Le code du cours pour lequel l'étudiant s'inscrit.
     * @param session La session du cours pour lequel l'étudiant s'inscrit.
     * @param partieDroite L'AnchorPane auquel les champs de texte du formulaire sont ajoutés.
     */

    private void inscrire(AtomicReference<ArrayList<TextField>> data, String cours, String code, String session, AnchorPane partieDroite) {
        try {

            String prenom = data.get().get(0).getText();
            String nom = data.get().get(1).getText();
            String email = data.get().get(2).getText();
            String matricule = data.get().get(3).getText();

            String error = "Le formulaire est invalide\n";
            boolean[] areValid = new boolean[] {false, false, false, false};

            // verif prenom

            if (prenom.length() == 0) {
                error += "Le champ \"Prénom\" est invalide!\n";
            } else {
                areValid[0] = true;
            }

            // verif nom

            if (nom.length() == 0) {
                error += "Le champ \"Nom\" est invalide!\n";
            } else {
                areValid[1] = true;
            }

            // verif email

            boolean isValidEmail = false;
            String[] email1 = email.split("@");
            if (email1.length == 2) {
                String[] email2 = email1[1].split("\\.");
                if (email2.length == 2) {
                    isValidEmail = true;
                }
            }

            if (!isValidEmail) {
                error += "Le champ \"Email\" est invalide!\n";
            } else {
                areValid[2] = true;
            }

            // verif matricule

            char[] matricule1 = matricule.toCharArray();
            int compteur = 0;
            for (int i = 0; i < matricule1.length; i++){
                if (Character.isDigit(matricule1[i])) {
                    compteur++;
                }
            }

            if (compteur == 8) {
                areValid[3] = true;
            } else {
                error += "Le champ \"Matricule\" est invalide!\n";
            }

            // verif every cases

            int i = 0;
            for (boolean validated : areValid) {
                if (validated == true) {
                    i++;
                }
            }

            if (i == areValid.length) {
                this.socket = new Socket("localhost", 1337);
                this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject("INSCRIRE");
                objectOutputStream.flush();

                RegistrationForm formulaire = new RegistrationForm(nom, prenom, email, matricule, new Course(cours, code, session));

                objectOutputStream.writeObject(formulaire);
                objectOutputStream.flush();

                Alert success = new Alert(AlertType.INFORMATION);
                success.setTitle("Message");
                success.setHeaderText("Message");
                success.setContentText("Félicitations! " + prenom + " " + nom + " est inscrit(e)\n"
                        + " avec succès pour le cours " + code + "!");
                success.showAndWait();

                for (TextField textField : data.get()) {
                    partieDroite.getChildren().remove(textField);
                }
                data.set(buildChoices(partieDroite));

                return;

            } else {
                for (int x = 0; x < 4; x++) {
                    if (!areValid[x]) {
                        data.get().get(x).setStyle("-fx-border-color: red;");
                    }
                }


                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText(error);
                alert.showAndWait();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }

                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Cette fonction envoie une requête à un serveur pour charger une liste de cours pour une session donnée.
     * @param session Un String représentant la session pour laquelle les cours doivent être chargés.
     * @return Une liste d'objets Course représentant les cours chargés depuis le serveur. Renvoie null si la requête échoue.
     */

    private ArrayList<Course> request(String session) {
        ArrayList<Course> listeDeCours = null;
        try {
            this.socket = new Socket("localhost", 1337);
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject("CHARGER " + session);
            objectOutputStream.flush();
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            listeDeCours = (ArrayList<Course>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return listeDeCours;
    }
    /**
     * Cette méthode efface les éléments du tableau de cours et la remplit à nouveau de cours de la liste donnée.
     * @param listeDeCours Une liste d'objets Course représentant les cours à afficher dans le tableau.
     * @param tableView Le tableau à remplir avec les cours
     */

    private void afficher(ArrayList<Course> listeDeCours, TableView<Cours> tableView) {
        tableView.getItems().clear();
        for (Course cours : listeDeCours) {
            Cours coursAjoute = new Cours(cours.getCode(), cours.getName(), cours.getSession());
            tableView.getItems().add(coursAjoute);
        }
    }

    @Override
    public void close() throws Exception {
        objectInputStream.close();
        objectOutputStream.close();
        socket.close();
    }
}