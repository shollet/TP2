package com.example.graphisme;
/**
 * Fichier de base de JavaFX
 */

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Classe lancant une petite interface graphique
 */
public class HelloController {
    @FXML
    /**
     * Fenetre de bienvenue
     */
    private Label welcomeText;

    @FXML
    /**
     * Bouton hello pour permettre de tester javaFX
     */
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}