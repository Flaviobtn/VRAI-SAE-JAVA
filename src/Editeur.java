import java.util.List;
import java.util.ArrayList;

public class Editeur {
    private String nom;
    private List<Livre> livresPublies;
    
    public Editeur(String nom) {
        this.nom = nom;
        this.livresPublies=new ArrayList<>();
    }
    public String getNom() {
        return nom;
    }
    public List<Livre> getLivresPublies() {
        return livresPublies;
    }
    
}
