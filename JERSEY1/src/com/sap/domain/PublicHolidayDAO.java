package com.sap.domain;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import com.sap.domain.PublicHoliday;;

public class PublicHolidayDAO {
	
    private DataSource dataSource;

    /**
     * Create new data access object with data source.
     */
    public PublicHolidayDAO(DataSource newDataSource) throws SQLException {
        setDataSource(newDataSource);
    }

    /**
     * Get data source which is used for the database operations.
     */
    public DataSource getDataSource() {
        return dataSource;
    }
 
    /**
     * Set data source to be used for the database operations.
     */
    public void setDataSource(DataSource newDataSource) throws SQLException {
        this.dataSource = newDataSource; 
        checkTable();
    }
    
    public void deleteAllHolidays() throws SQLException{
        Connection connection = dataSource.getConnection(); 
        
        try {
            PreparedStatement pstmt = connection
                    .prepareStatement("DELETE T_HOLIDAYS");
            pstmt.executeUpdate();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }        
    }

    /**
     * Add a public holiday to the table.
     */
    public void addPublicHoliday(PublicHoliday holiday) throws SQLException {
        Connection connection = dataSource.getConnection(); 

        try {
            PreparedStatement pstmt = connection
                    .prepareStatement("INSERT INTO T_HOLIDAYS (ID, DATEX) VALUES (?, ?)");
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, holiday.getDate());
            pstmt.executeUpdate();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Get all public holidays from the table.
     */
    public List<PublicHoliday> selectAllHolidays() throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement pstmt = connection
                    .prepareStatement("SELECT ID, DATEX FROM T_HOLIDAYS ORDER BY DATEX ASC");
            ResultSet rs = pstmt.executeQuery();
            ArrayList<PublicHoliday> list = new ArrayList<PublicHoliday>();
            while (rs.next()) {
            	PublicHoliday p = new PublicHoliday(rs.getString(2), rs.getString(1));
                //p.setId(rs.getString(1));
                //p.setDate(rs.getString(2));
                list.add(p);
            }
            return list;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
    
    /**
     * Get all public holidays from the table.
     */
    public String getPublicHoliday(String date) throws SQLException {
        Connection connection = dataSource.getConnection();
        String sqlStatement = "SELECT ID, DATEX FROM T_HOLIDAYS";
        
        try {
            PreparedStatement pstmt = connection
                    .prepareStatement(sqlStatement);
            ResultSet rs = pstmt.executeQuery();
            
            
            while (rs.next()) {
            	if(rs.getString(2).equals(date))
            	{
            		return "true";
            	}
            }
            
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        
        return "false";
    }    

    /**
     * Check if the public holiday table already exists and create it if not.
     */
    private void checkTable() throws SQLException {
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            if (!existsTable(connection)) {
                createTable(connection);
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Check if the public holiday table already exists.
     */
    private boolean existsTable(Connection conn) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getTables(null, null, "T_HOLIDAYS", null);
        while (rs.next()) {
            String name = rs.getString("TABLE_NAME");
            if (name.equals("T_HOLIDAYS")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create the public holiday table.
     */
    private void createTable(Connection connection) throws SQLException {
        PreparedStatement pstmt = connection
                .prepareStatement("CREATE TABLE T_HOLIDAYS "
                        + "(ID VARCHAR(255) PRIMARY KEY NOT NULL, "
                        + "DATEX VARCHAR (10))");
        pstmt.executeUpdate();
    }	

}
