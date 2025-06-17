package bd;
import Modele.*;
import java.sql.*;
import java.util.*;

public class EditeurBD {
    private Connection connection;

    public EditeurBD(Connection connection){
        this.connection = connection;
    }

    public Editeur getEditeur(int id){
        String req = "Select * FROM EDITEUR WHERE idedit = ?";
		try{
            PreparedStatement st = connection.prepareStatement(req);
            st.setInt(1, id);
		    ResultSet rs = st.executeQuery();
			while(rs.next()){
				int idEdit = rs.getInt("idedit");
				String  nom = rs.getString("nomedit");
                return new Editeur(idEdit, nom);
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
		}
        return null;
    }

	public List<Editeur> getListeEditeurs(){
		List<Editeur> listeEditeurs = new ArrayList<>();
		String req = "Select * FROM EDITEUR";
		try{
			PreparedStatement st = connection.prepareStatement(req);
		    ResultSet rs = st.executeQuery();
			while(rs.next()){
				int idEdit = rs.getInt("idedit");
				String  nom = rs.getString("nomedit");
				Editeur editeur = new Editeur(idEdit, nom);
				listeEditeurs.add(editeur);
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
		}
		return listeEditeurs;
	}

    public void liaisonLivreEditeur(Editeur editeur){
        try {
			String req = "SELECT * from EDITER WHERE idauteur = ?";
			PreparedStatement st = connection.prepareStatement(req);
			ResultSet rs = st.executeQuery();
			while(rs.next()){
                LivreBD livreBD = new LivreBD(connection);
				Livre livre = livreBD.getLivre(rs.getString("isbn"));
                livre.ajouterEditeur(editeur);
                editeur.ajouterLivrePublie(livre);
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
    }

    public Integer getIdEditeur(String nomedit){
        String req = "Select * FROM EDITEUR WHERE nomedit = ?";
		try{
            PreparedStatement st = connection.prepareStatement(req);
            st.setString(1, nomedit);
		    ResultSet rs = st.executeQuery();
			while(rs.next()){
				return rs.getInt("idedit");
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
		}
        return null;
    }

    public Integer genererId(){
		try {
			String req = "Select MAX(idmag) max FROM MAGASIN";
			PreparedStatement st = connection.prepareStatement(req);
			ResultSet rs = st.executeQuery();
			while(rs.next()){
                return rs.getInt("max")+1;
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
    public void insererEditeur(Editeur editeur){
		try {
			String req = "Insert Into EDITEUR Values(?,?)";
			PreparedStatement st = connection.prepareStatement(req);
			st.setInt(1, editeur.getId());
			st.setString(2, editeur.getNom());
			st.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}


}