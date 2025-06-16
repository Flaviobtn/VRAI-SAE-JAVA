package app;
import Modele.*;
import java.util.*;

/*public class Executable {
    public static void main(String[] args) {
        System.out.println("------------");
        //on créer des auteurs
        Auteur auteur1 = new Auteur("Hugo", "Victor");
        Auteur auteur2 = new Auteur("Dumas", "Alexandre");
        System.out.println("Il y a " + auteur1.getNom() + " " + auteur1.getPrenom() + " et " + auteur2.getNom() + " " + auteur2.getPrenom());
        System.out.println("------------");
        //on créer des éditeurs
        Editeur editeur1 = new Editeur("Gallimard");
        Editeur editeur2 = new Editeur("Folio");
        System.out.println("Les éditeurs sont : " + editeur1.getNom() + " et " + editeur2.getNom());
        System.out.println("------------");
        //on créer des livres
        Livre livre1 = new Livre("Les Misérables", auteur1, 300, 12.99, editeur1);
        Livre livre2 = new Livre("Les Trois Mousquetaires", auteur2, 1844, 10.99, editeur2);
        System.out.println("Les livres sont : " + livre1.getTitre() + " et " + livre2.getTitre());
        System.out.println("Le livre " + livre1.getTitre() + " a été écrit par " + livre1.getAuteur().getNom() + " " + livre1.getAuteur().getPrenom() + " et publié par " + livre1.getEditeur().getNom());
        System.out.println("Le livre " + livre2.getTitre() + " a été écrit par " + livre2.getAuteur().getNom() + " " + livre2.getAuteur().getPrenom() + " et publié par " + livre2.getEditeur().getNom());
        System.out.println("------------");
        //on créer un magasin
        Magasin magasin = new Magasin("Librairie de Paris", "Paris", "l'adresse la");
        System.out.println("Le magasin est : " + magasin.getNomMag() + " et il est situé à " + magasin.getVilleMag());
        System.out.println("------------");
        //on ajoute les livres au magasin
        magasin.ajouterLivre(livre1);
        magasin.ajouterLivre(livre2);
        //on affiche les livres du magasin
        System.out.println("Livres disponibles dans le magasin :" + magasin.getNomMag()); 
        for (Livre livre : magasin.getStock()) {
            System.out.println(livre.getTitre() + " de " + livre.getAuteur().getNom() + " " + livre.getAuteur().getPrenom());
        }
        System.out.println("------------");
        
        // on crée des clients
        Client client1 = new Client("Dupont", "Jean", 1, "222 rue de Bourgogne", 75000, "Paris");
        Client client2 = new Client("Martin", "Marie", 2, "333 rue de la République", 75001, "Paris");
        Client client3 = new Client("Durand", "Pierre", 3, "444 rue de la Liberté", 75002, "Paris");
        System.out.println("Les clients sont : " + client1.getNom() + " " + client1.getPrenom() + ", " + client2.getNom() + " " + client2.getPrenom() + " et " + client3.getNom() + " " + client3.getPrenom());
        System.out.println(client1.getNom() + " habite au " + client1.getAdresse() + " " + client1.getCodePostal() + " " + client1.getVille());
        System.out.println(client2.getNom() + " habite au " + client2.getAdresse() + " " + client2.getCodePostal() + " " + client2.getVille());
        System.out.println(client3.getNom() + " habite au " + client3.getAdresse() + " " + client3.getCodePostal() + " " + client3.getVille());
        System.out.println("------------");
        // on crée un vendeur
        Vendeur vendeur = new Vendeur("Leroy", "Paul", magasin);
        System.out.println("Le vendeur est : " + vendeur.getNom() + " " + vendeur.getPrenom() + " et il travaille dans le magasin " + vendeur.getMagasin().getNomMag());
        System.out.println("------------"); 
        // on crée des commandes
        
        Commande commande1 = new Commande(1, true, Livraison.DOMICILE, magasin,client1);
        Commande commande2 = new Commande(2, true, Livraison.MAGASIN, magasin,client2);
        Commande commande3 = new Commande(3,false, Livraison.DOMICILE, magasin,client3);

        List<Commande> commandes = new ArrayList<>();
        commandes.add(commande1);
        commandes.add(commande2);
        commandes.add(commande3);

        System.out.println("Les commandes sont : ");
        for (Commande commande : commandes) {
            String enLigne = commande.getEnligne() ? "en ligne" : "en magasin";
            System.out.println("Commande n°" + commande.getNumCommande() + " : " + enLigne + " | sera livrée au " + commande.getReception());
        }
        System.out.println("------------");
        
        //Detail commande
        DetailCommande detCom1 = new DetailCommande(0, livre2, 2, 1);
        DetailCommande detCom2 = new DetailCommande(1, livre1, 5, 1);
        commande1.ajouterDetailCommande(detCom1);
        commande1.ajouterDetailCommande(detCom2);
        
        System.out.println("Dans la commande, il y a :");
        for (DetailCommande d : commande1.getCommandeFinale()){
            System.out.println("la commande " + d.getNumDetailCommande() + " qui contient " + d.getQte() + " fois le livres : " + d.getLivre().getTitre() + ". Pour un total de " + d.prixLivres() + "€");
        }
        System.out.println("Le prix de la commande total est : " + commande1.prixTotal() + "€");
        System.out.println("---------");

        String facture = commande1.editerFacture();
        System.out.println(facture);
        System.out.println("---------");
        // Classification
        
    }
}*/