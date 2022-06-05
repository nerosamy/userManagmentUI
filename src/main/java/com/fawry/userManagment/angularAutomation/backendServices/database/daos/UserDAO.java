package com.fawry.userManagment.angularAutomation.backendServices.database.daos;

import com.fawry.userManagment.angularAutomation.backendServices.database.DBConnection;
import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.constants.database.RolesDBTable;
import com.fawry.userManagment.angularAutomation.constants.database.UserDBTable;
import com.fawry.userManagment.angularAutomation.dataModels.RolesDM;
import com.fawry.userManagment.angularAutomation.dataModels.UsersDM;
import com.fawry.userManagment.angularAutomation.utils.PropertiesFilesHandler;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class UserDAO {

    public UsersDM getUserDetails(Connection dbConn, String userEmail) throws SQLException {

        PropertiesFilesHandler propHandler = new PropertiesFilesHandler();
        Properties generalCofigsProps = propHandler.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);
        String beMail = generalCofigsProps.getProperty(GeneralConstants.VALID_MAIL);

        //Create DB Query to selected added/updated Category
        StringBuilder query = new StringBuilder();
        query.append("select u.email as EMAIL , u.mobile_number  as PHONE," +
                " r.name_en as ROLE_NAME, b.name_en as BRANCH , u.status as STATUS ," +
                " u.must_change_password as MUST_CHANGE_PASSWORD ," +
                " u.password_never_expire as PASSWORD_NEVER_EXPIRE " +
                " from auth_user u " +
                " left join BUSINESS_ENTITY.branch b on b.code = u.branch_code " +
                " left join user_role ur on ur.user_id = u.id " +
                " left join role r on r.id = ur.role_id " +
                " left join business_entity be on be.id  = u.business_entity_id " +
                " where be.email = '"+beMail+"' " +
                " and u.email='"+userEmail+"' " +
                " and r.name_en <> 'Super admin'");

        // Execute query
        DBConnection conn = new DBConnection();
        ResultSet userRS = conn.executeQueryAndGetRS(dbConn, query.toString());

        // fill data returned from DB into data model
        UsersDM usersDM = null;

        if (userRS.next())
        {
            usersDM = new UsersDM();
            usersDM.setEmail(userRS.getString(UserDBTable.EMAIL)==null?"":userRS.getString(UserDBTable.EMAIL));
            usersDM.setPhone(userRS.getString(UserDBTable.PHONE)==null?"":userRS.getString(UserDBTable.PHONE));
            usersDM.setRole(userRS.getString(UserDBTable.ROLE_NAME)==null?"":userRS.getString(UserDBTable.ROLE_NAME));
            usersDM.setBranch(userRS.getString(UserDBTable.BRANCH)==null?"":userRS.getString(UserDBTable.BRANCH));
            usersDM.setStatus(userRS.getString(UserDBTable.STATUS)==null?"":userRS.getString(UserDBTable.STATUS));
            usersDM.setMustChangePassword(userRS.getString(UserDBTable.MUST_CHANGE_PASSWORD)==null?"":userRS.getString(UserDBTable.MUST_CHANGE_PASSWORD));
            usersDM.setPasswordNeverExpire(userRS.getString(UserDBTable.PASSWORD_NEVER_EXPIRE)==null?"":userRS.getString(UserDBTable.PASSWORD_NEVER_EXPIRE));

        }
        return usersDM;
    }

    public ArrayList<UsersDM> getUsersList(Connection dbConn, UsersDM searchDM) throws SQLException {

        PropertiesFilesHandler propHandler = new PropertiesFilesHandler();
        Properties generalConfigsProps = propHandler.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);
        String beMail = generalConfigsProps.getProperty(GeneralConstants.VALID_MAIL);

        //Create DB Query to selected added/updated Category
        StringBuilder query = new StringBuilder();
        query.append("select be.id ,u.email as EMAIL ," +
                " u.mobile_number  as PHONE,r.name_en as ROLE_NAME," +
                " b.name_en as BRANCH , u.status as STATUS ," +
                " u.must_change_password as MUST_CHANGE_PASSWORD ," +
                " u.password_never_expire as PASSWORD_NEVER_EXPIRE " +
                " from auth_user u " +
                " left join BUSINESS_ENTITY.branch b on b.code = u.branch_code " +
                " left join user_role ur on ur.user_id = u.id " +
                " left join role r on r.id = ur.role_id " +
                " left join business_entity be on be.id  = u.business_entity_id " +
                " where be.email = '"+beMail+"' " +
                " and u.status <> 'DELETED' " +
                " and r.name_en <> 'Super admin'");

        if (searchDM != null) {
            if (searchDM.getEmail() != "") {
                query.append("and u.email = '" + searchDM.getEmail() + "'");
            }

            if (searchDM.getRole() != "") {
                query.append("and r.name_en = '" + searchDM.getRole() + "'");
            }

            if (searchDM.getStatus() != "") {
                query.append("and  u.status = '" + searchDM.getStatus() + "'");
            }
        }
        // Execute query
        DBConnection conn = new DBConnection();
        ResultSet userRS = conn.executeQueryAndGetRS(dbConn, query.toString());

        // fill data returned from DB into data model
        ArrayList<UsersDM> usersDMS = new ArrayList<>();

        UsersDM usersDM = null;

        while (userRS.next())
        {
            usersDM = new UsersDM();
            usersDM.setEmail(userRS.getString(UserDBTable.EMAIL)==null?"":userRS.getString(UserDBTable.EMAIL));
            usersDM.setPhone(userRS.getString(UserDBTable.PHONE)==null?"":userRS.getString(UserDBTable.PHONE));
            usersDM.setRole(userRS.getString(UserDBTable.ROLE_NAME)==null?"":userRS.getString(UserDBTable.ROLE_NAME));
            usersDM.setStatus(userRS.getString(UserDBTable.STATUS)==null?"":userRS.getString(UserDBTable.STATUS));
            usersDMS.add(usersDM);
        }
        return usersDMS;
    }

}
