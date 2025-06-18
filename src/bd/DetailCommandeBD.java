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



	public void insertDetailCommande(DetailCommande detailCommande){
		try{
			String req = "INSERT INTO DETAILCOMMANDE VALUES(?,?,?,?,?)";
			PreparedStatement st = laconnection.prepareStatement(req);
			st.setInt(1, detailCommande.getNumCo());
			st.setInt(2, detailCommande.getQte());
			st.setInt(3, detailCommande.getNumDetailCommande());
			st.setDouble(4, detailCommande.getPrixLivres());
			st.setString(5, detailCommande.getLivre().getIsbn());
			st.executeUpdate();
		}catch(SQLException e){
			System.err.println(e.getMessage());
		}
	}

	public Integer genererId(){
		try {
			String req = "Select MAX(numlig) max FROM DETAILCOMMANDE";
			PreparedStatement st = laconnection.prepareStatement(req);
			ResultSet rs = st.executeQuery();
			while(rs.next()){
                int max = rs.getInt("max")+1;
				return max;
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
}