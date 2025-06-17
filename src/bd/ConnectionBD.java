package bd;
import java.sql.*;
import java.util.*;
import java.io.*;

public class ConnectionBD {
    private static Connection connexion = null;

    public static Connection getConnection() {
        if (connexion != null) return connexion;

        Properties props = new Properties();
        try (FileReader reader = new FileReader("./INFOOBLIGATOIRE.txt")) {
            props.load(reader);
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture de INFOOBLIGATOIRE.txt : " + e.getMessage());
            return null;
        }

        String host = props.getProperty("host");
        String port = props.getProperty("port");
        String database = props.getProperty("database");
        String user = props.getProperty("user");
        String password = props.getProperty("password");

        String url = "jdbc:mariadb://" + host + ":" + port + "/" + database;

        try {
            connexion = DriverManager.getConnection(url, user, password);
            System.out.println("Connecté à la base de données");
            return connexion;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
            return null;
        }
    }

    public static Statement createStatement() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                return conn.createStatement();
            } else {
                System.out.println("Connexion non disponible pour créer un Statement.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création du Statement : " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Connexion réussie !");
        } else {
            System.out.println("Échec de la connexion.");
        }
    }
}