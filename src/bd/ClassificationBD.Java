package bd;
import Modele.*;
import java.sql.*;
import java.util.*;

public class ClassificationBD{
    private Connection connection;

    public ClassificationBD(Connection connection){
        this.connection=connection;
    }

    public Classification getClassification(String id){
        String req = "Select * FROM CLASSIFICATION WHERE iddewey = ?";
		try{
            PreparedStatement st = connection.prepareStatement(req);
            st.setString(1, id);
		    ResultSet rs = st.executeQuery();
			while(rs.next()){
				String iddewey = rs.getString("iddewey");
				String  nom = rs.getString("nomclass");
                return new Classification(iddewey, nom);
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
		}
        return null;
    }
}