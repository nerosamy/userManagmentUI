package com.fawry.userManagment.angularAutomation.backendServices.database;

import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.utils.Log;
import com.fawry.userManagment.angularAutomation.utils.PropertiesFilesHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class DBConnection {

        public  Connection openConnection(String dbName) throws SQLException, ClassNotFoundException
        {
            Log.info("***********      Openning DB connection to " + dbName + "       **************");
            Connection con = null;
            // Create Connection to DataBase
            String dbUrl =null;
            String username =null;
            String password =null;
            PropertiesFilesHandler propHnadler = new PropertiesFilesHandler();
            Properties prop = propHnadler.loadPropertiesFile(GeneralConstants.DB_CONFIG_FILE_NAME);
            if (dbName.equalsIgnoreCase(GeneralConstants.USERS_DB_NAME)) {
                dbUrl = prop.getProperty(GeneralConstants.USERS_DB_URL_KEY);
                username = prop.getProperty(GeneralConstants.USERS_DB_USERNAME_KEY);
                password = prop.getProperty(GeneralConstants.USERS_DB_PASSWORD_KEY);
            }


            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection(dbUrl, username, password);

            Log.info("**********      DB Connection created successfully to " + dbName + "       ***********");

            return con;
        }



        public ArrayList<ArrayList<Object>> executeQuery(Connection conn, String sqlQuery) throws SQLException
        {
            Log.info(" ************    Executing DB Query   ************");
            Log.info(sqlQuery);
            ArrayList<ArrayList<Object>> queryResults = new ArrayList<ArrayList<Object>>();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlQuery);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while(rs.next()) {
                ArrayList<Object> row = new ArrayList<Object>();
                for (int i = 1; i < columnsNumber + 1; i++) {
                    Object columnValue = new Object();
                    columnValue = rs.getObject(i);
                    row.add(columnValue);
                }
                queryResults.add(row);
            }

            Log.info("************ DB Query executed successfully ***************");
            return queryResults;
        }

        public ResultSet executeQueryAndGetRS(Connection conn, String sqlQuery) throws SQLException
        {
            Log.info("*********      Executing DB Query     ************");
            Log.info(sqlQuery);

            Statement statement = conn.createStatement();;
            ResultSet rs =  statement.executeQuery(sqlQuery);

            Log.info("************     DB Query executed successfully     ***************");

            return rs;
        }


        public  void closeDBConnection(Connection con) throws SQLException {
            Log.info("******     Closing DB Connection      *******");
            con.close();
            Log.info("******      DB Connection closed successfully     *******");
        }

    }

