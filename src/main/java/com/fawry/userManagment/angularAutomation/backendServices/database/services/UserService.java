package com.fawry.userManagment.angularAutomation.backendServices.database.services;

import com.fawry.userManagment.angularAutomation.backendServices.database.DBConnection;
import com.fawry.userManagment.angularAutomation.backendServices.database.daos.RolesDAO;
import com.fawry.userManagment.angularAutomation.backendServices.database.daos.UserDAO;
import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.dataModels.RolesDM;
import com.fawry.userManagment.angularAutomation.dataModels.UsersDM;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserService {


    public UsersDM getUserDetails(String userMail) throws SQLException, ClassNotFoundException {
        //Open connection to users database
        DBConnection conn = new DBConnection();
        Connection connection = conn.openConnection(GeneralConstants.USERS_DB_NAME);
        UserDAO userDAO = new UserDAO();
        UsersDM usersDM = userDAO.getUserDetails(connection ,userMail );
        //close db connection
        conn.closeDBConnection(connection);
        return usersDM;
    }

    public ArrayList<UsersDM> getUsersList(UsersDM usersDM) throws SQLException, ClassNotFoundException {
        //Open connection to users database
        DBConnection conn = new DBConnection();
        Connection connection = conn.openConnection(GeneralConstants.USERS_DB_NAME);
        UserDAO userDAO = new UserDAO();
       ArrayList<UsersDM> usersDMS = userDAO.getUsersList(connection ,usersDM );
        //close db connection
        conn.closeDBConnection(connection);
        return usersDMS;
    }
}
