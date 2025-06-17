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
public class ControleurConnexion implements EventHandler<ActionEvent> {
    /**
     * vue du jeu
     **/
    private LivreExpresss vue;
    private ClientBD modeleC;
    private VendeurBD modeleV;
    private AdministrateurBD modeleA;
    private Text lUtilisateur;

    /**
     * @param modelePendu modèle du jeu
     * @param p vue du jeu
     */
    public ControleurConnexion(LivreExpresss vue, Text lUtilisateur, ClientBD modeleC, VendeurBD modeleV, AdministrateurBD modeleA) {
        this.vue = vue;
        this.lUtilisateur = lUtilisateur;
        this.modeleC = modeleC;
        this.modeleV = modeleV;
        this.modeleA = modeleA;
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
            if(lUtilisateur.getText().equals("CLIENT")){
                try {
                    this.modeleC.seconnecterClient(this.vue.getIdentifiant(), this.vue.getMotdepasse());
                } catch (SQLException e) {
                    System.err.println("Erreur de connexion en tant que Client : " + e.getMessage());
                }
            } 
            //else {
            //    Alert alert = new Alert(Alert.AlertType.ERROR);
            //    alert.setTitle("Erreur de connexion");
            //    alert.setHeaderText(null);
            //    alert.setContentText("Identifiants incorrects. Veuillez réessayer.");
            //    Optional<ButtonType> result = alert.showAndWait();
            //}

            if(lUtilisateur.getText().equals("VENDEUR")){
                try {
                    this.modeleV.seconnecterVendeur(this.vue.getIdentifiant(), this.vue.getMotdepasse());
                } catch (SQLException e) {
                    System.err.println("Erreur de connexion en tant que Vendeur : " + e.getMessage());
                }
            } 
            //else {
            //    Alert alert = new Alert(Alert.AlertType.ERROR);
            //    alert.setTitle("Erreur de connexion");
            //    alert.setHeaderText(null);
            //    alert.setContentText("Identifiants incorrects. Veuillez réessayer.");
            //    Optional<ButtonType> result = alert.showAndWait();
            //}

             if(lUtilisateur.getText().equals("ADMINISTRATEUR")){
                try {
                    if(this.modeleA.seconnecterAdmin(this.vue.getIdentifiant(), this.vue.getMotdepasse())){
                    this.vue.modeAccueilC();

                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur de connexion");
                        alert.setHeaderText(null);
                        alert.setContentText("Identifiants incorrects. Veuillez réessayer.");
                        Optional<ButtonType> result = alert.showAndWait();
                    }
                } catch (SQLException e) {
                    System.err.println("Erreur de connexion en tant qu'Administrateur : " + e.getMessage());
                }
            }
        }
    }
}
