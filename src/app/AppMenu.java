package app;
import Modele.*;
import bd.*;
import java.sql.*;
import java.util.*;
import java.time.*;

public class AppMenu{
    private static Personne personneConnectee = null; // pour savoir qui est connecté
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
            int espacesARajouter = largeur - ligne.length() - 2; // -2 pour les 2 espaces de marge
            System.out.println("║ " + ligne + " ".repeat(Math.max(0, espacesARajouter)) + " ║");
        }

        // Afficher le cadre inférieur
        System.out.println("╚" + "═".repeat(largeur) + "╝");
    }

    public static String centrerTexte(String texte, int largeur) {
        int totalPadding = largeur - texte.length();
        int paddingGauche = totalPadding / 2;
        int paddingDroite = totalPadding - paddingGauche;
        return " ".repeat(Math.max(0, paddingGauche)) + texte + " ".repeat(Math.max(0, paddingDroite));
    }
    
    public static void main(String[] args) {
        Connection connection = ConnectionBD.getConnection();
        Scanner scanner = new Scanner(System.in);
        List<String> mainapp = List.of(
            "Administrateur",
            "Vendeur",
            "Client",
            "Quitter"
        );

        List<String> connect = List.of(
            "Se connecter",
            "S'inscrire",
            "Annuler"
        );

        List<String> client = List.of(
            "Accès Bibliothèque",
            "Recommandation",
            "Passer Commande",
            "Voir mes Commandes",
            "Profil",
            "Retour"
        );

        List<String> vendeur = List.of(
            "Ajouter livre ",
            "Supprimer livre",
            "Modifier Quantité Livre",
            "Vérifier Disponibilité",
            "Commander du stock",
            "Transférer un livre",
            "Profil",
            "Quitter"
        );

        List<String> admin = List.of(
            "Créer un compte vendeur",
            "Ajouter une nouvelle librairie",
            "Gérer les stocks globaux", //doit ouvrir un autre menu
            "Consulter les statistiques de vente",
            "Profil",
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
                do {
                afficherMenu("Qui êtes vous ?", mainapp);
                System.out.print("Votre choix : ");
                String input = scanner.nextLine();
                try {
                    choix = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("❌ Veuillez entrer un nombre valide.");
                    choix = 0; // force à redemander
                }
                } while (choix < 1 || choix > mainapp.size());

                int identif = choix; // on garde le choix pour la suite
                if (identif == 3){
                    afficherMenu("choisissez votre mode de connexion", connect);
                    int modeCo;
                    do {
                        System.out.print("Votre choix : ");
                        String inputMode = scanner.nextLine();
                        try {
                            modeCo = Integer.parseInt(inputMode);
                        } catch (NumberFormatException e) {
                            System.out.println("❌ Veuillez entrer un nombre valide.");
                            modeCo = 0;
                        }
                    } while (modeCo < 1 || modeCo > connect.size());
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
                        case 3 ->{
                            // on retourne au menu principal
                            System.out.println("Retour au menu principal");
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


                case 1 -> {
                    do {
                        afficherMenu("Accès Admin", admin);
                        System.out.print("Votre choix : ");

                        String input = scanner.nextLine();
                        try {
                            choix = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            System.out.println("❌ Veuillez entrer un nombre valide.");
                            choix = -1;
                        }
                        if (choix < 1 || choix > admin.size()) continue;

                        switch(choix){
                            case 1 -> creerVendeur();
                            case 2 -> ajouterLibrairie();
                            case 3 -> gererLesStocks();
                            case 4 -> consulterStat();
                            case 5 -> System.out.println(personneConnectee.toString());
                            case 6 -> {
                                System.out.println("👋 Au revoir " + personneConnectee.getPrenom()+ " !");
                                personneConnectee = null;
                                System.out.println("Deconnexion réussie.");
                            }
                            default -> System.out.println("❌ Option invalide.");
                        }
                    } while(choix != admin.size());
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
                                ajouterLivreMagasin();
                            }

                            //Supprimer livre
                            case 2 -> {
                                supprimerLivreMagasin();
                            }

                            //Modifier Quantité Livre
                            case 3-> {
                                modifierQte();
                            }
                            // Vérifier Disponibilité
                            case 4-> {
                                estDisponible();
                            }

                            // Commander du stock
                            case 5-> {
                                commanderStock();
                            }


                            //Transférer un livre
                            case 6-> {
                                transfererLivre();
                            }

                            //Profil
                            case 7 -> {
                                System.out.println(personneConnectee.toString());
                            }
                            //quitter 
                            case 8->{
                                System.out.println("👋 Au revoir " + personneConnectee.getPrenom()+ " !");
                                personneConnectee = null; // on déconnecte la personne
                                System.out.println("Deconnexion réussie.");
                                }
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

                            //"Profil"
                            case 5 -> {
                                System.out.println(personneConnectee.toString());
                            }
                            //"Retour"
                            case 6-> {
                                System.out.println("👋 Au revoir " + personneConnectee.getPrenom()+ " !");
                                personneConnectee = null; // on déconnecte la personne
                                System.out.println("Deconnexion réussie.");
                            }
                            default-> System.out.println("❌ Option invalide.");
                        }

                    } while(choix !=client.size());
                }
                case 4 -> System.out.println("Vous quittez l'application. 👋");
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
        if (mag == null) {
            // si le magasin est null, on quitte la fonction
            lstRep.add("Vous avez quitté la création du vendeur");
            afficherMenu("Création d'un vendeur ", lstRep);
            return;
        }
        
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
                leMag = null; // on quitte la fonction
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
        String idmag = magasinBD.genererId(); // on genere un id de magasin
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
        String affRet = "Retour au menu principal";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff1);
        lstRep.add(aff2);
        lstRep.add(aff3);
        lstRep.add(affRet);
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

            case 4 -> {
                lstRep = new ArrayList<>();
                lstRep.add(affRet);
                afficherMenu("Retour au menu principal", lstRep);
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
    Magasin mag = demanderMagasin("De quel magasin voulez vous les statistiques ?");
    if (mag == null) {
        // si le magasin est null, on quitte la fonction
        System.out.println("Vous avez quitté la consultation des statistiques");
        return;
    }
    else{
        Scanner scanner = new Scanner(System.in);
        String q1 = "Chiffre d'affaire total par année";
        String q2 = "Le livre le plus vendu";
        String q3 = "Le nombre de livre(s) vendu(s)";
        String q4 = "Le nombre de livre de la librairie";
        String quit = "Quitter";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(q1);
        lstRep.add(q2);
        lstRep.add(q3);
        lstRep.add(q4);
        lstRep.add(quit);
        afficherMenu("Consultation des statistiques du magasin "+mag.getNomMag(), lstRep);

        int rep = -1;
        while (true) {
            System.out.print("Veuillez entrer un nombre correspondant à votre choix : ");
            if (scanner.hasNextInt()) {
                rep = scanner.nextInt();
                break;
            } else {
                System.out.println("❌ Veuillez entrer un nombre valide.");
                scanner.next(); // Consomme l'entrée invalide
            }
        }

        switch(rep){
            case 1 -> {
                chiffreAffaire(mag);
            }
            case 2 -> {
                livreLePlusVendu(mag);
            }
            case 3 -> {
                nombreDeLivreVendu(mag);
            }
            case 4 -> {
                nbLivresMag(mag);
            }
            case 5 -> {
                System.out.println("Vous quittez la consultation des statistiques");
            }
            default -> {
                System.out.println("❌ Option invalide. Veuillez réessayer.");
            }
        }
    }
}

    public static void nbLivresMag(Magasin mag){
        // Cette fonction sert à afficher le nombre de livre d'un magasin
        MagasinBD magasinBD = new MagasinBD(ConnectionBD.getConnection());
        int nombreLivres = magasinBD.nombreDeLivre(mag);
        System.out.println("Le nombre de livre dans le magasin " + mag.getNomMag() + " est de : " + nombreLivres);
    }

    public static void nombreDeLivreVendu(Magasin mag){
        // Cette fonction sert à afficher le nombre de livre vendu d'un magasin
        MagasinBD magasinBD = new MagasinBD(ConnectionBD.getConnection());
        // on récupère l'année voulu 
        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuillez entrer l'année pour laquelle vous voulez le nombre de livre vendu : ");  
        int annee = scanner.nextInt();
        
        int nombreLivres = magasinBD.nombreDeLivreVendu(mag, annee);
        System.out.println("Le nombre de livre vendu dans le magasin " + mag.getNomMag() + " pour l'année " + annee + " est de : " + nombreLivres);
    }

    public static void livreLePlusVendu(Magasin mag){
        // Cette fonction sert à afficher le livre le plus vendu d'un magasin
        MagasinBD magasinBD = new MagasinBD(ConnectionBD.getConnection());
        // on récupère l'année voulu 
        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuillez entrer l'année pour laquelle vous voulez le livre le plus vendu : ");  
        int annee = scanner.nextInt();
        
        Livre livre = magasinBD.livreLePlusVendu(mag, annee);
        if (livre != null) {
            System.out.println("Le livre le plus vendu du magasin " + mag.getNomMag() + " est : " + livre.getTitre());
        } else {
            System.out.println("Aucun livre n'a été vendu dans le magasin " + mag.getNomMag());
        }
    }

    public static void chiffreAffaire(Magasin mag){
        // Cette fonction sert à afficher le chiffre d'affaire d'un magasin
        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuillez entrer l'année pour laquelle vous voulez le chiffre d'affaire : ");
        int annee = scanner.nextInt();
        MagasinBD magasinBD = new MagasinBD(ConnectionBD.getConnection());
        double chiffreAffaire = magasinBD.chiffreAffaire(mag, annee);
        System.out.println("Le chiffre d'affaire du magasin " + mag.getNomMag() + " pour l'année " + annee + " est de : " + chiffreAffaire + " euros");
    }

    // Les fonctions de vendeur ----------------------------------------------------------------------------------------------------------
    
    public static void ajouterLivreMagasin(){
        // Cette fonction sert à ajouter un livre à la BD
        List<String> lstRep = new ArrayList<>();
        Livre livre = demanderLivreAll();
        if (livre == null) {
            // si le livre est null, on quitte la fonction
            lstRep.add("Vous avez quitté l'ajout d'un livre");
            afficherMenu("Ajout d'un livre ", lstRep);
            return;
        }
        Connection connexion = ConnectionBD.getConnection();
        LivreBD livreBD = new LivreBD(connexion);
        Vendeur vendeur = (Vendeur) personneConnectee; // on cast la personne connectée en vendeur
        livreBD.ajouterLivreMagasin(vendeur, livre);
        lstRep.add("Le livre " + livre.getTitre() + " a été ajouté à le magasin "+ vendeur.getMagasin().getNomMag() + " .");
        afficherMenu("Ajout d'un livre ", lstRep);
    }

    public static void supprimerLivreMagasin(){
        // Cette fonction sert à supprimer un livre du magasin
        Scanner scanner = new Scanner(System.in);
        Connection connexion = ConnectionBD.getConnection();
        LivreBD livreBD = new LivreBD(connexion);
        Vendeur vendeur = (Vendeur) personneConnectee; // on cast la personne connectée en vendeur
        Livre livre = demanderLivreExistant("Quel livre voulez vous supprimer ?", vendeur.getMagasin());
        if (livre == null) {
            // si le livre est null, on quitte la fonction
            List<String> lstRep = new ArrayList<>();
            lstRep.add("Vous avez quitté la suppression du livre");
            afficherMenu("Suppression d'un livre ", lstRep);
            return;
        }
        // on le supprime du magasin
        livreBD.supprimerLivreMagasin(vendeur, livre);
    }
    
    /**
     * Cette fonction demande les informations d'un livre à l'utilisateur.
     * Elle retourne un objet Livre contenant les informations saisies.
     * 
     * @return Livre - un objet Livre avec les informations saisies par l'utilisateur.
     */
    public static Livre demanderLivre(){
        // Cette fonction sert à demander les informations d'un livre
        Scanner scanner = new Scanner(System.in);
        Connection connexion = ConnectionBD.getConnection();
        //String aff1 = "Veuillez entrer l'ISBN du livre"; //complètement con tu connais l'isbn du livre toi ?
        String aff2 = "Veuillez entrer le titre du livre";
        String aff6 = "Veuillez entrer le nombre de pages du livre";
        String aff7 = "Veuillez entrer le prix du livre";
        String aff8 = "Veuillez entrer la date de publication du livre";
        String aff9 = "Veuillez entrer le nom de l'éditeur du livre";

        List<String> lstRep = new ArrayList<>();
        //lstRep.add(aff1);
        afficherMenu("Ajout d'un livre ", lstRep);
        String isbn = scanner.nextLine();

        lstRep = new ArrayList<>();
        lstRep.add(aff2);
        afficherMenu("Ajout d'un livre ", lstRep);
        String titre = scanner.nextLine();
        // on demande le nombre d'auteurs du livre
        String aff3 = "Combien d'auteurs pour ce livre ?";
        lstRep = new ArrayList<>();
        lstRep.add(aff3);
        afficherMenu("Ajout d'un livre ", lstRep);
        int nbAuteurs = scanner.nextInt();
        List<Auteur> auteurs = new ArrayList<>();
        Auteur auteur = null;
        for (int i = 0; i < nbAuteurs; i++) {
            auteur = demanderAuteur("De quel auteur est ce livre ?");
            auteurs.add(auteur);
        }

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
        
        EditeurBD editeurBD = new EditeurBD(connexion);
        // on vérifie si l'éditeur existe déjà
        List<Editeur> editeurs = new ArrayList<>();
        Editeur editeur = null;
        // on demande le nombre d'éditeurs du livre
        String aff4 = "Combien d'éditeurs pour ce livre ?";
        lstRep = new ArrayList<>();
        lstRep.add(aff4);
        afficherMenu("Ajout d'un livre ", lstRep);
        int nbEditeurs = scanner.nextInt();
        for (int i = 0; i < nbEditeurs; i++) {
            editeur = demanderEditeur("De quel éditeur est ce livre ?");
            editeurs.add(editeur);
        }

        Livre livre = new Livre(isbn, titre, auteurs, dateParution, nbPages, prix, editeurs);
        return livre;
    }

    public static Livre demanderLivreAll(){
        Connection connexion = ConnectionBD.getConnection();
        Scanner scanner = new Scanner(System.in);
        //on va prendre la liste des livres existants dans la base de données
        LivreBD livreBD = new LivreBD(connexion);
        List<Livre> lstLivres = new ArrayList<>();
        lstLivres = livreBD.getTousLesLivresBase();
        
        List<String> lstRep = new ArrayList<>();
        Livre livre = null;
        boolean correct = false;
        while (! correct){ 
            lstRep = new ArrayList<>();
            for (Livre liv : lstLivres){
                lstRep.add(liv.getTitre());
            }
            lstRep.add("QUITTER");
            afficherMenu("Quel livre voulez vous modifier ?", lstRep);

            int choix = scanner.nextInt();
            if (choix == lstRep.size()){
                // on retourne au menu principal
                // on quitte la fonction
                lstRep = new ArrayList<>();
                lstRep.add("Vous quittez " );
                afficherMenu("Quel livre voulez vous modifier ?",lstRep);
                livre = null; // on quitte la fonction
                correct = true;
            }
            else{
                if (choix-1 < lstRep.size()){
                    livre = lstLivres.get(choix-1);
                    lstRep = new ArrayList<>();
                    lstRep.add("Vous avez choisi le livre : "+ livre.getTitre());
                    afficherMenu("Quel livre voulez vous modifier ?",lstRep);
                    correct = true;
                }
                else {
                    lstRep = new ArrayList<>();
                    lstRep.add("Veuillez choisir un livre correct ");
                    afficherMenu("Quel livre voulez vous modifier ?",lstRep);
                }
            }
        }
        return livre;
    }

    public static Editeur demanderEditeur(String question){
        // Cette fonction sert à demander les informations d'un éditeur
        List<Editeur> lstEditeurs = new ArrayList<>();
        Connection connexion = ConnectionBD.getConnection();
        EditeurBD editeurBD = new EditeurBD(connexion);
        lstEditeurs = editeurBD.getListeEditeurs();
        Scanner scanner = new Scanner(System.in);
        Editeur editeur = null;
        boolean correct = false;
        
        while (! correct){ 
            List<String> lstRep = new ArrayList<>();
            for (Editeur ed : lstEditeurs){
                lstRep.add(ed.getNom());
            }
            lstRep.add("QUITTER");
            afficherMenu(question, lstRep);

            int choix = scanner.nextInt();
            if (choix == lstRep.size()){
                lstRep = new ArrayList<>();
                lstRep.add("Vous quittez " );
                afficherMenu(question,lstRep);
                correct = true;
                editeur = null; // on quitte la fonction
            }
            else{
                if (choix-1 < lstRep.size()){
                    editeur = lstEditeurs.get(choix-1);
                    lstRep = new ArrayList<>();
                    lstRep.add("Vous avez choisi l'éditeur : "+ editeur.getNom() );
                    afficherMenu(question,lstRep);
                    correct = true;
                }
                else {
                    lstRep = new ArrayList<>();
                    lstRep.add("Veuillez choisir un(e) éditeur(trice) correct ");
                    afficherMenu(question,lstRep);
                }
            }
        }
        return editeur;
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
                auteur = null; // on quitte la fonction
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
        // Cette fonction sert à modifier la qte d'un livre dans un magasin
        List<String> lstRep = new ArrayList<>();
        //Magasin mag = demanderMagasin("Dans quel magasin voulez vous modifier la quantité ?");
        Vendeur vendeur = (Vendeur) personneConnectee; // on cast la personne connectée en vendeur
        Magasin mag = vendeur.getMagasin(); // on récupère le magasin du vendeur
        if (mag == null) {
            // si le magasin est null, on quitte la fonction
            lstRep.add("Vous avez quitté la modification de la quantité");
            afficherMenu("Modification de la quantité ", lstRep);
            return;
        }
        Livre livre = demanderLivreExistant("Quel livre voulez vous modifier ?", mag);
        if (livre == null) {
            // si le livre est null, on quitte la fonction
            lstRep.add("Vous avez quitté la modification de la quantité");
            afficherMenu("Modification de la quantité ", lstRep);
            return;
        }
        lstRep.add("Quelle est la nouvelle quantité pour le livre " + livre.getTitre() + " ?");
        afficherMenu("De quel livre voulez vous modifier la quantité ? ", lstRep);
        Scanner scanner = new Scanner(System.in);
        int nouvelleQte = scanner.nextInt();
        Connection connexion = ConnectionBD.getConnection();

    }

    public static Livre demanderLivreExistant(String question, Magasin mag){
        // on récupère la liste des livres existants
        Connection connexion = ConnectionBD.getConnection();
        LivreBD livreBD = new LivreBD(connexion);
        List<Livre> lstLivres = new ArrayList<>();
        lstLivres = livreBD.getTousLesLivres(mag);
        Scanner scanner = new Scanner(System.in);
        Livre livre = null;
        boolean correct = false;
        while (! correct){ 
            List<String> lstRep = new ArrayList<>();
            for (Livre liv : lstLivres){
                lstRep.add(liv.getTitre());
            }
            lstRep.add("QUITTER");
            afficherMenu(question, lstRep);

            int choix = scanner.nextInt();
            if (choix == lstRep.size()){
                // on retourne au menu principal
                // on quitte la fonction
                lstRep = new ArrayList<>();
                lstRep.add("Vous quittez " );
                afficherMenu(question,lstRep);
                livre = null; // on quitte la fonction
                correct = true;
            }
            else{
                if (choix-1 < lstRep.size()){
                    livre = lstLivres.get(choix-1);
                    lstRep = new ArrayList<>();
                    lstRep.add("Vous avez choisi le livre : "+ livre.getTitre());
                    afficherMenu(question,lstRep);
                    correct = true;
                }
                else {
                    lstRep = new ArrayList<>();
                    lstRep.add("Veuillez choisir un livre correct ");
                    afficherMenu(question,lstRep);
                }
            }
        }
        return livre;
    }

    public static void estDisponible(){
        Scanner scanner = new Scanner(System.in);
        Connection connexion = ConnectionBD.getConnection();
        // Cette fonction sert à savoir si un livre est disponible dans un magasin
        Magasin mag = demanderMagasin("Dans quel magasin voulez vous vérifier la disponibilité ?");
        if (mag == null) {
            // si le magasin est null, on quitte la fonction
            List<String> lstRep = new ArrayList<>();
            lstRep.add("Vous avez quitté la vérification de la disponibilité");
            afficherMenu("Vérification de la disponibilité ", lstRep);
            return;
        }
        String aff = "Veuillez entrer un livre";
        List<String> lstRep = new ArrayList<>();
        lstRep.add(aff);
        afficherMenu("Disponibilité d'un livre ", lstRep);
        String titreLivre = scanner.nextLine();
        LivreBD livreBD = new LivreBD(connexion);
        Livre livre = livreBD.getLivreParTitre(titreLivre);
        if (livre != null){
            System.out.println("Le livre " + livre.getTitre() + " est disponible dans le magasin " + mag.getNomMag() + ".");
        }
        else {
            System.out.println("Le livre " + titreLivre + " n'est pas disponible dans le magasin " + mag.getNomMag() + ".");
        }

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
        
        if (mag == null) {
            // si le magasin est null, on quitte la fonction
            List<String> lstRep = new ArrayList<>();
            lstRep.add("Vous avez quitté l'accès à la bibliothèque");
            afficherMenu("Accès à la bibliothèque ", lstRep);
            return;
        }
        List<Livre> lstLivres = new ArrayList<>();
        Connection connexion = ConnectionBD.getConnection();
        LivreBD livreBD = new LivreBD(connexion);
        List<String> lstRep = new ArrayList<>();
        lstLivres = livreBD.getTousLesLivres(mag);
        for (Livre livre : lstLivres) {
            lstRep.add(livre.getTitre());
        }
        if (lstRep.isEmpty()) {
            lstRep.add("Aucun livre disponible dans ce magasin.");
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
        Connection connexion = ConnectionBD.getConnection();
        Scanner scanner = new Scanner(System.in);
        // Cette fonction sert à passer une commande
        // on demande le magasin où passer la commande
        Magasin mag = demanderMagasin("Dans quel magasin voulez vous passer la commande ?");
        if (mag == null) {
            // si le magasin est null, on quitte la fonction
            List<String> lstRep = new ArrayList<>();
            lstRep.add("Vous avez quitté la commande");
            afficherMenu("Passer une commande ", lstRep);
            return;
        }else{
            System.out.println(mag.getIdmag());
            CommandeBD commandeBD = new CommandeBD(connexion);
            List<String> lstRep = new ArrayList<>();
            //  Magasin magasin, Client client
            //numero de commande 
            int numCommande = commandeBD.genererId(); // on génère un numéro de commande

            // la date 
            LocalDate date = LocalDate.now(); // on récupère la date du jour

            // en ligne ?
            boolean enLigne = true; // on suppose que la commande est en ligne

            // livraison ?
            // on demande si la commande est livrée ou pas
            Livraison livraison = Livraison.MAGASIN;
            lstRep = new ArrayList<>();
            lstRep.add("La commande est-elle livrée ? (oui/non)");
            afficherMenu("Passer une commande ", lstRep);
            String reponse = scanner.next();
            if (reponse.equalsIgnoreCase("oui") || reponse.equalsIgnoreCase("o")) {
                livraison = Livraison.DOMICILE; // on appelle la fonction domicile pour créer une livraison
            } else if (reponse.equalsIgnoreCase("non") || reponse.equalsIgnoreCase("n")) {
                livraison = Livraison.MAGASIN; // pas de livraison
            }

            // on a deja le magasin

            // on a le client
            Client client = (Client) personneConnectee; // on cast la personne connectée en client

            // on crée la commande
            Commande commande = new Commande(numCommande, date, enLigne, livraison, mag, client);

        // ------------------------------------------------------------------
        boolean commandeFini = false; // on initialise la commande à false
        List<DetailCommande> lstDetails = new ArrayList<>(); // on initialise la liste des détails de commande
        DetailCommandeBD detailCommandeBD = new DetailCommandeBD(connexion);
        while (!commandeFini) {
            // on demande le livre à commander
            lstRep = new ArrayList<>();
            lstRep.add("Quel livre voulez vous commander ?");
            afficherMenu("Passer une commande ", lstRep);
            
            // on demande le livre à commander
            Livre livre = demanderLivreExistant("Quel livre voulez vous commander ?", mag);
            if (livre == null) {
                // si le livre est null, on quitte la fonction
                lstRep = new ArrayList<>();
                lstRep.add("Vous avez quitté la commande");
                afficherMenu("Passer une commande ", lstRep);
                return;
            }
            
            // on demande la quantité à commander
            lstRep = new ArrayList<>();
            lstRep.add("Quelle est la quantité à commander pour le livre " + livre.getTitre() + " ?");
            afficherMenu("Passer une commande ", lstRep);
            int quantite = scanner.nextInt();
            
            // on ajoute le détail de commande
            //int numDetailCommande, Livre livre, int qte, int numCo
            int numIdDetCo = detailCommandeBD.genererId(); // on génère un numéro de détail de commande
            DetailCommande detailCommande = new DetailCommande(numIdDetCo,livre, quantite, numCommande);
            lstDetails.add(detailCommande); // on ajoute le détail de commande à la liste
            
            
            // on demande si l'utilisateur veut continuer ou pas
            lstRep = new ArrayList<>();
            lstRep.add("Voulez-vous ajouter un autre livre ? (oui/non)");
            afficherMenu("Passer une commande ", lstRep);
            String reponse2 = scanner.next();
            
            if (reponse2.equalsIgnoreCase("non") || reponse2.equalsIgnoreCase("n")) {
                commandeFini = true; // on quitte la boucle
            }
            }
            // on ajoute les détails de commande à la commande   
            for (DetailCommande detail : lstDetails) {
                commande.ajouterDetailCommande(detail);
            }
            // on ajoute la commande à la base de données
            commandeBD.insererCommande(commande);
            System.out.println("✅ Votre commande a été passée avec succès !");
            System.out.println("Voici le récapitulatif de votre commande :");
            System.out.println(commande.editerFacture());
        }
        
        
            
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
                    // demande a la bd getAdmin ident et mdp
                    Administrateur admin = adminBD.getAdministrateur(ident, mdp);
                    personneConnectee = admin; // on récupère l'admin connecté
                    System.out.println("✅ Bienvenue " + admin.getPrenom() + " " + admin.getNom() + " !");
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
                    // demande a la bd getVendeur ident et mdp
                    Vendeur vendeur = vendeurBD.getVendeur(ident, mdp);
                    personneConnectee = vendeur; // on récupère le vendeur connecté
                    System.out.println("Bienvenue " + vendeur.getPrenom() + " " + vendeur.getNom());
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
                    // demande a la bd getClient ident et mdp
                    Client client = clientBD.getClient(ident, mdp);
                    personneConnectee = client; // on récupère le client connecté
                    System.out.println("✅ Bienvenue " + client.getPrenom() + " " + client.getNom() + " !");
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