/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package line98;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author prdox
 */
public class DbConnection {
    private static DbConnection _instance = null;
    
    public Connection connect;
    
    private DbConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:score.db");
            Statement stm = connect.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS score" +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "score INTEGER           NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
            stm.executeUpdate(sql);
            stm.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
    
    public void insertScore(int score) {
        try {
            Statement stm = connect.createStatement();
            String sql = "INSERT INTO score (score) " +
                    "VALUES (" + String.valueOf(score) + ");";
            stm.executeUpdate(sql);
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Integer> getTopScore() {
        List<Integer> topScore = new ArrayList<>();
        try {
            Statement stm = connect.createStatement();
            ResultSet rs = stm.executeQuery( "SELECT * FROM score ORDER BY score DESC LIMIT 5;" );
            while ( rs.next() ) {
                topScore.add(rs.getInt("score"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return topScore;
    }
    
    public List<String> getTopScoreWithDate() {
        List<String> scores = new ArrayList<>();
        try {
            Statement stm = connect.createStatement();
            ResultSet rs = stm.executeQuery( "SELECT * FROM score ORDER BY score DESC LIMIT 5;" );
            while ( rs.next() ) {
                int score = rs.getInt("score");
                String time = rs.getString("created_at");
                scores.add(String.valueOf(score) + ": " + time);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return  scores;
    }
    
    public static DbConnection getInstance() {
        if (_instance == null) {
            _instance = new DbConnection();
        }
        return _instance;
    }
}
