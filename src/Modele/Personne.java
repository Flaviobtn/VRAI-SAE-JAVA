package Modele;
import bd.*;
public abstract class Personne{
    protected String nom;
    protected String prenom;

    public Personne(String nom, String prenom){
        this.nom = nom;
        this.prenom = prenom;

    }

    public Personne(String nom){
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public String toString() {
        return "Vous êtes : " + this.prenom + " " + this.nom;
    }
}