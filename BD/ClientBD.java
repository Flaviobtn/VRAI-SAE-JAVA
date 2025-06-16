import java.sql.*;
import java.util.*;

public class ClientBD{
    ConnectionBD laConnexion;
	Statement st;
	ClientBD(ConnectionBD laConnexion){
		this.laConnexion = laConnexion;
	}

	public Client getClient(int idcli)throws SQLException{
		String req = "Select * FROM CLIENT WHERE icli = ?";
		this.st = laConnexion.prepareStatement(req);
		st.setInt(1, idcli);
		ResultSet rs = st.executeUpdate(req);
		try{
			while(rs.next()){
				int idClient = rs.getInt("idcli");
				String rs.getString("nomcli");
				String prenomCli = rs.getString("prenomcli");
				String addresseCli = rs.getString("addressecli");
				String codePostalCli = rs.getString("villecli");
				String villeCli = rs.getString();
				Client client = new Client(nomCli, prenomCli, numCli, addresseCli, codePostalCli, villeCli)
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage())
		}
		return client;
	}

	public void Inserer
	/*public List<Livre> consulterCatalogue() throws SQLException {
	    List<Livre> livres = new ArrayList<>();
	
	    String sql = """
	        SELECT DISTINCT 
	            l.titre, l.nbpages, l.datepubli, l.prix,
	            a.nomauteur AS nomAuteur, a.prenomauteur AS prenomAuteur,
	            e.nomedit AS nomEditeur
	        FROM LIVRE l
	        JOIN POSSEDER p ON l.isbn = p.isbn
	        LEFT JOIN ECRIRE ea ON ea.isbn = l.isbn
	        LEFT JOIN AUTEUR a ON ea.idauteur = a.idauteur
	        LEFT JOIN EDITER ed ON ed.isbn = l.isbn
	        LEFT JOIN EDITEUR e ON ed.idedit = e.idedit
	        WHERE p.qte > 0
	    """;
	
	    try (Statement st = ConnectionBD.createStatement();
	         ResultSet rs = st.executeQuery(sql)) {
			
	        while (rs.next()) {
	            String titre = rs.getString("titre");
	            int nbPages = rs.getInt("nbpages");
	            int datePubli = rs.getInt("datepubli");
	            double prix = rs.getDouble("prix");
	
	            String nomAuteur = rs.getString("nomAuteur");
	            String prenomAuteur = rs.getString("prenomAuteur");
	            Auteur auteur = new Auteur(nomAuteur, prenomAuteur);
	
	            String nomEditeur = rs.getString("nomEditeur");
	            Editeur editeur = new Editeur(nomEditeur);
	
	            Livre livre = new Livre(titre, auteur, datePubli, nbPages, prix, editeur);
	            livres.add(livre);
	        }
	
	    } catch (SQLException e) {
	        throw new SQLException("Erreur lors de la récupération du catalogue : " + e.getMessage(), e);
	    }
	
	    return livres;
	}

	public void commanderLivre(Commande commande, List<DetailCommande> detailsCommande) throws SQLException {
    	Connection conn = laConnexion.getConnection();
    	try {
        	// 1. Insérer la commande principale
        	String reqCommande = """
            	INSERT INTO COMMANDE (numcommande, enligne, livraison, reception, numclient, nummag) 
            	VALUES (?, ?, ?, ?, ?, ?)
            	""";
        
        	try (PreparedStatement pstCommande = conn.prepareStatement(reqCommande)) {
            	pstCommande.setInt(1, commande.getNumCommande());
            	pstCommande.setBoolean(2, commande.getEnligne());
            	pstCommande.setString(3, commande.getLivraison().toString());
            	pstCommande.setString(4, commande.getReception());
            	pstCommande.setInt(5, commande.getClient().getNumeroClient());
            	pstCommande.setInt(6, commande.getMagasin().getNumMag());
            
            	pstCommande.executeUpdate();
        	}
        
        // 2. Insérer les détails de commande
        String reqDetailCommande = """
            INSERT INTO DETAILCOMMANDE (numlig, qte, prixvente, numcom) 
            VALUES (?, ?, ?, ?)
            """;
        
        	try (PreparedStatement pstDetail = conn.prepareStatement(reqDetailCommande)) {
            	for (DetailCommande detail : detailsCommande) {
                	// Récupérer l'ISBN du livre
                	LivreBD livreBD = new LivreBD(conn);
                	String isbn = livreBD.getIsbn(detail.getLivre());
                
                	pstDetail.setInt(1, detail.getNumDetailCommande());
                	pstDetail.setString(2, isbn);
                	pstDetail.setInt(3, detail.getQte());
                	pstDetail.setInt(4, commande.getNumCommande());
                
                	pstDetail.executeUpdate();
                
                // 3. Mettre à jour le stock (diminuer la quantité)
                	String reqMajStock = """
                    UPDATE POSSEDER 
                    SET qte = qte - ? 
                    WHERE isbn = ? AND nummag = ?
                    """;
                
                	try (PreparedStatement pstStock = conn.prepareStatement(reqMajStock)) {
                    	pstStock.setInt(1, detail.getQte());
                    	pstStock.setString(2, isbn);
                    	pstStock.setInt(3, commande.getMagasin().getNumMag());
                    
                    	int rowsAffected = pstStock.executeUpdate();
                    	if (rowsAffected == 0) {
                        	throw new SQLException("Impossible de mettre à jour le stock pour le livre: " + detail.getLivre().getTitre());
                    	}
                	}
            	}
        	}
        
    	} catch (SQLException e) {
        	System.err.println("Erreur lors de la commande : " + e.getMessage());
    	}
	}

// Méthode à ajouter dans la classe Vendeur
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
	}*/
}
