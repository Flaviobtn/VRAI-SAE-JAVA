package ihm.Vue;
import ihm.Controlleur.*;
import bd.*;
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
import java.sql.*;
import java.util.*;
import javax.swing.border.TitledBorder;
import java.io.File;


/**
 * Vue du jeu du pendu
 */
public class LivreExpresss extends Application {
    /**
     * connection à la base de données
     */
    private Connection connection;
    /**
     * Liste qui contient les images du jeu
     */
    private ArrayList<Image> lesImages;
    /**
     * Liste qui contient les noms des niveaux
     */    
    private List<String> roles;

    // les différents contrôles qui seront mis à jour ou consultés pour l'affichage
    
    /**
     * le text qui indique l'utilisateur
     */
    private Text lUtilisateur;

    private TextField identifiant;
    private TextField motdepasse;
    private List<TextField> inscriptions;
    
    /**
     * le panel Central qui pourra être modifié selon le mode (accueil ou jeu)
     */
    private BorderPane panelCentral;
    private BorderPane banniere;
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

    private BorderPane fenetre;

    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono ...)
     */
    @Override
    public void init() {
        this.lesImages = new ArrayList<Image>();
        this.chargerImages(/*"../img"*/);
        this.boutonMaison= new Button();
        this.bDeconnexion= new Button();
        this.bPanier= new Button();
        this.boutonProfil= new Button();
        this.connection = ConnectionBD.getConnection();

        // A terminer d'implementer
    }

    public String getIdentifiant() {
        return this.identifiant.getText();
    }
    public String getMotdepasse() {
        return this.motdepasse.getText();
    }

    /**
     * @return  le graphe de scène de la vue à partir de methodes précédantes
     */
    private Scene laScene(){
        this.fenetre = new BorderPane();
        fenetreChoix();
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
        return new Scene(fenetre, 1920, 1000, Color.WHITE);
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
    


    private void fenetreChoix() {
        // Utilisation de l'image des livres ou placeholder
        ImageView imgG = new ImageView(this.lesImages.get(8));
        imgG.setFitWidth(600);
        imgG.setFitHeight(1080);
        imgG.setPreserveRatio(false);

        // Partie droite - Formulaire de connexion dans la bannière
        VBox centerPanel = new VBox();
        centerPanel.setPrefWidth(450);
        centerPanel.setPrefHeight(200);
        centerPanel.setPadding(new Insets(100,0,0,250));
        centerPanel.setStyle("-fx-background-color: #2c2c2c;");

        // Titre "Livre Express"
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
        choixContainer.setSpacing(100); // <-- Cette valeur crée un grand espace vertical entre les éléments du VBox
        choixContainer.getChildren().addAll(comboBox, choix);
        choixContainer.setPadding(new Insets(100, 0, 0, 250));

        // Crée l'objet Text et ajoute un listener pour suivre la sélection
        this.lUtilisateur = new Text(""); // ou comboBox.getValue() si tu veux la valeur initiale
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("Choix de l'utilisateur")) {
                this.lUtilisateur.setText(newValue);
            }
        });

        // Ajout des éléments au panel central
        centerPanel.getChildren().addAll(titleContainer, choixContainer);

        // Initialisation des autres panels (vides pour cette vue)
        this.gauche = new Pane();
        this.panelCentral = new BorderPane();
        this.panelCentral.getChildren().add(centerPanel);
        this.gauche.getChildren().add(imgG);
        this.banniere = new BorderPane();

    }

    private void fenetreConnexion(){
        // Utilisation de l'image des livres ou placeholder
        ImageView imgG = new ImageView(this.lesImages.get(8));
        imgG.setFitWidth(600);
        imgG.setFitHeight(1080);
        imgG.setPreserveRatio(false);

        // Partie droite - Formulaire de connexion dans la bannière
        VBox centerPanel = new VBox();
        centerPanel.setPrefWidth(450);
        centerPanel.setPrefHeight(200);
        centerPanel.setPadding(new Insets(100,0,0,250));
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
        ligne.setPrefHeight(7);
        ligne.setPrefWidth(220);
        ligne.setStyle("-fx-border-color: #2c2c2c;");

        // Conteneur pour le titre et la ligne
        VBox titleContainer = new VBox();
        titleContainer.setSpacing(5);
        titleContainer.getChildren().addAll(titreLVE,titreText, ligne);

        // identifiant et mot de passe
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


        // Bouton de connexion et d'inscription
        HBox loginContainer = new HBox();
        Button inscription = new Button("INSCRIPTION");
        inscription.setUserData("INSCRIPTION");
        inscription.setOnAction(new ControleurRetourRedirection(this));
        inscription.setMinWidth(200);
        inscription.setMinHeight(100);
        inscription.setFont(Font.font("Arial", 15));
        inscription.setStyle("-fx-background-color: white; -fx-text-fill: #2c2c2c; " +
                             "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                             "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;");
        Button connexion = new Button("SE CONNECTER");
        connexion.setUserData("CONNEXION");
        connexion.setOnAction(new ControleurConnexion(this, this.lUtilisateur, new ClientBD(connection), new VendeurBD(connection), new AdministrateurBD(connection)));
        connexion.setMinWidth(200);
        connexion.setMinHeight(100);
        connexion.setFont(Font.font("Arial", 15));
        connexion.setStyle("-fx-background-color: white; -fx-text-fill: #2c2c2c; " +
                      "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;");

        
        // Effet de survol pour le bouton
        connexion.setOnMouseEntered(e ->
        connexion.setStyle("-fx-background-color: #2c2c2c; -fx-text-fill: white; " +
                          "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                          "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));

        connexion.setOnMouseExited(e ->
        connexion.setStyle("-fx-background-color: white; -fx-text-fill: #2c2c2c; " +
                          "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                          "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;")); 
        // Effet de survol pour le bouton
        inscription.setOnMouseEntered(e -> 
        inscription.setStyle("-fx-background-color: #2c2c2c; -fx-text-fill: white; " +
                          "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                          "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));                
        inscription.setOnMouseExited(e ->
        inscription.setStyle("-fx-background-color: white; -fx-text-fill: #2c2c2c; " +
                          "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                          "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));
        loginContainer.getChildren().addAll(inscription, connexion);
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.setSpacing(70); 

        // Conteneur pour le combobox et la Connexion
        VBox pannelcentrale = new VBox();
        pannelcentrale.setAlignment(Pos.CENTER);
        // L'écart important vient de cette ligne :
        pannelcentrale.setSpacing(100); // <-- Cette valeur crée un grand espace vertical entre les éléments du VBox
        pannelcentrale.getChildren().addAll(idcontainer, motdepassecontainer, loginContainer);
        pannelcentrale.setPadding(new Insets(100, 0, 0, 180));

        // Ajout des éléments au panel central
        centerPanel.getChildren().addAll(titleContainer, pannelcentrale);

        // Initialisation des autres panels (vides pour cette vue)
        this.gauche = new Pane();
        this.panelCentral = new BorderPane();
        this.panelCentral.getChildren().add(centerPanel);
        this.gauche.getChildren().add(imgG);
    }

    private void fenetreInscription(){
        // Utilisation de l'image des livres ou placeholder
        ImageView imgG = new ImageView(this.lesImages.get(8));
        imgG.setFitWidth(600);
        imgG.setFitHeight(1080);
        imgG.setPreserveRatio(false);

        // Partie droite - Formulaire de connexion dans la bannière
        VBox centerPanel = new VBox();
        centerPanel.setPrefWidth(450);
        centerPanel.setPrefHeight(200);
        centerPanel.setPadding(new Insets(100,0,0,250));
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

        // identifiant et mot de passe
        VBox dadContainer = new VBox();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();
        HBox hbox4 = new HBox();
        HBox hbox5 = new HBox();
        HBox hbox6 = new HBox();
        HBox ligne1 = new HBox();
        HBox ligne2 = new HBox();
        HBox ligne3 = new HBox();
        TextField nom = new TextField();
        TextField prenom = new TextField();
        TextField motdepasse = new TextField();
        TextField adresse = new TextField();
        TextField ville = new TextField();
        TextField codepostal = new TextField();
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
        nom.setMinWidth(350);
        motdepasse.setMinWidth(350);
        nom.setMinHeight(50);
        motdepasse.setMinHeight(50);
        prenom.setMinWidth(350);
        prenom.setMinHeight(50);
        adresse.setMinWidth(350);
        adresse.setMinHeight(50);
        ville.setMinWidth(350);
        ville.setMinHeight(50);
        codepostal.setMinWidth(350);
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
        ligne1.getChildren().addAll(hbox1, hbox6, hbox3);
        ligne2.getChildren().addAll(hbox2, hbox4);
        ligne3.getChildren().addAll(hbox5);
        dadContainer.getChildren().addAll(ligne1, ligne2, ligne3);


        // Bouton de connexion et d'inscription
        ImageView imgretour = new ImageView(this.lesImages.get(2));
        imgretour.setFitWidth(75);
        imgretour.setPreserveRatio(true);
        HBox loginContainer = new HBox();
        Button retour = new Button(null, imgretour);
        retour.setOnAction(new ControleurRetourRedirection(this));
        retour.setMinWidth(200);
        retour.setMinHeight(100);
        retour.setFont(Font.font("Arial", 15));
        retour.setStyle("-fx-background-color: white; -fx-text-fill: #2c2c2c; " +
                             "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                             "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;");
        Button creer = new Button("CRÉER COMPTE");
        creer.setUserData("CREER COMPTE");
        creer.setOnAction(new ControleurRetourRedirection(this));
        creer.setMinWidth(200);
        creer.setMinHeight(100);
        creer.setFont(Font.font("Arial", 15));
        creer.setStyle("-fx-background-color: white; -fx-text-fill: #2c2c2c; " +
                      "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;");

        
        // Effet de survol pour le bouton
        creer.setOnMouseEntered(e ->
        creer.setStyle("-fx-background-color: #2c2c2c; -fx-text-fill: white; " +
                          "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                          "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));

        creer.setOnMouseExited(e ->
        creer.setStyle("-fx-background-color: white; -fx-text-fill: #2c2c2c; " +
                          "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                          "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;")); 
        // Effet de survol pour le bouton
        retour.setOnMouseEntered(e -> 
        retour.setStyle("-fx-background-color: #2c2c2c; -fx-text-fill: white; " +
                          "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                          "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));                
        retour.setOnMouseExited(e ->
        retour.setStyle("-fx-background-color: white; -fx-text-fill: #2c2c2c; " +
                          "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                          "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));
        loginContainer.getChildren().addAll(retour, creer);
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.setSpacing(70); 

        // Conteneur pour le combobox et la Connexion
        VBox pannelcentrale = new VBox();
        pannelcentrale.setAlignment(Pos.CENTER);
        // L'écart important vient de cette ligne :
        pannelcentrale.setSpacing(100); // <-- Cette valeur crée un grand espace vertical entre les éléments du VBox
        pannelcentrale.getChildren().addAll(dadContainer, loginContainer);
        pannelcentrale.setPadding(new Insets(100, 0, 0, 180));

        // Ajout des éléments au panel central
        centerPanel.getChildren().addAll(titleContainer, pannelcentrale);

        // Initialisation des autres panels (vides pour cette vue)
        this.gauche = new Pane();
        this.panelCentral = new BorderPane();
        this.panelCentral.getChildren().add(centerPanel);
        this.gauche.getChildren().add(imgG);
    }
    

    private void fenetreAccueilC() {
        this.gauche=null;
        
        this.banniere=new BorderPane();
        banniere.setStyle("-fx-background-color: lightpink;");
        ImageView imgLogo = new ImageView(this.lesImages.get(0));
        imgLogo.setFitWidth(520);
        imgLogo.setPreserveRatio(true);
        banniere.setPadding(new Insets(10,0,0,0));

        Label user = new Label("Bienvenue");
        user.setStyle("-fx-font-size: 27px;");
        user.setPadding(new Insets(0,125,0,0));


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

        Button bDeconnexion = new Button(null,deco);
        bDeconnexion.setUserData("DECONNEXION");
        bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
        Button bPanier = new Button(null,panier);
        bPanier.setOnAction(new AllerPanier(this));
        bPanier.setUserData("PANIER");
        Button boutonMaison = new Button(null,maison);
        boutonMaison.setUserData("MAISON");
        Button boutonProfil = new Button(null,profil);
        boutonProfil.setUserData("PROFIL");
        bouttons.getChildren().addAll(bDeconnexion,bPanier,boutonMaison,boutonProfil);
        boutonProfil.setOnAction(new ControleurRetourRedirection(this));

        bouttons.setMargin(boutonMaison,new Insets(5));
        bouttons.setMargin(bDeconnexion,new Insets(5));
        bouttons.setMargin(bPanier,new Insets(5));
        bouttons.setMargin(boutonProfil,new Insets(5));
        banniere.setLeft(imgLogo);
        banniere.setCenter(user);
        banniere.setRight(bouttons);

        this.panelCentral= new BorderPane();
        ImageView loupe = new ImageView(this.lesImages.get(5));
        loupe.setFitWidth(40);
        loupe.setPreserveRatio(true);

        ImageView croix = new ImageView(this.lesImages.get(2));
        croix.setFitWidth(40);
        croix.setPreserveRatio(true);

        Button boutonLoupe = new Button(null,loupe);
        Button boutonCroix = new Button(null,croix);

        HBox barreRecherche = new HBox();
        barreRecherche.setAlignment(Pos.TOP_CENTER);

        TextField tf = new TextField();
        tf.setPrefWidth(500);
        tf.setStyle("-fx-font-size: 25px;");

        barreRecherche.getChildren().addAll(boutonLoupe,tf,boutonCroix);
        barreRecherche.setPadding(new Insets(120,0,0,0));
        panelCentral.setTop(barreRecherche);


        VBox listLivres = new VBox();
        listLivres.setPadding(new Insets(180,0,0,600));
        for(int i= 0; i<4;i++){
            HBox livre = new HBox(10);
            Label titre = new Label("titre"+i);
            titre.setStyle("-fx-font-size: 25px;");
            Label prix = new Label("prix"+i);
            prix.setStyle("-fx-font-size: 25px;");
            TextField quantite = new TextField("1");
            quantite.setStyle("-fx-font-size: 25px;");
            Button ajPanier = new Button("Ajouter Panier");
            ajPanier.setStyle("-fx-font-size: 25px;");
            livre.getChildren().addAll(titre,prix,quantite,ajPanier);
            livre.setPadding(new Insets(0,0,10,0));;
            listLivres.getChildren().addAll(livre);
        }

        panelCentral.setCenter(listLivres);

    }

    private void fenetrePanier() {

        this.banniere=new BorderPane();
        banniere.setStyle("-fx-background-color: lightpink;");
        ImageView imgLogo = new ImageView(this.lesImages.get(0));
        imgLogo.setFitWidth(520);
        imgLogo.setPreserveRatio(true);
        banniere.setPadding(new Insets(10,0,0,0));

        Label user = new Label("Panier");
        user.setStyle("-fx-font-size: 27px;");
        user.setPadding(new Insets(0,125,0,0));


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

        Button bDeconnexion = new Button(null,deco);
        bDeconnexion.setUserData("DECONNEXION");
        bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
        Button bPanier = new Button(null,panier);
        bPanier.setUserData("PANIER");
        Button boutonMaison = new Button(null,maison);
        boutonMaison.setUserData("MAISON");
        boutonMaison.setOnAction(new ControleurRetourRedirection(this));
        Button boutonProfil = new Button(null,profil);
        boutonProfil.setUserData("PROFIL");
        boutonProfil.setOnAction(new ControleurRetourRedirection(this));

        bouttons.getChildren().addAll(bDeconnexion,bPanier,boutonMaison,boutonProfil);

        bouttons.setMargin(boutonMaison,new Insets(5));
        bouttons.setMargin(bDeconnexion,new Insets(5));
        bouttons.setMargin(bPanier,new Insets(5));
        bouttons.setMargin(boutonProfil,new Insets(5));
        banniere.setLeft(imgLogo);
        banniere.setCenter(user);
        banniere.setRight(bouttons);



        this.panelCentral= new BorderPane();

        VBox listLivres = new VBox();
        
        for(int i= 0; i<4;i++){
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
            livre.setStyle("-fx-background-color: lightpink;");
            listLivres.getChildren().addAll(livre);
            listLivres.setMargin(livre,new Insets(0,655,0,665));
        }


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
        
        panelCentral.setMargin(listLivres,new Insets(150,0,0,0));
        panelCentral.setCenter(listLivres);    
    
    }

    private void fenetreAccueilV(){

    }

    private void fenetreProfil(){
    
    }

    /**
     * charge les images à afficher en fonction des erreurs
     * @param repertoire répertoire où se trouvent les images
     */
    private void chargerImages(/*String repertoire*/){
    File file = new File("src/ihm/img/LivreExpress.png");
    File file1 = new File("src/ihm/img/home.png");
    File file2 = new File("src/ihm/img/croix.png");
    File file3 = new File("src/ihm/img/logout.png");
    File file4 = new File("src/ihm/img/icon_profile.png");
    File file5 = new File("src/ihm/img/loupe.png");
    File file6 = new File("src/ihm/img/panier.png");
    File file7 = new File("src/ihm/img/retour.png");
    File file8 = new File("src/ihm/img/livre.png");
    this.lesImages.add(new Image(file.toURI().toString()));
    this.lesImages.add(new Image(file1.toURI().toString()));
    this.lesImages.add(new Image(file2.toURI().toString()));
    this.lesImages.add(new Image(file3.toURI().toString()));
    this.lesImages.add(new Image(file4.toURI().toString()));
    this.lesImages.add(new Image(file5.toURI().toString()));
    this.lesImages.add(new Image(file6.toURI().toString()));
    this.lesImages.add(new Image(file7.toURI().toString()));
    this.lesImages.add(new Image(file8.toURI().toString()));
}

    public String modeChoix(){
        fenetreChoix();
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
        return this.lUtilisateur.getText();
    }
    
    public void modeConnexion(){
        fenetreConnexion();
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    }
    
    public void modeInscription(){
        fenetreInscription();
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    }

    /** lance une partie */
    public void modeAccueilC(){
        fenetreAccueilC();
        this.boutonMaison.setDisable(true);
        this.boutonProfil.setDisable(false);
        this.bPanier.setDisable(false);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
        
    }

    /** lance une partie */
    public void modePanier(){
        fenetrePanier();
        this.boutonMaison.setDisable(true);
        this.boutonProfil.setDisable(false);
        this.bPanier.setDisable(false);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
       
    }

    /** lance une partie */
    public void modeAccueilV(){
        fenetreAccueilV();
    }

     /** lance une partie */
    public void modeProfil(){
        fenetreProfil();
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
