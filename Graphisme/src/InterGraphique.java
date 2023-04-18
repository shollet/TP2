import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class InterGraphique extends Application {
    @Override
    public void start(Stage primaryStage) {
        AnchorPane leftSide = new AnchorPane();
        AnchorPane rightSide = new AnchorPane();

        BackgroundFill BackGroundColor = new BackgroundFill(Color.BEIGE, null, null);
        Background background = new Background(BackGroundColor);
        leftSide.setBackground(background);
        rightSide.setBackground(background);

        Label right = new Label("Formulaire d'inscription");
        right.setStyle("-fx-font-size: 21;");
        rightSide.getChildren().add(right);
        AnchorPane.setTopAnchor(right, 20.0);
        AnchorPane.setLeftAnchor(right, 45.0);


        Label left = new Label("Liste des cours");
        left.setStyle("-fx-font-size: 20;");
        leftSide.getChildren().add(left);
        AnchorPane.setTopAnchor(left, 20.0);
        AnchorPane.setLeftAnchor(left, 70.0);


        Separator separator = new Separator();
        separator.setMinWidth(5);
        separator.setMaxWidth(5);
        separator.setStyle("-fx-background-color: white;");

        HBox hbox = new HBox();
        hbox.getChildren().addAll(leftSide, separator, rightSide);
        hbox.setStyle("-fx-border-width: 6; -fx-border-color: white;");

        leftSide.prefWidthProperty().bind(hbox.widthProperty().divide(2));
        rightSide.prefWidthProperty().bind(hbox.widthProperty().divide(2));

        Scene scene = new Scene(hbox, 600, 400);

        ArrayList<String> etudiant = new ArrayList<>();
        etudiant.add("PrÃ©nom");
        etudiant.add("Nom");
        etudiant.add("Email");
        etudiant.add("Matricule");
        int i;
        for (i = 0 ; i < 4 ; i++) {
            Label test2 = new Label(etudiant.get(i));
            test2.setStyle("-fx-font-size: 11;");
            rightSide.getChildren().add(test2);
            AnchorPane.setTopAnchor(test2, 75.0 + i*30);
            AnchorPane.setLeftAnchor(test2, 15.0);
        }
        for (i = 0; i != 4; i++) {

            TextField inputField = new TextField();
            inputField.setPromptText("Entrez votre texte ici");
            inputField.setPrefHeight(5);
            rightSide.getChildren().add(inputField);
            AnchorPane.setTopAnchor(inputField, 70.0 + i*30);
            AnchorPane.setLeftAnchor(inputField, 100.0 );
            AnchorPane.setRightAnchor(inputField, 30.0);
        }

        primaryStage.setTitle("Inscription Udem");
        primaryStage.setScene(scene);
        primaryStage.show();

        Button sendButton = new Button("Envoyer");
        rightSide.getChildren().add(sendButton);
        AnchorPane.setBottomAnchor(sendButton, 190.0);
        AnchorPane.setRightAnchor(sendButton, 90.0);

        Button loadButton = new Button("Charger");
        leftSide.getChildren().add(loadButton);
        AnchorPane.setBottomAnchor(loadButton, 20.0);
        AnchorPane.setLeftAnchor(loadButton, 170.0);

        ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(
                "Automne", "Hiver", "Ete"
        ));
        leftSide.getChildren().add(choiceBox);
        AnchorPane.setBottomAnchor(choiceBox, 20.0);
        AnchorPane.setLeftAnchor(choiceBox, 20.0);

        TableView<String> tableView = new TableView<>();

        TableColumn<String, String> firstNameColumn = new TableColumn<>("Code");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("Code"));

        TableColumn<String, String> lastNameColumn = new TableColumn<>("Cours");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("Cours"));

        tableView.getColumns().addAll(firstNameColumn, lastNameColumn);

        firstNameColumn.prefWidthProperty().bind(tableView.widthProperty().divide(2));
        lastNameColumn.prefWidthProperty().bind(tableView.widthProperty().divide(2));

        leftSide.getChildren().add(tableView);
        AnchorPane.setTopAnchor(tableView, 60.0);
        AnchorPane.setLeftAnchor(tableView, 15.0);
        AnchorPane.setRightAnchor(tableView, 15.0);
        AnchorPane.setBottomAnchor(tableView, 70.0);


    }


    public static void main(String[] args) {
        launch();
    }
}