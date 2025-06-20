package bd;
import Modele.*;
import java.sql.*;
import java.util.*;

public class AdministrateurBD {
    Connection laConnexion;
	Statement st;
	public AdministrateurBD(Connection laConnexion){
		this.laConnexion=laConnexion;
	}

    public boolean seconnecterAdmin(String login, String mdp) throws SQLException {
        String sql = "SELECT * FROM ADMINISTRATEUR WHERE prenom = ? AND motDePasse = ?";
        try (PreparedStatement ps = laConnexion.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, mdp);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Si un résultat est trouvé, la connexion est réussie
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la connexion de l'administrateur : " + e.getMessage(), e);
        }
        
    }

    public Administrateur getAdministrateur(String login, String mdp) throws SQLException {
        String sql = "SELECT * FROM ADMINISTRATEUR WHERE prenom = ? AND motDePasse = ?";
        PreparedStatement ps = laConnexion.prepareStatement(sql);
            ps.setString(1, login);
            ps.setString(2, mdp);
            ResultSet rs = ps.executeQuery();
            Administrateur admin = null;
            while (rs.next()) {
                admin = new Administrateur(rs.getString("idAdmin"),rs.getString("nom"), rs.getString("prenom"), rs.getString("motDePasse"));
            }
            rs.close(); 
            return admin;
    }

	public void creerVendeur(Vendeur vendeur, Magasin magasin) throws SQLException {
        VendeurBD vendeurBD = new VendeurBD(laConnexion);
        vendeurBD.insertVendeur(vendeur);
    }   

    public void ajouteMagasin(String idMag, String nomMag, String villeMag) throws SQLException {
        Statement st = laConnexion.createStatement();
        String req = "INSERT INTO MAGASIN VALUES ('" +
            idMag + "', '" +
            nomMag + "', '" +
            villeMag + "');";
        st.executeQuery(req); 
        st.close();
    }

    public void ajouterLivreAuMagasin(Livre livre, Magasin magasin, int qte) throws SQLException {
        Statement st = laConnexion.createStatement();
        String req = "INSERT INTO POSSEDER VALUES ('" +
            magasin.getIdmag() + "', '" +
            livre.getIsbn() + "', " +
            qte + ");";
        st.executeQuery(req); 
        st.close();
    } 

    public void modifierPrixLivre(String isbn, double nouveauPrix) throws SQLException {
        String sql = "UPDATE LIVRE SET prix = ? WHERE isbn = ?";
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
            ps.setDouble(1, nouveauPrix);
            ps.setString(2, isbn);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Aucun livre trouvé avec l'ISBN : " + isbn);
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la modification du prix du livre (ISBN=" + isbn + ") : " + e.getMessage(), e);
        }
    }

    public void modifierQuantiteLivreMagasin(String idMag, String isbn, int nouvelleQte) throws SQLException {
        String sql = "UPDATE POSSEDER SET qte = ? WHERE idmag = ? AND isbn = ?";
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
            ps.setInt(1, nouvelleQte);
            ps.setString(2, idMag);
            ps.setString(3, isbn);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Aucun enregistrement POSSEDER trouvé pour magasin=" + idMag + " et livre=" + isbn);
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la modification de la quantité du livre dans le magasin : " + e.getMessage(), e);
        }
    }

    public void supprimerLivreDuMagasin(String idMag, String isbn) throws SQLException {
        String sql = "DELETE FROM POSSEDER WHERE idmag = ? AND isbn = ?";
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
            ps.setString(1, idMag);
            ps.setString(2, isbn);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Aucun enregistrement POSSEDER supprimé : ce livre n'existe pas dans ce magasin.");
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la suppression du livre du magasin : " + e.getMessage(), e);
        }
    }

    public Map<String, Integer> consulterStatistiquesVentes() throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT isbn, SUM(qte) AS totalVentes FROM DETAILCOMMANDE GROUP BY isbn";
        try (Statement st = ConnectionBD.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                int totalVentes = rs.getInt("totalVentes");
                stats.put(isbn, totalVentes);
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la récupération des statistiques de vente : " + e.getMessage(), e);
        }
        return stats;
    }
}