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
        Magasin magasin = null;
        try {
            magasin = magasinBD.getMagasin(vue.getComboBoxSave().getValue());
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du magasin : " + e.getMessage());
            vue.popUpConnexionImpossible();
            return;
        }
        List<TextField> champs = vue.getCreationVendeur();
    if (champs.size() >= 3) {
        String nom = champs.get(0).getText();
        String prenom = champs.get(1).getText();
        String mdp = champs.get(2).getText();
        // ...
    } else {
        System.out.println("Erreur : pas assez de champs dans creationVendeur !");
        vue.popUpConnexionImpossible();
        return;
    }
        Vendeur vendeur = new Vendeur(
            vue.getIdentifiantSave(),
            vue.getCreationVendeur().get(0).getText(),
            vue.getCreationVendeur().get(1).getText(),
            vue.getCreationVendeur().get(2).getText(),
            magasin
        );
        try {
            modeleA.creerVendeur(vendeur, magasin);
            vue.modeConnexion();
        } catch (Exception e) {
            vue.popUpConnexionImpossible();
            System.err.println("Erreur lors de l'inscription : " + e.getMessage());
        }
    }
}
