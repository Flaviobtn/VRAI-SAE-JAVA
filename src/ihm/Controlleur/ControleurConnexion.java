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
        if(nomDuBouton.equals("Facile")){
            this.vue.setNiveau(0);
        }
        if(nomDuBouton.equals("Moyen")){
            this.vue.(1);
        }
}
}
