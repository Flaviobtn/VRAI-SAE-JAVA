package Modele;
import bd.*;
import java.util.List;
import java.util.ArrayList;

public class Editeur {
    private int id;
    private String nom;
    private List<Livre> livresPublies;
    
    public Editeur(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.livresPublies=new ArrayList<>();
    }
    public String getNom() {
        return nom;
    }
    public List<Livre> getLivresPublies() {
        return livresPublies;
    }
    public int getId() {
        return id;
    }

    public void ajouterLivrePublie(Livre livre){
        this.livresPublies.add(livre);
    }
    
}
