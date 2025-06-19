package ihm.Controlleur;
import ihm.Vue.*;
import bd.*;
import Modele.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import java.util.*;
import java.sql.*;



public class ControleurVerifDispo implements EventHandler<ActionEvent> {
    private LivreExpresss vue;
    private Magasin magasin;
    private LivreBD livreBD;
    private String rech

    public ControleurVerifDispo(LivreExpresss vue, LivreBD livrebd, Magasin mag, String nomL) {
        this.vue = vue;
        this.magasin = mag;
        this.livreBD = livrebd;
        this.rech = nomL;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        try {
            // Récupérer les valeurs des champs
            List<Livre> lesLivres = new ArrayList<>();
            String id = magasin.getIdmag();
            lesLivres = livreBD.getLivresNomAPeuPres(id, this.rech);
            if (lesLivres.isEmpty()) {
                System.out.println("bah ouais mais ça me casse les couilles");;
                return;
            }

        } catch (Exception e) {
            System.err.println("Encore pire");
        }
    }
}
