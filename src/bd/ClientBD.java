package bd;
import Modele.*;
import java.sql.*;
import java.util.*;

public class ClientBD{
    Connection laConnexion;
	PreparedStatement st;
	public ClientBD(Connection laConnexion){
		this.laConnexion = laConnexion;
	}

	public Client getClient(int idcli)throws SQLException{
		String req = "Select * FROM CLIENT WHERE idcli = ?";
		this.st = laConnexion.prepareStatement(req);
		this.st.setString(1, String.valueOf(idcli));
		ResultSet rs = st.executeQuery();
		try{
			while(rs.next()){
				int idClient = rs.getInt("idcli");
				String nomCli = rs.getString("nomcli");
				String prenomCli = rs.getString("prenomcli");
				String identifiant = rs.getString("identifiant");
				String addresseCli = rs.getString("adressecli");
				int codePostalCli = rs.getInt("codepostal");
				String motdepasse = rs.getString("identifiant");
				String villeCli = rs.getString("villecli");
				Client client = new Client(idClient, nomCli,prenomCli, identifiant,addresseCli, codePostalCli, motdepasse, villeCli);
			return client;
			}
			rs.close();
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
		}
		return null;
	}

	public Set<Livre> getRecoLivre(int numCo,String isbn){
		Set<Livre> livres = new HashSet<>();
		String req = "SELECT DISTINCT d3.isbn AS idLivre FROM DETAILCOMMANDE d1 " +
				 "JOIN DETAILCOMMANDE d2 ON d1.isbn = d2.isbn " +
				 "JOIN DETAILCOMMANDE d3 ON d2.numcom = d3.numcom " +
				 "WHERE d1.numcom = ? AND d2.numcom != ? " +
				 "AND d3.isbn NOT IN (SELECT isbn FROM DETAILCOMMANDE WHERE numcom = ?) " +
				 "LIMIT 10";
		try {
			PreparedStatement st = laConnexion.prepareStatement(req);
			st.setInt(1, numCo);
			st.setInt(2, numCo);
			st.setInt(3, numCo);
			ResultSet rs = st.executeQuery();
			LivreBD livrebd = new LivreBD(laConnexion);
			while (rs.next()) {
				livres.add(livrebd.getLivre(rs.getString("idLivre")));
			}
			return livres;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return livres;
	}

	// verifier 2 clients
	public boolean verifierIdentifiant(String prenom, String nom) throws SQLException {
		String req = "Select * FROM CLIENT WHERE nomcli = ? AND prenomcli = ?";
		this.st = laConnexion.prepareStatement(req);
		this.st.setString(1, nom);
		this.st.setString(2, prenom);
		ResultSet rs = st.executeQuery();
		try {
			if(rs.next()) {
				System.out.println("Client déjà existant.");
				return false;
			} else {
				System.out.println("Identifiant disponible.");
				return true;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			rs.close();
		}
		return false;
	}

	public Client getClient(String id, String mdp) throws SQLException {
		try {
			String req = "Select * FROM CLIENT WHERE identifiant = ? AND motdepasse = ?";
			this.st = laConnexion.prepareStatement(req);
			this.st.setString(1, id);
			this.st.setString(2, mdp);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				int idClient = rs.getInt("idcli");
				String nomCli = rs.getString("nomcli");
				String prenomCli = rs.getString("prenomcli");
				String identifiant = rs.getString("identifiant");
				String addresseCli = rs.getString("adressecli");
				int codePostalCli = rs.getInt("codepostal");
				String motdepasse = rs.getString("motDePasse");
				String villeCli = rs.getString("villecli");
				Client client = new Client(idClient, nomCli, prenomCli, identifiant, addresseCli, codePostalCli, motdepasse, villeCli);

				return client;
			}
			rs.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());			
			}
		return null;
	}



	public void inscrireClient(String prenom, String nom, String mdp, String adresse, String ville, int codePostal) throws SQLException {
		Integer idCli = genererId();
		String ident = String.valueOf(prenom.charAt(0)).toLowerCase() + nom.toLowerCase();
		// Vérification de l'identifiant
		
		if(verifierIdentifiant(prenom, nom)) {
			Client client = new Client(idCli, nom, prenom, ident, adresse, codePostal, mdp, ville);
		insererClient(client);
	}
	}

	public boolean seconnecterClient(String  identifiant, String mdp) throws SQLException {
		String req = "Select * FROM CLIENT WHERE identifiant = ? AND motdepasse = ?";
		this.st = laConnexion.prepareStatement(req);
		this.st.setString(1, identifiant);
		this.st.setString(2, mdp);
		ResultSet rs = st.executeQuery();
		try {
			if(rs.next()) {
				System.out.println("Connexion réussie.");
				return true;
			} else {
				System.out.println("Identifiant ou mot de passe incorrect.");
				return false;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			rs.close();
		}
		return false;
	}

	public void insererClient(Client client){
		try {
			String req = "Insert Into CLIENT Values(?,?,?,?,?,?,?,?)";
			this.st = laConnexion.prepareStatement(req);
			st.setInt(1, client.getNumeroClient());
			st.setString(2, client.getNom());
			st.setString(3, client.getPrenom());
			st.setString(4, client.getIdentifiant());
			st.setString(5, client.getAdresse());
			st.setString(6, String.valueOf((client.getCodePostal())));
			st.setString(7, client.getMotDePasse());
			st.setString(8, client.getVille());
			st.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public Integer genererId(){
		try {
			String req = "Select MAX(idcli) max FROM CLIENT";
			this.st = laConnexion.prepareStatement(req);
			ResultSet rs = st.executeQuery();
			while(rs.next()){
				return rs.getInt("max")+1;
			}
			rs.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return 1;
	}
	
	
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
