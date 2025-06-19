package bd;
import Modele.*;
import java.sql.*;
import java.time.*;
import java.util.*;

public class CommandeBD {
    Connection laConnexion;
	PreparedStatement st;
	public CommandeBD(Connection laConnexion){
		this.laConnexion=laConnexion;
	}
    //numcom  int NOT NULL,
    //datecom date,
    //enligne char(1),
    //livraison char(1),
    //idcli   int NOT NULL,
    //idmag   VARCHAR(42) NOT NULL


    public Commande getCommande(Client client, int nunumcomm)throws SQLException, IllegalArgumentException{
		Commande commande = new Commande(nunumcomm, null, false, null, null, null,false);
		String req = "Select * FROM COMMANDE WHERE numcom = ?";
		this.st = laConnexion.prepareStatement(req);
		st.setInt(1, nunumcomm);
		ResultSet rs = st.executeQuery();
		try{
			while(rs.next()){
				int numcom = rs.getInt("numcom");
				LocalDate  datecom = rs.getDate("datecom").toLocalDate();
				String modeComm = rs.getString("enligne");
				boolean enligne = "O".equalsIgnoreCase(modeComm);
                String livraison = rs.getString("livraison");
				Livraison mode = Livraison.fromCode(livraison);
                String idmag = rs.getString("idmag");
                MagasinBD magBD = new MagasinBD(laConnexion);
				boolean fini = "1".equalsIgnoreCase(modeComm);
                commande = new Commande(numcom, datecom, enligne, mode,  magBD.getMagasin(idmag),client,fini);
			}
			String req2 = "Select * FROM DETAILCOMMANDE WHERE numcom = ?";
			this.st = laConnexion.prepareStatement(req2);
			st.setInt(1, nunumcomm);
			rs = st.executeQuery();
			while(rs.next()){
				int numlig  = rs.getInt("numlig");
				LivreBD livrebd = new LivreBD(laConnexion);
				Livre livre = livrebd.getLivre(rs.getString("isbn"));
				int qte = rs.getInt("qte");
				commande.ajouterDetailCommande(new DetailCommande(numlig,livre,qte,nunumcomm));
				client.ajouterCommande(commande);
			}
			return commande;
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
		}
		return null;
	}

	public Commande getCommande(int nunumcomm)throws SQLException, IllegalArgumentException{
		Commande commande = new Commande(nunumcomm, null, false, null, null, null,false);
		String req = "Select * FROM COMMANDE WHERE numcom = ?";
		this.st = laConnexion.prepareStatement(req);
		st.setInt(1, nunumcomm);
		ResultSet rs = st.executeQuery();
		try{
			while(rs.next()){
				int numcom = rs.getInt("numcom");
				LocalDate  datecom = rs.getDate("datecom").toLocalDate();
				String modeComm = rs.getString("enligne");
				boolean enligne = "O".equalsIgnoreCase(modeComm);
                String livraison = rs.getString("livraison");
				Livraison mode = Livraison.fromCode(livraison);
                String idmag = rs.getString("idmag");
                MagasinBD magBD = new MagasinBD(laConnexion);
				ClientBD clientbd = new ClientBD(laConnexion);
				Client client = clientbd.getClient(rs.getInt("idcli"));
				boolean fini = "1".equalsIgnoreCase(modeComm);
                commande = new Commande(numcom, datecom, enligne, mode,  magBD.getMagasin(idmag),client,fini);
			}
			String req2 = "Select * FROM DETAILCOMMANDE WHERE numcom = ?";
			this.st = laConnexion.prepareStatement(req2);
			st.setInt(1, nunumcomm);
			rs = st.executeQuery();
			while(rs.next()){
				int numlig  = rs.getInt("numlig");
				LivreBD livrebd = new LivreBD(laConnexion);
				Livre livre = livrebd.getLivre(rs.getString("isbn"));
				int qte = rs.getInt("qte");
				commande.ajouterDetailCommande(new DetailCommande(numlig,livre,qte,nunumcomm));
			}
			return commande;
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
		}
		return null;
	}

	public void setCommande(Client client){
		String req = "Select numcom FROM COMMANDE WHERE idcli = ?";
		try {
			PreparedStatement st = this.laConnexion.prepareStatement(req);
			st.setInt(1, client.getNumeroClient());
			ResultSet rs = st.executeQuery();
			while(rs.next()){
				getCommande(client, rs.getInt("numcom"));
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		

	}

	public List<Commande> getAllCommandesBD(){
		try {
			List<Commande> lstComm = new ArrayList<>();
			String req = "SELECT DISTINCT numcom FROM COMMANDE;";
			this.st = laConnexion.prepareStatement(req);
			ResultSet rs = st.executeQuery();
			while(rs.next()){
                lstComm.add(getCommande(rs.getInt("numcom")));
			}
			rs.close();
			return lstComm;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	

    public Integer genererId(){
		try {
			String req = "Select MAX(numcom) max FROM COMMANDE";
			this.st = laConnexion.prepareStatement(req);
			ResultSet rs = st.executeQuery();
			while(rs.next()){
                int max = Integer.parseInt(rs.getString("max"))+1;
				return max;
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

    public void insererCommande(Commande commande){
		try {
			String req = "Insert Into COMMANDE Values(?,?,?,?,?,?,?)";
			this.st = laConnexion.prepareStatement(req);
			System.out.println("ID MAGASIN utilisé : " + commande.getMagasin().getIdmag());
			st.setInt(1, commande.getNumCommande());
			st.setDate(2, java.sql.Date.valueOf(commande.getDatecomm()));
			st.setString(3, commande.getEnligne()? "O":"N");
			st.setString(4, commande.getLivraison().getCode());
			st.setInt(5, commande.getClient().getNumeroClient());
        	st.setString(6, commande.getMagasin().getIdmag());
			st.setBoolean(7, commande.estFinie());
			st.executeUpdate();
			for(DetailCommande det : commande.getCommandeFinale()){
				DetailCommandeBD detail = new DetailCommandeBD(laConnexion);
				detail.insertDetailCommande(det);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
    }
}