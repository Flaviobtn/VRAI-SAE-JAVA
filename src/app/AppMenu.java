package app;
import Modele.*;
import bd.*;
import java.sql.*;
import java.util.*;

public class AppMenu{
    public static void afficherMenu(String titre, List<String> options) {
        //clear();
        // Déterminer la largeur maximale
        int largeur = titre.length();
        for (int i = 0; i < options.size(); i++) {
            String ligne = (i + 1) + ". " + options.get(i);
            if (ligne.length() > largeur) {
                largeur = ligne.length();
            }
        }
        largeur += 2; // marges

        // Afficher le cadre supérieur
        System.out.println("╔" + "═".repeat(largeur) + "╗");
        System.out.println("║" + centrerTexte(titre, largeur) + "║");
        System.out.println("╠" + "═".repeat(largeur) + "╣");

        // Afficher les options
        for (int i = 0; i < options.size(); i++) {
            String ligne = (i + 1) + ". " + options.get(i);
            System.out.println("║ "+ligne+" ".repeat(largeur-options.get(i).length()-4)+"║");
        }

        // Afficher le cadre inférieur
        System.out.println("╚" + "═".repeat(largeur) + "╝");
    }

    public static String centrerTexte(String texte, int largeur) {
    int padding = (largeur - texte.length()) / 2;
    if(padding % 2 == 0){
        padding += 1;
    }
    return " ".repeat(Math.max(0, padding)) + texte + " ".repeat(Math.max(0, padding));
}
    
