package Modele;
import bd.*;
public class DetailCommande{
    private int numDetailCommande;
    private Livre livre;
    private int qte;
    private int numCo;

    
    public int getNumDetailCommande() {
        return numDetailCommande;
    }
    public Livre getLivre() {
        return livre;
    }
    public int getQte() {
        return qte;
    }


    public DetailCommande(int numDetailCommande, Livre livre, int qte, int numCo) {
        this.numDetailCommande = numDetailCommande;
        this.livre = livre;
        this.qte = qte;
        this.numCo = numCo;
    }

    public double getPrixLivres(){
        return this.livre.getPrix()*qte;
    }
    
    public int getNumCo() {
        return numCo;
    }

    @Override
    public String toString(){
        return "Titre du livre: "+ this.getLivre().getTitre()+"\n" +
        "Quantité: "+this.getQte()+"\n"+
        "Prix Unitaire: "+this.livre.getPrix()+" euros\n";
    }
}