import java.util.List;
import java.util.ArrayList;

public class Auteur extends Personne{
    public String id;
    public int anneenais;
    public int anneedeces;
    private List<Livre> livreEcrits;

    public Auteur(String id, String nom, String prenom){
        super(nom,prenom);
        this.livreEcrits=new ArrayList<>();
    }
    
    public Auteur(String id, String nomprenom, int anneenais, int anneedeces){
        super(nomprenom);
        this.anneedeces = anneedeces;
        this.anneenais = anneenais;
        this.livreEcrits=new ArrayList<>();
    }

    /*public Auteur(String nom){
        super(nom);
        this.livreEcrits=new ArrayList<>();
    }*/

    public String getIdAuteur(){
        return this.id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getAnneeNais(){
        return this.anneenais;
    }

    public int getAnneeDeces(){
        return this.anneedeces;
    }

    public List<Livre> getLivreEcrits() {
        return livreEcrits;
    }

    public void ajouterLivreEcrit(Livre livre){
        this.livreEcrits.add(livre);
    }

    public String toString(){
        return this.nom;
    }

}