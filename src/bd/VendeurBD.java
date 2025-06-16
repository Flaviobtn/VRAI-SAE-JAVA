package bd;
import Modele.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendeurBD {
    Connection laConnexion;
	PreparedStatement st;
	public VendeurBD(Connection laConnexion){
		this.laConnexion=laConnexion;
	}

    public Vendeur getVendeur(String idVendeur)throws SQLException{
		String req = "Select * FROM VENDEUR WHERE idVendeur = ?";
		this.st = laConnexion.prepareStatement(req);
		st.setString(1, idVendeur);
		ResultSet rs = st.executeQuery();
		try{
			while(rs.next()){
				String idV = rs.getString("idVendeur");
				String  nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
                String mdp = rs.getString("motDePasse");
                String mag = rs.getString("idmag");
                MagasinBD magBD = new MagasinBD(laConnexion);
                return new Vendeur(idV, nom, prenom, mdp, magBD.getMagasin(mag));
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
		}
		return null;
	}

    public boolean seconnecterVendeur(String idVendeur, String mdp) throws SQLException {
        String req = "SELECT * FROM VENDEUR WHERE idVendeur = ? AND motDePasse = ?";
        this.st = laConnexion.prepareStatement(req);
        st.setString(1, idVendeur);
        st.setString(2, mdp);
        ResultSet rs = st.executeQuery();
        boolean isConnected = rs.next(); // Si un résultat est trouvé, la connexion est réussie
        rs.close();
        return isConnected;
    }

    public String genererId(){
		try {
			String req = "Select MAX(idVendeur) max FROM VENDEUR";
			this.st = laConnexion.prepareStatement(req);
			ResultSet rs = st.executeQuery();
			while(rs.next()){
                // Si le champ max est null, on initialise à 1
                if(rs.getString("max") == null || rs.getString("max").isEmpty()){
                    return "1";
                }else{
                    int max = Integer.parseInt(rs.getString("max"));
                    max++;
				    return String.valueOf(max);
                    }
                }rs.close();
                
			}catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

    public void insertVendeur(Vendeur vendeur){
		try {
			String req = "Insert Into VENDEUR Values(?,?,?,?,?)";
			this.st = laConnexion.prepareStatement(req);
			st.setString(1, vendeur.idVendeur());
			st.setString(2, vendeur.getNom());
			st.setString(3, vendeur.getPrenom());
            st.setString(4, vendeur.getMotdepasse());
            st.setString(5, vendeur.getMagasin().getIdmag());
            st.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
    }
}
/*
	public String modifierLivre(String isbn, String idmag, Integer qte ){
		return null;
	}

	public void ajouterLivre(Livre livre) throws SQLException{
        String isbnStr = "";
		this.st = this.laConnexion.createStatement();
        ResultSet rs = this.st.executeQuery("SELECT MAX(isbn) max FROM livre;");
        while (rs.next()){
            String str=rs.getString("max");
            Long isbn = Long.parseLong(str);
            isbn++;
            isbnStr = Long.toString(isbn);
        }
        rs.close();
        st=laConnexion.createStatement();
        String req = "INSERT INTO livre VALUES(" +isbnStr+",'"+
        livre.getTitre() + "'," +
        String.valueOf(livre.getNbDePages()) + "," + 
		String.valueOf(livre.getDateparution())+ "," + 
        String.valueOf(livre.getPrix()) + ")";

        List<String> reqDeux = new ArrayList<>();
        for (Classification cl : livre.getThemes()){
            String reqClass = "INSERT INTO themes VALUES( "+ isbnStr + "," + cl.getIddewey() +")";
            reqDeux.add(reqClass);
        }
        // AJOUTER DANS LIVRE
        st.executeUpdate(req);

        // AJOUTER DANS CLASSIFICATION
        for (String lesReq : reqDeux){
            st.executeQuery(lesReq);
        }

        // AJOUTER DANS ECRIRE
        AuteurBD auteurBD = new AuteurBD(laConnexion);
        String reqEcr = "INSERT INTO ecrire VALUES(" + isbnStr +",'"+ auteurBD.getIdAuteur(livre.getAuteur().getNom())+"')";
        st.executeUpdate(reqEcr);

        // AJOUTER DANS EDITER
        ResultSet rsEdit = st.executeQuery("SELECT DISTINCT idedit FROM EDITEUR WHERE nomedit='" + livre.getEditeur().getNom()+"'");
		if(!rsEdit.next()){
			System.err.println("L'éditeur ne correspond à aucun éditeur de notre base de données");
		}else{
            int idedit = rsEdit.getInt("idedit");
            if(rsEdit.wasNull()){
                System.err.println("Le champ est null");
            }else{
			    String reqEdit = "INSERT INTO editer VALUES("+isbnStr +","+ String.valueOf(idedit)+")";
        	    st.executeUpdate(reqEdit);
            }
        }
        rsEdit.close();
	}

	public void modifierQuantiteLivreMagasin(String isbn, String idmag, int qte) throws SQLException{
		try{
			st=laConnexion.createStatement();
        	String req = "UPDATE POSSEDER SET qte = " + String.valueOf(qte) + " WHERE isbn = '" + isbn
        	+ "' AND idmag = '"+ idmag+"'";
            st.executeUpdate(req);
		}catch(SQLException e){
			System.err.println(e.getMessage());
		}
	}

    public List<Vendeur> getListVendeurs(){
        String req = "SELECT * FROM vendeur";
        List<Vendeur> vendeurs = new ArrayList<>();
        try{
        Statement st = ConnectionBD.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            MagasinBD magBD = new MagasinBD(laConnexion);
            Magasin mag = magBD.idMagToMag(rs.getString("idmag"));
            Vendeur vendeur = new Vendeur(nom,prenom,mag);
            vendeurs.add(vendeur);
        }

    } catch (SQLException e) {
        System.out.println("Erreur lors de la récupération des auteurs : " + e.getMessage());
    }
        return vendeurs;
    }

    public boolean verifierDisponibiliteLivre(String isbn) throws SQLException {
        String sql = "SELECT COUNT(*) AS nb FROM POSSEDER WHERE isbn = ? AND qte > 0";
        try (PreparedStatement pst = laConnexion.prepareStatement(sql)) {
            pst.setString(1, isbn);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int nb = rs.getInt("nb");
                return nb > 0; 
            } else {
                return false; 
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la vérification de la disponibilité du livre : " + e.getMessage(), e);
        }
    }
    
    public void commanderLivreClient(Commande commande, DetailCommande detailCommande, int numeroClient) throws SQLException {
    
    	try {
       		// Ajouter le détail à la commande
        	commande.ajouterDetailCommande(detailCommande);
        
        	// Créer une liste pour les détails (pour la méthode de ClientBD)
        	List<DetailCommande> detailsCommande = new ArrayList<>();
        	detailsCommande.add(detailCommande);
        
        	// Appeler la méthode de ClientBD pour persister en base
        	ClientBD clientBD = new ClientBD(this.laConnexion);
        	clientBD.commanderLivre(commande, detailsCommande);
        
        	System.out.println("Commande créée avec succès pour le client n°" + numeroClient);
        	System.out.println(commande.editerFacture());
        
        
    	} catch (SQLException e) {
        	System.err.println("Erreur lors de la création de la commande: " + e.getMessage());
    	}
	}
}
*/