    public static void main(String[] args) {
        Connection connexion = ConnectionBD.getConnection();
        Scanner scanner = new Scanner(System.in);
        List<String> mainapp = List.of(
            "Administrateur",
            "Vendeur",
            "Client",
            "Quitter"
        );

        List<String> connect = List.of(
            "Se connecter",
            "S'inscrire"
        );

        List<String> client = List.of(
            "Accès Bibliothèque",
            "Recommandation",
            "Passer Commande",
            "Voir mes Commandes",
            "Retour"
        );

        List<String> vendeur = List.of(
            "Ajouter livre ",
            "Modifier Quantité Livre",
            "Vérifier Disponibilité",
            "Commander du stock",
            "Transférer un livre",
            "Quitter"
        );

        List<String> admin = List.of(
            "Créer un compte vendeur",
            "Ajouter une nouvelle librairie",
            "Gérer les stocks globaux", //doit ouvrir un autre menu
            "Consulter les statistiques de vente",
            "Quitter"
        );

        List<String> statistiques = List.of(
            "Nombre de livres vendu d'un magasin",
            "Chiffre d'affaire du magasin",
            "Le livre le plus vendu",
            "quitter"
        );


        int choix = 0;
        do {
            //On commence par gérer la connexion
            

            // si c'est un client, il peut soit s'inscrire soit se connecter
            boolean estConnecte = false;
            while (! estConnecte){
                afficherMenu("Qui êtes vous ?", mainapp);
                int identif = scanner.nextInt();
                choix = identif; // on garde le choix pour la suite
                if (identif == 3){
                    afficherMenu("choisissez votre mode de connexion", connect);
                    int modeCo = scanner.nextInt();
                    switch (modeCo) {
                        case 1 -> {
                            if (! seconnecter(identif)){
                                System.out.println("Singe");

                            }
                            else{
                                System.out.println("bogoss");
                                estConnecte = true; // on sort de la boucle de connexion
                            }
                        }

                        case 2 -> {
                            inscription();
                        }

                        default -> {
                            //on retourne juste au menu
                            System.out.println("Retour au menu");
                        }
                    }
                }
                // sinon si c'est un admin ou un vendeur, on les connecte
                else if (identif == 1 || identif == 2) {
                    if (! seconnecter(identif)){
                        System.out.println("Singe");
                        // on retourne au menu de connexion
                        
                    }
                    else{
                        System.out.println("bogoss");
                        estConnecte = true; // on sort de la boucle de connexion
                    }
                }
                else {
                    // si l'utilisateur a choisi de quitter, on sort de la boucle
                    if (identif == 4) {
                        System.out.println("👋 Au revoir !");
                        return; // on quitte le programme
                    }
                    else {
                        System.out.println("❌ Option invalide. Veuillez réessayer.");
                    }

                    
                }
            }
            
            
            switch (choix) {
                //Boucle Admin


                case 1 ->{
                    do {
                        afficherMenu("Accès Admin", admin);
                        System.out.print("Votre choix : ");

                        while (!scanner.hasNextInt()) {
                            System.out.println("❌ Veuillez entrer un nombre valide.");
                            scanner.next();
                            System.out.print("Votre choix : ");
                        }

                        choix = scanner.nextInt();
                        switch(choix){
                            //créer un compte vendeur
                            case 1->{
                                    //Vendeur unVendeur = creerVendeur();
                                    creerVendeur();
                            }
                            //Ajouter une nouvelle librairie
                            case 2-> {
                                    ajouterLibrairie();
                            }

                            //Gérer les stocks globaux //doit ouvrir un autre menu
                            case 3-> {
                                gererLesStocks();
                                // afficher les stock d'un magasin précis
                                //ajoute un livre dans la bd
                                //ajoute un livre à un magasin

                            }

                            //Consulter les statistiques de vente
                            case 4-> {
                                consulterStat();
                            }

                            case 5-> System.out.println("Retour au main");
                            default-> System.out.println("❌ Option invalide.");
                        }

                    } while(choix !=client.size());
                }
        

                //Boucle Vendeur
                case 2 -> {
                    do {
                        afficherMenu("Accès Vendeur", vendeur);
                        System.out.print("Votre choix : ");

                        while (!scanner.hasNextInt()) {
                            System.out.println("❌ Veuillez entrer un nombre valide.");
                            scanner.next();
                            System.out.print("Votre choix : ");
                        }

                        choix = scanner.nextInt();
                        switch(choix){
                            //Ajouter livre
                            case 1-> {
                                ajouterLivre();
                            }
                            //Modifier Quantité Livre
                            case 2-> {
                                modifierQte();
                            }
                            // Vérifier Disponibilité
                            case 3-> {
                                estDisponible();
                            }

                            // Commander du stock
                            case 4-> {
                                commanderStock();
                            }


                            //Transférer un livre
                            case 5-> {
                                transfererLivre();
                            }

                            //quitter 
                            case 6-> System.out.println("Retour au main");
                            default-> System.out.println("❌ Option invalide.");
                        }

                    } while(choix !=vendeur.size());
                }
                
                //Boucle Client
                case 3 -> {
                    do {
                        afficherMenu("Accès Client", client);
                        System.out.print("Votre choix : ");

                        while (!scanner.hasNextInt()) {
                            System.out.println("❌ Veuillez entrer un nombre valide.");
                            scanner.next();
                            System.out.print("Votre choix : ");
                        }
                        choix = scanner.nextInt();
                        switch(choix){
                           //"Accès Bibliothèque"
                            case 1-> {
                               accesBibli();
                            }

                            //"Recommandation"
                            case 2-> {
                                recommandation();
                            }

                            //"Passer Commande"
                            case 3-> {
                                passerComm();
                            }

                            //"Voir mes Commandes"
                            case 4-> {
                                voirCommandes();
                            }

                            //"Retour"
                            case 5-> System.out.println("Retour au main");
                            default-> System.out.println("❌ Option invalide.");
                        }

                    } while(choix !=client.size());
                }
                case 4 -> System.out.println("👋 Au revoir !");
                default -> System.out.println("❌ Option invalide.");
            }

            System.out.println(); // espace

        } while (choix != mainapp.size());

        scanner.close();
    }


    // les fonctions d'administrateur  ----------------------------------------------------------------------------------------------------------
    public static void  creerVendeur(){
        Connection connexion = ConnectionBD.getConnection();
        Scanner scanner = new Scanner(System.in);
        // Cette fonction sert à créer un nouveau vendeur et à l'ajouter dans la base de donnée
        
        //on enregistre le nom
        String aff1 = "Quel est son nom ?";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff1);
        afficherMenu("Vous voulez créer un vendeur ", lstRep);
        String nom = scanner.nextLine();

        //on enregistre le prenom
        lstRep = new ArrayList<>();
        aff1 = "Quel est son prenom ?";
        lstRep.add(aff1);
        afficherMenu("Vous voulez créer un vendeur ", lstRep);
        String prenom = scanner.nextLine();

