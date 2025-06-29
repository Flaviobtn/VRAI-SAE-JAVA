package bd;
import Modele.*;
import java.sql.*;
import java.util.*;

public class MagasinBD {
    private Connection laConnexion;

    public MagasinBD(Connection laConnexion){
		this.laConnexion = laConnexion;
	}

    public Magasin getMagasin(String idmag)throws SQLException{
		String req = "Select * FROM MAGASIN WHERE idmag = ?";
		PreparedStatement st = laConnexion.prepareStatement(req);
		st.setString(1, idmag);
		ResultSet rs = st.executeQuery();
		try{
			while(rs.next()){
				String id = rs.getString("idmag");
				String  nom = rs.getString("nommag");
				String ville = rs.getString("villemag");
                return new Magasin(id, nom,ville);
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
		}
		return null;
	}

    public int nombreDeLivre(Magasin mag){
        int nombre = 0;
        String req = "SELECT SUM(qte) AS total FROM POSSEDER WHERE idmag = ?";
        try {
            PreparedStatement st = laConnexion.prepareStatement(req);
            st.setString(1, mag.getIdmag());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                nombre = rs.getInt("total");
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return nombre;
    }

    public int nombreDeLivreVendu(Magasin mag, int annee){
        int nombre = 0;
        String req = "SELECT SUM(dc.qte) AS totalVendu " +
                     "FROM COMMANDE c " +
                     "JOIN DETAILCOMMANDE dc ON c.numcom = dc.numcom " +
                     "WHERE c.idmag = ? AND YEAR(c.datecom) = ?";
        try {
            PreparedStatement st = laConnexion.prepareStatement(req);
            st.setString(1, mag.getIdmag());
            st.setInt(2, annee);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                nombre = rs.getInt("totalVendu");
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return nombre;
    }

    public Livre livreLePlusVendu(Magasin mag, int annee){
        Livre livre = null;
        String req = "SELECT l.isbn, l.titre, SUM(dc.qte) AS totalVendu " +
                     "FROM COMMANDE c " +
                     "JOIN DETAILCOMMANDE dc ON c.numcom = dc.numcom " +
                     "JOIN POSSEDER p ON p.isbn = dc.isbn " +
                     "JOIN LIVRE l ON p.isbn = l.isbn " +
                     "WHERE c.idmag = ? AND YEAR(c.datecom) = ? " +
                     "GROUP BY l.isbn, l.titre " +
                     "ORDER BY totalVendu DESC LIMIT 1";
        try {
            PreparedStatement st = laConnexion.prepareStatement(req);
            st.setString(1, mag.getIdmag());
            st.setInt(2, annee);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String isbn = rs.getString("isbn");
                String titre = rs.getString("titre");
                LivreBD livreBD = new LivreBD(laConnexion);
                livre = livreBD.getLivre(isbn);
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return livre;
    }

    public List<Magasin> getToutLesMagasins(){
        List<Magasin> magasins = new ArrayList<>();
        String req = "Select DISTINCT idmag FROM MAGASIN";
		try{
            Statement st = laConnexion.createStatement();
            ResultSet rs = st.executeQuery(req);
			while(rs.next()){
				magasins.add(getMagasin(rs.getString("idmag")));
			}
		}
		catch(SQLException e){
			System.err.println(e.getMessage());
		}
		return magasins;
    }

    public void insertMagasin(Magasin mag){
		try {
			String req = "Insert Into MAGASIN Values(?,?,?)";
			PreparedStatement st = laConnexion.prepareStatement(req);
			st.setString(1, mag.getIdmag());
			st.setString(2, mag.getNomMag());
			st.setString(3, mag.getVilleMag());
            st.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
    }

    public double chiffreAffaire(Magasin mag, int annee){
        double chiffreAffaire = 0.0;
        String req = "SELECT SUM(dc.prixvente * dc.qte) AS chiffre FROM COMMANDE c " +
                     "JOIN DETAILCOMMANDE dc ON c.numcom = dc.numcom " +
                     "WHERE c.idmag = ? AND YEAR(c.datecom) = ?";
        try {
            PreparedStatement st = laConnexion.prepareStatement(req);
            st.setString(1, mag.getIdmag());
            st.setInt(2, annee);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                chiffreAffaire = rs.getDouble("chiffre");
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return chiffreAffaire;
    }

    public String genererId(){
		try {
			String req = "Select MAX(idmag) max FROM MAGASIN";
			PreparedStatement st = laConnexion.prepareStatement(req);
			ResultSet rs = st.executeQuery();
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

    public String getidMag(Connection laConnexion, String nomMag) {
        String res = null;
        String titre = nomMag.replace("'", "''");
        String req = "SELECT idmag FROM MAGASIN WHERE nommag = '" + titre + "';";
        try {
            Statement st = laConnexion.createStatement();
            ResultSet rs = st.executeQuery(req);
            if (rs.next()) {
                res = rs.getString("idmag");
            }
            rs.close();
            st.close(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    public void modifQteLivre(Magasin mag, Livre livre, int qte){
        String req = "UPDATE POSSEDER SET qte = ? WHERE idmag = ? AND isbn = ?";
        try {
            PreparedStatement st = laConnexion.prepareStatement(req);
            st.setInt(1, qte);
            st.setString(2, mag.getIdmag());
            st.setString(3, livre.getIsbn());
            st.executeUpdate();
            System.out.println("Quantité du livre mise à jour avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la quantité du livre : " + e.getMessage());
        }            
    }
}



/*
    // liste livre du mag 
    // "select titre from livre where mag =" + magasin.nonmmag 

    public List<Livre> livresParMagasin(String idMag) throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = """
            SELECT l.isbn, l.titre, l.nbpages, l.datepubli, l.prix,
                   a.nomauteur,
                   e.nomedit,
                   p.qte
            FROM POSSEDER p
            JOIN LIVRE l ON p.isbn = l.isbn
            LEFT JOIN ECRIRE ec ON l.isbn = ec.isbn
            LEFT JOIN AUTEUR a ON ec.idauteur = a.idauteur
            LEFT JOIN EDITER ed ON l.isbn = ed.isbn
            LEFT JOIN EDITEUR e ON ed.idedit = e.idedit
            WHERE p.idmag = ? AND p.qte > 0
        """;
        // a.prenomauteur,,
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(sql)) {
            pst.setString(1, idMag);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String titre = rs.getString("titre");
                    int nbPages = rs.getInt("nbpages");
                    int datePubli = rs.getInt("datepubli");
                    double prix = rs.getDouble("prix");

                    String nomAuteur = rs.getString("nomauteur");
                    Auteur auteur = new Auteur(nomAuteur);

                    String nomEditeur = rs.getString("nomedit");
                    Editeur editeur = new Editeur(nomEditeur);

                    Livre livre = new Livre(titre, auteur, datePubli, nbPages, prix, editeur);
                    livres.add(livre);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la récupération des livres du magasin : " + e.getMessage(), e);
        }
        return livres;
    }

    public static List<Magasin> listeMagasins() throws SQLException {
    List<Magasin> magasins = new ArrayList<>();

    String sql = "SELECT nommag, villemag FROM MAGASIN ORDER BY idmag ";

    try (Statement st = ConnectionBD.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {
            String nomMag = rs.getString("nommag");
            String villeMag = rs.getString("villemag");

            Magasin magasin = new Magasin(nomMag, villeMag);
            magasins.add(magasin);
        }

    } catch (SQLException e) {
        throw new SQLException("Erreur lors de la récupération de la liste des magasins : " + e.getMessage(), e);
    }

    return magasins;
    }

    public Magasin idMagToMag(String idMag) throws SQLException {
        String sql = "SELECT nommag, villemag FROM MAGASIN WHERE idmag = ?";
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(sql)) {
            pst.setString(1, idMag);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String nom = rs.getString("nommag");
                    String ville = rs.getString("villemag");
                    return new Magasin(nom, ville);
                } else {
                    throw new SQLException("Aucun magasin trouvé avec l'ID : " + idMag);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la récupération du magasin : " + e.getMessage(), e);
        }
    }

    public int getNombreLivresVendusParMagasin(String idMag) throws SQLException {
        String sql = """
            SELECT SUM(dc.qte) AS total
            FROM COMMANDE c
            JOIN DETAILCOMMANDE dc ON c.numcom = dc.numcom
            WHERE c.idmag = ?
        """;

        
        try (PreparedStatement pst = laConnexion.prepareStatement(sql)) {
            pst.setString(1, idMag);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            } else {
                return 0;
            }
        }
    }

    public double getChiffreAffaireMagasin(String idMag) throws SQLException {
        String sql = """
            SELECT SUM(dc.qte * dc.prixvente) AS chiffre
            FROM COMMANDE c
            JOIN DETAILCOMMANDE dc ON c.numcom = dc.numcom
            WHERE c.idmag = ?
        """;

        try (PreparedStatement pst = laConnexion.prepareStatement(sql)) {
            pst.setString(1, idMag);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getDouble("chiffre");
            } else {
                return 0.0;
            }
        }
    }

    public String getLivreLePlusVendu(String idMag) throws SQLException {
        String sql = """
            SELECT titre, dc.isbn, SUM(dc.qte) AS total
            FROM COMMANDE c
            JOIN DETAILCOMMANDE dc ON c.numcom = dc.numcom 
            JOIN livre ON dc.isbn = livre.isbn
            WHERE c.idmag = ? 
            GROUP BY dc.isbn
            ORDER BY total DESC
            LIMIT 1
        """;

        try (PreparedStatement pst = laConnexion.prepareStatement(sql)) {
            pst.setString(1, idMag);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("titre");
            } else {
                return null;
            }
        }
    }
}
*/