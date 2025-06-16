package bd;

import Modele.*;
import java.util.*;
import java.sql.*;

public class LivreBD {
    private Connection laConnexion;

    public LivreBD(Connection laConnection) {
        this.laConnexion = laConnection;
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
