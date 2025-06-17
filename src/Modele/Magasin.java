package Modele;
import bd.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Magasin {
    private String idmag;
    private String nomMag;
    private String villeMag;
    private List<Livre> stock;

    public Magasin(String idmag, String nomMag, String villeMag){
        this.nomMag = nomMag;
        this.villeMag = villeMag;
        this.idmag = idmag;
        this.stock = new ArrayList<>();
    }
    public String getIdmag(){
        return this.idmag;
    }
    public String getNomMag() {
        return this.nomMag;
    }

    public String getVilleMag() {
        return this.villeMag;
    }

    public List<Livre> getStock() {
        return this.stock;
    }

    public void ajouterLivre(Livre livre){
        this.stock.add(livre);
    }
    
    public void supprimerLivre(Livre livre){
        this.stock.remove(livre);
    }
}