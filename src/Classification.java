import java.util.ArrayList;
import java.util.List;

public class Classification {
    private int iddewey;
    private String theme;
    private List<Livre> livresGenre;

    public Classification(String theme){
        this.theme=theme;
        this.livresGenre=new ArrayList<>();
    }

    public String getTheme() {
        return this.theme;
    }

    public int getIddewey(){
        return this.iddewey;
    }

    public List<Livre> getLivresGenre() {
        return livresGenre;
    }

    public void ajouterLivreGenre(Livre livre){
        this.livresGenre.add(livre);
    }
}