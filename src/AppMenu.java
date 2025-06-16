import java.util.*;
import java.sql.*;
import java.io.*;

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
        if(padding%2==0){
            padding+=1;
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
            afficherMenu("Qui êtes vous ?", mainapp);
            int identif = scanner.nextInt();

            // si c'est un client, il peut soit s'inscrire soit se connecter
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
                        }
                    }

                    case 2 -> {
                        inscription();
                    }

                    default -> {
                        System.out.println("❌ Option invalide.");
                    }
                }
            }
            // sinon si c'est un admin ou un vendeur, on les connecte
            else if (identif == 1 || identif == 2) {
                if (! seconnecter(identif)){
                    System.out.println("Singe");
                }
                else{
                    System.out.println("bogoss");
                }
            }
            else {
                System.out.println("❌ Option invalide.");
                continue; // on recommence le menu principal
            }
            
            choix = identif;

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

    public static void  creerVendeur(){ //public static Vendeur creerVendeur()
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
        lstRep.add("Le vendeur travaillera au magasin : "+ mag.getNomMag());
        afficherMenu("Le magasin du vendeur ", lstRep);
        //Vendeur unVendeur = new Vendeur();
                
    }

    public static Magasin demanderMagasin(String question){
        Scanner scanner = new Scanner(System.in);
        // jeu de test en attendant les fonctions pour récupérer les magasins(String idmag, String nomMag, String villeMag)
        List<Magasin> lstMag = new ArrayList<>();
        Magasin mag1 = new Magasin("1", "Fnac" , "Orléans" );
        Magasin mag2 = new Magasin("2", "LibrairieLa" , "Blois" );
        Magasin mag3 = new Magasin("3", "Amazon" , "Tours" );
        lstMag.add(mag1);
        lstMag.add(mag2);
        lstMag.add(mag3);
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
        String aff = "vous avez créé une librairie";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff);
        afficherMenu("Vous voulez ajouter une librairie ", lstRep);
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

    public static void ajouterLivre(){
        // Cette fonction sert à ajouter un livre à la BD
        String aff = "Quel livre voulez vous ajouter ?";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff);
        afficherMenu("Ajout d'un livre ", lstRep);
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

    public static void accesBibli(){
        // Cette fonction sert à afficher tous les livres disponible d'un magasin
        String aff = "Voici les livres disponible dans ce magasin :";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff);
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

    public static boolean seconnecter(int lequel){
        Connection connexion = ConnectionBD.getConnection();
        Scanner scanner = new Scanner(System.in);
        List<String> lstRep = new ArrayList<>();

        lstRep.add("Veuillez entrer votre identifiant");
        afficherMenu("Connection ", lstRep);
        String ident = scanner.nextLine();
        lstRep = new ArrayList<>();
        lstRep.add("Veuillez entrer votre mot de passe");
        afficherMenu("Connection ", lstRep);
        String mdp = scanner.nextLine();
        
        if (lequel == 1){
            System.out.println("Vous êtes un administrateur");
            AdministrateurBD admin = new AdministrateurBD(connexion);
            try{
                admin.seconnecterAdmin(ident, mdp);
                System.out.println("Connexion réussie en tant qu'administrateur.");
            }
            catch (SQLException e){
                System.err.println("Erreur de connexion : " + e.getMessage());
            }
        } else if (lequel == 2){
            System.out.println("Vous êtes un vendeur");
            VendeurBD vendeur = new VendeurBD(connexion);
            try{
                vendeur.seconnecterVendeur(ident, mdp);
                System.out.println("Connexion réussie en tant que vendeur.");
            }
            catch (SQLException e){
                System.err.println("Erreur de connexion : " + e.getMessage());
            }

        } else if (lequel == 3){
            System.out.println("Vous êtes un client");
            ClientBD client = new ClientBD(connexion);
            try{
                client.seconnecterClient(ident, mdp);
                System.out.println("Connexion réussie en tant que client.");
            }
            catch (SQLException e){
                System.err.println("Erreur de connexion : " + e.getMessage());
            }

        } else {
            System.out.println("Erreur de connexion, veuillez réessayer");
            return false;
        }
        /* 
        il faudrat penser a creer une liste contenant tous les vendeurs/admin/clients existant 
        pour voir s'il contient bien les informations rentrees
        */
        lstRep = new ArrayList<>();
        lstRep.add("Content de vous revoir "+ ident);
        afficherMenu("Connection ", lstRep);
        return true;
    }

    public static boolean inscription(){
        Scanner scanner = new Scanner(System.in);
        Connection connexion = ConnectionBD.getConnection();
        /* 
        il faudrat penser a creer une liste contenant tous les vendeurs/admin/clients existant 
        pour voir s'il y a pas deja les infos dedans (donc si l'identifiant existe deja)
        */
        List<String> lstRep = new ArrayList<>();
        lstRep.add("Veuillez entrer votre identifiant");
        afficherMenu("Connection ", lstRep);
        String ident = scanner.nextLine();
        lstRep = new ArrayList<>();
        lstRep.add("Veuillez entrer votre mot de passe");
        afficherMenu("Connection ", lstRep);
        String mdp = scanner.nextLine();
        lstRep = new ArrayList<>();
        lstRep.add("D'accord, bienvenue parmi nous "+ ident);
        afficherMenu("Connection ", lstRep);
        return true;



    }
}