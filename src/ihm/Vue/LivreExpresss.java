package ihm.Vue;
import Modele.*;
import bd.*;
import ihm.Controleur.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ButtonBar.ButtonData ;
import java.sql.*;
import java.util.*;
import java.io.File;


/**
 * Vue de l'application
 */
public class LivreExpresss extends Application {
    /**
     * connection à la base de données
     */
    public Connection connection;
    /**
     * Liste qui contient les images de l'application
     */
    private ArrayList<Image> lesImages;
    /**
     * Liste qui contient les noms des rôles
     */    
    private List<String> roles;

    // les différents contrôles qui seront mis à jour ou consultés pour l'affichage
    
    /**
     * le texte qui indique le rôle de l'utilisateur
     */
    private Text lUtilisateur;
    
    /**
     * Sert à contenir le noms des magasins dans une combo box 
     */
    private ComboBox<String> comboBoxSave;

    /**
     * le texte qui sert à conserver l'identifiant d'un client pour l'inscrire
     */
    private TextField identifiant;

    /**
     * le texte qui sert à conserver le mot de passe d'un client pour l'inscrire
     */
    private TextField motdepasse;

    /**
     * la liste des éléments nécéssaires à inscrire un client
     */
    private List<TextField> inscriptions;
    /**
     * Sert à créer un vendeur
     */
    private Vendeur creationVendeur;
    /**
     * la liste des éléments nécéssaires à insérer un livre dans la base de données
     */
    private List<TextField> infosLivre;
    /**
     * Sert à créer un livre
     */
    private Livre creationLivre;
    /**
     * Sert à créer un magasin
     */
    private Magasin magasinCreation;
    /**
     * Personne avec les infos liées à la personne connectée
     */
    private Personne personneConnectee;
    /**
     * le dictionnaire numéro de page / les livres de la page
     */
    private Map<Integer, List<Livre>> catalogues;
    /**
     * le numéro de la page du catalogue sur laquelle nous sommes
     */
    private int pageCataActu;
    /**
     * la liste des éléments nécéssaires à créer un magasin
     */
    private List<TextField> infoMag;


    //BD
    private MagasinBD magasinBD; 
    private VendeurBD vendeurBD;

    /**
     * le panel Central qui pourra être modifié selon le mode
     */
    private BorderPane panelCentral;
    /**
     * la bannière qui pourra être modifiée selon le mode
     */
    private BorderPane banniere;
    /**
     * la partie de gauche qui pourra être modifiée selon le mode
     */
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
     * le bouton qui permet de se déconnecter
     */ 
    private Button bDeconnexion;

    /**
     * la fenêtre 
     */
    private BorderPane fenetre;

    /**
     * initialise les attributs (créer les modèles, charge les images, crée les boutons ...)
     */
    @Override
    public void init() {
        this.lesImages = new ArrayList<Image>();
        this.roles = new ArrayList<String>();
        this.catalogues = new HashMap<>();
        this.chargerImages(/*"../img"*/);
        this.boutonMaison= new Button();
        this.bDeconnexion= new Button();
        this.bPanier= new Button();
        this.magasinCreation = null;
        this.pageCataActu = 1;
        this.boutonProfil= new Button();
        this.creationVendeur = null; 
        this.personneConnectee = null;
        this.connection = ConnectionBD.getConnection();
        this.magasinBD= new MagasinBD(this.connection);
        this.vendeurBD = new VendeurBD(this.connection);
        this.infoMag = new ArrayList<TextField>();
    }

    /**
     * récupère l'identifiant de l'utilisateur
     */
    public String getIdentifiant() {
        return this.identifiant.getText();
    }

    /**
     * récupère le mot de passe de l'utilisateur
     */
    public String getMotdepasse() {
        return this.motdepasse.getText();
    }
    /**
     * récupère le rôle de l'utilisateur
     */
    public String getLUtilisateur() {
        return this.lUtilisateur.getText();
    }

    public ComboBox<String> getComboBoxSave() {
    return this.comboBoxSave;
}

    public Map<Integer, List<Livre>> getCatalogues() {
        return this.catalogues;
    }

    public List<TextField> getInfoMag(){
        return this.infoMag;
    }

    public void SetCatalogues(Map<Integer, List<Livre>> catalogues) {
        this.catalogues = catalogues;
    }
    
    public List<TextField> getInscriptions() {
        return this.inscriptions;
    }

    public List<TextField> getCreationVendeur() {
    return this.inscriptions;
}

     public List<TextField> getInfosLivre() {
        return this.infosLivre;
    }

    public List<TextField> getCreationLivre() {
    return this.infosLivre;
}

    public void setPersonneConnectee(Personne personne) {
        this.personneConnectee = personne;
    }

    public Magasin getMagasinCreation() {
        return this.magasinCreation;
    }

    public int getPageActu(){
        return this.pageCataActu;
    }

    public void setPageActu(int pageactu){
        this.pageCataActu = pageactu;
    }
    public void setCatalogues(Map<Integer, List<Livre>> newMap){
        this.catalogues = newMap;
    }

    
    public int ajouteEspace() {
    Integer max = null;
    for (Livre livre : this.catalogues.get(this.pageCataActu)) {
        int longueur = livre.getTitre().length();
        if (max == null || longueur > max) {
            max = longueur;
        }
    }
    return max != null ? max : 0;
    }


    /**
     * Crée le catalogue à afficher dans l'accueil client
     * Si il n'a jamais passé de commande : on affiche tout les livres
     * Sinon, on affiche les livres recommandés
     */
    public void setCataloguesC(){
        if(this.personneConnectee instanceof Client){;
            Client client = (Client) this.personneConnectee;
            if(client.aCommande()){
                CommandeBD cbd = new CommandeBD(connection);
                cbd.setCommande(client);
                if(client.onVousRecommande().size()>0){
                    this.catalogues = client.recotoMap();
                    System.out.println(this.catalogues.get(1));
                }
            }    
            else{
                LivreBD catalogueLivre = new LivreBD(connection);
                this.catalogues = catalogueLivre.catalogue();
            }
        }
    }

    /**
     * @return  le graphe de scène de la vue à partir de methodes précédantes
     */
    private Scene laScene(){
        this.fenetre = new BorderPane();
        modeChoix();
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
        return new Scene(fenetre, 1920, 1000, Color.WHITE);
    }

    /**
     * création de la fenetre de choix d'utilisateur
     */
    private void fenetreChoix() {
        // Utilisation de l'image des livres 
        ImageView imgG = new ImageView(this.lesImages.get(8));
        imgG.setFitWidth(600);
        imgG.setFitHeight(1080);
        imgG.setPreserveRatio(false);

        // Partie droite - Formulaire de connexion dans la bannière
        VBox centerPanel = new VBox();
        centerPanel.setPrefWidth(450);
        centerPanel.setPrefHeight(200);
        centerPanel.setPadding(new Insets(100,0,0,225));
        centerPanel.setStyle("-fx-background-color: #154c45;");

        // Image logo "Livre Express"
        ImageView titreLVE = new ImageView(this.lesImages.get(0));
        titreLVE.setFitWidth(800);
        titreLVE.setFitHeight(200);
        titreLVE.setPreserveRatio(true);

        // Ligne décorative
        Region ligne = new Region();
        ligne.setPrefHeight(7);
        ligne.setPrefWidth(220);
        ligne.setStyle("-fx-border-color: #2c2c2c;");

        // Conteneur pour le titre et la ligne
        VBox titleContainer = new VBox();
        titleContainer.setSpacing(5);
        titleContainer.getChildren().addAll(titreLVE, ligne);

        // Menu déroulant
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("ADMINISTRATEUR", "VENDEUR", "CLIENT");
        comboBox.setValue("Choix de l'utilisateur"); 
        comboBox.setMinWidth(350);
        comboBox.setMinHeight(100);
        comboBox.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; " +
                       "-fx-background-color: white; -fx-border-color: #2c2c2c; " +
                         "-fx-border-width: 2; -fx-border-radius: 0;");

        // Bouton de connexion
        Button choix = new Button("CONNEXION");
        choix.setUserData("CONNEXION");
        choix.setOnAction(new ControleurRetourRedirection(this));
        choix.setMinWidth(200);
        choix.setMinHeight(100);
        choix.setFont(Font.font("Arial", 15));
        choix.setDisable(true);
        choix.setStyle("-fx-background-color: white; -fx-text-fill: #1e1e1e; " +
               "-fx-border-color: #1e1e1e; -fx-border-width: 2; " +
               "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;");

        choix.setOnMouseEntered(e -> 
            choix.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white; " +
                        "-fx-border-color: #1e1e1e; -fx-border-width: 2; " +
                        "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));

        choix.setOnMouseExited(e -> 
            choix.setStyle("-fx-background-color: white; -fx-text-fill: #1e1e1e; " +
                        "-fx-border-color: #1e1e1e; -fx-border-width: 2; " +
                        "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));
                        

        // Conteneur pour le combobox et la Connexion
        VBox choixContainer = new VBox();
        choixContainer.setAlignment(Pos.CENTER);

        // L'écart important vient de cette ligne :
        choixContainer.setSpacing(140); 
        choixContainer.getChildren().addAll(comboBox, choix);
        choixContainer.setPadding(new Insets(100, 0, 0, 220));

        // Crée l'objet Text et ajoute un listener pour suivre la sélection
        this.lUtilisateur = new Text("");
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("Choix de l'utilisateur")) {
                choix.setDisable(false);
                this.lUtilisateur.setText(newValue);
            }
            
        });

        // Ajout des éléments au panel central
        centerPanel.getChildren().addAll(titleContainer, choixContainer);

