package Modele;
import bd.*;
import java.util.ArrayList;
import java.util.List;

public class Classification {
    private String iddewey;
    private String theme;
    private List<Livre> livresGenre;

    public Classification(String iddewey, String theme){
        this.theme=theme;
        this.iddewey = iddewey;
        this.livresGenre=new ArrayList<>();
    }

    public String getTheme() {
        return this.theme;
    }

    public String getIddewey(){
        return this.iddewey;
    }

    public List<Livre> getLivresGenre() {
        return livresGenre;
    }

    public void ajouterLivreGenre(Livre livre){
        this.livresGenre.add(livre);
    }
}