        //on enregistre le magasin

        // le nom du magasin
        lstRep = new ArrayList<>();
        String question = "Choisissez le magasin ou assigner le vendeur ";
        Magasin mag = demanderMagasin(question);
        
        VendeurBD vendeurBD = new VendeurBD(connexion);
        String idVendeur = vendeurBD.genererId();
        // on genere le mdp
        String mdp =  String.valueOf(prenom.charAt(0)) + nom.toLowerCase();
        Vendeur unVendeur = new Vendeur(idVendeur, nom, prenom, mdp, mag);
        vendeurBD.insertVendeur(unVendeur);
        lstRep.add("Le vendeur travaillera au magasin : "+ mag.getNomMag());
        lstRep.add("Son identifiant sera : " + idVendeur);
        lstRep.add("Son mot de passe sera : " + mdp);
        afficherMenu("Le magasin du vendeur ", lstRep);
        
                
    }

    public static Magasin demanderMagasin(String question){
        Connection connexion = ConnectionBD.getConnection();
        Scanner scanner = new Scanner(System.in);
        // on récupère la liste des magasins
        MagasinBD magasinBD = new MagasinBD(connexion);
        List<Magasin> lstMag = magasinBD.getToutLesMagasins();
        // on affiche le menu pour choisir un magasin
        // ------------------------------------------------------------------
        
        Magasin leMag = null;
        boolean correct = false;
        
        while (! correct){ 
            List<String> lstRep = new ArrayList<>();
            for (Magasin mag : lstMag){
                lstRep.add(mag.getNomMag());
            }
            lstRep.add("QUITTER");
            afficherMenu(question, lstRep);

            int choix = scanner.nextInt();
            if (choix == lstRep.size()){
                lstRep = new ArrayList<>();
                lstRep.add("Vous quittez " );
                afficherMenu(question,lstRep);
                correct = true;
            }
            else{
                if (choix-1 < lstRep.size()){
                    leMag = lstMag.get(choix-1);
                    lstRep = new ArrayList<>();
                    lstRep.add("Vous avez choisi le magasin "+ leMag.getNomMag() );
                    afficherMenu(question,lstRep);
                    correct = true;
                }
                else {
                    lstRep = new ArrayList<>();
                    lstRep.add("Veuillez choisir un magasin correct ");
                    afficherMenu(question,lstRep);
                }
            }
        }
        
        return leMag;
    }

    

    public static void ajouterLibrairie(){
        // Cette fonction sert à ajouter une nouvelle librairie au réseau
        Scanner scanner = new Scanner(System.in);
        List<String> lstRep = new ArrayList<>();
        // deja pour l'id on recupere la liste des magasins existantes et on ajoute 1
        Connection connexion = ConnectionBD.getConnection();
        MagasinBD magasinBD = new MagasinBD(connexion);
        List<Magasin> lstMag = magasinBD.getToutLesMagasins();
        String idmag = String.valueOf(lstMag.size() + 1); // on genere un id de magasin
        // le nom de la librairie
        String aff1 = "Quel est le nom de la librairie ?";
        lstRep.add(aff1);
        afficherMenu("Vous voulez ajouter une librairie ", lstRep);
        String nomMag = scanner.nextLine();

        // la ville de la librairie
        lstRep = new ArrayList<>();
        String aff2 = "Quelle est la ville de la librairie ?";
        lstRep.add(aff2);
        afficherMenu("Vous voulez ajouter une librairie ", lstRep);
        String villeMag = scanner.nextLine();

        Magasin mag = new Magasin(idmag, nomMag, villeMag);
        magasinBD.insertMagasin(mag);

        
    }

    public static void gererLesStocks(){
        Scanner scanner = new Scanner(System.in);
        // Cette fonction permet de "gérer les stock", donc on peut soit :
        // - afficher les stock d'un magasin précis
        // - ajoute un livre dans la bd
        // - ajoute un livre à un magasin
        String aff1 = "afficher les stock d'un magasin";
        String aff2 = "ajoute un livre dans la bd";
        String aff3 = "ajoute un livre à un magasin";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff1);
        lstRep.add(aff2);
        lstRep.add(aff3);
        afficherMenu("Vous voulez ajouter une librairie ", lstRep);
        int choix = scanner.nextInt();
        List<String> nvlstRep = new ArrayList<>();
        switch (choix) {
            case 1 -> {
                lstRep = new ArrayList<>();
                lstRep.add(aff1);
                afficherMenu("Voici les stocks du magasin : ", lstRep);
            }
                
            case 2 -> {
                lstRep = new ArrayList<>();
                lstRep.add(aff2);
                afficherMenu("Vous voulez ajouter un livre au magasin", lstRep);
            }

            case 3 -> {
                lstRep = new ArrayList<>();
                lstRep.add(aff3);
                afficherMenu("Vous voulez ajouter un livre à la base", lstRep);
            }

                
            default -> {
                lstRep = new ArrayList<>();
                String aff = "Nombre Invalide, retour au menu";
                lstRep.add(aff);
                afficherMenu(" Erreur lors de la réponse ", lstRep);
            }
        }
    }

    public static void consulterStat(){
        // Cette fonction sert à consulter les statistiques d'un magasin
        String aff = "Vous consultez les statistiques";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff);
        afficherMenu("Consultation statistique ", lstRep);
    }


    // Les fonctions de vendeur ----------------------------------------------------------------------------------------------------------
    public static void ajouterLivre(){
        // Cette fonction sert à ajouter un livre à la BD
        Livre livre = demanderLivre();
        Connection connexion = ConnectionBD.getConnection();
        LivreBD livreBD = new LivreBD(connexion);
        livreBD.ajouterLivre(livre);
        List<String> lstRep = new ArrayList<>();
        lstRep.add("Le livre " + livre.getTitre() + " a été ajouté à la base de données.");
        afficherMenu("Ajout d'un livre ", lstRep);
    }


    public static Livre demanderLivre(){
        // Cette fonction sert à demander les informations d'un livre
        Scanner scanner = new Scanner(System.in);
        String aff1 = "Veuillez entrer l'ISBN du livre";
        String aff2 = "Veuillez entrer le titre du livre";
        String aff6 = "Veuillez entrer le nombre de pages du livre";
        String aff7 = "Veuillez entrer le prix du livre";
        String aff8 = "Veuillez entrer la date de publication du livre";
        String aff9 = "Veuillez entrer le nom de l'éditeur du livre";

        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff1);
        afficherMenu("Ajout d'un livre ", lstRep);
        String isbn = scanner.nextLine();

        lstRep = new ArrayList<>();
        lstRep.add(aff2);
        afficherMenu("Ajout d'un livre ", lstRep);
        String titre = scanner.nextLine();

        Auteur auteur = demanderAuteur("De quel auteur est ce livre ?");

        lstRep = new ArrayList<>();
        lstRep.add(aff6);
        afficherMenu("Ajout d'un livre ", lstRep);
        int nbPages = scanner.nextInt();

        lstRep = new ArrayList<>();
        lstRep.add(aff7);
        afficherMenu("Ajout d'un livre ", lstRep);
        double prix = scanner.nextDouble();

        
        lstRep = new ArrayList<>();
        lstRep.add(aff8);
        afficherMenu("Ajout d'un livre ", lstRep);
        int dateParution = scanner.nextInt();

        lstRep = new ArrayList<>();
        lstRep.add(aff9);
        afficherMenu("Ajout d'un livre ", lstRep);
        String editeurNom = scanner.nextLine();
        Editeur editeur = new Editeur(editeurNom);
        Livre livre = new Livre(isbn, titre, auteur, dateParution, nbPages, prix, editeur);
        return livre;
    }

    public static Auteur demanderAuteur(String question){
        // Cette fonction sert à demander les informations d'un auteur
        List<Auteur> lstAuteurs = new ArrayList<>();
        Connection connexion = ConnectionBD.getConnection();
        AuteurBD auteurBD = new AuteurBD(connexion);
        try { 
            lstAuteurs = auteurBD.getListeAuteurs();
        }
        catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des auteurs : " + e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);
        Auteur auteur = null;
        boolean correct = false;
        
        while (! correct){ 
            List<String> lstRep = new ArrayList<>();
            for (Auteur aut : lstAuteurs){
                lstRep.add(aut.getNom() + " " + aut.getPrenom());
            }
            lstRep.add("QUITTER");
            afficherMenu(question, lstRep);

            int choix = scanner.nextInt();
            if (choix == lstRep.size()){
                lstRep = new ArrayList<>();
                lstRep.add("Vous quittez " );
                afficherMenu(question,lstRep);
                correct = true;
            }
            else{
                if (choix-1 < lstRep.size()){
                    auteur = lstAuteurs.get(choix-1);
                    lstRep = new ArrayList<>();
                    lstRep.add("Vous avez choisi l'auteur(trice) : "+ auteur.getNom() + " " + auteur.getPrenom() );
                    afficherMenu(question,lstRep);
                    correct = true;
                }
                else {
                    lstRep = new ArrayList<>();
                    lstRep.add("Veuillez choisir un(e) auteur(trice) correct ");
                    afficherMenu(question,lstRep);
                }
            }
        }
        return auteur;
    }

    public static void modifierQte(){
        // Cette fonction sert à modifier la qte d'un livre dans la BD
        String aff = "De quel livre voulez vous modifier la quantité ?";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff);
        afficherMenu("Quantité d'un livre ", lstRep);
    }

    public static void estDisponible(){
        // Cette fonction sert à savoir si un livre est disponible
        String aff = "Veuillez entrer un livre";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff);
        afficherMenu("Disponibilité d'un livre ", lstRep);
    }

    public static void commanderStock(){
        // Cette fonction sert à commander un stock pour un magasin
        String aff1 = "Veuillez entrer un livre à commander";
        String aff2 = "Veuillez entrer un magasin où recevoir";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff1);
        lstRep.add(aff2);
        afficherMenu("Commande de stock", lstRep);
    }

    public static void transfererLivre(){
        // Cette fonction sert à transferer un livre d'un magasin à un autre
        String aff1 = "Veuillez entrer un livre à transférer";
        String aff2 = "Veuillez entrer le magasin d'où le livre provient";
        String aff3 = "Veuillez entrer le magasin où l'envoyer";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff1);
        lstRep.add(aff2);
        lstRep.add(aff3);
        afficherMenu("Commande de stock", lstRep);
    }


    // les fonctions du client  ----------------------------------------------------------------------------------------------------------
    public static void accesBibli(){
        // Cette fonction sert à afficher tous les livres disponible d'un magasin
        Magasin mag = demanderMagasin("Dans quel magasin voulez vous accéder à la bibliothèque ?");
        List<Livre> lstLivres = new ArrayList<>();
        Connection connexion = ConnectionBD.getConnection();
        LivreBD livreBD = new LivreBD(connexion);
        List<String> lstRep = new ArrayList<>();
        lstLivres = livreBD.getTousLesLivres(mag);
        for (Livre livre : lstLivres) {
            lstRep.add(livre.getTitre());
        }
        afficherMenu("Accès bibliothèque ", lstRep);
    }

    public static void recommandation(){
        // Cette fonction sert à afficher les recomandations pour un client
        String aff = "Voici vos recommandations :";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff);
        afficherMenu("Recommandations", lstRep);
    }

    public static void passerComm(){
        // Cette fonction sert à passer une commande
        String aff = "Dans quel magasin voulez vous commander ?";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff);
        afficherMenu("Commande", lstRep);
    }

    public static void voirCommandes(){
        // Cette fonction sert à voir les commandes passées
        String aff = "Voici vos commandes";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff);
        afficherMenu("Vos commande", lstRep);
    }

    // autres fonctions ------------------------------------------


    public static boolean seconnecter(int lequel){
        Connection connexion = ConnectionBD.getConnection();
        Scanner scanner = new Scanner(System.in);
        List<String> lstRep = new ArrayList<>();

        lstRep.add("Veuillez entrer votre identifiant");
        lstRep.add("Pour quitter, tapez 'q'");
        afficherMenu("Connection ", lstRep);
        String ident = scanner.nextLine();
        if (ident.equalsIgnoreCase("q")) {
            System.out.println("Retour au menu principal");
            return false; // on quitte le programme
        }
        lstRep = new ArrayList<>();
        lstRep.add("Veuillez entrer votre mot de passe");
        lstRep.add("Pour quitter, tapez 'q'");
        afficherMenu("Connection ", lstRep);
        String mdp = scanner.nextLine();
        if (mdp.equalsIgnoreCase("q")) {
            System.out.println(" Retour au menu principal");
            return false; // on quitte le programme
        }
        
        // on veut que ça connecte si il y a un client, un vendeur ou un admin avec ces infos
        // sinon ça return false et on revient au menu de connexion
        if (lequel == 1) { // si c'est un admin
            AdministrateurBD adminBD = new AdministrateurBD(connexion);
            try {
                if (adminBD.seconnecterAdmin(ident, mdp)) {
                    return true;
                } else {
                    System.out.println("❌ Identifiant ou mot de passe incorrect.");
                    return false;
                }
            } catch (SQLException e) {
                System.err.println("Erreur de connexion : " + e.getMessage());
                return false;
            }
        } else if (lequel == 2) { // si c'est un vendeur
            VendeurBD vendeurBD = new VendeurBD(connexion);
            try {
                if (vendeurBD.seconnecterVendeur(ident, mdp)) {
                    return true;
                } else {
                    System.out.println("❌ Identifiant ou mot de passe incorrect.");
                    return false;
                }
            } catch (SQLException e) {
                System.err.println("Erreur de connexion : " + e.getMessage());
                return false;
            }
        } else if (lequel == 3) { // si c'est un client
            ClientBD clientBD = new ClientBD(connexion);
            try {
                if (clientBD.seconnecterClient(ident, mdp)) {
                    return true;
                } else {
                    System.out.println("❌ Identifiant ou mot de passe incorrect.");
                    return false;
                }
            } catch (SQLException e) {
                System.err.println("Erreur de connexion : " + e.getMessage());
                return false;
            }
        }
        return false; // si aucun des cas n'est rempli, on retourne false
    }

    public static boolean inscription(){
        Scanner scanner = new Scanner(System.in);
        Connection connexion = ConnectionBD.getConnection();
        /* 
        il faudrat penser a creer une liste contenant tous les vendeurs/admin/clients existant 
        pour voir s'il y a pas deja les infos dedans (donc si l'identifiant existe deja)
        */
       // le prenom
        List<String> lstRep = new ArrayList<>();
        lstRep.add("Veuillez entrer votre prenom");
        afficherMenu("Connection ", lstRep);
        String prenom = scanner.nextLine();

        // le nom
        lstRep = new ArrayList<>();
        lstRep.add("Veuillez entrer votre nom");
        afficherMenu("Connection ", lstRep);
        String nom = scanner.nextLine();

        // le mdp
        lstRep = new ArrayList<>();
        lstRep.add("Veuillez entrer votre mot de passe");
        afficherMenu("Connection ", lstRep);
        String mdp = scanner.nextLine();

        // l'addresse 
        lstRep = new ArrayList<>();
        lstRep.add("Veuillez entrer votre adresse ");
        afficherMenu("Connection ", lstRep);
        String adr = scanner.nextLine();

        // la ville
        lstRep = new ArrayList<>();
        lstRep.add("Veuillez entrer votre ville de résidence");
        afficherMenu("Connection ", lstRep);
        String ville = scanner.nextLine();

        // le code postal 
        lstRep = new ArrayList<>();
        lstRep.add("Veuillez entrer votre code postal");
        afficherMenu("Connection ", lstRep);
        int cp = scanner.nextInt();

        ClientBD clientBD = new ClientBD(connexion);
        Integer idCli = clientBD.genererId();

        String id = String.valueOf(prenom.charAt(0)).toLowerCase() + nom.toLowerCase();
        lstRep = new ArrayList<>();
        lstRep.add("D'accord, bienvenue parmi nous "+ prenom + ". Votre identifiant sera : " + id );
        afficherMenu("Connection ", lstRep);
        ClientBD client = new ClientBD(connexion);
        try {
            client.inscrireClient(idCli, prenom, nom, mdp, adr, ville , cp);
        } catch (SQLException e) {
            System.err.println("Erreur d'inscription : " + e.getMessage());
            return false;
        }

        return true;
    }
}