package Modele;
import bd.*;
import java.util.*;
import java.time.*;

public class Commande{
    private int numCommande;
    private LocalDate datecomm;
    private List<DetailCommande> commandeFinale;
    private boolean enligne;
    private Livraison livraison;
    private Client client;
    private Magasin magasin;
    private boolean fini;


    //si demande de livraison en magasin
    public Commande(int numCommande, LocalDate datecomm, boolean enligne, Livraison livraison, Magasin magasin, Client client, boolean fini){
        this.numCommande = numCommande;
        this.datecomm = datecomm;
        this.commandeFinale = new ArrayList<>();
        this.enligne=enligne;
        this.livraison=livraison;
        this.client = client;
        this.magasin = magasin;
        this.fini = fini;
    }


    public Client getClient() {
        return this.client;
    }


    public Magasin getMagasin() {
        return this.magasin;
    }


    public int getNumCommande() {
        return this.numCommande;
    }
    
    public LocalDate getDatecomm() {
        return this.datecomm;
    }


    public List<DetailCommande> getCommandeFinale() {
        return this.commandeFinale;
    }
    

    public void ajouterDetailCommande(DetailCommande dc){
        this.commandeFinale.add(dc);
    }

    public boolean getEnligne() {
        return this. enligne;
    }
    public Livraison getLivraison() {
        return this.livraison;
    }

    public List<Livre> tousLesLivres(){
        List<Livre> lesLivres = new ArrayList<>();
        for(DetailCommande dc : this.commandeFinale){
            lesLivres.add(dc.getLivre());
        }
        return lesLivres;
    }
    
    public double prixTotal(){
        double prixTotal =0;
        for(DetailCommande dc : this.commandeFinale){
            prixTotal+= dc.getLivre().getPrix()*dc.getQte();
        }
        return prixTotal;
    }

    public boolean estFinie(){
        return this.fini;
    }

    public String editerFacture(){
        String facture = "";
        facture += "Commande n°" + this.numCommande + "\n";
        facture += "\n";
        facture += "Numéro de client : " + this.client.getNumeroClient() + "\n";
        facture += "Client : " + this.client.getNom() + " " + this.client.getPrenom() + "\n" + "\n";
        if (this.enligne){
            facture += "Commande en ligne sur le magasin " + this.magasin.getNomMag() + "\n";
        } else {
            facture += "Commande en magasin au magasin " + this.magasin.getNomMag() + "\n";
        }
        if (this.livraison == Livraison.DOMICILE){
            facture += "Livraison à domicile\n";
        } else {
            facture += "Retrait en magasin\n";
        }
        facture += "Commande en ligne: " + (this.enligne ? "Oui" : "Non") + "\n" + "\n";
        facture += "Détails de la commande:\n";

        for(DetailCommande dc : this.commandeFinale){
            facture += dc.toString() + "\n";
        }

        facture += "Prix total: " + this.prixTotal() + " euros\n";

        return facture;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + numCommande;
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Commande other = (Commande) obj;
        if (numCommande != other.numCommande)
            return false;
        return true;
    }

    

}