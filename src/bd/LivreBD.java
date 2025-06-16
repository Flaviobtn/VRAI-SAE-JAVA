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
            String req = "SELECT * FROM livre WHERE idmagasin = ?";
            PreparedStatement st = laConnexion.prepareStatement(req);
            st.setString(1, mag.getIdmag());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                //on recuper l'isbn
                String isbn = rs.getString("isbn");
                //on recupere le titre
                String titre = rs.getString("titre");
                //on recupere l'id de l'auteur puis toutes ses info
                int idauteur = rs.getInt("idauteur");
                //on recupere les info de l'auteur 
                String reqAuteur = "SELECT * FROM auteur WHERE idauteur = ?";
                PreparedStatement stAuteur = laConnexion.prepareStatement(reqAuteur);
                ResultSet rsAuteur = stAuteur.executeQuery();
                Auteur auteur = null;
                while (rsAuteur.next()) {
                    String nom = rsAuteur.getString("nomauteur");
                    int anneeNais = rsAuteur.getInt("anneenais");
                    int anneeDeces = rsAuteur.getInt("anneedeces");
                    auteur = new Auteur(String.valueOf(idauteur), nom, anneeNais, anneeDeces);
                }

                // on recupere le nombre de pages
                int nbpages = rs.getInt("nbpages");

                //on recupere le prix
                double prix = rs.getDouble("prix");

                //on recupere la date de publication
                int datepub = rs.getInt("datepubli");

                // on recupere l'editeur
                String reqEditeur = "SELECT * FROM editeur WHERE idediteur = ?";
                PreparedStatement stEditeur = laConnexion.prepareStatement(reqEditeur);
                ResultSet rsEditeur = stEditeur.executeQuery();
                Editeur editeur = null;
                while (rsEditeur.next()) {
                    String nomEditeur = rsEditeur.getString("nom");
                    editeur = new Editeur(nomEditeur);
                }

                
                livres.add(new Livre(isbn, titre, auteur, datepub, nbpages, prix, editeur));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return livres;       
    }

    public boolean ajouterLivre(Livre livre) {
        try {
            if (! livreExiste(livre.getIsbn())) {
            String req = "INSERT INTO livre (isbn, titre, idauteur, nbpages, prix, datepubli, idmagasin) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement st = laConnexion.prepareStatement(req);
            st.setString(1, livre.getIsbn());
            st.setString(2, livre.getTitre());
            st.setInt(3, Integer.parseInt(livre.getAuteur().getIdAuteur()));
            st.setInt(4, livre.getNbDePages());
            st.setDouble(5, livre.getPrix());
            st.setInt(6, livre.getDateparution());
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

    public boolean livreExiste(String isbn) {
        try {
            String req = "SELECT * FROM livre WHERE isbn = ?";
            PreparedStatement st = laConnexion.prepareStatement(req);
            st.setString(1, isbn);
            ResultSet rs = st.executeQuery();
            return rs.next(); // Si un résultat est trouvé, le livre existe
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
/*
    public Livre getLivre(String id) throws SQLException{
        try {
            String req = "SELECT * FROM livre WHERE isbn = ?";
            PreparedStatement st = laConnexion.prepareStatement(req);
            st.setString(1, String.valueOf(id));
            ResultSet rsIsbn = st.executeQuery(req);
            while (rsIsbn.next()) {
                String isbn = rsIsbn.getString("isbn");
                String titre = rsIsbn.getString("titre");
                AuteurBD auteurBD = new AuteurBD(laConnexion);
                
                return new Livre(isbn, titre, null, datepub, nbpages, prix, null);
                //int 
                //double
                

            }
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
    */


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
