package ihm.Controlleur;
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
    private VendeurBD modeleV;
    private AdministrateurBD modeleA;
    private List<TextField> champsSaisie;

    /**
     * @param modelePendu modèle du jeu
     * @param p vue du jeu
     */
    public ControleurInscription(LivreExpresss vue, ClientBD modeleC, VendeurBD modeleV, AdministrateurBD modeleA) {
        this.vue = vue;
        this.modeleC = modeleC;
        this.modeleV = modeleV;
        this.modeleA = modeleA;
        this.champsSaisie = vue.getInscriptions();
    }

    /**
     * L'action consiste à recommencer une partie. Il faut vérifier qu'il n'y a pas une partie en cours
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getTarget();
        String nomDuBouton = button.getText();
        if(nomDuBouton.equals("SE CONNECTER")){
            if(vue.getLUtilisateur().equals("CLIENT")){
                try {
                    if(this.modeleC.seconnecterClient(this.vue.getIdentifiant(), this.vue.getMotdepasse())){;
                    this.vue.modeAccueilC();}
                    else {
                        vue.popUpConnexionImpossible();
                    }
                } catch (SQLException e) {
                    System.err.println("Erreur de connexion en tant que Client : " + e.getMessage());
                }
            } 
        
            if(vue.getLUtilisateur().equals("VENDEUR")){
                try {
                    if(this.modeleV.seconnecterVendeur(this.vue.getIdentifiant(), this.vue.getMotdepasse())){
                    this.vue.modeAccueilV();}
                    else {
                        vue.popUpConnexionImpossible();
                    }
                } catch (SQLException e) {
                    System.err.println("Erreur de connexion en tant qu'Vendeur : " + e.getMessage());
                }
            }

             if(vue.getLUtilisateur().equals("ADMINISTRATEUR")){
                try {
                    if(this.modeleA.seconnecterAdmin(this.vue.getIdentifiant(), this.vue.getMotdepasse())){
                    this.vue.modeAccueilC();
                    }
                    else {
                        vue.popUpConnexionImpossible();
                    }
                } catch (SQLException e) {
                    System.err.println("Erreur de connexion en tant qu'Administrateur : " + e.getMessage());
                }
            }
        }
    }
}