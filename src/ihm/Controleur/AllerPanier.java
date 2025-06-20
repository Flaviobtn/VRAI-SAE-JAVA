package ihm.Controleur;
import ihm.Vue.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Contrôleur à activer lorsque l'on clique sur le bouton Accueil
 */
public class AllerPanier implements EventHandler<ActionEvent> {
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
    public AllerPanier(LivreExpresss vue) {
        // A implémenter
        this.vue=vue;
    }


    /**
     * L'action consiste à retourner sur la page d'accueil. Il faut vérifier qu'il n'y avait pas une partie en cours
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        
        vue.modePanier();
        
    }
}