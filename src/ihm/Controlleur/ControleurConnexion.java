package ihm.Controlleur;
import ihm.Vue.*;
import ihm.Vue.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import java.util.Optional;


/**
 * Contrôleur à activer lorsque l'on clique sur le bouton rejouer ou Lancer une partie
 */
public class ControleurConnexion implements EventHandler<ActionEvent> {
    /**
     * vue du jeu
     **/
    private LivreExpresss vue;

    /**
     * @param modelePendu modèle du jeu
     * @param p vue du jeu
     */
    public ControleurConnexion(LivreExpresss vue) {
        this.vue = vue;
    }

    /**
     * L'action consiste à recommencer une partie. Il faut vérifier qu'il n'y a pas une partie en cours
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getTarget();
        String nomDuBouton = button.getText();
        System.out.println(nomDuBouton);
        if(nomDuBouton.equals("CONNEXION")){
            this.vue.modeConnexion();
        }
        if(nomDuBouton.equals("SE CONNECTER")){
            if(this.vue.lUtilisateur.getText().equals("CLIENT")){
                System.out.println("Connexion en tant que Client");
            } 
            //else {
            //    Alert alert = new Alert(Alert.AlertType.ERROR);
            //    alert.setTitle("Erreur de connexion");
            //    alert.setHeaderText(null);
            //    alert.setContentText("Identifiants incorrects. Veuillez réessayer.");
            //    Optional<ButtonType> result = alert.showAndWait();
            //}

             if(this.vue.lUtilisateur.getText().equals("VENDEUR")){
                System.out.println("Connexion en tant que Vendeur");
            } 
            //else {
            //    Alert alert = new Alert(Alert.AlertType.ERROR);
            //    alert.setTitle("Erreur de connexion");
            //    alert.setHeaderText(null);
            //    alert.setContentText("Identifiants incorrects. Veuillez réessayer.");
            //    Optional<ButtonType> result = alert.showAndWait();
            //}

             if(this.vue.lUtilisateur.getText().equals("ADMINISTRATEUR")){
                System.out.println("Connexion en tant qu'Administrateur");
            } 
            //else {
            //    Alert alert = new Alert(Alert.AlertType.ERROR);
            //    alert.setTitle("Erreur de connexion");
            //    alert.setHeaderText(null);
            //    alert.setContentText("Identifiants incorrects. Veuillez réessayer.");
            //    Optional<ButtonType> result = alert.showAndWait();
            //}
        }
}
}
