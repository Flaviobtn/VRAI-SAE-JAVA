package ihm.Vue;
import ihm.Controlleur.*;
import Modele.*;
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
    private Vendeur creationVendeur;
    private Magasin magasinCreation;
    private Personne personneConnectee;
    private Map<String, List<Livre>> catalogues;

    //BD
    private MagasinBD magasinBD; 
    private VendeurBD vendeurBD;

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
        this.roles = new ArrayList<String>();
        this.catalogues = new HashMap<String, List<Livre>>();
        this.chargerImages(/*"../img"*/);
        this.boutonMaison= new Button();
        this.bDeconnexion= new Button();
        this.bPanier= new Button();
        this.magasinCreation = null;
        this.boutonProfil= new Button();
        this.creationVendeur = null; 
        this.personneConnectee = null;
        this.connection = ConnectionBD.getConnection();
        this.magasinBD= new MagasinBD(this.connection);
        this.vendeurBD = new VendeurBD(this.connection);

        // A terminer d'implementer
    }

    public String getIdentifiant() {
        return this.identifiant.getText();
    }
    public String getMotdepasse() {
        return this.motdepasse.getText();
    }
    public String getLUtilisateur() {
        return this.lUtilisateur.getText();
    }

    public Map<String, List<Livre>> getCatalogues() {
        return this.catalogues;
    }

    public void SetCatalogues(Map<String, List<Livre>> catalogues) {
        this.catalogues = catalogues;
    }
    
    public List<TextField> getInscriptions() {
        return this.inscriptions;
    }

    public Vendeur getCreationVendeur(){
        return this.creationVendeur;
    }

    public void setPersonneConnectee(Personne personne) {
        this.personneConnectee = personne;
    }

    public Magasin getMagasinCreation() {
        return this.magasinCreation;
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
        centerPanel.setPadding(new Insets(100,0,0,225));
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
        choixContainer.setSpacing(140); // <-- Cette valeur crée un grand espace vertical entre les éléments du VBox
        choixContainer.getChildren().addAll(comboBox, choix);
        choixContainer.setPadding(new Insets(100, 0, 0, 220));

        // Crée l'objet Text et ajoute un listener pour suivre la sélection
        this.lUtilisateur = new Text(""); // ou comboBox.getValue() si tu veux la valeur initiale
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("Choix de l'utilisateur")) {
                choix.setDisable(false);
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
        if(!(getLUtilisateur().equals("CLIENT"))){
            inscription.setDisable(true);
        }
        Button connexion = new Button("SE CONNECTER");
        connexion.setUserData("CONNEXION");
        connexion.setOnAction(new ControleurConnexion(this, new ClientBD(connection), new VendeurBD(connection), new AdministrateurBD(connection), new LivreBD(connection)));
        connexion.setMinWidth(200);
        connexion.setMinHeight(100);
        connexion.setFont(Font.font("Arial", 15));
        connexion.setStyle("-fx-background-color: white; -fx-text-fill: #2c2c2c; " +
                      "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;");
        Button retour = new Button("⟵ Annuler");
        retour.setUserData("ANNULERC");
        retour.setOnAction(new ControleurRetourRedirection(this));
        retour.setMinWidth(200);
        retour.setMinHeight(100);
        retour.setFont(Font.font("Arial", 15));
        retour.setStyle("-fx-background-color: white; -fx-text-fill: #2c2c2c; " +
                      "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;");

        //---------------------------------------------------------------------------------------------

        // on fait en sorte que quand on passe la sourist sur le bouton, il change de couleur
        // on définit un deux styles différents pour le bouton
    /*String styleNormal = "-fx-background-color: white; -fx-text-fill: #2c2c2c; " +
                      "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;";

*/

        //---------------------------------------------------------------------------------------------

        // Conteneur pour le combobox et la Connexion
        VBox pannelcentrale = new VBox();
        pannelcentrale.setAlignment(Pos.CENTER);
        loginContainer.getChildren().addAll(retour, inscription, connexion);
        // L'écart important vient de cette ligne :
        pannelcentrale.setSpacing(100); // <-- Cette valeur crée un grand espace vertical entre les éléments du VBox
        pannelcentrale.getChildren().addAll(idcontainer, motdepassecontainer, loginContainer);
        pannelcentrale.setPadding(new Insets(80, 0, 0, 30));

        // Ajout des éléments au panel central
        centerPanel.getChildren().addAll(titleContainer, pannelcentrale);

        // Initialisation des autres panels (vides pour cette vue)
        this.gauche = new Pane();
        this.panelCentral = new BorderPane();
        this.panelCentral.getChildren().add(centerPanel);
        this.panelCentral.setStyle("-fx-background-color: #c8c4b8;"); // couleur de fond du panel central
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

        // identifiant et mot de passe
        VBox dadContainer = new VBox();
        VBox hbox1 = new VBox();
        VBox hbox2 = new VBox();
        VBox hbox3 = new VBox();
        VBox hbox4 = new VBox();
        VBox hbox5 = new VBox();
        VBox hbox6 = new VBox();
        HBox ligne1 = new HBox(70);
        HBox ligne2 = new HBox(70);
        HBox ligne3 = new HBox(70);
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


        // Bouton de connexion et d'inscription
        HBox loginContainer = new HBox();
        Button retour = new Button("⟵ Annuler");
        retour.setUserData("ANNULERI");
        retour.setOnAction(new ControleurRetourRedirection(this));
        retour.setMinWidth(200);
        retour.setPrefHeight(50);
        retour.setFont(Font.font("Arial", 25));
        retour.setStyle("-fx-background-color: white; -fx-text-fill: #2c2c2c; " +
                      "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;");
        Button creer = new Button("CRÉER COMPTE");
        creer.setUserData("CREER COMPTE");
        creer.setOnAction(new ControleurInscription(this, new ClientBD(connection)));
        creer.setMinWidth(200);
        creer.setPrefHeight(50);
        creer.setDisable(true);
        creer.setFont(Font.font("Arial", 20));
        creer.setStyle("-fx-background-color: white; -fx-text-fill: #2c2c2c; " +
                      "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;");


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

        
        // Effet de survol pour le bouton
        retour.setOnMouseEntered(e ->
        retour.setStyle("-fx-background-color: #2c2c2c; -fx-text-fill: white; " +
                          "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                          "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));

        retour.setOnMouseExited(e ->
        retour.setStyle("-fx-background-color: #2c2c2c; -fx-text-fill: white; " +
                          "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                          "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));
        // Effet de survol pour le bouton
        creer.setOnMouseEntered(e ->
        creer.setStyle("-fx-background-color: #2c2c2c; -fx-text-fill: white; " +
                          "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                          "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));

        creer.setOnMouseExited(e ->
        creer.setStyle("-fx-background-color: #2c2c2c; -fx-text-fill: white; " +
                          "-fx-border-color: #2c2c2c; -fx-border-width: 2; " +
                          "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold;"));
        loginContainer.getChildren().addAll(retour, creer);
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.setSpacing(500); 

        // Conteneur pour le combobox et la Connexion
        VBox pannelcentrale = new VBox();
        pannelcentrale.setAlignment(Pos.CENTER);
        // L'écart important vient de cette ligne :
        pannelcentrale.setSpacing(100); // <-- Cette valeur crée un grand espace vertical entre les éléments du VBox
        pannelcentrale.getChildren().addAll(dadContainer, loginContainer);
        pannelcentrale.setPadding(new Insets(100, 0, 0, 10));

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
    banniere.setStyle("-fx-background-color: #084a48;");
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
    bDeconnexion.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    Button bPanier = new Button(null,panier);
    bPanier.setOnAction(new ControleurRetourRedirection(this));
    bPanier.setUserData("PANIER");
    bPanier.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

    Button boutonMaison = new Button(null,maison);
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

    this.panelCentral= new BorderPane();
    ImageView loupe = new ImageView(this.lesImages.get(5));
    loupe.setFitWidth(43);
    loupe.setPreserveRatio(true);

    ImageView croix = new ImageView(this.lesImages.get(2));
    croix.setFitWidth(43);
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
    panelCentral.setStyle("-fx-background-color: #ece3d3;");

    VBox listLivres = new VBox();
    listLivres.setPadding(new Insets(180,150,0,600));
    for(int i= 0; i<4;i++){
        HBox livre = new HBox(30);
        Label titre = new Label("titre"+i);
        titre.setStyle("-fx-font-size: 25px;");
        Label prix = new Label("prix"+i);
        prix.setStyle("-fx-font-size: 25px;");
        TextField quantite = new TextField("1");
        quantite.setStyle("-fx-font-size: 25px;");
        Button ajPanier = new Button("Ajouter Panier");
        ajPanier.setPrefWidth(200);
        
        ajPanier.setStyle("-fx-text-fill: white; " +
                      "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 22px; -fx-background-color: #154c45;");
        livre.getChildren().addAll(titre,prix,quantite,ajPanier);
        livre.setPadding(new Insets(10,0,10,0));
        ajPanier.setPadding(new Insets(7,0,7,0));
        listLivres.getChildren().addAll(livre);
    }

    panelCentral.setCenter(listLivres);
    } 


    private void fenetrePanier() {
        this.gauche=null;
        this.banniere=new BorderPane();
        banniere.setStyle("-fx-background-color: #ece3d4;");
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
        
        panelCentral.setMargin(listLivres,new Insets(150,0,150,0));
        panelCentral.setCenter(listLivres);    
    
    }

    private void fenetreAccueilV(){
        this.gauche=null;

        
        this.banniere=new BorderPane();
        banniere.setStyle("-fx-background-color: lightpink;");
        ImageView imgLogo = new ImageView(this.lesImages.get(0));
        imgLogo.setFitWidth(520);
        imgLogo.setPreserveRatio(true);
        banniere.setPadding(new Insets(10,0,0,0));

        Label user = new Label("Accueil V");
        user.setStyle("-fx-font-size: 27px;");
        user.setPadding(new Insets(0,125,0,0));


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

        Button bDeconnexion = new Button(null,deco);
        bDeconnexion.setUserData("DECONNEXION");
        bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
        Button bPanier = null;
        
        Button boutonMaison = new Button(null,maison);
        boutonMaison.setUserData("MAISON");

        Button boutonProfil = new Button(null,profil);
        boutonProfil.setUserData("PROFIL");
        boutonProfil.setOnAction(new ControleurRetourRedirection(this));

        bouttons.getChildren().addAll(bDeconnexion,boutonMaison,boutonProfil);

        bouttons.setMargin(boutonMaison,new Insets(5));
        bouttons.setMargin(bDeconnexion,new Insets(5));
        bouttons.setMargin(boutonProfil,new Insets(5));
        banniere.setLeft(imgLogo);
        banniere.setCenter(user);
        banniere.setRight(bouttons);



        this.panelCentral= new BorderPane();

        VBox listLivres = new VBox(50);
        
        Button ajLivre = new Button("Ajouter un livre");
        ajLivre.setStyle("-fx-text-fill: white; " +
                      "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 28px; -fx-background-color: #154c45;");
        ajLivre.setPrefWidth(400);

        Button gererStock = new Button("Gérer le stock");
        gererStock.setStyle("-fx-text-fill: white; " +
                      "-fx-font-color:#ece3d3; -fx-border-width: 2; " +
                      "-fx-border-radius: 25; -fx-background-radius: 25; -fx-font-weight: bold; -fx-font-size: 28px; -fx-background-color: #154c45;");
        gererStock.setPrefWidth(400);

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

        listLivres.getChildren().addAll(ajLivre,gererStock,comCli,transLivre);
        listLivres.setMargin(ajLivre,new Insets(5,20,5,20));
        listLivres.setMargin(gererStock,new Insets(5,20,5,20));
        listLivres.setMargin(comCli,new Insets(5,20,5,20));
        listLivres.setMargin(transLivre,new Insets(5,20,5,20));
        
        listLivres.setPadding(new Insets(200,600,200,780));
        listLivres.setStyle("-fx-background-color: #2c2c2c;");

        panelCentral.setCenter(listLivres);
    }

    private void fenetreAccueilA(){
        this.gauche=null;

        
        this.banniere=new BorderPane();
        banniere.setStyle("-fx-background-color: lightpink;");
        ImageView imgLogo = new ImageView(this.lesImages.get(0));
        imgLogo.setFitWidth(520);
        imgLogo.setPreserveRatio(true);
        banniere.setPadding(new Insets(10,0,0,0));

        // On re cast ?
        //Administrateur admin = (Administrateur) personneConnectee;
        Label user = new Label("Accueil Admin : bienvenue " + personneConnectee.getNom()
        );
        user.setStyle("-fx-font-size: 27px;");
        user.setPadding(new Insets(0,125,0,0));


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

        Button bDeconnexion = new Button(null,deco);
        bDeconnexion.setUserData("DECONNEXION");
        bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
        Button bPanier = null;
        
        Button boutonMaison = new Button(null,maison);
        boutonMaison.setUserData("MAISON");

        Button boutonProfil = new Button(null,profil);
        boutonProfil.setUserData("PROFIL");
        boutonProfil.setOnAction(new ControleurRetourRedirection(this));

        bouttons.getChildren().addAll(bDeconnexion,boutonMaison,boutonProfil);

        bouttons.setMargin(boutonMaison,new Insets(5));
        bouttons.setMargin(bDeconnexion,new Insets(5));
        bouttons.setMargin(boutonProfil,new Insets(5));
        banniere.setLeft(imgLogo);
        banniere.setCenter(user);
        banniere.setRight(bouttons);



        this.panelCentral= new BorderPane();

        VBox listLivres = new VBox(40);
        
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
        listLivres.setStyle("-fx-background-color: #2c2c2c;");


        panelCentral.setCenter(listLivres);
    }

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
    banniere.setStyle("-fx-background-color: #5470d6;"); // Couleur bleue comme sur l'image
    
    ImageView imgLogo = new ImageView(this.lesImages.get(0));
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
    panelCentral.setStyle("-fx-background-color: #f5f5f5;");

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

    panelCentral.setCenter(infoContainer);
}
//--------------------------------------------------------------------------------------------------------------------------
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
    banniere.setStyle("-fx-background-color: #5470d6;"); // Couleur bleue comme sur l'image
    
    ImageView imgLogo = new ImageView(this.lesImages.get(0));
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
    panelCentral.setStyle("-fx-background-color: #f5f5f5;");

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
    
    Label magasinLabel = new Label("MAGASIN :  " + vend.getMagasin().getNomMag());
    magasinLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");
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

    panelCentral.setCenter(infoContainer);
}


