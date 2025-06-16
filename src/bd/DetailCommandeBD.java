package bd;
import Modele.*;
import java.sql.*;
import java.util.*;

public class DetailCommandeBD {
    private Connection laconnection;

    public DetailCommandeBD(Connection laconnection){
        this.laconnection = laconnection;
    }

    /*public DetailCommande getDetailCommande(int id){
        String req = "Select * FROM DETAILCOMMANDE WHERE numlig = ?";
		PreparedStatement st = laconnection.prepareStatement(req);
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();
		try{
			while(rs.next()){
				String iddc = rs.getString("numlig");
				int  nom = rs.getInt("qte");
				double ville = rs.getDouble("");
                String mag = rs.getString("idmag");
                
                MagasinBD magBD = new MagasinBD(laconnection);
                return new DetailCommande(idV, nom,ville, magBD.getMagasin(mag));
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
		}
		return null;
		
    }*/
}