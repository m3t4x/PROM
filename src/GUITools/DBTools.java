package GUITools;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBTools {

    public static void InsertData(String tableName, Map<String, String> columnData) throws SQLException {
            MySQLConnector conn = MySQLConnector.getInstance();
            String columnNames = String.join(", ", columnData.keySet());
            String placeholders = String.join(", ", java.util.Collections.nCopies(columnData.size(), "?"));

            String query = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + placeholders + ")";
            PreparedStatement statement = conn.getConnection().prepareStatement(query);

            int parameterIndex = 1;
            for (String value : columnData.values()) {
                statement.setString(parameterIndex++, value);
            }

            statement.executeUpdate();
    }

    public static void InsertSessionData(HashMap<String, Object> sessionData) throws SQLException {
        String query = "INSERT INTO sessions (id_cours, nb_heures, date) VALUES (?, ?, ?)";
        PreparedStatement statement = MySQLConnector.getInstance().getConnection().prepareStatement(query);

        // Retrieve data from the HashMap
        int idCours = (int) sessionData.get("id_cours");
        int nbHeures = (int) sessionData.get("nb_heures");
        Date date = (Date) sessionData.get("date");

        // Set the values in the prepared statement
        statement.setInt(1, idCours);
        statement.setInt(2, nbHeures);
        statement.setDate(3, date);

        // Execute the query
        statement.executeUpdate();

        // Close the statement
        statement.close();
    }
    public static String[][] RetrieveSessionData() throws SQLException {
        String query = "SELECT * FROM sessions";

        PreparedStatement statement = MySQLConnector.getInstance().getConnection().prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        List<String[]> dataList = new ArrayList<>();

        while (resultSet.next()) {
            int idSession = resultSet.getInt("id_session");
            int idCours = resultSet.getInt("id_cours");
            float nbHeures = resultSet.getFloat("nb_heures");
            String date = resultSet.getDate("date").toString();

            // Retrieve additional data from related tables
            String nomMatiere = retrieveNomMatiere(idCours);
            String nomEnseignant = retrieveNomEnseignant(idCours);
            String nomGroupe = retrieveNomGroupe(idCours);

            String[] rowData = {String.valueOf(idSession), nomMatiere, nomEnseignant, nomGroupe, String.valueOf(nbHeures), date};
            dataList.add(rowData);
        }

        resultSet.close();
        statement.close();

        // Convert the list to a 2D String array
        int rowCount = dataList.size();
        int columnCount = 6; // Number of columns in the result set
        String[][] data = new String[rowCount][columnCount];

        for (int i = 0; i < rowCount; i++) {
            data[i] = dataList.get(i);
        }

        return data;
    }

    private static String retrieveNomMatiere(int idCours) throws SQLException {
        String query = "SELECT m.nom_matiere " +
                "FROM cours c " +
                "JOIN matieres m ON c.id_matiere = m.id_matiere " +
                "WHERE c.id_cours = ?";

        PreparedStatement statement = MySQLConnector.getInstance().getConnection().prepareStatement(query);
        statement.setInt(1, idCours);
        ResultSet resultSet = statement.executeQuery();

        String nomMatiere = "";

        if (resultSet.next()) {
            nomMatiere = resultSet.getString("nom_matiere");
        }

        resultSet.close();
        statement.close();

        return nomMatiere;
    }

    private static String retrieveNomEnseignant(int idCours) throws SQLException {
        String query = "SELECT e.nom, e.prenom " +
                "FROM cours c " +
                "JOIN enseignants e ON c.id_enseignant = e.id_enseignant " +
                "WHERE c.id_cours = ?";

        PreparedStatement statement = MySQLConnector.getInstance().getConnection().prepareStatement(query);
        statement.setInt(1, idCours);
        ResultSet resultSet = statement.executeQuery();

        String nomEnseignant = "";

        if (resultSet.next()) {
            String nom = resultSet.getString("nom");
            String prenom = resultSet.getString("prenom");
            nomEnseignant = nom + " " + prenom;
        }

        resultSet.close();
        statement.close();

        return nomEnseignant;
    }

    private static String retrieveNomGroupe(int idCours) throws SQLException {
        String query = "SELECT g.nom_groupe " +
                "FROM cours c " +
                "JOIN groupes g ON c.id_groupe = g.id_groupe " +
                "WHERE c.id_cours = ?";

        PreparedStatement statement = MySQLConnector.getInstance().getConnection().prepareStatement(query);
        statement.setInt(1, idCours);
        ResultSet resultSet = statement.executeQuery();

        String nomGroupe = "";

        if (resultSet.next()) {
            nomGroupe = resultSet.getString("nom_groupe");
        }

        resultSet.close();
        statement.close();

        return nomGroupe;
    }

    public static void AddGroup(String groupName) throws SQLException {
        MySQLConnector conn = MySQLConnector.getInstance();
        String query = "INSERT INTO groupes (nom_groupe) VALUES (?)";
        PreparedStatement statement = conn.getConnection().prepareStatement(query);
        statement.setString(1, groupName);
        statement.executeUpdate();
    }
    public static List<List<String>> GetGroupes() throws SQLException {
        List<List<String>> groupesData = new ArrayList<>();
        MySQLConnector conn = MySQLConnector.getInstance();
            String query = "SELECT * FROM groupes";
            Statement statement = conn.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                List<String> rowData = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    String value = resultSet.getString(i);
                    rowData.add(value);
                }
                groupesData.add(rowData);
            }

        return groupesData;
    }
    public static String[][] GetAllData(String tablename) throws SQLException {
        Statement statement = MySQLConnector.getInstance().getConnection().createStatement();
        String query = "SELECT * FROM " + tablename;
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetMetaData metaData = resultSet.getMetaData();

        List<List<String>> data = new ArrayList<>();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            List<String> row = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(resultSet.getString(i));
            }
            data.add(row);
        }

        statement.close();
        String[][] dataArray = new String[data.size()][columnCount];
        for (int i = 0; i < data.size(); i++) {
            List<String> row = data.get(i);
            for (int j = 0; j < row.size(); j++) {
                dataArray[i][j] = row.get(j);
            }
        }

        return dataArray;
    }
    public static ArrayList<String> GetColumnNames(String tableName) throws SQLException {
        ArrayList<String> columnNames = new ArrayList<>();
        DatabaseMetaData meta = MySQLConnector.getInstance().getConnection().getMetaData();
        ResultSet rs = meta.getColumns(null, null, tableName, null);
        while (rs.next()) {
            String columnName = rs.getString("COLUMN_NAME");
            columnNames.add(columnName);
        }
        return columnNames;
    }

    public static String GetGroupName(int id) throws SQLException {
        String groupName = null;
        String query = "SELECT nom_groupe FROM groupes WHERE id_groupe = ?";
        PreparedStatement statement = MySQLConnector.getInstance().getConnection().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            groupName = resultSet.getString("nom_groupe");
        }
        return groupName;
    }

    public static int GetGroupCount(int groupId) throws SQLException {
            int rowCount = 0;
            String query = "SELECT COUNT(*) AS row_count FROM etudiants WHERE id_groupe = ?";
            PreparedStatement statement = MySQLConnector.getInstance().getConnection().prepareStatement(query);
            statement.setInt(1, groupId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                rowCount = resultSet.getInt("row_count");
            }
        return rowCount;
    }
    public static boolean CheckCredentials(String tableName, String email, String password) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE email = ? AND mdp = ?";
        PreparedStatement statement = MySQLConnector.getInstance().getConnection().prepareStatement(query);
        statement.setString(1, email);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        boolean isValid = resultSet.next();
        resultSet.close();
        statement.close();
        return isValid;
    }
    public static String[][] GetCours() throws SQLException {
        String query = "SELECT c.id_cours, m.nom_matiere, m.nb_heures, e.nom, e.prenom, g.nom_groupe " +
                "FROM cours c " +
                "JOIN matieres m ON c.id_matiere = m.id_matiere " +
                "JOIN enseignants e ON c.id_enseignant = e.id_enseignant " +
                "JOIN groupes g ON c.id_groupe = g.id_groupe";
        PreparedStatement statement = MySQLConnector.getInstance().getConnection().prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        List<String[]> coursList = new ArrayList<>();
        while (resultSet.next()) {
            int idCours = resultSet.getInt("id_cours");
            String nomMatiere = resultSet.getString("nom_matiere");
            int nbHeures = resultSet.getInt("nb_heures");
            String enseignantNom = resultSet.getString("nom");
            String enseignantPrenom = resultSet.getString("prenom");
            String groupeNom = resultSet.getString("nom_groupe");

            // Create an array to hold the data for each row
            String[] row = new String[5];
            row[0] = String.valueOf(idCours);
            row[1] = nomMatiere;
            row[2] = enseignantNom + " " + enseignantPrenom;
            row[3] = Integer.toString(nbHeures);
            row[4] = groupeNom;

            // Add the row to the list
            coursList.add(row);
        }

        resultSet.close();
        statement.close();

        // Convert the list of rows to a 2D array
        String[][] coursArray = new String[coursList.size()][5];
        for (int i = 0; i < coursList.size(); i++) {
            coursArray[i] = coursList.get(i);
        }

        return coursArray;
    }
    public static String[][] GetCoursData() throws SQLException {
        String query = "SELECT c.id_cours, m.nom_matiere, e.nom, e.prenom, g.nom_groupe " +
                "FROM cours c " +
                "JOIN matieres m ON c.id_matiere = m.id_matiere " +
                "JOIN enseignants e ON c.id_enseignant = e.id_enseignant " +
                "JOIN groupes g ON c.id_groupe = g.id_groupe " +
                "ORDER BY c.id_cours ASC";

        PreparedStatement statement = MySQLConnector.getInstance().getConnection().prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        // Create a list to store the retrieved data
        List<String[]> dataList = new ArrayList<>();

        // Iterate over the result set
        while (resultSet.next()) {
            int idCours = resultSet.getInt("id_cours");
            String nomMatiere = resultSet.getString("nom_matiere");
            String enseignantNom = resultSet.getString("nom");
            String enseignantPrenom = resultSet.getString("prenom");
            String groupeNom = resultSet.getString("nom_groupe");

            // Create an array to store the row data
            String[] rowData = {String.valueOf(idCours), nomMatiere, enseignantNom, enseignantPrenom, groupeNom};
            dataList.add(rowData);
        }

        // Close the result set and statement
        resultSet.close();
        statement.close();

        // Convert the list to a 2D String array
        String[][] data = new String[dataList.size()][5];
        for (int i = 0; i < dataList.size(); i++) {
            data[i] = dataList.get(i);
        }

        // Return the 2D String array
        return data;
    }
    public static void DeleteRow(String tableName, int rowId) throws SQLException {
        String idName = null;
        switch (tableName) {
            case "etudiants" -> {
                idName = "id_etud";
            }
            case "groupes" -> {
                idName = "id_groupe";
            }
            case "utilisateurs" -> {
                idName = "id_util";
            }
            case "enseignants" -> {
                idName = "id_enseignant";
            }
            case "matieres" -> {
                idName = "id_matiere";
            }
            case "cours" -> {
                idName = "id_cours";
            }
            case "sessions" -> {
                idName = "id_session";
            }
        }
        String query = "DELETE FROM " + tableName + " WHERE "+idName+" = ?";
        PreparedStatement statement = MySQLConnector.getInstance().getConnection().prepareStatement(query);
        statement.setInt(1, rowId);
        statement.executeUpdate();
        statement.close();
        System.out.println("done");
    }
}
