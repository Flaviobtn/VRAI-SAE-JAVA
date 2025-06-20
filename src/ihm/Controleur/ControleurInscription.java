package ihm.Controleur;
import ihm.Vue.*;
import bd.* ;
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
public class ControleurInscription implements EventHandler<ActionEvent> {
    /**
     * vue du jeu
     **/
    private LivreExpresss vue;
    private ClientBD modeleC;
    private List<TextField> champsSaisie;

    /**
     * @param modelePendu modèle du jeu
     * @param p vue du jeu
     */
    public ControleurInscription(LivreExpresss vue, ClientBD modeleC) {
        this.vue = vue;
        this.modeleC = modeleC;
        this.champsSaisie = vue.getInscriptions();
    }

    /**
     * L'action consiste à recommencer une partie. Il faut vérifier qu'il n'y a pas une partie en cours
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        String nom = champsSaisie.get(0).getText();
        String prenom = champsSaisie.get(1).getText();
        String motdepasse = champsSaisie.get(2).getText();
        String adresse = champsSaisie.get(3).getText();
        String ville = champsSaisie.get(4).getText();
        int codepostal;
        // on vérifie que le code postal est un entier
        // on try de récupérer un entier, si ça échoue on affiche une erreur
        try{
            codepostal = Integer.parseInt(champsSaisie.get(5).getText());
        } catch (NumberFormatException e) {
            vue.popUpInscriptionImpossible();
            return;
        }

        //int codepostal = Integer.parseInt(champsSaisie.get(5).getText());
        try {
            modeleC.inscrireClient(nom, prenom, motdepasse, adresse, ville, codepostal);
            vue.modeConnexion();
        } catch (Exception e) {
            vue.popUpConnexionImpossible();
            System.err.println("Erreur lors de l'inscription : " + e.getMessage());
        }
    }
}
