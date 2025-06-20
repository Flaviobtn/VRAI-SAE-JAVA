package ihm.Controlleur;
import ihm.Vue.*;
import bd.*;
import Modele.*;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import java.util.List;
import java.sql.*;

import java.util.Optional;

public class ControleurAjouterLivre implements EventHandler<ActionEvent> {
    /**
     * vue du jeu
     **/
    private LivreExpresss vue;
    //private ClientBD modeleC;
    private Connection connexion;
    private Client client;
    private Livre livre;
    private int qte;
    
    

    /**
     * @param modelePendu modèle du jeu
     * @param p vue du jeu
     */
    public ControleurAjouterLivre(LivreExpresss vue, Client client,Livre livre,int qte, Connection connexion) {
        this.vue = vue;
        this.connexion = connexion;
        this.client = client;
        this.livre = livre;
        this.qte = qte;
    }
    

    /**
     * L'action consiste à recommencer une partie. Il faut vérifier qu'il n'y a pas une partie en cours
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        System.out.println("");
        LivreBD livrebd = new LivreBD(connexion);
        Button button = (Button) actionEvent.getTarget();
        Object data = button.getUserData();
            CommandeBD commandeBD = new CommandeBD(this.connexion);
            Commande commande = new Commande(commandeBD.genererId(), java.time.LocalDate.now(), true, Livraison.DOMICILE, livrebd.premierMagDispo(this.livre), this.client,false);
            commandeBD.setCommande(client);
            System.out.println("On a les commandes du client");
            if(client.getDerniereCommande() == null || client.getDerniereCommande().estFinie()){ 
                System.out.println(client.getDerniereCommande());
                System.out.println("La commande est finie");
                client.ajouterCommande(commande);
                System.out.println("commande créée");
                CommandeBD coBD = new CommandeBD(connexion);
                coBD.insererCommande(commande);
                System.out.println("La commande est dans la bd");
            }else{
                System.out.println("La commande est en cours");
                commande = client.getDerniereCommande();
            }
            DetailCommandeBD detBD = new DetailCommandeBD(this.connexion);
            DetailCommande detailCommande = new DetailCommande(detBD.genererId(),this.livre, this.qte, commande.getNumCommande());
            detBD.insertDetailCommande(detailCommande);
            System.out.println("DetailCommande ajouté");
    }
}