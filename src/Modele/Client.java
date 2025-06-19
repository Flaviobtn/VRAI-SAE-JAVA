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
    
    public void voirMesCommandes(){
        for(Commande commande : this.getCommandes()){
            System.out.println(commande.editerFacture());
        }
    }

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
            for (DetailCommande detCom : comm.getCommandeFinale()){
                if(recommandations.size()<10){
                    ClientBD clientBD = new ClientBD(ConnectionBD.getConnection());
                    temp = clientBD.getRecoLivre(comm.getNumCommande(),detCom.getLivre().getIsbn());
                    for(Livre livre : temp){
                        if(banlivres.contains(livre)){
                            banlivres.remove(livre);
                        }
                    }
                    recommandations.addAll(temp);
                }
            }
        }
        if(recommandations.size()>=6){
            int cpt = 0;
            List<Livre> lst = new ArrayList<>();
            for(Livre livre : recommandations){
                if(cpt<6){
                    lst.add(livre);
                    cpt++;
                }
            }
            return lst;
        }
        return new ArrayList<>(recommandations);
    }

    public Map<Integer, List<Livre>> recotoMap(){
        Map<Integer,List<Livre>> map = new HashMap<>();
        map.put(1, onVousRecommande());
        return map;
    }


    @Override
    public String toString() {
        return super.toString() + ", connecté en tant que Client";
    }
}