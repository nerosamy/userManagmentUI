package com.fawry.userManagment.angularAutomation.backendServices.database.services;

import com.fawry.userManagment.angularAutomation.backendServices.database.DBConnection;
import com.fawry.userManagment.angularAutomation.backendServices.database.daos.RolesDAO;
import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.dataModels.RolesDM;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class RolesService {


    public ArrayList<RolesDM> getRolesDetails(String roleName) throws SQLException, ClassNotFoundException {
        //Open connection to users database
        DBConnection conn = new DBConnection();
        Connection connection = conn.openConnection(GeneralConstants.USERS_DB_NAME);
        RolesDAO rolesDAO = new RolesDAO();
        ArrayList<RolesDM> rolesDMS = rolesDAO.getRolesDetails(connection , roleName);
        //close db connection
        conn.closeDBConnection(connection);
        return rolesDMS;
    }


    public ArrayList<RolesDM> getRolesList(RolesDM searchDM) throws SQLException, ClassNotFoundException {
        //Open connection to users database
        DBConnection conn = new DBConnection();
        Connection connection = conn.openConnection(GeneralConstants.USERS_DB_NAME);
        RolesDAO rolesDAO = new RolesDAO();
        ArrayList<RolesDM> rolesDMS = rolesDAO.getRolesList(connection , searchDM);
        //close db connection
        conn.closeDBConnection(connection);
        return rolesDMS;
    }


    public String getNotAssignedRoleName() throws SQLException, ClassNotFoundException {
        //Open connection to users database
        DBConnection conn = new DBConnection();
        Connection connection = conn.openConnection(GeneralConstants.USERS_DB_NAME);
        RolesDAO rolesDAO = new RolesDAO();
        String roleName = rolesDAO.getNotAssignedRoleName(connection);
        //close db connection
        conn.closeDBConnection(connection);
        return roleName;
    }


    public String getAssignedRoleName() throws SQLException, ClassNotFoundException {
        //Open connection to users database
        DBConnection conn = new DBConnection();
        Connection connection = conn.openConnection(GeneralConstants.USERS_DB_NAME);
        RolesDAO rolesDAO = new RolesDAO();
        String roleName = rolesDAO.getAssignedRoleName(connection);
        //close db connection
        conn.closeDBConnection(connection);
        return roleName;
    }


    public String getRoleStatus(String roleName) throws SQLException, ClassNotFoundException {
        //Open connection to users database
        DBConnection conn = new DBConnection();
        Connection connection = conn.openConnection(GeneralConstants.USERS_DB_NAME);
        RolesDAO rolesDAO = new RolesDAO();
        String status = rolesDAO.getRoleStatus(connection , roleName);
        //close db connection
        conn.closeDBConnection(connection);
        return status;
    }
}
