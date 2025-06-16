import java.sql.*;
import java.util.*;

public class AuteurBD{
    Connection laConnexion;
	Statement st;
	AuteurBD(Connection laConnexion){
		this.laConnexion = laConnexion;
	}

    public Auteur getAuteur(Sring id) throws SQLException{
        String req = "SELECT * FROM AUTEUR WHERE idauteur = ?";
        this.st=laConnexion.prepareStatement(req);
        st.setString(1, id);
        ResultSet rs = st.executeUpdate(req);
        try{
            while(rs.next()){
                // idauteur -> String
                String idauteur = rs.getString("idauteur");
                // nomauteur  -> String
                String nomauteur = rs.getString("nomauteur");
                // anneenais -> int
                int anneenais = rs.getInt
                // anneedeces -> int 

            }
        }
    }

    public void insererAuteur(Auteur aut){

    }

    public String maxIdAuteur(){
        
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
