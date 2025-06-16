import java.util.*;
import java.sql.*;

public class LivreBD {
    private Connection laConnexion;

    public LivreBD(Connection laConnection){
        this.laConnexion = laConnection;
    }

    /*public Livre getLivre(String id) throws SQLException{
        try {
            String req = "SELECT DISTINCT isbn FROM livre WHERE isbn = ?";
            PreparedStatement st = laConnexion.prepareStatement(req);
            st.setString(1, String.valueOf(id));
            ResultSet rsIsbn = st.executeQuery(req);
            while (rsIsbn.next()) {
                String isbn = rsIsbn.getString("isbn");
                String 


            }
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }*/
}
