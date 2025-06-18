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
        System.out.println(data);
        if(data.equals("MAISON")) {
            if(vue.getLUtilisateur().equals("CLIENT")){
                vue.modeAccueilC();}
            if(vue.getLUtilisateur().equals("VENDEUR")){
                vue.modeAccueilV();}
            if(vue.getLUtilisateur().equals("ADMINISTRATEUR")){
                vue.modeAccueilA();
                }
        } if(data.equals("PROFIL")) {
            if(vue.getLUtilisateur().equals("CLIENT")){
                vue.modeProfilC();}
            if(vue.getLUtilisateur().equals("VENDEUR")){
                vue.modeProfilV();}
            if(vue.getLUtilisateur().equals("ADMINISTRATEUR")){
                vue.modeProfilA();
                }
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
        if(data.equals("ANNULERC")){
            vue.modeChoix();
        }
        if(data.equals("ANNULERI")){
            vue.modeConnexion();
        }
        if(data.equals("OPTVENDEUR")){
            vue.modeAccueilV();
        }
        if(data.equals("CREER VENDEUR")){
            vue.modeCreerVendeur();
        }
    }
}