//--------------------------------------------------------------------------------------------------------------------

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
    banniere.setStyle("-fx-background-color: #5470d6;"); // Couleur bleue comme sur l'image
    
    ImageView imgLogo = new ImageView(this.lesImages.get(0));
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
    panelCentral.setStyle("-fx-background-color: #f5f5f5;");

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
        boutonsContainer);

    panelCentral.setCenter(infoContainer);
    
    } 





    public void fenetreCreerVendeur(){
        this.gauche=null;

        
        this.banniere=new BorderPane();
        banniere.setStyle("-fx-background-color: lightpink;");
        ImageView imgLogo = new ImageView(this.lesImages.get(0));
        imgLogo.setFitWidth(520);
        imgLogo.setPreserveRatio(true);
        banniere.setPadding(new Insets(10,0,0,0));

        // On re cast ?
        //Administrateur admin = (Administrateur) personneConnectee;
        Label user = new Label("Accueil Admin : bienvenue " + personneConnectee.getNom()
        );
        user.setStyle("-fx-font-size: 27px;");
        user.setPadding(new Insets(0,125,0,0));


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

        Button bDeconnexion = new Button(null,deco);
        bDeconnexion.setUserData("DECONNEXION");
        bDeconnexion.setOnAction(new ControleurRetourRedirection(this));
        Button bPanier = null;
        
        Button boutonMaison = new Button(null,maison);
        boutonMaison.setUserData("MAISON");

        Button boutonProfil = new Button(null,profil);
        boutonProfil.setUserData("PROFIL");
        boutonProfil.setOnAction(new ControleurRetourRedirection(this));

        bouttons.getChildren().addAll(bDeconnexion,boutonMaison,boutonProfil);

        bouttons.setMargin(boutonMaison,new Insets(5));
        bouttons.setMargin(bDeconnexion,new Insets(5));
        bouttons.setMargin(boutonProfil,new Insets(5));
        banniere.setLeft(imgLogo);
        banniere.setCenter(user);
        banniere.setRight(bouttons);

        banniere.setLeft(imgLogo);
        banniere.setCenter(user);
        banniere.setRight(bouttons);



        this.panelCentral= new BorderPane();

        VBox vendeur = new VBox(150);

        HBox partieGauche = new HBox(50);

        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();

        TextField nom = new TextField();
        TextField prenom = new TextField();
        PasswordField motdepasse = new PasswordField();

        Text nomText = new Text("Nom :");
        Text prenomText = new Text("Prénom :");
        Text motdepasseText = new Text("Mot de passe :");

        nomText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;-fx-text-fill: #2c2c2c;");
        prenomText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;-fx-text-fill: #2c2c2c;");
        motdepasseText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;-fx-text-fill: #2c2c2c;");

        nom.setPromptText("Entrez votre nom");
        prenom.setPromptText("Entrez votre prénom");
        motdepasse.setPromptText("Entrez votre mot de passe");

        hbox1.getChildren().addAll(nomText,nom);
        hbox2.getChildren().addAll(prenomText,prenom);
        hbox3.getChildren().addAll(motdepasseText,motdepasse);
        partieGauche.getChildren().addAll(hbox1,hbox2,hbox3);

        HBox partieDroite = new HBox(50);

        ComboBox<String> comboBox = new ComboBox<>();
        List<Magasin> mags = magasinBD.getToutLesMagasins();
        for(Magasin mag : mags){
            comboBox.getItems().add(mag.getNomMag());
        }
        comboBox.setValue("Choix du magasin"); 
        comboBox.setMinWidth(350);
        comboBox.setMinHeight(100);
        comboBox.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; " +
                       "-fx-background-color: white; -fx-border-color: #2c2c2c; " +
                         "-fx-border-width: 2; -fx-border-radius: 0;");
        
        // Bouton de connexion
        Button choix = new Button("Créer");
        choix.setOnAction(new ControleurCreerVendeur(this, new  AdministrateurBD(connection), magasinBD, vendeurBD));
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

        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
        boolean allFilled = !nom.getText().trim().isEmpty()
                      && !prenom.getText().trim().isEmpty()
                      && !motdepasse.getText().trim().isEmpty();

            choix.setDisable(!allFilled);
        };
        String id = vendeurBD.genererId();
        nom.textProperty().addListener(listener);
        prenom.textProperty().addListener(listener);
        motdepasse.textProperty().addListener(listener);
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("Choix de l'utilisateur")) {
                choix.setDisable(false);
                try {
                    this.magasinCreation = magasinBD.getMagasin(comboBox.getValue());
                } catch (Exception e) {
                    System.out.println("Erreur lors de la récupération du magasin : " + e.getMessage());
                }
            } 
        });
        this.creationVendeur = new Vendeur(id,String.valueOf(nom.getText()), String.valueOf(prenom.getText()), String.valueOf(motdepasse.getText()), magasinCreation);

        partieDroite.getChildren().addAll(comboBox,choix);

        vendeur.getChildren().addAll(partieGauche,partieDroite);
        
        panelCentral.setCenter(vendeur);
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

    public void modeChoix(){
        fenetreChoix();
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);

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

    public void modeAccueilC(){
        fenetreAccueilC();
        this.boutonMaison.setDisable(true);
        this.boutonProfil.setDisable(false);
        this.bPanier.setDisable(false);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
        
    }

    public void modePanier(){
        fenetrePanier();
        this.boutonMaison.setDisable(false);
        this.boutonProfil.setDisable(false);
        this.bPanier.setDisable(true);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
       
    }

    public void modeAccueilV(){
        fenetreAccueilV();
        this.boutonMaison.setDisable(true);
        this.boutonProfil.setDisable(false);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    }

    public void modeAccueilA(){
        fenetreAccueilA();
        this.boutonMaison.setDisable(true);
        this.boutonProfil.setDisable(false);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    }

    public void modeProfilC(){
        fenetreProfilC();
        this.boutonMaison.setDisable(false);
        this.boutonProfil.setDisable(true);
        this.bPanier.setDisable(false);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    } 

   

    public void modeProfilV(){
        fenetreProfilV();
        this.boutonMaison.setDisable(false);
        this.boutonProfil.setDisable(true);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    }

    public void modeProfilA(){
        fenetreProfilA();
        this.boutonMaison.setDisable(false);
        this.boutonProfil.setDisable(true);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    } 

    public void modeCreerVendeur(){
        fenetreCreerVendeur();
        this.boutonMaison.setDisable(false);
        this.boutonProfil.setDisable(false);
        fenetre.setTop(this.banniere);
        fenetre.setLeft(this.gauche);
        fenetre.setCenter(this.panelCentral);
    }


    public void popUpPasChoix(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de choix");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez choisir un.");
        Optional<ButtonType> result = alert.showAndWait();
    }
        
    public void popUpConnexionImpossible(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de connexion");
        alert.setHeaderText(null);
        alert.setContentText("Identifiants incorrects. Veuillez réessayer.");
        Optional<ButtonType> result = alert.showAndWait();
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
