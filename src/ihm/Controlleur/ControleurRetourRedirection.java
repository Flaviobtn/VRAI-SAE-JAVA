package ihm.Controlleur;
import ihm.Vue.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Contrôleur à activer lorsque l'on clique sur le bouton Accueil
 */
public class ControleurRetourRedirection implements EventHandler<ActionEvent> {
    /**
     * modèle du jeu
     */

    /**
     * vue du jeu
     **/
    private LivreExpresss vue;
    

    /**
     * @param modelePendu modèle du jeu
     * @param vuePendu vue du jeu
     */
    public ControleurRetourRedirection(LivreExpresss vue) {
        // A implémenter
        this.vue=vue;
    }


    /**
     * L'action consiste à retourner sur la page d'accueil. Il faut vérifier qu'il n'y avait pas une partie en cours
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getTarget();
        Object data = button.getUserData();
        if(data.equals("MAISON")) {
            vue.modeAccueilC();
        } if(data.equals("PROFIL")) {
            vue.modeProfil();
        } if(data.equals("PANIER")) {
            vue.modePanier();
        } if(data.equals("DECONNEXION")) {
            vue.modeChoix();
        } if(data.equals("INSCRIPTION")){
            vue.modeInscription();
        }
        if(data.equals("CONNEXION")){
            vue.modeConnexion();
        }
    }
}