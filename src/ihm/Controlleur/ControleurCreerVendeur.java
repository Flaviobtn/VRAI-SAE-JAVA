package ihm.Controlleur;
import ihm.Vue.*;
import bd.* ;
import Modele.*;   
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import java.util.List;
import java.sql.*;

import java.util.Optional;


/**
 * Contrôleur à activer lorsque l'on clique sur le bouton rejouer ou Lancer une partie
 */
public class ControleurCreerVendeur implements EventHandler<ActionEvent> {
    /**
     * vue du jeu
     **/
    private LivreExpresss vue;
    private AdministrateurBD modeleA;
    private List<TextField> vals;
    private MagasinBD magasinBD;
    private VendeurBD vendBD;

    /**
     * @param modelePendu modèle du jeu
     * @param p vue du jeu
     */
    public ControleurCreerVendeur(LivreExpresss vue, AdministrateurBD modeleA, MagasinBD magBD, VendeurBD vendBD) {
        this.vue = vue;
        this.modeleA = modeleA;
        this.magasinBD = magBD;
        this.vendBD = vendBD;
    }

    
    @Override
    public void handle(ActionEvent actionEvent) {
        try {
            modeleA.creerVendeur(vue.getCreationVendeur(), vue.getMagasinCreation());
            System.out.println("askip on a ajouté le vendeur");
            vue.modeAccueilA();
        } catch (Exception e) {
            vue.popUpConnexionImpossible();
            System.err.println("Erreur lors de l'inscription : " + e.getMessage());
        }
    }
}
