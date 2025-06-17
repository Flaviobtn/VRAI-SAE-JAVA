package Modele;
import bd.*;

public class Administrateur extends Personne{
    private String id;
    private String motDePasse;

    public Administrateur(String id, String nom, String Prenom, String motdepasse){
        super(nom, Prenom);
        this.id = id;
        this.motDePasse = motdepasse;
    }

    public String getId() {
        return id;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    @Override
    public String toString() {
        return super.toString() + ", connecté en tant qu'Administrateur";
    }
}
