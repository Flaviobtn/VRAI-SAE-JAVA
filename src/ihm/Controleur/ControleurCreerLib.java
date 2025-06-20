package ihm.Controleur;
import ihm.Vue.*;
import bd.*;
import Modele.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import java.util.List;
import java.sql.*;

public class ControleurCreerLib implements EventHandler<ActionEvent> {
    private LivreExpresss vue;
    private AdministrateurBD modeleA;
    private MagasinBD magBD;
    
    

   

    public ControleurCreerLib(LivreExpresss vue, AdministrateurBD modeleA, MagasinBD magBD) {
        this.vue = vue;
        this.modeleA = modeleA;
        this.magBD = magBD;
        
        
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        try {
            if (vue.getInfoMag().isEmpty()){
                return;
            }
            // Récupérer les infos du magasin
            String idMag = magBD.genererId();
            String nommag = vue.getInfoMag().get(0).getText(); //test.get(0).getText();
            String villemag = vue.getInfoMag().get(1).getText();
            
            modeleA.ajouteMagasin(idMag, nommag, villemag);
            System.out.println("le magasin à bien été créé");
            vue.modeAccueilA();
            vue.popUpCreaMagReussi();
            

        } catch (Exception e) {
            System.err.println("Erreur lors de la création du magasin : " + e.getMessage());
            vue.popUpConnexionImpossible();
        }
    }
}