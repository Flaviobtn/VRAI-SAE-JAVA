package ihm.Controlleur;
import ihm.Vue.*;
import bd.*;
import Modele.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import java.util.List;
import java.sql.*;

public class ControleurCreerVendeur implements EventHandler<ActionEvent> {
    private LivreExpresss vue;
    private AdministrateurBD modeleA;
    private MagasinBD magasinBD;
    private VendeurBD vendBD;

    public ControleurCreerVendeur(LivreExpresss vue, AdministrateurBD modeleA, MagasinBD magBD, VendeurBD vendBD) {
        this.vue = vue;
        this.modeleA = modeleA;
        this.magasinBD = magBD;
        this.vendBD = vendBD;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        try {
            // Récupérer les valeurs des champs
            List<TextField> champs = vue.getCreationVendeur();
            if (champs == null || champs.size() < 3) {
                vue.popUpConnexionImpossible();
                return;
            }

            String nom = champs.get(0).getText();
            String prenom = champs.get(1).getText();
            String mdp = champs.get(2).getText();
            String nomMagasin = vue.getComboBoxSave().getValue();

            if (nomMagasin == null || nomMagasin.trim().isEmpty()) {
                vue.popUpConnexionImpossible();
                return;
            }

            // Récupérer le magasin
            Magasin magasin = magasinBD.getMagasin(magasinBD.getidMag(vue.connection, nomMagasin));
            if (magasin == null) {
                vue.popUpConnexionImpossible();
                return;
            }

            // Créer le vendeur
            Vendeur vendeur = new Vendeur(
                vendBD.genererId(),
                nom,
                prenom,
                mdp,
                magasin
            );

            // Insérer en base
            vendBD.insertVendeur(vendeur);
            
            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Le vendeur a été créé avec succès!");
            alert.showAndWait();
            
            // Retour à l'accueil admin
            vue.modeAccueilA();

        } catch (Exception e) {
            System.err.println("Erreur lors de la création du vendeur: " + e.getMessage());
            vue.popUpConnexionImpossible();
        }
    }
}