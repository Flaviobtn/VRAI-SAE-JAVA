package bd;

import Modele.*;
import java.sql.*;
import java.util.*;

public class LivreBD {
    private Connection laConnexion;

    public LivreBD(Connection laConnection) {
        this.laConnexion = laConnection;
    }


    
    public List<Livre> getTousLesLivres(Magasin mag){
        List<Livre> livres = new ArrayList<>();
        try {
            String req = "SELECT * FROM LIVRE NATURAL JOIN POSSEDER NATURAL JOIN MAGASIN WHERE idmag = ?";
            PreparedStatement st = laConnexion.prepareStatement(req);
            st.setString(1, mag.getIdmag());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {

                //on recuper l'isbn
                String isbn = rs.getString("isbn");

                //on recupere le titre
                String titre = rs.getString("titre");

                
                // on recupere le nombre de pages
                int nbpages = rs.getInt("nbpages");

                //on recupere le prix
                double prix = rs.getDouble("prix");

                //on recupere la date de publication
                int datepub = rs.getInt("datepubli");

                // Récupération de l'auteur
                String reqAuteur = "SELECT * FROM ECRIRE WHERE isbn = ?";
                PreparedStatement stAuteur = laConnexion.prepareStatement(reqAuteur);
                stAuteur.setString(1, isbn);
                ResultSet rsAuteur = stAuteur.executeQuery();
                List<Auteur> auteurs = new ArrayList<>();
                while (rsAuteur.next()){
                    // comme il y a plusieurs auteurs, on les ajoutes tous à une liste
                    String idAuteur = rsAuteur.getString("idauteur");
                    String reqAuteurDetails = "SELECT * FROM AUTEUR WHERE idauteur = ?";
                    PreparedStatement stAuteurDetails = laConnexion.prepareStatement(reqAuteurDetails);
                    stAuteurDetails.setString(1, idAuteur);
                    ResultSet rsAuteurDetails = stAuteurDetails.executeQuery();
                    while (rsAuteurDetails.next()) {
                        String nom = rsAuteurDetails.getString("nomauteur");
                        int anneeNais = rsAuteurDetails.getInt("anneenais");
                        int anneeDeces = rsAuteurDetails.getInt("anneedeces");
                        auteurs.add(new Auteur(idAuteur, nom, anneeNais, anneeDeces));
                    }
                }


                // Récupération de l'éditeur
                List<Editeur> editeurs = new ArrayList<>();
                String reqEditeur = "SELECT * FROM EDITER WHERE isbn = ?";
                PreparedStatement stEditeur = laConnexion.prepareStatement(reqEditeur);
                stEditeur.setString(1, isbn);
                ResultSet rsEditeur = stEditeur.executeQuery();
                while (rsEditeur.next()) {
                    // on récupère les différents éditeurs s'il y en a plusieurs
                    int idedit = rsEditeur.getInt("idedit");
                    String reqEditeurDetails = "SELECT * FROM EDITEUR WHERE idedit = ?";
                    PreparedStatement stEditeurDetails = laConnexion.prepareStatement(reqEditeurDetails);
                    stEditeurDetails.setInt(1, idedit);
                    ResultSet rsEditeurDetails = stEditeurDetails.executeQuery();
                    
                    while (rsEditeurDetails.next()) {
                        String nomEditeur = rsEditeurDetails.getString("nomedit");
                        editeurs.add(new Editeur(idedit, nomEditeur));
                    }
                }

                
                livres.add(new Livre(isbn, titre, auteurs, datepub, nbpages, prix, editeurs));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return livres;       
    }

    public Livre getLivreParTitre(String titreLivre){
        Livre livre = null;
        try {
            String req = "SELECT * FROM LIVRE WHERE titre = ?";
            PreparedStatement st = laConnexion.prepareStatement(req);
            st.setString(1, titreLivre);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                String isbn = rs.getString("isbn");
                String titre = rs.getString("titre");
                int nbPages = rs.getInt("nbpages");
                int datepub = rs.getInt("datepubli");
                double prix = rs.getDouble("prix");

                // Récupération de l'auteur
                String reqAuteur = "SELECT * FROM ECRIRE WHERE isbn = ?";
                PreparedStatement stAuteur = laConnexion.prepareStatement(reqAuteur);
                stAuteur.setString(1, isbn);
                ResultSet rsAuteur = stAuteur.executeQuery();
                List<Auteur> auteurs = new ArrayList<>();
                while (rsAuteur.next()){
                    // comme il y a plusieurs auteurs, on les ajoutes tous à une liste
                    String idAuteur = rsAuteur.getString("idauteur");
                    String reqAuteurDetails = "SELECT * FROM AUTEUR WHERE idauteur = ?";
                    PreparedStatement stAuteurDetails = laConnexion.prepareStatement(reqAuteurDetails);
                    stAuteurDetails.setString(1, idAuteur);
                    ResultSet rsAuteurDetails = stAuteurDetails.executeQuery();
                    while (rsAuteurDetails.next()) {
                        String nom = rsAuteurDetails.getString("nomauteur");
                        int anneeNais = rsAuteurDetails.getInt("anneenais");
                        int anneeDeces = rsAuteurDetails.getInt("anneedeces");
                        auteurs.add(new Auteur(idAuteur, nom, anneeNais, anneeDeces));
                    }
                }


                // Récupération de l'éditeur
                List<Editeur> editeurs = new ArrayList<>();
                String reqEditeur = "SELECT * FROM EDITER WHERE isbn = ?";
                PreparedStatement stEditeur = laConnexion.prepareStatement(reqEditeur);
                stEditeur.setString(1, isbn);
                ResultSet rsEditeur = stEditeur.executeQuery();
                while (rsEditeur.next()) {
                    // on récupère les différents éditeurs s'il y en a plusieurs
                    int idedit = rsEditeur.getInt("idedit");
                    String reqEditeurDetails = "SELECT * FROM EDITEUR WHERE idedit = ?";
                    PreparedStatement stEditeurDetails = laConnexion.prepareStatement(reqEditeurDetails);
                    stEditeurDetails.setInt(1, idedit);
                    ResultSet rsEditeurDetails = stEditeurDetails.executeQuery();
                    
                    while (rsEditeurDetails.next()) {
                        String nomEditeur = rsEditeurDetails.getString("nomedit");
                        editeurs.add(new Editeur(idedit, nomEditeur));
                    }
                }

                // Création du livre
                livre = new Livre(isbn, titre, auteurs, datepub, nbPages, prix, editeurs);
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return livre;
    }

    public List<Livre> getTousLesLivresBase(){
        String req = "SELECT * FROM LIVRE";
        List<Livre> livres = new ArrayList<>();
        Livre livre = null;
        try {
            PreparedStatement st = laConnexion.prepareStatement(req);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                livre = getLivre(isbn);
                livres.add(livre);
        }
        } 
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }   
        return livres;  
    }

    public boolean ajouterLivreMagasin(Vendeur vendeur, Livre livre) {
        try {
            if (! livreExisteMagasin(vendeur, livre.getIsbn())) {
            String req = "INSERT INTO POSSEDER VALUES (?, ?, ?)";
            PreparedStatement st = laConnexion.prepareStatement(req);
            st.setString(1, vendeur.getMagasin().getIdmag());
            st.setString(2, livre.getIsbn());
            st.setInt(3, 1);
            st.executeUpdate();
            System.out.println("Livre ajouté avec succès.");
            return true;
            } else {
                System.out.println("Le livre existe déjà.");
        }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public boolean livreExisteMagasin(Vendeur vendeur, String isbn) {
        try {
            String req = "SELECT * FROM LIVRE NATURAL JOIN POSSEDER NATURAL JOIN MAGASIN WHERE isbn = ? and idmag = ?";
            PreparedStatement st = laConnexion.prepareStatement(req);
            st.setString(1, isbn);
            st.setString(2, vendeur.getMagasin().getIdmag());
            ResultSet rs = st.executeQuery();
            return rs.next(); // Si un résultat est trouvé, le livre existe
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    

    public Livre getLivre(String isbn) throws SQLException{
        Livre livre = new Livre();
        try {
            String req = "SELECT * FROM LIVRE WHERE isbn = ?";
            PreparedStatement st = laConnexion.prepareStatement(req);
            st.setString(1, String.valueOf(isbn));
            ResultSet rsIsbn = st.executeQuery();
            while (rsIsbn.next()) {
                String id = rsIsbn.getString("isbn");
                String titre = rsIsbn.getString("titre");
                int nbpages = rsIsbn.getInt("nbpages");
                int datepub = rsIsbn.getInt("datepubli");
                double prix = rsIsbn.getDouble("prix");

                livre.setIsbn(id);
                livre.setPrix(prix);
                livre.setTitre(titre);
                livre.setNbDePages(nbpages);
                livre.setDateParution(datepub);
            }


            req = "SELECT * FROM LIVRE NATURAL JOIN ECRIRE NATURAL JOIN AUTEUR WHERE isbn = ?";
            st = laConnexion.prepareStatement(req);
            st.setString(1, String.valueOf(isbn));
            rsIsbn = st.executeQuery();
            List<Auteur> listAut = new ArrayList<>();
            while (rsIsbn.next()) {
                AuteurBD auteurBD = new AuteurBD(laConnexion);
                Auteur auteur = auteurBD.getAuteur(rsIsbn.getString("idauteur"));
                listAut.add(auteur);
            }
            livre.setAuteurs(listAut);

            req = "SELECT * FROM LIVRE NATURAL JOIN EDITER NATURAL JOIN EDITEUR WHERE isbn = ?";
            st = laConnexion.prepareStatement(req);
            st.setString(1, String.valueOf(isbn));
            rsIsbn = st.executeQuery();
            List<Editeur> listEdit = new ArrayList<>();
            while (rsIsbn.next()) {
                EditeurBD editeurBD = new EditeurBD(laConnexion);
                Editeur editeur = editeurBD.getEditeur(rsIsbn.getInt("idedit"));
                listEdit.add(editeur);
            }
            livre.setEditeurs(listEdit);

            req = "SELECT * FROM LIVRE NATURAL JOIN THEMES NATURAL JOIN CLASSIFICATION WHERE isbn = ?";
            st = laConnexion.prepareStatement(req);
            st.setString(1, String.valueOf(isbn));
            rsIsbn = st.executeQuery();
            List<Classification> listClass = new ArrayList<>();
            while (rsIsbn.next()) {
                //ClassificationBD classificationBD = new ClassificationBD(laConnexion);
                //Classification classification = classificationBD.getClassification(rsIsbn.getString("iddewey"));
                //listClass.add(classification);
            }
            livre.setThemes(listClass);
            return livre;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void supprimerLivreMagasin(Vendeur vendeur, Livre livre){
        
        if (livreExisteMagasin(vendeur, livre.getIsbn())) {
            System.out.println("Le livre existe dans le magasin, suppression en cours...");
            String req = "Delete FROM POSSEDER WHERE isbn = ? and idmag = ?";
            try {
                PreparedStatement st = laConnexion.prepareStatement(req);
                st.setString(1, livre.getIsbn());
                st.setString(2, vendeur.getMagasin().getIdmag());
                int rowsAffected = st.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Livre supprimé avec succès du magasin.");
                } else {
                    System.out.println("Aucun livre trouvé à supprimer.");
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.out.println("Le livre n'existe pas dans le magasin, suppression impossible.");
            return;
        }
    }
        
    


/*
    public Auteur getAuteur(){
        List<Livre> livres = new ArrayList<>();
        try{
            String req = "SELECT * FROM ECRIR WHERE idauteur = ?";
            PreparedStatement st=laConnexion.prepareStatement(req);
            st.setString(1, auteur.getIdAuteur());
            ResultSet rs = st.executeQuery(req);
            while(rs.next()){
                getAuteur(rs.getString("idauteur"));
                Auteur aut = getAuteur(rs.getString("idauteur"));
                
                return livres;
            }
        }catch(SQLException e){
			System.err.println(e.getMessage());
		}
        return livres;
    }

    */
}
