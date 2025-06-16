package bd;
import Modele.*;
import java.sql.*;
import java.util.*;

public class AuteurBD{
    Connection laConnexion;
	AuteurBD(Connection laConnexion){
		this.laConnexion = laConnexion;
	}

    public Auteur getAuteur(String id) throws SQLException{
        String req = "SELECT * FROM AUTEUR WHERE idauteur = ?";
        PreparedStatement st=laConnexion.prepareStatement(req);
        st.setString(1, id);
        ResultSet rs = st.executeQuery(req);
        try{
            while(rs.next()){
                // idauteur -> String
                String idauteur = rs.getString("idauteur");
                // nomauteur  -> String
                String nomprenomauteur = rs.getString("nomauteur");
                // anneenais -> int
                int anneenais = rs.getInt("annenais");
                // anneedeces -> int 
                int anneedeces = rs.getInt("anneedeces");
                // Creation de l'auteur
                Auteur auteur = new Auteur(idauteur, nomprenomauteur, anneenais, anneedeces);
                return auteur;
            }
        }
        catch(SQLException e){
			System.err.println(e.getMessage());
		}
        return null;
    }

    public List<Livre> getListeLivres(Auteur auteur){
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

    public void insererAuteur(Auteur aut){
        try {
            String req = "INSERT INTO AUTEUR VALUES(?,?,?,?)";
            PreparedStatement st=laConnexion.prepareStatement(req);
            st.setString(1,aut.getIdAuteur());
            st.setString(2,aut.getNom());
            st.setInt(3,aut.getAnneeNais());
            st.setInt(4,aut.getAnneeDeces());
        } catch (Exception e) {
            System.err.println("Le programme retourn l'erreur suivante: "+ e.getMessage());
        }
        
    }

    public String genererId(){
        try {
			String req = "SELECT MAX(CAST(SUBSTRING(idauteur, 3, LENGTH(code) - 3) AS UNSIGNED)) AS max FROM AUTEUR WHERE code LIKE 'OL%A'";
			PreparedStatement st = laConnexion.prepareStatement(req);
			ResultSet rs = st.executeQuery(req);
			while(rs.next()){
				int max =  rs.getInt("max")+1;
                return "OL"+max+"A";
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return null;
    }


	/*
    public String getIdAuteur(String nomprenom) throws SQLException {
        String sql = "SELECT idauteur FROM AUTEUR WHERE nomauteur = ?";

        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(sql)) {
            pst.setString(1, nomprenom);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("idauteur");
                }else {
                    throw new SQLException("Auteur non trouvé pour : " + nomprenom);
                }
            }
        }catch (SQLException e) {
            throw new SQLException("Erreur lors de la récupération de l'id de l'auteur : " + e.getMessage());
        }
    }

	public List<Auteur> listeAuteurs() throws SQLException {
    List<Auteur> auteurs = new ArrayList<>();

    String sql = "SELECT nomauteur FROM AUTEUR";

    try{
        Statement st = ConnectionBD.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            String nom = rs.getString("nomauteur");
            //System.out.println(nom);
            //String prenom = rs.getString("prenomauteur");

            //Auteur auteur = new Auteur(nom, prenom);
            Auteur auteur = new Auteur(nom);
            auteurs.add(auteur);
        }

    } catch (SQLException e) {
        throw new SQLException("Erreur lors de la récupération des auteurs : " + e.getMessage());
    }
    return auteurs;
    }
    */

}
