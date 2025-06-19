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
    Connection connexion;
    private Client client;
    
    

    /**
     * @param modelePendu modèle du jeu
     * @param p vue du jeu
     */
    public ControleurAjouterLivre(LivreExpresss vue, Client client, Livre livre, ConnectionBD connectionBD) {
        this.vue = vue;
        this.connexion = ConnectionBD.getConnection();
        this.client = client;
    }
    

    /**
     * L'action consiste à recommencer une partie. Il faut vérifier qu'il n'y a pas une partie en cours
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getTarget();
        Object data = button.getUserData();
        if(data.equals(1)){
            CommandeBD commandeBD = new CommandeBD(this.connexion);
            Commande commande = new Commande(commandeBD.genererId(), null, false, null, null, null,false);
            commandeBD.setCommande(client);
            if(client.getDerniereCommande().estFinie()){
                client.ajouterCommande(commande);
                
            }
        }
        if(data.equals(2)){
            
        }
        if(data.equals(3)){
            
        }
        if(data.equals(4)){
            
        }
        if(data.equals(5)){
            
        }
        if(data.equals(6)){
            
        }
    }
}