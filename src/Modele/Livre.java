package Modele;
import bd.*;
import java.util.ArrayList;
import java.util.List;

public class Livre{
    private String isbn;
    private String titre;
    private List<Auteur> auteurs;
    private Integer nbDePages;
    private Double prix;
    private Integer dateParution;
    private List<Editeur> editeurs;
    private List<Classification> themes;

    public String getTitre() {
        return titre;
    }
    public List<Auteur> getAuteur() {
        return auteurs;
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
    public List<Editeur> getEditeur() {
        return editeurs;
    }

    public String getIsbn() {
        return isbn;
    }   
    public Livre(){
        this.isbn = null;
        this.titre = null;
        this.auteurs = null;
        this.dateParution = null;
        this.nbDePages = null;
        this.prix = null;
        this.editeurs = null;
        this.themes = null;
    }
    public Livre(String isbn,String titre, List<Auteur> auteurs,int dateParution, int nbDePages, double prix, List<Editeur> editeurs) {
        this.isbn = isbn;
        this.titre = titre;
        this.auteurs = auteurs;
        this.dateParution = dateParution;
        this.nbDePages = nbDePages;
        this.prix = prix;
        this.editeurs = editeurs;
        this.themes = new ArrayList<>();
    }

    public void ajouterThemes(Classification theme){
        this.themes.add(theme);
    }

    public int getDateparution(){
        return this.dateParution;
    }
    public void setAuteurs(List<Auteur> auteurs) {
        this.auteurs = auteurs;
    }

    public void ajouterAuteur(Auteur auteur){
        this.auteurs.add(auteur);
    }

    public void ajouterEditeur(Editeur editeur){
        this.editeurs.add(editeur);
    }
    public void setEditeurs(List<Editeur> editeurs) {
        this.editeurs = editeurs;
    }
    public void setThemes(List<Classification> themes) {
        this.themes = themes;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public void setNbDePages(Integer nbDePages) {
        this.nbDePages = nbDePages;
    }
    public void setPrix(Double prix) {
        this.prix = prix;
    }
    public void setDateParution(Integer dateParution) {
        this.dateParution = dateParution;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((isbn == null) ? 0 : isbn.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Livre other = (Livre) obj;
        if (isbn == null) {
            if (other.isbn != null)
                return false;
        } else if (!isbn.equals(other.isbn))
            return false;
        return true;
    }
}
