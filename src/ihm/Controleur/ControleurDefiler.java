package ihm.Controleur;
import ihm.Vue.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Contrôleur à activer lorsque l'on clique sur le bouton Accueil
 */
public class ControleurDefiler implements EventHandler<ActionEvent> {
    /**
     * modèle du jeu
     */

    /**
     * vue du jeu
     **/
    private LivreExpresss vue;
    private String defile;
    

    /**
     * @param modelePendu modèle du jeu
     * @param vuePendu vue du jeu
     */
    public ControleurDefiler(LivreExpresss vue,String defile) {
        // A implémenter
        this.vue=vue;
        this.defile = defile;
    }


    /**
     * L'action consiste à retourner sur la page d'accueil. Il faut vérifier qu'il n'y avait pas une partie en cours
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getTarget();
        Object data = button.getUserData();
        System.out.println(data);
        if(data.equals("AVANT")) {
            vue.setPageActu(vue.getPageActu()-1);
            int ajustement = this.vue.ajouteEspace();
        }
        else{
             vue.setPageActu(vue.getPageActu()+1);
             int ajustement = this.vue.ajouteEspace();
        }
        if(defile.equals("PANIER")){
            vue.modePanier();
        }
        if(defile.equals("CLIENT")){
            vue.modeAccueilC();
        }
        if(defile.equals("VENDEUR")){
            vue.modeVerifDispo();
        }
    }
}