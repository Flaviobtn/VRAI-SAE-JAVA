package bd;
import Modele.*;
import java.sql.*;
import java.time.*;
import java.util.*;

public class CommandeBD {
    Connection laConnexion;
	PreparedStatement st;
	CommandeBD(Connection laConnexion){
		this.laConnexion=laConnexion;
	}
    //numcom  int NOT NULL,
    //datecom date,
    //enligne char(1),
    //livraison char(1),
    //idcli   int NOT NULL,
    //idmag   VARCHAR(42) NOT NULL


    public Commande getCommande(int nunumcomm)throws SQLException, IllegalArgumentException{
		Commande commande = new Commande(nunumcomm, null, false, null, null, null);
		String req = "Select * FROM COMMANDE WHERE numcomm = ?";
		this.st = laConnexion.prepareStatement(req);
		st.setInt(1, nunumcomm);
		ResultSet rs = st.executeQuery(req);
		try{
			while(rs.next()){
				int numcom = rs.getInt("numcom");
				LocalDate  datecom = rs.getDate("datecom").toLocalDate();
				String modeComm = rs.getString("enligne");
				boolean enligne = "O".equalsIgnoreCase(modeComm);
                String livraison = rs.getString("livraison");
				Livraison mode = Livraison.fromCode(livraison);
                int idcli = rs.getInt("idcli");
                String idmag = rs.getString("idmag");
                MagasinBD magBD = new MagasinBD(laConnexion);
				ClientBD cliBD = new ClientBD(laConnexion);
                commande = new Commande(numcom, datecom, enligne, mode,  magBD.getMagasin(idmag), cliBD.getClient(idcli));
			}
			String req2 = "Select * FROM DETAILCOMMANDE WHERE numcomm = ?";
			this.st = laConnexion.prepareStatement(req);
			st.setInt(1, nunumcomm);
			rs = st.executeQuery(req);
			while(rs.next()){
				int numlig  = rs.getInt("numlig");
				LivreBD livrebd = new LivreBD(laConnexion);
				Livre livre = livrebd.getLivre(rs.getString("isbn"));
				int qte = rs.getInt("qte");
				commande.ajouterDetailCommande(new DetailCommande(rs.getInt(""),livre,qte,nunumcomm));
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
		}
		return null;
	}


    public String genererId(){
		try {
			String req = "Select MAX(numcom) max FROM COMMANDE";
			this.st = laConnexion.prepareStatement(req);
			ResultSet rs = st.executeQuery(req);
			while(rs.next()){
                int max = Integer.parseInt(rs.getString("max"))+1;
				return String.valueOf(max);
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

    public void insererCommande(Commande commande){
		try {
			String req = "Insert Into COMMANDE Values(?,?,?,?,?,?)";
			this.st = laConnexion.prepareStatement(req);
			st.setInt(1, commande.getNumCommande());
			st.setDate(2, java.sql.Date.valueOf(commande.getDatecomm()));
			st.setString(3, commande.getEnligne()? "O":"N");
			st.setString(3, commande.getLivraison().getCode());
            st.setString(5, commande.getMagasin().getIdmag());
			st.setInt(6, commande.getClient().getNumeroClient());
			st.executeUpdate(req);
			
			//req = "Insert Into DETAILCOMMANDE VALUES(?,?,)"
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
    }
}