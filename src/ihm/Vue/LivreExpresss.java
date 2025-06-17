package ihm.Vue;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ButtonBar.ButtonData ;

import java.util.List;

import javax.swing.border.TitledBorder;

import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;


/**
 * Vue du jeu du pendu
 */
public class LivreExpresss extends Application {
    /**
     * modèle du jeu
     **/
   
    /**
     * Liste qui contient les images du jeu
     */
    private ArrayList<Image> lesImages;
    /**
     * Liste qui contient les noms des niveaux
     */    
    public List<String> roles;

    // les différents contrôles qui seront mis à jour ou consultés pour l'affichage
    
    /**
     * le text qui indique l'utilisateur
     */
    private Text lUtilisateur;
    
    /**
     * le panel Central qui pourra être modifié selon le mode (accueil ou jeu)
     */
    private Pane panelCentral;
    private Pane banniere;
    private Pane gauche;


    /**
     * le bouton Profil
     */
    private Button boutonProfil;
    /**
     * le bouton Accueil / Maison
     */    
    private Button boutonMaison;
    /**
     * le bouton qui permet de voir notre panier
     */ 
    private Button bPanier;
    /**
     * le bouton qui permet de voir notre panier
     */ 
    private Button bDeconnexion;

    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono ...)
     */
    @Override
    public void init() {
        this.lesImages = new ArrayList<Image>();
        this.chargerImages(/*"../img"*/);
        // A terminer d'implementer
    }

    /**
     * @return  le graphe de scène de la vue à partir de methodes précédantes
     */
    private Scene laScene(){
        BorderPane fenetre = new BorderPane();
        fenetreAccueil();
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
        return new Scene(fenetre, 800, 600);
    }


    // /**
     // * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
     // *         de progression et le clavier
     // */
    // private Pane fenetreJeu(){
        // A implementer
        // Pane res = new Pane();
        // return res;
    // }

    // /**
     // * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de jeu
     // */
    private void fenetreAccueil(){   
        this.banniere = null;
        
        this.gauche = new Pane();
        ImageView imgG = new ImageView(this.lesImages.get(1));
        gauche.getChildren().add(imgG);

        this.panelCentral= new VBox();
        ImageView imgLogo = new ImageView(this.lesImages.get(0));
        imgLogo.setFitWidth(400);
        imgLogo.setPreserveRatio(true);


        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Administrateur", "Vendeur", "Client");
        comboBox.setValue("Choisissez un rôle");

        HBox listBoutons = new HBox();
        listBoutons.getChildren().add(comboBox);
        panelCentral.getChildren().addAll(imgLogo);
    }
    /**
     * charge les images à afficher en fonction des erreurs
     * @param repertoire répertoire où se trouvent les images
     */
    private void chargerImages(/*String repertoire*/){
    File file = new File("src/ihm/img/LivreExpress.png");
    File file1 = new File("src/ihm/img/home.png");
    File file2 = new File("src/ihm/img/info.png");
    File file3 = new File("src/ihm/img/logout.png");
    File file4 = new File("src/ihm/img/parametres.png");
    this.lesImages.add(new Image(file.toURI().toString()));
    this.lesImages.add(new Image(file1.toURI().toString()));
    this.lesImages.add(new Image(file2.toURI().toString()));
    this.lesImages.add(new Image(file3.toURI().toString()));
    this.lesImages.add(new Image(file4.toURI().toString()));
    }

    public void modeAccueil(){
        // A implementer
    }
    
    public void modeJeu(){
        // A implementer
    }
    
    public void modeParametres(){
        // A implémenter
    }

    /** lance une partie */
    public void lancePartie(){
        // A implementer
    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage(){
        // A implementer
    }

    public Alert popUpPartieEnCours(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"La partie est en cours!\n Etes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        return alert;
    }
        
    public Alert popUpReglesDuJeu(){
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }
    
    public Alert popUpMessageGagne(){
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);        
        return alert;
    }
    
    public Alert popUpMessagePerdu(){
        // A implementer    
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }

    /**
     * créer le graphe de scène et lance le jeu
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        stage.setScene(this.laScene());
        this.modeAccueil();
        stage.show();
    }

    /**
     * Programme principal
     * @param args inutilisé
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
