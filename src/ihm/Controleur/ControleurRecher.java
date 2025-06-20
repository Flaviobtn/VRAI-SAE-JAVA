
package ihm.Controleur;
import ihm.Vue.*;
import bd.*;
import Modele.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import java.util.*;
import java.sql.*;

public class ControleurRecher implements EventHandler<ActionEvent> {
    private LivreExpresss vue;
    private LivreBD livreBD;
    private TextField rechField; // On stocke le TextField, pas la String

    public ControleurRecher(LivreExpresss vue, LivreBD livrebd, TextField rechField) {
        this.vue = vue;
        this.livreBD = livrebd;
        this.rechField = rechField; // On passe le TextField
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        try {
            // On récupère la valeur AU MOMENT DU CLIC
            String recherche = rechField.getText().trim();
            if (recherche.isEmpty()) {
                System.out.println("Veuillez entrer un nom de livre.");
                return;
            }

            List<Livre> lesLivres = new ArrayList<>();
            System.out.println("La recherche est : " + recherche);

            lesLivres = livreBD.getLivresNomAPeuPresAll(recherche);
            Map<Integer, List<Livre>> nvDic = new HashMap<>();
            if (lesLivres.isEmpty()) {
                System.out.println("Aucun livre trouvé pour : " + recherche);
            } else {
                
                System.out.println("Il y a " + lesLivres.size() + " livres disponibles dans les magasins ");
                for (Livre liv : lesLivres) {
                    System.out.println("Le livre " + liv.getTitre() + " est disponible.");
                }
                    Integer numpage = 1;
                    int cpt = 0;
                    boolean fini = false;
                    List<Livre> listeLivres = new ArrayList<>();
                    while (!fini) {
                        if (cpt + 6 >= lesLivres.size()) { // Si on a moins de 6 livres restants
                            for (int i = cpt; i < lesLivres.size(); i++) {
                                listeLivres.add(lesLivres.get(i));
                            }
                            fini = true; // On arrête la boucle
                        }
                        else{
                            for (int i = cpt; i < cpt +6; i++) { 
                                listeLivres.add(lesLivres.get(i));
                        }
                            nvDic.put(numpage, listeLivres);
                            cpt += 6; 
                            numpage++;
                            listeLivres = new ArrayList<>(); // On vide la liste pour la prochaine page
                        }
                        }
                        vue.setCatalogues(nvDic);
                        vue.modeVerifDispo();
                        }

                    }catch (Exception e) {
            System.err.println("Erreur lors de la recherche : " + e.getMessage());
                    
                }
                
                }
            }




