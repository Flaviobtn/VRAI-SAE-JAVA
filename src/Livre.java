import java.util.ArrayList;
import java.util.List;

public class Livre{
    private String isbn;
    private String titre;
    private Auteur auteur;
    private int nbDePages;
    private double prix;
    private int dateParution;
    private Editeur editeur;
    private List<Classification> themes;

    public String getTitre() {
        return titre;
    }
    public Auteur getAuteur() {
        return auteur;
    }
    public int getNbDePages() {
        return nbDePages;
    }
    public double getPrix() {
        return prix;
    }
    public List<Classification> getThemes() {
        return themes;
    }
    public Editeur getEditeur() {
        return editeur;
    }

    public String getIsbn() {
        return isbn;
    }   

    public Livre(String isbn,String titre, Auteur auteur,int dateParution, int nbDePages, double prix, Editeur editeur) {
        this.isbn = isbn;
        this.titre = titre;
        this.auteur = auteur;
        this.dateParution = dateParution;
        this.nbDePages = nbDePages;
        this.prix = prix;
        this.editeur = editeur;
        this.themes = new ArrayList<>();
    }

    public void ajouterThemes(Classification theme){
        this.themes.add(theme);
    }

    public int getDateparution(){
        return this.dateParution;
    }
}
