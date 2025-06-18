package Modele;
import bd.*;
import java.util.*;

public class Client extends Personne {
    private String identifiant;
    private int numeroClient;
    private String adresse;
    private int codePostal;
    private String ville;
    private String motdepasse;
    private List<Commande> commandes;

    public Client(int numeroClient, String nom, String prenom, String identifiant, String adresse, int codePostal,String motdepasse, String ville) {
        super(nom, prenom);
        this.identifiant = identifiant;
        this.motdepasse = motdepasse;
        this.numeroClient = numeroClient;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
        this.commandes= new ArrayList<>();
    }
    
    public String getIdentifiant() {
        return identifiant;
    }
    public int getNumeroClient() {
        return numeroClient;
    }
    public String getAdresse() {
        return adresse;
    }
    public int getCodePostal() {
        return codePostal;
    }
    public String getVille() {
        return ville;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public String getMotDePasse() {
        return motdepasse;
    }
    public List<Commande> getCommandes() {
        return commandes;
    }

    public void ajouterCommande(Commande commande){
        this.commandes.add(commande);    
    }
    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }
    public void setVille(String ville) {
        this.ville = ville;
    }
    public boolean aCommande(){
        return this.commandes.size()>0;
    }

    public List<Livre> tousLesLivresClient(){
        List<Livre> res = new ArrayList<>();
        for(Commande commande : this.commandes){
            for(Livre livre :commande.tousLesLivres()){
                if(!(res.contains(livre)))
            res.add(livre);
            }
        }
        return res;
    }

    public void commanderLivre(Livre livre, int qte, Magasin magasin, Commande commande, int nbPlusGrandDetailCommande){
        DetailCommande detailCommande = new DetailCommande(nbPlusGrandDetailCommande+1, livre, qte, commande.getNumCommande());
        commande.ajouterDetailCommande(detailCommande);
        magasin.supprimerLivre(livre);
    }

    /*
    public List<Livre> onVousRecommande(Client client, List<Commande> toutesLesCommandes){
        List<Livre> recommandations= new ArrayList<>();
        System.out.println(toutesLesCommandes);

        Iterator<Commande> it = toutesLesCommandes.iterator();
        while(it.hasNext()){
            Commande commande = it.next();
            if(!(client.commandes.contains(commande))){//si la commande qu'on regarde n'est pas celle du client
                for(Livre livre : client.tousLesLivresClient()){// on parcourt chaque livre que le client à commandé
                if(commande.tousLesLivres().contains(livre)){// si dans la commande qu'on regarde il y a le livre que le client a commandé 
                    // (il faut donc lui recommander les livres de cette commande)
                    for(Livre livreCommande : commande.tousLesLivres() ){// pour chaque livre de la commande
                        if(!(client.tousLesLivresClient().contains(livreCommande))&& !(recommandations.contains(livreCommande))){
                            //si le client n'a jamais commandé ce livre et qu'il n'est pas dans les recommandations
                            recommandations.add(livreCommande);// l'ajouter aux recommandations
                            }
                        }
                    }
                }
            }
        }
        return recommandations;
        
    }
    */

   public List<Livre> onVousRecommande() {
        Set<Livre> recommandations = new HashSet<>();
        Set<Livre> temp = new HashSet<>();
        System.out.println(this.getCommandes());
        Set<Livre> banlivres = new HashSet<>();
        for (Commande comm : this.getCommandes()){
            for(DetailCommande det : comm.getCommandeFinale()){
                banlivres.add(det.getLivre());
            }
        }
        for (Commande comm : this.getCommandes()) {
            System.out.println(comm.getCommandeFinale());
            for (DetailCommande detCom : comm.getCommandeFinale()){
                if(recommandations.size()<10){
                    System.out.println("Dans le if");
                    ClientBD clientBD = new ClientBD(ConnectionBD.getConnection());
                    temp = clientBD.getRecoLivre(comm.getNumCommande(),detCom.getLivre().getIsbn());
                    System.out.println(temp);
                    for(Livre livre : temp){
                        if(banlivres.contains(livre)){
                            banlivres.remove(livre);
                        }
                    }
                        recommandations.addAll(temp);
                }
            }
        }
        return new ArrayList<>(recommandations);
    }



    @Override
    public String toString() {
        return super.toString() + ", connecté en tant que Client";
    }
}