        // Initialisation des autres panels 
        this.gauche = new Pane();
        this.panelCentral = new BorderPane();
        this.panelCentral.getChildren().add(centerPanel);
        this.panelCentral.setStyle("-fx-background-color: #c8c4b8;");
        this.gauche.getChildren().add(imgG);
        this.banniere = new BorderPane();

    }

    /**
     * création de la fenetre de connexion
     */
    private void fenetreConnexion(){
        // Utilisation de l'image des livres 
        ImageView imgG = new ImageView(this.lesImages.get(8));
        imgG.setFitWidth(600);
        imgG.setFitHeight(1080);
        imgG.setPreserveRatio(false);

        // Partie droite - Formulaire de connexion 
        VBox centerPanel = new VBox();
        centerPanel.setPrefWidth(450);
        centerPanel.setPrefHeight(200);
        centerPanel.setPadding(new Insets(100,0,0,225));
        centerPanel.setStyle("-fx-background-color: #2c2c2c;");

        // Image logo "Livre Express"
        ImageView titreLVE = new ImageView(this.lesImages.get(0));
        Text titreText = new Text("Connexion Compte " + this.lUtilisateur.getText());
        titreText.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;-fx-text-fill: #2c2c2c;");
        titreLVE.setFitWidth(800);
        titreLVE.setFitHeight(200);
        titreLVE.setPreserveRatio(true);

        // Ligne décorative
        Region ligne = new Region();
        ligne.setPrefHeight(7);
        ligne.setPrefWidth(220);
        ligne.setStyle("-fx-border-color: #2c2c2c;");

        // Conteneur pour le titre et la ligne
        VBox titleContainer = new VBox();
        titleContainer.setSpacing(5);
        titleContainer.getChildren().addAll(titreLVE,titreText, ligne);

        // deux VBox : une pour contenir le texte identifiant et son textefield, une pour le mot de passe
        VBox idcontainer = new VBox();
        VBox motdepassecontainer = new VBox();
        this.identifiant = new TextField();
        this.motdepasse = new PasswordField();
        Text identifiantText = new Text("Identifiant :");
        Text motdepasseText = new Text("Mot de passe :");
        identifiantText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;-fx-text-fill: #2c2c2c;");
        motdepasseText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;-fx-text-fill: #2c2c2c;");
        this.identifiant.setPromptText("Entrez votre identifiant");
        this.motdepasse.setPromptText("Entrez votre mot de passe");
        this.identifiant.setMinWidth(350);
        this.motdepasse.setMinWidth(350);
        this.identifiant.setMinHeight(50);
        this.motdepasse.setMinHeight(50);
        this.identifiant.setStyle("-fx-font-size: 20px; -fx-background-color: white; -fx-border-color: #2c2c2c; " +
                                   "-fx-border-width: 2; -fx-border-radius: 0;");
        this.motdepasse.setStyle("-fx-font-size: 20px; -fx-background-color: white; -fx-border-color: #2c2c2c; " +
                                   "-fx-border-width: 2; -fx-border-radius: 0;");
        idcontainer.getChildren().addAll(identifiantText,this.identifiant);
        motdepassecontainer.getChildren().addAll(motdepasseText,this.motdepasse);


        // Bouton de connexion, d'inscription et annuler
        HBox loginContainer = new HBox();
        Button inscription = new Button("INSCRIPTION");
        inscription.setUserData("INSCRIPTION");
        inscription.setOnAction(new ControleurRetourRedirection(this));
        inscription.setMinWidth(200);
        inscription.setMinHeight(100);
        inscription.setFont(Font.font("Arial", 15));
        inscription.setStyle("-fx-background-color: white; -fx-text-fill: #1e1e1e; " +
               "-fx-border-color: #1e1e1e; -fx-border-width: 2; " +
               "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;");

        inscription.setOnMouseEntered(e -> 
        inscription.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white; " +
                   "-fx-border-color: #1e1e1e; -fx-border-width: 2; " +
                   "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));

        inscription.setOnMouseExited(e -> 
        inscription.setStyle("-fx-background-color: white; -fx-text-fill: #1e1e1e; " +
                   "-fx-border-color: #1e1e1e; -fx-border-width: 2; " +
                   "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));
        //griser le bouton s'inscrire pour les admins et vendeurs
        if(!(getLUtilisateur().equals("CLIENT"))){
            inscription.setDisable(true);
        }
        Button connexion = new Button("SE CONNECTER");
        connexion.setUserData("CONNEXION");
        connexion.setOnAction(new ControleurConnexion(this, new ClientBD(connection), new VendeurBD(connection), new AdministrateurBD(connection), new LivreBD(connection)));
        connexion.setMinWidth(200);
        connexion.setMinHeight(100);
        connexion.setFont(Font.font("Arial", 15));
        connexion.setStyle("-fx-background-color: white; -fx-text-fill: #1e1e1e; " +
               "-fx-border-color: #1e1e1e; -fx-border-width: 2; " +
               "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;");

        connexion.setOnMouseEntered(e -> 
        connexion.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white; " +
                   "-fx-border-color: #1e1e1e; -fx-border-width: 2; " +
                   "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));

        connexion.setOnMouseExited(e -> 
        connexion.setStyle("-fx-background-color: white; -fx-text-fill: #1e1e1e; " +
                   "-fx-border-color: #1e1e1e; -fx-border-width: 2; " +
                   "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));
        
        Button retour = new Button("⟵ Annuler");
        retour.setUserData("ANNULERC");
        retour.setOnAction(new ControleurRetourRedirection(this));
        retour.setMinWidth(200);
        retour.setMinHeight(100);
        retour.setFont(Font.font("Arial", 15));
        retour.setStyle("-fx-background-color: white; -fx-text-fill: #1e1e1e; " +
               "-fx-border-color: #1e1e1e; -fx-border-width: 2; " +
               "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;");

        retour.setOnMouseEntered(e -> 
        retour.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white; " +
                   "-fx-border-color: #1e1e1e; -fx-border-width: 2; " +
                   "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));

        retour.setOnMouseExited(e -> 
        retour.setStyle("-fx-background-color: white; -fx-text-fill: #1e1e1e; " +
                   "-fx-border-color: #1e1e1e; -fx-border-width: 2; " +
                   "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));

        // création du pannel central
        VBox pannelcentrale = new VBox();
        pannelcentrale.setAlignment(Pos.CENTER);
        loginContainer.getChildren().addAll(retour, inscription, connexion);
        loginContainer.setSpacing(30);

        // L'écart important vient de cette ligne :
        pannelcentrale.setSpacing(100); 
        pannelcentrale.getChildren().addAll(idcontainer, motdepassecontainer, loginContainer);
        pannelcentrale.setPadding(new Insets(80, 0, 0, 60));

        // Ajout des éléments au panel central
        centerPanel.getChildren().addAll(titleContainer, pannelcentrale);

        // Initialisation des autres panels
        this.gauche = new Pane();
        this.panelCentral = new BorderPane();
        this.panelCentral.getChildren().add(centerPanel);
        this.panelCentral.setStyle("-fx-background-color: #c8c4b8;"); // couleur de fond du panel central
        this.gauche.getChildren().add(imgG);
    }
    /**
     * création de la fenetre d'inscription d'un client
     */
    private void fenetreInscription(){
        // Utilisation de l'image des livres 
        ImageView imgG = new ImageView(this.lesImages.get(8));
        imgG.setFitWidth(600);
        imgG.setFitHeight(1080);
        imgG.setPreserveRatio(false);

        // Partie droite - Formulaire d'inscription
        VBox centerPanel = new VBox();
        centerPanel.setPrefWidth(450);
        centerPanel.setPrefHeight(200);
        centerPanel.setPadding(new Insets(100,0,0,225));
        centerPanel.setStyle("-fx-background-color: #2c2c2c;");

        // Titre "Livre Express"
        ImageView titreLVE = new ImageView(this.lesImages.get(0));
        Text titreText = new Text("Connexion Compte " + this.lUtilisateur.getText());
        titreText.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;-fx-text-fill: #2c2c2c;");
        titreLVE.setFitWidth(800);
        titreLVE.setFitHeight(200);
        titreLVE.setPreserveRatio(true);

        // Ligne décorative
        Region ligne = new Region();
        ligne.setPrefHeight(3);
        ligne.setPrefWidth(220);
        ligne.setStyle("-fx-border-color: #2c2c2c;");

        // Conteneur pour le titre et la ligne
        VBox titleContainer = new VBox();
        titleContainer.setSpacing(5);
        titleContainer.getChildren().addAll(titreLVE,titreText, ligne);

        // Vbox contenant toutes les HBox "ligne"
        VBox dadContainer = new VBox();
        //chaque vbox sert à contenir un label et un textfield
        VBox hbox1 = new VBox();
        VBox hbox2 = new VBox();
        VBox hbox3 = new VBox();
        VBox hbox4 = new VBox();
        VBox hbox5 = new VBox();
        VBox hbox6 = new VBox();
        //chaque ligne contient 2 vbox
        HBox ligne1 = new HBox(70);
        HBox ligne2 = new HBox(70);
        HBox ligne3 = new HBox(70);
        //création des textfield et labels
        TextField nom = new TextField();
        TextField prenom = new TextField();
        PasswordField motdepasse = new PasswordField();
        TextField adresse = new TextField();
        TextField ville = new TextField();
        TextField codepostal = new TextField();
        this.inscriptions = new ArrayList<TextField>();
        this.inscriptions.add(nom);
        this.inscriptions.add(prenom);
        this.inscriptions.add(motdepasse);
        this.inscriptions.add(adresse);
        this.inscriptions.add(ville);
        this.inscriptions.add(codepostal);
        Text nomText = new Text("Nom :");
        Text prenomText = new Text("Prénom :");
        Text adresseText = new Text("Adresse :");
        Text villeText = new Text("Ville :");
        Text codepostalText = new Text("Code Postal :");
        Text motdepasseText = new Text("Mot de passe :");
        nomText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;-fx-text-fill: #2c2c2c;");
        prenomText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;-fx-text-fill: #2c2c2c;");
        adresseText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;-fx-text-fill: #2c2c2c;");
        villeText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;-fx-text-fill: #2c2c2c;");
        codepostalText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;-fx-text-fill: #2c2c2c;");
        motdepasseText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;-fx-text-fill: #2c2c2c;");
        nom.setPromptText("Entrez votre nom");
        prenom.setPromptText("Entrez votre prénom");
        motdepasse.setPromptText("Entrez votre mot de passe");
        adresse.setPromptText("Entrez votre adresse");
        ville.setPromptText("Entrez votre ville");
        codepostal.setPromptText("Entrez votre code postal");
        nom.setPrefWidth(350);
        motdepasse.setPrefWidth(350);
        nom.setMinHeight(50);
        motdepasse.setMinHeight(50);
        prenom.setPrefWidth(350);
        prenom.setMinHeight(50);
        adresse.setPrefWidth(350);
        adresse.setMinHeight(50);
        ville.setPrefWidth(350);
        ville.setMinHeight(50);
        codepostal.setPrefWidth(350);
        codepostal.setMinHeight(50);
        nom.setStyle("-fx-font-size: 20px; -fx-background-color: white; -fx-border-color: #2c2c2c; " +
                                   "-fx-border-width: 2; -fx-border-radius: 0;");
        prenom.setStyle("-fx-font-size: 20px; -fx-background-color: white; -fx-border-color: #2c2c2c; " +
                                   "-fx-border-width: 2; -fx-border-radius: 0;");
        adresse.setStyle("-fx-font-size: 20px; -fx-background-color: white; -fx-border-color: #2c2c2c; " +
                                   "-fx-border-width: 2; -fx-border-radius: 0;");
        ville.setStyle("-fx-font-size: 20px; -fx-background-color: white; -fx-border-color: #2c2c2c; " +
                                   "-fx-border-width: 2; -fx-border-radius: 0;");
        codepostal.setStyle("-fx-font-size: 20px; -fx-background-color: white; -fx-border-color: #2c2c2c; " +
                                   "-fx-border-width: 2; -fx-border-radius: 0;");
        motdepasse.setStyle("-fx-font-size: 20px; -fx-background-color: white; -fx-border-color: #2c2c2c; " +
                                   "-fx-border-width: 2; -fx-border-radius: 0;");
        hbox1.getChildren().addAll(nomText, nom);
        hbox2.getChildren().addAll(prenomText, prenom);
        hbox3.getChildren().addAll(adresseText, adresse);
        hbox4.getChildren().addAll(villeText, ville);
        hbox5.getChildren().addAll(codepostalText, codepostal);
        hbox6.getChildren().addAll(motdepasseText, motdepasse);
        ligne1.getChildren().addAll(hbox1, hbox2);
        ligne2.getChildren().addAll(hbox6,hbox3);
        ligne3.getChildren().addAll(hbox4, hbox5);
        ligne1.setPadding(new Insets(0,0,30,40));
        ligne2.setPadding(new Insets(0,0,30,40));
        ligne3.setPadding(new Insets(0,0,30,40));
        dadContainer.getChildren().addAll(ligne1, ligne2, ligne3);


        // Bouton d'inscription et annuler
        HBox loginContainer = new HBox();
        Button retour = new Button("⟵ ANNULER");
        retour.setUserData("ANNULERI");
        retour.setOnAction(new ControleurRetourRedirection(this));
        retour.setMinWidth(200);
        retour.setMinHeight(100);
        retour.setFont(Font.font("Arial", 15));
        retour.setStyle("-fx-background-color: white; -fx-text-fill: #1e1e1e; " +
               "-fx-border-color: #1e1e1e; -fx-border-width: 2; " +
               "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;");

        retour.setOnMouseEntered(e -> 
        retour.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white; " +
                   "-fx-border-color: #1e1e1e; -fx-border-width: 2; " +
                   "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));

        retour.setOnMouseExited(e -> 
        retour.setStyle("-fx-background-color: white; -fx-text-fill: #1e1e1e; " +
                   "-fx-border-color: #1e1e1e; -fx-border-width: 2; " +
                   "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));
        
        Button creer = new Button("CREER COMPTE");
        creer.setUserData("CREER COMPTE");
        creer.setOnAction(new ControleurInscription(this, new ClientBD(connection)));
        creer.setMinWidth(200);
        creer.setMinHeight(100);
        creer.setFont(Font.font("Arial", 15));
        creer.setDisable(true);
        creer.setStyle("-fx-background-color: #154c45; -fx-text-fill:rgb(251, 255, 254);" +
               "-fx-border-color: #154c45; -fx-border-width: 2; " +
               "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;");

        creer.setOnMouseEntered(e -> 
        creer.setStyle("-fx-background-color: #154c45; -fx-text-fill: white; " +
                   "-fx-border-color: #154c45; -fx-border-width: 2; " +
                   "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));

        creer.setOnMouseExited(e -> 
        creer.setStyle("-fx-background-color: #154c45; -fx-text-fill: white; " +
                   "-fx-border-color: #154c45; -fx-border-width: 2; " +
                   "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));

        //grise le bouton s'inscrire tant que tous les textfield ne sont pas remplis
        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
        boolean allFilled = !nom.getText().trim().isEmpty()
                      && !prenom.getText().trim().isEmpty()
                      && !motdepasse.getText().trim().isEmpty()
                      && !adresse.getText().trim().isEmpty()
                      && !ville.getText().trim().isEmpty()
                      && !codepostal.getText().trim().isEmpty();

            creer.setDisable(!allFilled);
        };

        nom.textProperty().addListener(listener);
        prenom.textProperty().addListener(listener);
        motdepasse.textProperty().addListener(listener);
        adresse.textProperty().addListener(listener);
        ville.textProperty().addListener(listener);
        codepostal.textProperty().addListener(listener);

        
        loginContainer.getChildren().addAll(retour, creer);
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.setSpacing(500); 

        // Conteneur de la partie centrale de la page
        VBox pannelcentrale = new VBox();
        pannelcentrale.setAlignment(Pos.CENTER);
        
        // L'écart important vient de cette ligne :
        pannelcentrale.setSpacing(100); 
        pannelcentrale.getChildren().addAll(dadContainer, loginContainer);
        pannelcentrale.setPadding(new Insets(100, 0, 0, 10));

        // Ajout des éléments au panel central
        centerPanel.getChildren().addAll(titleContainer, pannelcentrale);

        // Initialisation des autres panels 
        this.gauche = new Pane();
        this.panelCentral = new BorderPane();
        this.panelCentral.setStyle("-fx-background-color: #c8c4b8;");
        this.panelCentral.getChildren().add(centerPanel);
        this.gauche.getChildren().add(imgG);

    }
    
    /**
     * création de la fenetre d'accueil du client
     */
    private void fenetreAccueilC() {
        //pas de partie de gauche
        this.gauche=null;

        //créer la bannière
        this.banniere=new BorderPane();
        banniere.setStyle("-fx-background-color: #084a48;");
        //logo
        ImageView imgLogo = new ImageView(this.lesImages.get(9));
        imgLogo.setFitWidth(520);
        imgLogo.setPreserveRatio(true);
        banniere.setPadding(new Insets(10,0,0,0));
        
        Label user = new Label("Bienvenue");
        user.setStyle("-fx-font-size: 27px; -fx-text-fill: white; -fx-font-weight: bold;");
        user.setPadding(new Insets(0,125,0,0));
        //boutons de droite
        HBox bouttons = new HBox();
        ImageView deco = new ImageView(this.lesImages.get(3));
        deco.setFitWidth(75);
        deco.setPreserveRatio(true);
        
        ImageView panier = new ImageView(this.lesImages.get(6));
        panier.setFitWidth(75);
        panier.setPreserveRatio(true);
        
        ImageView maison = new ImageView(this.lesImages.get(1));
        maison.setFitWidth(75);
        maison.setPreserveRatio(true);
        
        ImageView profil = new ImageView(this.lesImages.get(4));
        profil.setFitWidth(75);
        profil.setPreserveRatio(true);
        
        this.bDeconnexion = new Button(null,deco);
        bDeconnexion.setUserData("DECONNEXION");
        bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
        bDeconnexion.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        
        this.bPanier = new Button(null,panier);
        bPanier.setOnAction(new ControleurRetourRedirection(this));
        bPanier.setUserData("PANIER");
        bPanier.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        
        this.boutonMaison = new Button(null,maison);
        boutonMaison.setUserData("MAISON");
        boutonMaison.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        
        Button boutonProfil = new Button(null,profil);
        boutonProfil.setUserData("PROFIL");
        boutonProfil.setOnAction(new ControleurRetourRedirection(this));
        boutonProfil.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        
        bouttons.getChildren().addAll(bDeconnexion,bPanier,boutonMaison,boutonProfil);
        
        bouttons.setMargin(boutonMaison,new Insets(5));
        bouttons.setMargin(bDeconnexion,new Insets(5));
        bouttons.setMargin(bPanier,new Insets(5));
        bouttons.setMargin(boutonProfil,new Insets(5));
        banniere.setLeft(imgLogo);
        banniere.setCenter(user);
        banniere.setRight(bouttons);
        
        //panel central
        this.panelCentral= new BorderPane();
        //création de la barre de recherche: un bouton, un textfield, un bouton
        ImageView loupe = new ImageView(this.lesImages.get(5));
        loupe.setFitWidth(43);
        loupe.setPreserveRatio(true);
        
        ImageView croix = new ImageView(this.lesImages.get(2));
        croix.setFitWidth(43);
        croix.setPreserveRatio(true);
        
        
        HBox barreRecherche = new HBox();
        barreRecherche.setAlignment(Pos.TOP_CENTER);
        

        TextField tf = new TextField();
        tf.setPromptText("Entre un livre à rechercher ...");
        tf.setPrefWidth(500);
        tf.setStyle("-fx-font-size: 25px;");

        Button boutonLoupe = new Button(null, loupe);
        Client cli = (Client) this.personneConnectee;
        boutonLoupe.setOnAction(new ControleurRecher(this, new LivreBD(connection), tf) );
        Button boutonCroix = new Button(null, croix);


        barreRecherche.getChildren().addAll(boutonLoupe, tf, boutonCroix);
        barreRecherche.setPadding(new Insets(120, 0, 0, 0));
        panelCentral.setTop(barreRecherche);
        this.panelCentral.setStyle("-fx-background-color: #c8c4b8;");

        // livres recommandés à afficher
        VBox listLivres = new VBox();
        listLivres.setPadding(new Insets(110, 150, 0, 500));
        this.setCataloguesC();
        int pageMax = this.catalogues.size(); // nombre total de pages
        List<Livre> livresPage = this.catalogues.get(this.pageCataActu);
        int ajustement = ajouteEspace();

        // HBox pour navigation pagination
        HBox pagination = new HBox(20);
        pagination.setAlignment(Pos.CENTER);
        Button btnPrev = new Button("<");
        btnPrev.setUserData("AVANT");
        btnPrev.setOnAction(new ControleurDefiler(this, "CLIENT"));
        Button btnNext = new Button(">");
        btnNext.setUserData("APRES");
        btnNext.setOnAction(new ControleurDefiler(this, "CLIENT"));

        // Correction pagination et affichage page
        int currentPage = (livresPage == null || livresPage.isEmpty()) ? 0 : this.pageCataActu;
        int totalPages = (pageMax == 0) ? 0 : pageMax;
        Label pageLabel = new Label("Page " + currentPage + " / " + totalPages);
        pageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        btnPrev.setStyle("-fx-font-size: 22px; -fx-background-color: #154c45; -fx-text-fill: white;");
        btnNext.setStyle("-fx-font-size: 22px; -fx-background-color: #154c45; -fx-text-fill: white;");
        btnPrev.setDisable(totalPages <= 1 || currentPage <= 1);
        btnNext.setDisable(totalPages <= 1 || currentPage >= totalPages);
        if (totalPages == 0) {
            btnPrev.setDisable(true);
            btnNext.setDisable(true);
        }
        pagination.getChildren().addAll(btnPrev, pageLabel, btnNext);
        pagination.setPadding(new Insets(0, 0, 30, 0));
        Client commandecli = (Client)this.personneConnectee;
        int cptbtn = 0;
        if (livresPage == null || livresPage.isEmpty()) {
            Label noLivre = new Label("Aucun livre à afficher pour cette page.");
            noLivre.setStyle("-fx-font-size: 22px; -fx-text-fill: #154c45;");
            listLivres.getChildren().add(noLivre);
        } else {
            for (Livre livreVue : livresPage) {
                //ligne qui correspond à un livre
                HBox livre = new HBox(30);
                Label titre = new Label(livreVue.getTitre()+ " ".repeat(ajustement-livreVue.getTitre().length())); //" ".repeat(ajustement-livreVue.getTitre().length()));
                titre.setStyle("-fx-font-size: 25px;");
                Label prix = new Label(String.format("%.2f €", livreVue.getPrix()));
                prix.setStyle("-fx-font-size: 25px;");
                TextField quantite = new TextField("0");
                quantite.setStyle("-fx-font-size: 25px;");
                Button ajPanier = new Button("Ajouter Panier");
                ChangeListener<String> listener = (observable, oldValue, newValue) -> {
                    boolean allFilled = !quantite.getText().trim().isEmpty();
                    ajPanier.setDisable(!allFilled);
                };
                quantite.textProperty().addListener(listener);
                Integer qte = Integer.parseInt(quantite.getText());
                ajPanier.setOnAction(new ControleurAjouterLivre(this, commandecli, livreVue, qte, this.connection));

                cptbtn++;
                ajPanier.setUserData(String.valueOf(cptbtn));
                ajPanier.setStyle("-fx-text-fill: white; " +
                    "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                    "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 22px; -fx-background-color: #154c45;");
                livre.getChildren().addAll(titre, prix, quantite, ajPanier);
                livre.setPadding(new Insets(10, 0, 10, 0));
                ajPanier.setPadding(new Insets(7, 0, 7, 0));
                listLivres.getChildren().add(livre);
            }
        }

        // Ajoute la pagination en bas de la liste
        this.panelCentral.setBottom(pagination);
        this.panelCentral.setCenter(listLivres);
        
    }   

    /**
     * création de la fenetre du panier
     */
    private void fenetrePanier() {
        //pas de partie de gauche
        this.gauche=null;

        //créer la bannière
        this.banniere=new BorderPane();
        banniere.setStyle("-fx-background-color: #084a48;");
        //logo
        ImageView imgLogo = new ImageView(this.lesImages.get(9));
        imgLogo.setFitWidth(520);
        imgLogo.setPreserveRatio(true);
        banniere.setPadding(new Insets(10,0,0,0));
        
        Label user = new Label("Bienvenue");
        user.setStyle("-fx-font-size: 27px; -fx-text-fill: white; -fx-font-weight: bold;");
        user.setPadding(new Insets(0,125,0,0));
        //boutons de droite
        HBox bouttons = new HBox();
        ImageView deco = new ImageView(this.lesImages.get(3));
        deco.setFitWidth(75);
        deco.setPreserveRatio(true);
        
        ImageView panier = new ImageView(this.lesImages.get(6));
        panier.setFitWidth(75);
        panier.setPreserveRatio(true);
        
        ImageView maison = new ImageView(this.lesImages.get(1));
        maison.setFitWidth(75);
        maison.setPreserveRatio(true);
        
        ImageView profil = new ImageView(this.lesImages.get(4));
        profil.setFitWidth(75);
        profil.setPreserveRatio(true);
        
        this.bDeconnexion = new Button(null,deco);
        bDeconnexion.setUserData("DECONNEXION");
        bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
        bDeconnexion.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        
        this.bPanier = new Button(null,panier);
        bPanier.setOnAction(new ControleurRetourRedirection(this));
        bPanier.setUserData("PANIER");
        bPanier.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        
        this.boutonMaison = new Button(null,maison);
        boutonMaison.setUserData("MAISON");
        boutonMaison.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        
        Button boutonProfil = new Button(null,profil);
        boutonProfil.setUserData("PROFIL");
        boutonProfil.setOnAction(new ControleurRetourRedirection(this));
        boutonProfil.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        
        bouttons.getChildren().addAll(bDeconnexion,bPanier,boutonMaison,boutonProfil);
        
        bouttons.setMargin(boutonMaison,new Insets(5));
        bouttons.setMargin(bDeconnexion,new Insets(5));
        bouttons.setMargin(bPanier,new Insets(5));
        bouttons.setMargin(boutonProfil,new Insets(5));
        banniere.setLeft(imgLogo);
        banniere.setCenter(user);
        banniere.setRight(bouttons);

        //panel central
        this.panelCentral= new BorderPane();

        //livres dans le panier
        VBox listLivres = new VBox();
        
        for(int i= 0; i<4;i++){
            //ligne pour un livre
            HBox livre = new HBox(15);
            Label titre = new Label("titre"+i);
            titre.setStyle("-fx-font-size: 25px;");
            Label prix = new Label("prix total livre "+i);
            prix.setStyle("-fx-font-size: 25px;");
            Label quantite = new Label("1");
            quantite.setStyle("-fx-font-size: 25px;");
            Button suPanier = new Button("Supprimer Panier");
            suPanier.setStyle("-fx-font-size: 25px;");
            livre.getChildren().addAll(titre,prix,quantite,suPanier);
            livre.setPadding(new Insets(0,0,0,50));
            livre.setStyle("-fx-background-color: #154c45;");
            listLivres.getChildren().addAll(livre);
            listLivres.setMargin(livre,new Insets(0,655,0,665));
        }

        // ligne sous le panier pour idiquer les informations générales
        HBox total = new HBox(25);
        Label prixTotal = new Label("prix Total");
        prixTotal.setStyle("-fx-font-size: 25px;");
        RadioButton livraison = new RadioButton("Livraison en magasin");
        livraison.setStyle("-fx-font-size: 25px;");
        Button comPanier = new Button("Commander Panier");
        comPanier.setStyle("-fx-font-size: 25px;");
        total.getChildren().addAll(prixTotal,livraison,comPanier);
        total.setPadding(new Insets(150,0,0,650));

        listLivres.getChildren().addAll(total);
        this.panelCentral.setStyle("-fx-background-color: #c8c4b8;");
        panelCentral.setMargin(listLivres,new Insets(150,0,150,0));
        panelCentral.setCenter(listLivres);    
    
    }

    /**
     * crée la fenetre d'accueil du vendeur
     */
    private void fenetreAccueilV(){
        //pas de partie de gauche
        this.gauche=null;

        //créer la bannière
        this.banniere=new BorderPane();
        banniere.setStyle("-fx-background-color: #084a48;");
        //logo
        ImageView imgLogo = new ImageView(this.lesImages.get(9));
        imgLogo.setFitWidth(520);
        imgLogo.setPreserveRatio(true);
        banniere.setPadding(new Insets(10,0,0,0));
        
        Label user = new Label("Bienvenue");
        user.setStyle("-fx-font-size: 27px; -fx-text-fill: white; -fx-font-weight: bold;");
        user.setPadding(new Insets(0,125,0,0));

        //boutons de droite
        HBox bouttons = new HBox();
        ImageView deco = new ImageView(this.lesImages.get(3));
        deco.setFitWidth(75);
        deco.setPreserveRatio(true);
        
        ImageView maison = new ImageView(this.lesImages.get(1));
        maison.setFitWidth(75);
        maison.setPreserveRatio(true);
        
        ImageView profil = new ImageView(this.lesImages.get(4));
        profil.setFitWidth(75);
        profil.setPreserveRatio(true);
        
        this.bDeconnexion = new Button(null,deco);
        bDeconnexion.setUserData("DECONNEXION");
        bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
        bDeconnexion.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        
        this.boutonMaison = new Button(null,maison);
        boutonMaison.setUserData("MAISON");
        boutonMaison.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        
        Button boutonProfil = new Button(null,profil);
        boutonProfil.setUserData("PROFIL");
        boutonProfil.setOnAction(new ControleurRetourRedirection(this));
        boutonProfil.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        
        bouttons.getChildren().addAll(bDeconnexion,boutonMaison,boutonProfil);
        
        bouttons.setMargin(boutonMaison,new Insets(5));
        bouttons.setMargin(bDeconnexion,new Insets(5));
        bouttons.setMargin(boutonProfil,new Insets(5));
        banniere.setLeft(imgLogo);
        banniere.setCenter(user);
        banniere.setRight(bouttons);

        //panel central
        this.panelCentral= new BorderPane();

        //liste des options (boutons) du vendeur
        VBox listLivres = new VBox(50);
        
        //les boutons
        Button ajLivre = new Button("Ajouter un livre");
        ajLivre.setUserData("AJOUTER LIVRE");
        ajLivre.setOnAction( new ControleurRetourRedirection(this));
        ajLivre.setStyle("-fx-text-fill: white; " +
                      "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 28px; -fx-background-color: #154c45;");
        ajLivre.setPrefWidth(400);

        Button supLivre = new Button("Supprimer un livre");
        supLivre.setStyle("-fx-text-fill: white; " +
                      "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 28px; -fx-background-color: #154c45;");
        supLivre.setPrefWidth(400);

        Button modifQte = new Button("Modifier la quantité d'un livre");
        modifQte.setStyle("-fx-text-fill: white; " +
                      "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 28px; -fx-background-color: #154c45;");
        modifQte.setPrefWidth(400);

        Button verifDispo = new Button("Vérifier la disponibilité d'un livre");
        verifDispo.setUserData("VERIFIER DISPO");
        verifDispo.setOnAction(new ControleurRetourRedirection(this));
        verifDispo.setStyle("-fx-text-fill: white; " +
                      "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 28px; -fx-background-color: #154c45;");
        verifDispo.setPrefWidth(400);


        Button comCli = new Button("Passer une commande");
        comCli.setStyle("-fx-text-fill: white; " +
                      "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 28px; -fx-background-color: #154c45;");
        comCli.setPrefWidth(400);

        Button transLivre = new Button("Transférer un livre");
        transLivre.setStyle("-fx-text-fill: white; " +
                      "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 28px; -fx-background-color: #154c45;");
        transLivre.setPrefWidth(400);

        listLivres.getChildren().addAll(ajLivre,supLivre,modifQte, verifDispo,comCli,transLivre); // gererStock ?
        
        
        listLivres.setPadding(new Insets(150,600,200,780));
        listLivres.setStyle(" -fx-background-color: #c8c4b8;");

        panelCentral.setCenter(listLivres);
        
    }
    
    private void fenetreAccueilA(){
        //pas de partie de gauche
        this.gauche=null;

        //créer la bannière
        this.banniere=new BorderPane();
        banniere.setStyle("-fx-background-color: #084a48;");
        //logo
        ImageView imgLogo = new ImageView(this.lesImages.get(9));
        imgLogo.setFitWidth(520);
        imgLogo.setPreserveRatio(true);
        banniere.setPadding(new Insets(10,0,0,0));
        
        Label user = new Label("Bienvenue");
        user.setStyle("-fx-font-size: 27px; -fx-text-fill: white; -fx-font-weight: bold;");
        user.setPadding(new Insets(0,125,0,0));
        //boutons de droite
        HBox bouttons = new HBox();
        ImageView deco = new ImageView(this.lesImages.get(3));
        deco.setFitWidth(75);
        deco.setPreserveRatio(true);
        
        
        ImageView maison = new ImageView(this.lesImages.get(1));
        maison.setFitWidth(75);
        maison.setPreserveRatio(true);
        
        ImageView profil = new ImageView(this.lesImages.get(4));
        profil.setFitWidth(75);
        profil.setPreserveRatio(true);
        
        this.bDeconnexion = new Button(null,deco);
        bDeconnexion.setUserData("DECONNEXION");
        bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
        bDeconnexion.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        
        
        this.boutonMaison = new Button(null,maison);
        boutonMaison.setUserData("MAISON");
        boutonMaison.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        
        Button boutonProfil = new Button(null,profil);
        boutonProfil.setUserData("PROFIL");
        boutonProfil.setOnAction(new ControleurRetourRedirection(this));
        boutonProfil.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        
        bouttons.getChildren().addAll(bDeconnexion,boutonMaison,boutonProfil);
        
        bouttons.setMargin(boutonMaison,new Insets(5));
        bouttons.setMargin(bDeconnexion,new Insets(5));

        bouttons.setMargin(boutonProfil,new Insets(5));
        banniere.setLeft(imgLogo);
        banniere.setCenter(user);
        banniere.setRight(bouttons);


        //panel central
        this.panelCentral= new BorderPane();

        //liste des actions (boutons) de l'administrateur
        VBox listLivres = new VBox(40);
        //les boutons
        Button stats = new Button("Consulter les statistiques");
        stats.setUserData("STATS");
        stats.setOnAction(new ControleurRetourRedirection(this));
        stats.setStyle("-fx-text-fill: white; " +
                      "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 28px; -fx-background-color: #154c45;");
        stats.setPrefWidth(400);

        Button creerVendeur = new Button("Créer un vendeur");
        creerVendeur.setUserData("CREER VENDEUR");
        creerVendeur.setOnAction(new ControleurRetourRedirection(this));
        creerVendeur.setStyle("-fx-text-fill: white; " +
                      "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 28px; -fx-background-color: #154c45;");
        creerVendeur.setPrefWidth(400);

        Button ajMag = new Button("Ajouter une librairie");
        ajMag.setUserData("AJOUTER MAGASIN");
        ajMag.setOnAction(new ControleurRetourRedirection(this));
        ajMag.setStyle("-fx-text-fill: white; " +
                      "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 28px; -fx-background-color: #154c45;");
        ajMag.setPrefWidth(400);

        Button stocks = new Button("Gérer les stocks globaux");
        stocks.setUserData("GERER STOCKS");
        stocks.setOnAction(new ControleurRetourRedirection(this));
        stocks.setStyle("-fx-text-fill: white; " +
                      "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 28px; -fx-background-color: #154c45;");
        stocks.setPrefWidth(400);

        Button optVendeur = new Button("Options vendeur");
        optVendeur.setStyle("-fx-text-fill: white; " +
                      "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 28px; -fx-background-color: #154c45;");
        optVendeur.setPrefWidth(400);
        optVendeur.setUserData("OPTVENDEUR");
        optVendeur.setOnAction(new ControleurRetourRedirection(this));

        listLivres.getChildren().addAll(stats,creerVendeur,ajMag,stocks,optVendeur);
        listLivres.setMargin(stats,new Insets(5,20,5,20));
        listLivres.setMargin(creerVendeur,new Insets(5,20,5,20));
        listLivres.setMargin(ajMag,new Insets(5,20,5,20));
        listLivres.setMargin(stocks,new Insets(5,20,5,20));
        listLivres.setMargin(optVendeur, new Insets(5,20,5,20));
        
        listLivres.setPadding(new Insets(200,600,200,780));
        //listLivres.setStyle("-fx-background-color: #2c2c2c;");   
        listLivres.setStyle("-fx-background-color: #c8c4b8;");

        panelCentral.setCenter(listLivres);
    }
    /**
     * fenetre du profil du client
     */
    private void fenetreProfilC(){
    // Image à gauche
    ImageView imgG = new ImageView(this.lesImages.get(8));
    imgG.setFitWidth(600);
    imgG.setFitHeight(1080);
    imgG.setPreserveRatio(false);

    this.gauche = new Pane();
    this.gauche.getChildren().add(imgG);

    // Bannière en haut
    this.banniere = new BorderPane();
    banniere.setStyle("-fx-background-color:  #154c45;"); 
    
    ImageView imgLogo = new ImageView(this.lesImages.get(9));
    imgLogo.setFitWidth(520);
    imgLogo.setPreserveRatio(true);
    banniere.setPadding(new Insets(10,0,0,0));

    Label user = new Label("PROFIL UTILISATEUR");
    user.setStyle("-fx-font-size: 27px; -fx-text-fill: white; -fx-font-style: italic;");
    user.setPadding(new Insets(0,125,0,0));

    // Boutons de navigation dans la bannière
    HBox bouttons = new HBox();
    ImageView deco = new ImageView(this.lesImages.get(3));
    deco.setFitWidth(75);
    deco.setPreserveRatio(true);

    ImageView panier = new ImageView(this.lesImages.get(6));
    panier.setFitWidth(75);
    panier.setPreserveRatio(true);

    ImageView maison = new ImageView(this.lesImages.get(1));
    maison.setFitWidth(75);
    maison.setPreserveRatio(true);

    ImageView profil = new ImageView(this.lesImages.get(4));
    profil.setFitWidth(75);
    profil.setPreserveRatio(true);

    this.bDeconnexion = new Button(null,deco);
    bDeconnexion.setUserData("DECONNEXION");
    bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
    bDeconnexion.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    this.bPanier = new Button(null,panier);
    bPanier.setOnAction(new ControleurRetourRedirection(this));
    bPanier.setUserData("PANIER");
    bPanier.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    this.boutonMaison = new Button(null,maison);
    boutonMaison.setUserData("MAISON");
    boutonMaison.setOnAction(new ControleurRetourRedirection(this));
    boutonMaison.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    this.boutonProfil = new Button(null,profil);
    boutonProfil.setUserData("PROFIL");
    boutonProfil.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    bouttons.getChildren().addAll(bDeconnexion, bPanier, boutonMaison, boutonProfil);
    bouttons.setMargin(boutonMaison, new Insets(5));
    bouttons.setMargin(bDeconnexion, new Insets(5));
    bouttons.setMargin(bPanier, new Insets(5));
    bouttons.setMargin(boutonProfil, new Insets(5));
    
    banniere.setLeft(imgLogo);
    banniere.setCenter(user);
    banniere.setRight(bouttons);

    // Panel central avec les informations du profil
    this.panelCentral = new BorderPane();
    panelCentral.setStyle("-fx-background-color: #c8c4b8;");

    // Container principal pour les informations
    VBox infoContainer = new VBox();
    infoContainer.setAlignment(Pos.CENTER);
    infoContainer.setSpacing(30);
    infoContainer.setPadding(new Insets(100, 200, 100, 200));

    // Labels des informations utilisateur
    Client client = (Client) this.personneConnectee;
    Label identifiantLabel = new Label("IDENTIFIANT :  " + client.getIdentifiant());
    identifiantLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

    Label nomLabel = new Label("NOM :  " + client.getNom());
    nomLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

    Label prenomLabel = new Label("PRÉNOM :  " + client.getPrenom());
    prenomLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

    Label adresseLabel = new Label("ADRESSE :  " + client.getAdresse());
    adresseLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

    Label villeLabel = new Label("VILLE :  " + client.getVille());
    villeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

    Label codePostalLabel = new Label("CODE POSTAL :  " + client.getCodePostal());
    codePostalLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");


    // Boutons en bas
    HBox boutonsContainer = new HBox();
    boutonsContainer.setAlignment(Pos.CENTER);
    boutonsContainer.setSpacing(50);
    boutonsContainer.setPadding(new Insets(50, 0, 0, 0));

    Button voirPanierBtn = new Button("VOIR LE PANIER");
    voirPanierBtn.setUserData("PANIER");
    voirPanierBtn.setOnAction(new ControleurRetourRedirection(this));
    voirPanierBtn.setPrefWidth(250);
    voirPanierBtn.setPrefHeight(60);
    voirPanierBtn.setStyle("-fx-background-color: #8a8a8a; -fx-text-fill: white; " +
                          "-fx-font-size: 18px; -fx-font-weight: bold; " +
                          "-fx-border-radius: 30; -fx-background-radius: 30;");

    Button deconnexionBtn = new Button("SE DÉCONNECTER");
    deconnexionBtn.setUserData("DECONNEXION");
    deconnexionBtn.setOnAction(new ControleurRetourRedirection(this));
    deconnexionBtn.setPrefWidth(250);
    deconnexionBtn.setPrefHeight(60);
    deconnexionBtn.setStyle("-fx-background-color: #8a8a8a; -fx-text-fill: white; " +
                           "-fx-font-size: 18px; -fx-font-weight: bold; " +
                           "-fx-border-radius: 30; -fx-background-radius: 30;");

    // Effets de survol pour les boutons
    voirPanierBtn.setOnMouseEntered(e -> 
        voirPanierBtn.setStyle("-fx-background-color: #666666; -fx-text-fill: white; " +
                              "-fx-font-size: 18px; -fx-font-weight: bold; " +
                              "-fx-border-radius: 30; -fx-background-radius: 30;"));
    voirPanierBtn.setOnMouseExited(e -> 
        voirPanierBtn.setStyle("-fx-background-color: #8a8a8a; -fx-text-fill: white; " +
                              "-fx-font-size: 18px; -fx-font-weight: bold; " +
                              "-fx-border-radius: 30; -fx-background-radius: 30;"));

    deconnexionBtn.setOnMouseEntered(e -> 
        deconnexionBtn.setStyle("-fx-background-color: #666666; -fx-text-fill: white; " +
                               "-fx-font-size: 18px; -fx-font-weight: bold; " +
                               "-fx-border-radius: 30; -fx-background-radius: 30;"));
    deconnexionBtn.setOnMouseExited(e -> 
        deconnexionBtn.setStyle("-fx-background-color: #8a8a8a; -fx-text-fill: white; " +
                               "-fx-font-size: 18px; -fx-font-weight: bold; " +
                               "-fx-border-radius: 30; -fx-background-radius: 30;"));

    boutonsContainer.getChildren().addAll(voirPanierBtn, deconnexionBtn);

    // Ajout de tous les éléments au container principal
    infoContainer.getChildren().addAll(
        identifiantLabel, nomLabel, prenomLabel, 
        adresseLabel, villeLabel, codePostalLabel, 
        boutonsContainer
    );
    this.panelCentral.setStyle("-fx-background-color: #c8c4b8;");
    panelCentral.setCenter(infoContainer);
}
//--------------------------------------------------------------------------------------------------------------------------
    /**
     * fenetre du profil vendeur
     */
    public void fenetreProfilV(){
    // Image à gauche
    ImageView imgG = new ImageView(this.lesImages.get(8));
    imgG.setFitWidth(600);
    imgG.setFitHeight(1080);
    imgG.setPreserveRatio(false);

    this.gauche = new Pane();
    this.gauche.getChildren().add(imgG);

    // Bannière en haut
    this.banniere = new BorderPane();
    banniere.setStyle("-fx-background-color:  #154c45;"); 
    ImageView imgLogo = new ImageView(this.lesImages.get(9));
    imgLogo.setFitWidth(520);
    imgLogo.setPreserveRatio(true);
    banniere.setPadding(new Insets(10,0,0,0));

    Label user = new Label("PROFIL VENDEUR");
    user.setStyle("-fx-font-size: 27px; -fx-text-fill: white; -fx-font-style: italic;");
    user.setPadding(new Insets(0,125,0,0));

    // Boutons de navigation dans la bannière
    HBox bouttons = new HBox();
    ImageView deco = new ImageView(this.lesImages.get(3));
    deco.setFitWidth(75);
    deco.setPreserveRatio(true);

    ImageView maison = new ImageView(this.lesImages.get(1));
    maison.setFitWidth(75);
    maison.setPreserveRatio(true);

    ImageView profil = new ImageView(this.lesImages.get(4));
    profil.setFitWidth(75);
    profil.setPreserveRatio(true);

    this.bDeconnexion = new Button(null,deco);
    bDeconnexion.setUserData("DECONNEXION");
    bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
    bDeconnexion.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    this.bPanier = null;

    this.boutonMaison = new Button(null,maison);
    boutonMaison.setUserData("MAISON");
    boutonMaison.setOnAction(new ControleurRetourRedirection(this));
    boutonMaison.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    this.boutonProfil = new Button(null,profil);
    boutonProfil.setUserData("PROFIL");
    boutonProfil.setOnAction(new ControleurRetourRedirection(this));
    boutonProfil.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    bouttons.getChildren().addAll(bDeconnexion,  boutonMaison, boutonProfil);
    bouttons.setMargin(boutonMaison, new Insets(5));
    bouttons.setMargin(bDeconnexion, new Insets(5));
    bouttons.setMargin(boutonProfil, new Insets(5));
    
    banniere.setLeft(imgLogo);
    banniere.setCenter(user);
    banniere.setRight(bouttons);

    // Panel central avec les informations du profil
    this.panelCentral = new BorderPane();
    panelCentral.setStyle("-fx-background-color: #c8c4b8;");

    // Container principal pour les informations
    VBox infoContainer = new VBox();
    infoContainer.setAlignment(Pos.CENTER);
    infoContainer.setSpacing(30);
    infoContainer.setPadding(new Insets(100, 200, 100, 200));

    // Labels des informations utilisateur
    Vendeur vend = (Vendeur) this.personneConnectee;
    Label identifiantLabel = new Label("IDENTIFIANT :  " + vend.idVendeur());
    identifiantLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

    Label nomLabel = new Label("NOM :  " + vend.getNom());
    nomLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

    Label prenomLabel = new Label("PRÉNOM :  " + vend.getPrenom());
    prenomLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");
    Label magasinLabel;
    if (vend.getMagasin() == null) {
        magasinLabel = new Label("MAGASIN :  Aucun magasin associé");
        magasinLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        infoContainer.getChildren().add(magasinLabel);
    }
    else {
        magasinLabel = new Label("MAGASIN :  " + vend.getMagasin().getNomMag());
        magasinLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");
    }
;


    // Boutons en bas
    HBox boutonsContainer = new HBox();
    boutonsContainer.setAlignment(Pos.CENTER);
    boutonsContainer.setSpacing(50);
    boutonsContainer.setPadding(new Insets(50, 0, 0, 0));

    Button deconnexionBtn = new Button("SE DÉCONNECTER");
    deconnexionBtn.setUserData("DECONNEXION");
    deconnexionBtn.setOnAction(new ControleurRetourRedirection(this));
    deconnexionBtn.setPrefWidth(250);
    deconnexionBtn.setPrefHeight(60);
    deconnexionBtn.setStyle("-fx-background-color: #8a8a8a; -fx-text-fill: white; " +
                           "-fx-font-size: 18px; -fx-font-weight: bold; " +
                           "-fx-border-radius: 30; -fx-background-radius: 30;");

    deconnexionBtn.setOnMouseEntered(e -> 
        deconnexionBtn.setStyle("-fx-background-color: #666666; -fx-text-fill: white; " +
                               "-fx-font-size: 18px; -fx-font-weight: bold; " +
                               "-fx-border-radius: 30; -fx-background-radius: 30;"));
    deconnexionBtn.setOnMouseExited(e -> 
        deconnexionBtn.setStyle("-fx-background-color: #8a8a8a; -fx-text-fill: white; " +
                               "-fx-font-size: 18px; -fx-font-weight: bold; " +
                               "-fx-border-radius: 30; -fx-background-radius: 30;"));

    boutonsContainer.getChildren().addAll( deconnexionBtn);

    // Ajout de tous les éléments au container principal
    infoContainer.getChildren().addAll(
        identifiantLabel, nomLabel, prenomLabel, 
        magasinLabel, 
        boutonsContainer
    );
    this.panelCentral.setStyle("-fx-background-color: #c8c4b8;");
    panelCentral.setCenter(infoContainer);
}


//--------------------------------------------------------------------------------------------------------------------
    /**
     * fenetre du profil administrateur
     */
    public void fenetreProfilA(){
    // Image à gauche
    ImageView imgG = new ImageView(this.lesImages.get(8));
    imgG.setFitWidth(600);
    imgG.setFitHeight(1080);
    imgG.setPreserveRatio(false);

    this.gauche = new Pane();
    this.gauche.getChildren().add(imgG);

    // Bannière en haut
    this.banniere = new BorderPane();
    banniere.setStyle("-fx-background-color:  #154c45;"); 
    ImageView imgLogo = new ImageView(this.lesImages.get(9));
    imgLogo.setFitWidth(520);
    imgLogo.setPreserveRatio(true);
    banniere.setPadding(new Insets(10,0,0,0));

    Label user = new Label("PROFIL ADMIN");
    user.setStyle("-fx-font-size: 27px; -fx-text-fill: white; -fx-font-style: italic;");
    user.setPadding(new Insets(0,125,0,0));

    // Boutons de navigation dans la bannière
    HBox bouttons = new HBox();
    ImageView deco = new ImageView(this.lesImages.get(3));
    deco.setFitWidth(75);
    deco.setPreserveRatio(true);

    ImageView maison = new ImageView(this.lesImages.get(1));
    maison.setFitWidth(75);
    maison.setPreserveRatio(true);

    ImageView profil = new ImageView(this.lesImages.get(4));
    profil.setFitWidth(75);
    profil.setPreserveRatio(true);

    this.bDeconnexion = new Button(null,deco);
    bDeconnexion.setUserData("DECONNEXION");
    bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
    bDeconnexion.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    this.bPanier = null;

    this.boutonMaison = new Button(null,maison);
    boutonMaison.setUserData("MAISON");
    boutonMaison.setOnAction(new ControleurRetourRedirection(this));
    boutonMaison.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    this.boutonProfil = new Button(null,profil);
    boutonProfil.setUserData("PROFIL");
    boutonProfil.setOnAction(new ControleurRetourRedirection(this));
    boutonProfil.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    bouttons.getChildren().addAll(bDeconnexion, boutonMaison, boutonProfil);
    bouttons.setMargin(boutonMaison, new Insets(5));
    bouttons.setMargin(bDeconnexion, new Insets(5));
    bouttons.setMargin(boutonProfil, new Insets(5));
    
    banniere.setLeft(imgLogo);
    banniere.setCenter(user);
    banniere.setRight(bouttons);

    // Panel central avec les informations du profil
    this.panelCentral = new BorderPane();
    panelCentral.setStyle("-fx-background-color: #c8c4b8;");

    // Container principal pour les informations
    VBox infoContainer = new VBox();
    infoContainer.setAlignment(Pos.CENTER);
    infoContainer.setSpacing(30);
    infoContainer.setPadding(new Insets(100, 200, 100, 200));

    // Labels des informations utilisateur
    Administrateur admin = (Administrateur) this.personneConnectee;
    Label identifiantLabel = new Label("IDENTIFIANT :  " + admin.getId());
    identifiantLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

    Label nomLabel = new Label("NOM :  " + admin.getNom());
    nomLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

    Label prenomLabel = new Label("PRÉNOM :  " + admin.getPrenom());
    prenomLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");
    



    // Boutons en bas
    HBox boutonsContainer = new HBox();
    boutonsContainer.setAlignment(Pos.CENTER);
    boutonsContainer.setSpacing(50);
    boutonsContainer.setPadding(new Insets(50, 0, 0, 0));

    Button deconnexionBtn = new Button("SE DÉCONNECTER");
    deconnexionBtn.setUserData("DECONNEXION");
    deconnexionBtn.setOnAction(new ControleurRetourRedirection(this));
    deconnexionBtn.setPrefWidth(250);
    deconnexionBtn.setPrefHeight(60);
    deconnexionBtn.setStyle("-fx-background-color: #8a8a8a; -fx-text-fill: white; " +
                           "-fx-font-size: 18px; -fx-font-weight: bold; " +
                           "-fx-border-radius: 30; -fx-background-radius: 30;");

    deconnexionBtn.setOnMouseEntered(e -> 
        deconnexionBtn.setStyle("-fx-background-color: #666666; -fx-text-fill: white; " +
                               "-fx-font-size: 18px; -fx-font-weight: bold; " +
                               "-fx-border-radius: 30; -fx-background-radius: 30;"));
    deconnexionBtn.setOnMouseExited(e -> 
        deconnexionBtn.setStyle("-fx-background-color: #8a8a8a; -fx-text-fill: white; " +
                               "-fx-font-size: 18px; -fx-font-weight: bold; " +
                               "-fx-border-radius: 30; -fx-background-radius: 30;"));

    boutonsContainer.getChildren().addAll( deconnexionBtn);

    // Ajout de tous les éléments au container principal
    infoContainer.getChildren().addAll(
        identifiantLabel, nomLabel, prenomLabel, 
        boutonsContainer);
    this.panelCentral.setStyle("-fx-background-color: #c8c4b8;");
    panelCentral.setCenter(infoContainer);
    
    } 

    /**
     * fenetre pour créer une librairie
     */
    public void fenetreCreerLib(){
    //pas de partie gauche
    this.gauche = null;

    // Configuration de la bannière
    this.banniere = new BorderPane();
    banniere.setStyle("-fx-background-color: #154c45;");
    ImageView imgLogo = new ImageView(this.lesImages.get(9));
    imgLogo.setFitWidth(520);
    imgLogo.setPreserveRatio(true);
    banniere.setPadding(new Insets(10, 0, 0, 0));

    Label user = new Label("Création d'une librairie");
    user.setStyle("-fx-font-size: 27px; -fx-text-fill: white;");
    user.setPadding(new Insets(0, 125, 0, 0));

    // Boutons de navigation
    HBox bouttons = new HBox();
    ImageView deco = new ImageView(this.lesImages.get(3));
    deco.setFitWidth(75);
    deco.setPreserveRatio(true);

    ImageView maison = new ImageView(this.lesImages.get(1));
    maison.setFitWidth(75);
    maison.setPreserveRatio(true);

    ImageView profil = new ImageView(this.lesImages.get(4));
    profil.setFitWidth(75);
    profil.setPreserveRatio(true);

    Button bDeconnexion = new Button(null, deco);
    bDeconnexion.setUserData("DECONNEXION");
    bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
    bDeconnexion.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    Button boutonMaison = new Button(null, maison);
    boutonMaison.setUserData("MAISON");
    boutonMaison.setOnAction(new ControleurRetourRedirection(this));
    boutonMaison.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    Button boutonProfil = new Button(null, profil);
    boutonProfil.setUserData("PROFIL");
    boutonProfil.setOnAction(new ControleurRetourRedirection(this));
    boutonProfil.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    bouttons.getChildren().addAll(bDeconnexion, boutonMaison, boutonProfil);
    bouttons.setMargin(boutonMaison, new Insets(5));
    bouttons.setMargin(bDeconnexion, new Insets(5));
    bouttons.setMargin(boutonProfil, new Insets(5));
    banniere.setLeft(imgLogo);
    banniere.setCenter(user);
    banniere.setRight(bouttons);

    // Panel central
    this.panelCentral = new BorderPane();
    this.panelCentral.setStyle("-fx-background-color: #c8c4b8;");

    // Conteneur principal
    VBox mainContainer = new VBox(20);
    mainContainer.setAlignment(Pos.CENTER);

    // Titre
    Text titre = new Text("Ajouter une librairie");
    titre.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
    mainContainer.getChildren().add(titre);

    // Conteneur pour les champs de saisie 
    HBox fieldsContainer = new HBox(50);
    fieldsContainer.setAlignment(Pos.CENTER);
    fieldsContainer.setPadding(new Insets(50, 0, 0, 0));


    // Champ Nom
    Label nomLabel = new Label("Nom");
    nomLabel.setStyle("-fx-font-size: 20px;");
    TextField nomField = new TextField();
    nomField.setPromptText("Entrez le nom de la librairie");
    nomField.setPrefWidth(300);
    nomField.setStyle("-fx-font-size: 18px;");

    // Champ ville
    Label villeLabel = new Label("Ville");
    villeLabel.setStyle("-fx-font-size: 20px;");
    TextField villeField = new TextField();
    villeField.setPromptText("Entrez la ville de la librarie");
    villeField.setPrefWidth(300);
    villeField.setStyle("-fx-font-size: 18px;");



    fieldsContainer.getChildren().addAll(nomLabel, nomField, villeLabel, villeField);


    // Bouton Créer
    Button creerBtn = new Button("Créer la librairie");
    creerBtn.setStyle("-fx-background-color: #154c45; -fx-text-fill: white; -fx-font-size: 18px;");
    creerBtn.setPrefWidth(200);
    creerBtn.setPrefHeight(50);
    creerBtn.setOnAction(new ControleurCreerLib(this, new AdministrateurBD(connection), magasinBD));

    // Désactiver le bouton tant que tous les champs ne sont pas remplis
    creerBtn.setDisable(true);
    ChangeListener<String> listener = (observable, oldValue, newValue) -> {
        boolean allFilled = !nomField.getText().trim().isEmpty()
                && !villeField.getText().trim().isEmpty();
        creerBtn.setDisable(!allFilled);
    };

    nomField.textProperty().addListener(listener);
    villeField.textProperty().addListener(listener);
    
    // Stocker les champs pour le contrôleur
    String idMag = magasinBD.genererId();
    this.infoMag = new ArrayList<>();
    //this.infoMag.add(idMag);
    this.infoMag.add(nomField);
    this.infoMag.add(villeField);
    fieldsContainer.getChildren().addAll(creerBtn);
    mainContainer.getChildren().add(fieldsContainer);

    panelCentral.setCenter(mainContainer);
    }

    /**
     * fenetre pour créer un vendeur
     */
    public void fenetreCreerVendeur() {
    this.gauche = null;

    // Configuration de la bannière
    this.banniere = new BorderPane();
    banniere.setStyle("-fx-background-color: #154c45;");
    ImageView imgLogo = new ImageView(this.lesImages.get(9));
    imgLogo.setFitWidth(520);
    imgLogo.setPreserveRatio(true);
    banniere.setPadding(new Insets(10, 0, 0, 0));

    Label user = new Label("Création d'un vendeur");
    user.setStyle("-fx-font-size: 27px; -fx-text-fill: white;");
    user.setPadding(new Insets(0, 125, 0, 0));

    // Boutons de navigation
    HBox bouttons = new HBox();
    ImageView deco = new ImageView(this.lesImages.get(3));
    deco.setFitWidth(75);
    deco.setPreserveRatio(true);

    ImageView maison = new ImageView(this.lesImages.get(1));
    maison.setFitWidth(75);
    maison.setPreserveRatio(true);

    ImageView profil = new ImageView(this.lesImages.get(4));
    profil.setFitWidth(75);
    profil.setPreserveRatio(true);

    Button bDeconnexion = new Button(null, deco);
    bDeconnexion.setUserData("DECONNEXION");
    bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
    bDeconnexion.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    Button boutonMaison = new Button(null, maison);
    boutonMaison.setUserData("MAISON");
    boutonMaison.setOnAction(new ControleurRetourRedirection(this));
    boutonMaison.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    Button boutonProfil = new Button(null, profil);
    boutonProfil.setUserData("PROFIL");
    boutonProfil.setOnAction(new ControleurRetourRedirection(this));
    boutonProfil.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    bouttons.getChildren().addAll(bDeconnexion, boutonMaison, boutonProfil);
    bouttons.setMargin(boutonMaison, new Insets(5));
    bouttons.setMargin(bDeconnexion, new Insets(5));
    bouttons.setMargin(boutonProfil, new Insets(5));
    banniere.setLeft(imgLogo);
    banniere.setCenter(user);
    banniere.setRight(bouttons);

    // Panel central
    this.panelCentral = new BorderPane();
    this.panelCentral.setStyle("-fx-background-color: #c8c4b8;");

    // Conteneur principal
    VBox mainContainer = new VBox(20);
    mainContainer.setAlignment(Pos.CENTER);

    // Titre
    Text titre = new Text("CRÉER UN VENDEUR");
    titre.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
    mainContainer.getChildren().add(titre);

    // Conteneur pour les champs de saisie et le combo box
    HBox fieldsContainer = new HBox(50);
    fieldsContainer.setAlignment(Pos.CENTER);
    fieldsContainer.setPadding(new Insets(50, 0, 0, 0));

    // Partie gauche - Champs de saisie
    VBox leftFields = new VBox(20);
    leftFields.setAlignment(Pos.CENTER_LEFT);

    // Champ Nom
    Label nomLabel = new Label("Nom");
    nomLabel.setStyle("-fx-font-size: 20px;");
    TextField nomField = new TextField();
    nomField.setPromptText("Entrez le nom");
    nomField.setPrefWidth(300);
    nomField.setStyle("-fx-font-size: 18px;");

    // Champ Prénom
    Label prenomLabel = new Label("Prénom");
    prenomLabel.setStyle("-fx-font-size: 20px;");
    TextField prenomField = new TextField();
    prenomField.setPromptText("Entrez le prénom");
    prenomField.setPrefWidth(300);
    prenomField.setStyle("-fx-font-size: 18px;");

    // Champ Mot de passe
    Label mdpLabel = new Label("Mot de passe");
    mdpLabel.setStyle("-fx-font-size: 20px;");
    PasswordField mdpField = new PasswordField();
    mdpField.setPromptText("Entrez le mot de passe");
    mdpField.setPrefWidth(300);
    mdpField.setStyle("-fx-font-size: 18px;");

    leftFields.getChildren().addAll(nomLabel, nomField, prenomLabel, prenomField, mdpLabel, mdpField);

    // Partie droite - Combo box et bouton
    VBox rightFields = new VBox(30);
    rightFields.setAlignment(Pos.CENTER);

    // Combo box pour les magasins
    Label magasinLabel = new Label("Magasin");
    magasinLabel.setStyle("-fx-font-size: 20px;");
    this.comboBoxSave = new ComboBox<>();
    List<Magasin> magasins = magasinBD.getToutLesMagasins();
    for (Magasin mag : magasins) {
        comboBoxSave.getItems().add(mag.getNomMag());
    }
    comboBoxSave.setPromptText("Sélectionnez un magasin");
    comboBoxSave.setPrefWidth(300);
    comboBoxSave.setStyle("-fx-font-size: 18px;");

    // Bouton Créer
    Button creerBtn = new Button("Créer le vendeur");
    creerBtn.setStyle("-fx-background-color: #154c45; -fx-text-fill: white; -fx-font-size: 18px;");
    creerBtn.setPrefWidth(200);
    creerBtn.setPrefHeight(50);
    creerBtn.setOnAction(new ControleurCreerVendeur(this, new AdministrateurBD(connection), magasinBD, vendeurBD));

    // Désactiver le bouton tant que tous les champs ne sont pas remplis
    creerBtn.setDisable(true);
    ChangeListener<String> listener = (observable, oldValue, newValue) -> {
        boolean allFilled = !nomField.getText().trim().isEmpty()
                && !prenomField.getText().trim().isEmpty()
                && !mdpField.getText().trim().isEmpty()
                && comboBoxSave.getValue() != null;
        creerBtn.setDisable(!allFilled);
    };
    nomField.textProperty().addListener(listener);
    prenomField.textProperty().addListener(listener);
    mdpField.textProperty().addListener(listener);
    comboBoxSave.valueProperty().addListener((obs, oldVal, newVal) -> {
        boolean allFilled = !nomField.getText().trim().isEmpty()
                && !prenomField.getText().trim().isEmpty()
                && !mdpField.getText().trim().isEmpty()
                && newVal != null;
        creerBtn.setDisable(!allFilled);
    });

    // Stocker les champs pour le contrôleur
    this.inscriptions = new ArrayList<>();
    this.inscriptions.add(nomField);
    this.inscriptions.add(prenomField);
    this.inscriptions.add(mdpField);

    rightFields.getChildren().addAll(magasinLabel, comboBoxSave, creerBtn);
    fieldsContainer.getChildren().addAll(leftFields, rightFields);
    mainContainer.getChildren().add(fieldsContainer);

    panelCentral.setCenter(mainContainer);
}

/**
 * fenetre pour verifier la disponibilité d'un livre
 */
public void fenetreVerifDispo() {
    this.gauche = null;

    // Configuration de la bannière
    this.banniere = new BorderPane();
    banniere.setStyle("-fx-background-color: #154c45;");
    ImageView imgLogo = new ImageView(this.lesImages.get(9));
    imgLogo.setFitWidth(520);
    imgLogo.setPreserveRatio(true);
    banniere.setPadding(new Insets(10, 0, 0, 0));

    Label user = new Label("Vérification de disponibilité");
    user.setStyle("-fx-font-size: 27px; -fx-text-fill: white;");
    user.setPadding(new Insets(0, 125, 0, 0));

    // Boutons de navigation
    HBox bouttons = new HBox();
    ImageView deco = new ImageView(this.lesImages.get(3));
    deco.setFitWidth(75);
    deco.setPreserveRatio(true);

    ImageView maison = new ImageView(this.lesImages.get(1));
    maison.setFitWidth(75);
    maison.setPreserveRatio(true);

    ImageView profil = new ImageView(this.lesImages.get(4));
    profil.setFitWidth(75);
    profil.setPreserveRatio(true);

    Button bDeconnexion = new Button(null, deco);
    bDeconnexion.setUserData("DECONNEXION");
    bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
    bDeconnexion.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    Button boutonMaison = new Button(null, maison);
    boutonMaison.setUserData("MAISON");
    boutonMaison.setOnAction(new ControleurRetourRedirection(this));
    boutonMaison.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    Button boutonProfil = new Button(null, profil);
    boutonProfil.setUserData("PROFIL");
    boutonProfil.setOnAction(new ControleurRetourRedirection(this));
    boutonProfil.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    bouttons.getChildren().addAll(bDeconnexion, boutonMaison, boutonProfil);
    bouttons.setMargin(boutonMaison, new Insets(5));
    bouttons.setMargin(bDeconnexion, new Insets(5));
    bouttons.setMargin(boutonProfil, new Insets(5));
    banniere.setLeft(imgLogo);
    banniere.setCenter(user);
    banniere.setRight(bouttons);

    // Panel central
    this.panelCentral = new BorderPane();
    this.panelCentral.setStyle("-fx-background-color: #c8c4b8;");

    // Conteneur principal
    VBox mainContainer = new VBox(20);
    mainContainer.setAlignment(Pos.CENTER);

    // Titre principal
    Text titrePrincipal = new Text("Disponibilité");
    titrePrincipal.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
    mainContainer.getChildren().add(titrePrincipal);

    // Conteneur pour les champs de saisie
    HBox fieldsContainer = new HBox(50);
    fieldsContainer.setAlignment(Pos.CENTER);
    fieldsContainer.setPadding(new Insets(50, 0, 0, 0));

    // Champ Recherche
    Label rechercheLabel = new Label("Recherche");
    rechercheLabel.setStyle("-fx-font-size: 20px;");
    TextField recheField = new TextField();
    recheField.setPromptText("Entrez le nom du livre que vous cherchez");
    recheField.setPrefWidth(300);
    recheField.setStyle("-fx-font-size: 18px;");

    // Bouton Rechercher
    Button cherBtn = new Button("Rechercher");
    cherBtn.setStyle("-fx-background-color: #154c45; -fx-text-fill: white; -fx-font-size: 18px;");
    cherBtn.setPrefWidth(200);
    cherBtn.setPrefHeight(50);

    // Désactiver le bouton initialement
    cherBtn.setDisable(true);

    // Listener pour activer/désactiver le bouton
    ChangeListener<String> listener = (observable, oldValue, newValue) -> {
        cherBtn.setDisable(recheField.getText().trim().isEmpty());
    };
    recheField.textProperty().addListener(listener);

    // Configurer l'action du bouton
    Vendeur vend = null;
    if (personneConnectee instanceof Vendeur) {
        vend = (Vendeur) personneConnectee;
        // ...utilisation de vend...
    }
    Magasin magActu = vend.getMagasin();
    cherBtn.setOnAction(new ControleurVerifDispo(this, new LivreBD(connection), magActu, recheField));

    fieldsContainer.getChildren().addAll(rechercheLabel, recheField, cherBtn);
    mainContainer.getChildren().addAll(fieldsContainer);
    //afficher les livres liés à la recherche
    VBox listLivres = new VBox();
        listLivres.setPadding(new Insets(50,150,0,600));
        this.setCataloguesC();
        int pageMax = this.catalogues.size();
        List<Livre> livresPage = this.catalogues.get(this.pageCataActu);

        // HBox pour navigation pagination
        HBox pagination = new HBox(20);
        pagination.setAlignment(Pos.CENTER);
        Button btnPrev = new Button("<");
        btnPrev.setUserData("AVANT");
        btnPrev.setOnAction(new ControleurDefiler(this, "VENDEUR"));
        Button btnNext = new Button(">");
        btnNext.setUserData("APRES");
        btnNext.setOnAction(new ControleurDefiler(this, "VENDEUR"));

        // Correction pagination et affichage page
        int currentPage = (livresPage == null || livresPage.isEmpty()) ? 0 : this.pageCataActu;
        int totalPages = (pageMax == 0) ? 0 : pageMax;
        Label pageLabel = new Label("Page " + currentPage + " / " + totalPages);
        pageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        btnPrev.setStyle("-fx-font-size: 22px; -fx-background-color: #154c45; -fx-text-fill: white;");
        btnNext.setStyle("-fx-font-size: 22px; -fx-background-color: #154c45; -fx-text-fill: white;");
        // Désactiver la pagination si pas de livres ou une seule page
        btnPrev.setDisable(pageMax <= 1 || currentPage <= 1);
        btnNext.setDisable(pageMax <= 1 || currentPage >= totalPages);
        if (totalPages == 0) {
            btnPrev.setDisable(true);
            btnNext.setDisable(true);
        }
        pagination.getChildren().addAll(btnPrev, pageLabel, btnNext);

        int cptbtn = 0;
        if (livresPage == null || livresPage.isEmpty()) {
            Label noLivre = new Label("Aucun livre à afficher pour cette page.");
            noLivre.setStyle("-fx-font-size: 22px; -fx-text-fill: #154c45;");
            listLivres.getChildren().add(noLivre);
        } else {
            for (Livre livreVue : livresPage) {
                HBox livre = new HBox(30);
                Label titreLivre = new Label(livreVue.getTitre());
                titreLivre.setStyle("-fx-font-size: 25px;");
                Label prix = new Label(String.format("%.2f €", livreVue.getPrix()));
                prix.setStyle("-fx-font-size: 25px;");
                TextField quantite = new TextField("Quelle quantité souhaitez vous commander");
                quantite.setStyle("-fx-font-size: 25px;");
                Button ajPanier = new Button("Ajouter Panier");
                cptbtn++;
                ajPanier.setUserData(String.valueOf(cptbtn));
                ajPanier.setStyle("-fx-text-fill: white; " +
                    "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                    "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 22px; -fx-background-color: #154c45;");
                // Correction ici : utiliser titreLivre au lieu de titre
                livre.getChildren().addAll(titreLivre, prix, quantite, ajPanier);
                livre.setPadding(new Insets(10, 0, 10, 0));
                ajPanier.setPadding(new Insets(7, 0, 7, 0));
                listLivres.getChildren().add(livre);
            }
        }

        // Ajoute la pagination en bas de la liste
        VBox center = new VBox(20);
        center.getChildren().addAll(mainContainer, listLivres);
        this.panelCentral.setBottom(pagination);
        this.panelCentral.setCenter(center);
    }
/**
 * fenetre pour ajouter un livre à la bd
 */
public void fenetreAjouterLivre(){
     this.gauche = null;

    // Configuration de la bannière
    this.banniere = new BorderPane();
    banniere.setStyle("-fx-background-color: #154c45;");
    ImageView imgLogo = new ImageView(this.lesImages.get(9));
    imgLogo.setFitWidth(520);
    imgLogo.setPreserveRatio(true);
    banniere.setPadding(new Insets(10, 0, 0, 0));

    Label user = new Label("Création d'un livre");
    user.setStyle("-fx-font-size: 27px; -fx-text-fill: white;");
    user.setPadding(new Insets(0, 250, 0, 0));

    // Boutons de navigation
    HBox bouttons = new HBox();
    ImageView deco = new ImageView(this.lesImages.get(3));
    deco.setFitWidth(75);
    deco.setPreserveRatio(true);

    ImageView maison = new ImageView(this.lesImages.get(1));
    maison.setFitWidth(75);
    maison.setPreserveRatio(true);

    ImageView profil = new ImageView(this.lesImages.get(4));
    profil.setFitWidth(75);
    profil.setPreserveRatio(true);

    Button bDeconnexion = new Button(null, deco);
    bDeconnexion.setUserData("DECONNEXION");
    bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
    bDeconnexion.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    Button boutonMaison = new Button(null, maison);
    boutonMaison.setUserData("MAISON");
    boutonMaison.setOnAction(new ControleurRetourRedirection(this));
    boutonMaison.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    Button boutonProfil = new Button(null, profil);
    boutonProfil.setUserData("PROFIL");
    boutonProfil.setOnAction(new ControleurRetourRedirection(this));
    boutonProfil.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    bouttons.getChildren().addAll(bDeconnexion, boutonMaison, boutonProfil);
    bouttons.setMargin(boutonMaison, new Insets(5));
    bouttons.setMargin(bDeconnexion, new Insets(5));
    bouttons.setMargin(boutonProfil, new Insets(5));
    banniere.setLeft(imgLogo);
    banniere.setCenter(user);
    banniere.setRight(bouttons);






    // Panel central
    this.panelCentral = new BorderPane();
    this.panelCentral.setStyle("-fx-background-color: #c8c4b8;");

    // Conteneur principal
    VBox mainContainer = new VBox(20);
    mainContainer.setAlignment(Pos.CENTER);

    // Titre
    Text titre = new Text("INSÉRER UN LIVRE");
    titre.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
    mainContainer.getChildren().add(titre);

    // Conteneur pour les champs de saisie 
    HBox fieldsContainer = new HBox(70);
    fieldsContainer.setAlignment(Pos.CENTER);
    fieldsContainer.setPadding(new Insets(50, 0, 0, 0));

    // Partie gauche - Champs de saisie
    VBox leftFields = new VBox(20);
    leftFields.setAlignment(Pos.CENTER_LEFT);

    // Champ titre
    Label titreLabel = new Label("Titre");
    titreLabel.setStyle("-fx-font-size: 20px;");
    TextField titreField = new TextField();
    titreField.setPromptText("Entrez le titre");
    titreField.setPrefWidth(300);
    titreField.setStyle("-fx-font-size: 18px;");

    // Champ auteur
    Label auteurLabel = new Label("Auteur");
    auteurLabel.setStyle("-fx-font-size: 20px;");
    TextField auteurField = new TextField();
    auteurField.setPromptText("Entrez l'auteur principal");
    auteurField.setPrefWidth(300);
    auteurField.setStyle("-fx-font-size: 18px;");

    // Champ date
    Label dateLabel = new Label("Année de parution");
    dateLabel.setStyle("-fx-font-size: 20px;");
    TextField dateField = new TextField();
    dateField.setPromptText("Entrez l'année");
    dateField.setPrefWidth(300);
    dateField.setStyle("-fx-font-size: 18px;");

    //champ nb page
    Label pageLabel = new Label("Nombre de pages");
    pageLabel.setStyle("-fx-font-size: 20px;");
    TextField pageField = new TextField();
    pageField.setPromptText("Entrez le nombre de pages");
    pageField.setPrefWidth(300);
    pageField.setStyle("-fx-font-size: 18px;");

    leftFields.getChildren().addAll(titreLabel, titreField, auteurLabel, auteurField, dateLabel, dateField,pageLabel,pageField);

    // Partie droite 
    VBox rightFields = new VBox(30);
    rightFields.setAlignment(Pos.CENTER_LEFT);

    // Champ prix
    Label prixLabel = new Label("Prix");
    prixLabel.setStyle("-fx-font-size: 20px;");
    TextField prixField = new TextField();
    prixField.setPromptText("Entrez le prix");
    prixField.setPrefWidth(300);
    prixField.setStyle("-fx-font-size: 18px;");

    // Champ theme
    Label themeLabel = new Label("Thème");
    themeLabel.setStyle("-fx-font-size: 20px;");
    TextField themeField = new TextField();
    themeField.setPromptText("Entrez le thème principal");
    themeField.setPrefWidth(300);
    themeField.setStyle("-fx-font-size: 18px;");

    // Champ editeur
    Label editeurLabel = new Label("Éditeur");
    editeurLabel.setStyle("-fx-font-size: 20px;");
    TextField editeurField = new TextField();
    editeurField.setPromptText("Entrez l'éditeur");
    editeurField.setPrefWidth(300);
    editeurField.setStyle("-fx-font-size: 18px;");


    rightFields.getChildren().addAll(prixLabel, prixField, themeLabel, themeField, editeurLabel, editeurField);


    Button creerBtn = new Button("Créer le livre");
    creerBtn.setUserData("CREER LIVRE");
    creerBtn.setStyle("-fx-background-color: #154c45; -fx-text-fill: white; -fx-font-size: 18px;");
    creerBtn.setPrefWidth(200);
    creerBtn.setPrefHeight(50);
    /////////////////////creerBtn.setOnAction(new ControleurCreerVendeur(this, new AdministrateurBD(connection), magasinBD, vendeurBD));


    // Désactiver le bouton tant que tous les champs ne sont pas remplis
    creerBtn.setDisable(true);
    ChangeListener<String> listener = (observable, oldValue, newValue) -> {
        boolean allFilled = !titreField.getText().trim().isEmpty()
                && !prixField.getText().trim().isEmpty();
        creerBtn.setDisable(!allFilled);
    };
    titreField.textProperty().addListener(listener);
    prixField.textProperty().addListener(listener);

    // Stocker les champs pour le contrôleur
    this.infosLivre = new ArrayList<>();
    this.infosLivre.add(titreField);
    this.infosLivre.add(auteurField);
    this.infosLivre.add(pageField);
    this.infosLivre.add(prixField);
    this.infosLivre.add(dateField);
    this.infosLivre.add(editeurField);
    this.infosLivre.add(themeField);


    rightFields.getChildren().addAll(creerBtn);
    fieldsContainer.getChildren().addAll(leftFields, rightFields);
    mainContainer.getChildren().add(fieldsContainer);

    panelCentral.setCenter(mainContainer);

}

    /**
     * charge les images à afficher
     */
    private void chargerImages(){
    File file = new File("src/ihm/img/LivreExpress.png");
    File file1 = new File("src/ihm/img/home.png");
    File file2 = new File("src/ihm/img/croix.png");
    File file3 = new File("src/ihm/img/logout.png");
    File file4 = new File("src/ihm/img/icon_profile.png");
    File file5 = new File("src/ihm/img/loupe.png");
    File file6 = new File("src/ihm/img/panier.png");
    File file7 = new File("src/ihm/img/retour.png");
    File file8 = new File("src/ihm/img/livre.png");
    File file9 = new File("src/ihm/img/LivreExpressFondVert.png");
    this.lesImages.add(new Image(file.toURI().toString()));
    this.lesImages.add(new Image(file1.toURI().toString()));
    this.lesImages.add(new Image(file2.toURI().toString()));
    this.lesImages.add(new Image(file3.toURI().toString()));
    this.lesImages.add(new Image(file4.toURI().toString()));
    this.lesImages.add(new Image(file5.toURI().toString()));
    this.lesImages.add(new Image(file6.toURI().toString()));
    this.lesImages.add(new Image(file7.toURI().toString()));
    this.lesImages.add(new Image(file8.toURI().toString()));
    this.lesImages.add(new Image(file9.toURI().toString()));
    }

    
    /**
     * Affiche la fenêtre de choix de l'utilisateur.
     */
    public void modeChoix(){
        fenetreChoix();
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);

    }
    
    /**
     * Affiche la fenêtre de connexion de l'utilisateur.
     */
    public void modeConnexion(){
        fenetreConnexion();
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    }
    /**
     * Affiche la fenêtre d'inscription d'un client
     */
    public void modeInscription(){
        fenetreInscription();
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    }

    /**
     * Affiche la fenêtre d'accueil du client.
     */
    public void modeAccueilC(){
        fenetreAccueilC();
        this.boutonMaison.setDisable(true);
        this.boutonProfil.setDisable(false);
        this.bPanier.setDisable(false);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
        
    }
    /**
     * Affiche la fenêtre du panier du client.
     */
    public void modePanier(){
        fenetrePanier();
        this.boutonMaison.setDisable(false);
        this.boutonProfil.setDisable(false);
        this.bPanier.setDisable(true);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
       
    }
    /**
     * Affiche la fenêtre d'accueil du vendeur.
     */
    public void modeAccueilV(){
        fenetreAccueilV();
        this.boutonMaison.setDisable(true);
        this.boutonProfil.setDisable(false);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    }

    /**
     * Affiche la fenêtre d'accueil de l'administrateur.
     */
    public void modeAccueilA(){
        fenetreAccueilA();
        this.boutonMaison.setDisable(true);
        this.boutonProfil.setDisable(false);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    }
    /**
     * Affiche la fenêtre du profil du client.
     */
    public void modeProfilC(){
        fenetreProfilC();
        this.boutonMaison.setDisable(false);
        this.boutonProfil.setDisable(true);
        this.bPanier.setDisable(false);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    } 

    /**
     * Affiche la fenêtre du profil du vendeur.
     */
    public void modeProfilV(){
        fenetreProfilV();
        this.boutonMaison.setDisable(false);
        this.boutonProfil.setDisable(true);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    }

    /**
     * Affiche la fenêtre du profil de l'administrateur.
     */
    public void modeProfilA(){
        fenetreProfilA();
        this.boutonMaison.setDisable(false);
        this.boutonProfil.setDisable(true);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    } 
    /**
     * Affiche la fenêtre pour créer un vendeur.
     */
    public void modeCreerVendeur(){
        fenetreCreerVendeur();
        this.boutonMaison.setDisable(false);
        this.boutonProfil.setDisable(false);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    }
    /**
     * Affiche la fenêtre pour créer une librairie.
     */
    public void modeCreerLib(){
        fenetreCreerLib();
        this.boutonMaison.setDisable(false);
        this.boutonProfil.setDisable(false);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    }
    /**
     * Affiche la fenêtre pour créer un livre.
     */
    public void modeCreerLivre(){
        fenetreAjouterLivre();
        this.boutonMaison.setDisable(false);
        this.boutonProfil.setDisable(false);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    } 
    /**
     * Affiche la fenêtre de vérification de disponibilité d'un livre.
     */
     public void modeVerifDispo(){
        fenetreVerifDispo();
        this.boutonMaison.setDisable(false);
        this.boutonProfil.setDisable(false);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    } 


    /**
     * Affiche une alerte d'erreur de connexion.
     */
    public void popUpConnexionImpossible(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de connexion");
        alert.setHeaderText(null);
        alert.setContentText("Identifiants incorrects. Veuillez réessayer.");
        Optional<ButtonType> result = alert.showAndWait();
    }
    /**
     * Affiche une alerte d'erreur d'inscription.
     */
    public void popUpInscriptionImpossible(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de connexion");
        alert.setHeaderText(null);
        alert.setContentText("Problème rencontré durant l'inscription. Veuillez réessayer.");
        Optional<ButtonType> result = alert.showAndWait();
    }
    /**
     * Affiche une alerte de succès de création de librairie.
     */
    public void popUpCreaMagReussi(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Erreur de connexion");
        alert.setHeaderText(null);
        alert.setContentText("Vous avez bien créé le magasin");
        Optional<ButtonType> result = alert.showAndWait();
    }

    /**
     * créer le graphe de scène et lance le jeu
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Livre Express - La lecture à portée de main ! ");
        stage.setScene(this.laScene());
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
