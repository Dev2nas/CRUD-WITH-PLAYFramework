
package services;

import Models.Personne;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PersonneService {

    private final Connection connection;

    // Constructeur de la classe qui initialise la connexion à la base de données
    public PersonneService() {
        connection = new Provider().dbconnect();
    }

    // Méthode pour ajouter une personne à la base de données
    public void addPersonne(Personne personne) {
        try {
            String req = "INSERT INTO personnel(nom, prenom, sexe, age, telephone, profession, email) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, personne.getNom());
            preparedStatement.setString(2, personne.getPrenom());
            preparedStatement.setString(3, String.valueOf(personne.getSexe()));
            preparedStatement.setInt(4, personne.getAge());
            preparedStatement.setString(5, personne.getTelephone());
            preparedStatement.setString(6, personne.getProfession());
            preparedStatement.setString(7, personne.getEmail());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour récupérer la liste des personnes depuis la base de données
    public List<Personne> ListePersonne() throws ClassNotFoundException, SQLException {
        List<Personne> ListePersonne = new ArrayList<Personne>();

        Personne personne = new Personne();
        // Les résultats de la requête sont stockés dans l'objet result
        String sql = "SELECT * FROM personnel";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {

            personne.setId(resultSet.getInt("id"));
            personne.setNom(resultSet.getString("nom"));
            personne.setPrenom(resultSet.getString("prenom"));
            personne.setSexe(resultSet.getString("sexe").charAt(0));
            personne.setAge(resultSet.getInt("age"));
            personne.setTelephone(resultSet.getString("telephone"));
            personne.setProfession(resultSet.getString("profession"));
            personne.setEmail(resultSet.getString("email"));
            ListePersonne.add(personne);
            // On vide l'objet personne avant de repasser la boucle
            personne = new Personne();
        }
        // Retourne la liste constituée
        return ListePersonne;
    }

    // Méthode pour rechercher des personnes dans la base de données en fonction d'un mot-clé
    public List<Personne> rechercher(String mot) throws SQLException, ClassNotFoundException {
        List<Personne> personnes = new ArrayList<>();

        String sql = "SELECT * FROM personnel WHERE nom LIKE ? OR prenom LIKE ? OR telephone LIKE ? OR profession LIKE ?";
        PreparedStatement prepareStat = this.connection.prepareStatement(sql);
        prepareStat.setString(1, "%" + mot + "%");
        prepareStat.setString(2, "%" + mot + "%");
        prepareStat.setString(3, "%" + mot + "%");
        prepareStat.setString(4, "%" + mot + "%");

        ResultSet resultSet = prepareStat.executeQuery();

        while (resultSet.next()) {
            Personne personne = new Personne();
            personne.setNom(resultSet.getString("nom"));
            personne.setPrenom(resultSet.getString("prenom"));
            personne.setTelephone(resultSet.getString("telephone"));
            personne.setProfession(resultSet.getString("profession"));
            personnes.add(personne);
        }

        return personnes;
    }

    // Méthode pour supprimer une personne en fonction de son ID
    public String delete(int id) throws SQLException, ClassNotFoundException {
        PreparedStatement prepareStat = this.connection.prepareStatement("DELETE FROM personnel WHERE id=? ");
        prepareStat.setInt(1, id);

        int rowsDeleted = prepareStat.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Personnel a été supprimé avec succès!");
            return "OK";
        } else
            return "Une erreur est survenue lors de la suppression";
    }
    public Personne findByID(int id) throws SQLException, ClassNotFoundException {

        Personne personne = new Personne();
        PreparedStatement prepareStat = this.connection.prepareStatement("SELECT * FROM personnel WHERE id =? ");
        prepareStat.setInt(1, id);

        ResultSet resultSet = prepareStat.executeQuery();

        if (resultSet.next()) {
            personne.setId(Integer.parseInt(resultSet.getString("id").replace(" ", "")));
            personne.setNom(resultSet.getString("nom"));
            personne.setPrenom(resultSet.getString("prenom"));
            personne.setSexe(resultSet.getString("sexe").charAt(0));
            personne.setAge(resultSet.getInt("age"));
            personne.setTelephone(resultSet.getString("telephone"));
            personne.setProfession(resultSet.getString("profession"));
            personne.setEmail(resultSet.getString("email"));

        }

        return personne;

    }
    // Méthode pour mettre à jour les informations d'une personne
    public String update(Personne personne) throws SQLException {
        try {
            String sql = "UPDATE personnel SET nom=?, prenom=?, sexe=?, age=?, telephone=?, profession=?, email=? WHERE id=?";
            PreparedStatement prepareStat = this.connection.prepareStatement(sql);
            prepareStat.setString(1, personne.getNom());
            prepareStat.setString(2, personne.getPrenom());
            prepareStat.setString(3, personne.getSexe() + "");
            prepareStat.setInt(4, personne.getAge());
            prepareStat.setString(5, personne.getTelephone());
            prepareStat.setString(6, personne.getProfession());
            prepareStat.setString(7, personne.getEmail());
            prepareStat.setInt(8, personne.getId());

            int rowsUpdated = prepareStat.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Modifié avec succès!");
            }
            return "modifier";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}

