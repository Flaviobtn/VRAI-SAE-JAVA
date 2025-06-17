package bd;
import Modele.*;
import java.sql.*;
import java.util.*;

public class DetailCommandeBD {
    private Connection laconnection;

    public DetailCommandeBD(Connection laconnection){
        this.laconnection = laconnection;
    }

    public DetailCommande getDetailCommande(int id){
       
		try{
			 String req = "Select * FROM DETAILCOMMANDE WHERE numlig = ?";
			PreparedStatement st = laconnection.prepareStatement(req);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			while(rs.next()){
				int numlig = rs.getInt("numlig");
				LivreBD livrebd = new LivreBD(laconnection);
				Livre  livre = livrebd.getLivre(rs.getString("isbn"));
				int qte = rs.getInt("qte");
                int numCo = rs.getInt("numcom");
                MagasinBD magBD = new MagasinBD(laconnection);
                return new DetailCommande(numlig, livre,qte, numCo);
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
		}
		return null;
		
    }

}