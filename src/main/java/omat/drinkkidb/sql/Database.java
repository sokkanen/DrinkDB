
package omat.drinkkidb.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    
    private String databaseAddress;
    private Connection con;
    
    public Database(String databaseAddress){
        this.databaseAddress = databaseAddress;
    }
    
    public Connection connect() throws SQLException{
        this.con = DriverManager.getConnection(databaseAddress);
        return this.con;
    }
    
    public void disconnect() throws SQLException{
        this.con.close();
    }
}
