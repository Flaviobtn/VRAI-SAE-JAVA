package Modele;
import bd.*;
import java.util.List;

public class Vendeur extends Personne {

    private String idVendeur;
    private Magasin magasin;
    private String motdepasse;

    public Vendeur(String idVendeur, String nom, String prenom, String motdepasse,Magasin magasin){
        super(nom, prenom);
        this.idVendeur = idVendeur;
        this.magasin=magasin;
        this.motdepasse = motdepasse;
    }

    public String idVendeur(){
        return this.idVendeur;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public String getIdVendeur(){
        return this.idVendeur;
    }

    /*
    public void ajouterLivre(String isbn,String titre, Auteur auteur, int dateParution,int nbDePages, double prix, List<Classification> themes, Editeur editeur){
    Livre livre = new Livre(isbn,titre, auteur, dateParution, nbDePages, prix, editeur);
        for(Classification theme : themes){
        livre.ajouterThemes(theme);}
        for(Classification theme : themes){
        theme.ajouterLivreGenre(livre);}
        this.magasin.getStock().add(livre);
    }
    */

    public void majStock(Livre livre, int qte){
        if(qte>0){
            for(int i=0;i<qte;i++){
                //this.ajouterLivre(livre.getIsbn() ,livre.getTitre(), livre.getAuteur(),livre.getDateparution(), livre.getNbDePages(), livre.getPrix(), livre.getThemes(),livre.getEditeur());
            }}
        else{
            for(int i=0;i>qte;i--){
                this.magasin.getStock().remove(livre);}
        }
    } 

    public boolean estDispo(Livre livre, Magasin magasin){
        if(magasin.getStock().contains(livre)){
            return true;
        }
        return false;
    }

    public boolean commandeClient(Livre livre, Client client, int qte,Commande commande,int nbPlusGrandDetailCommande){
        int nbtrue =0;
        for(int i = 0; i<qte; i++){
            if(this.estDispo(livre, this.magasin)){
                client.commanderLivre(livre, qte, this.magasin,commande, nbPlusGrandDetailCommande );
            nbtrue+=1;}
            }
        if(nbtrue == qte){
        return true;}
        return false;
    }

    public void transfertLivre(Livre livre, Magasin autreMagasin){
        autreMagasin.getStock().remove(livre);
        this.majStock(livre, 1);
    }

    @Override
    public String toString() {
        return super.toString() + ", connecté en tant que Vendeur";
    }
}