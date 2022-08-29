package com.fawry.userManagment.angularAutomation.backendServices.database.daos;

import com.fawry.userManagment.angularAutomation.backendServices.database.DBConnection;
import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.constants.database.RolesDBTable;
import com.fawry.userManagment.angularAutomation.dataModels.RolesDM;
import com.fawry.userManagment.angularAutomation.utils.PropertiesFilesHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class RolesDAO {


    public ArrayList<RolesDM> getRolesDetails(Connection dbConn, String roleName) throws SQLException {

        //Create DB Query to selected added/updated Category
        StringBuilder query = new StringBuilder();
        query.append("Select r.name_en as NAME_EN , r.name_ar as NAME_AR ," +
                " r.description as DESCRIPTION , p.name_en as PERMISSION_NAME " +
                " from Role r left join role_permission rp on rp.role_id=r.id " +
                " left join permission p on p.id= rp.permission_id " +
                " where r.name_en = '"+roleName+"'");

        // Execute query
        DBConnection conn = new DBConnection();
        ResultSet businessEntityRS = conn.executeQueryAndGetRS(dbConn, query.toString());

        // fill data returned from DB into data model
        ArrayList<RolesDM> rolesDMS = new ArrayList<>();
        RolesDM rolesDM = null;

        while (businessEntityRS.next())
        {
            rolesDM = new RolesDM();
            rolesDM.setEnglishName(businessEntityRS.getString(RolesDBTable.NAME_EN)==null?"":businessEntityRS.getString(RolesDBTable.NAME_EN));
            rolesDM.setArabicName(businessEntityRS.getString(RolesDBTable.NAME_AR)==null?"":businessEntityRS.getString(RolesDBTable.NAME_AR));
            rolesDM.setDescription(businessEntityRS.getString(RolesDBTable.DESCRIPTION)==null?"":businessEntityRS.getString(RolesDBTable.DESCRIPTION));
            rolesDM.setPermissions(businessEntityRS.getString(RolesDBTable.PERMISSION_NAME)==null?"":businessEntityRS.getString(RolesDBTable.PERMISSION_NAME));

            rolesDMS.add(rolesDM);
        }
        return rolesDMS;
    }

    public ArrayList<RolesDM> getRolesList(Connection dbConn, RolesDM searchDM) throws SQLException {

        //Create DB Query to selected added/updated Category
        StringBuilder query = new StringBuilder();
        PropertiesFilesHandler propHandler = new PropertiesFilesHandler();
        Properties generalCofigsProps = propHandler.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);
        String userMail = generalCofigsProps.getProperty(GeneralConstants.VALID_MAIL);
        query.append("select DISTINCT r.id , r.name_en as NAME_EN ," +
                " r.name_ar as NAME_AR , r.description as DESCRIPTION " +
                " from Role r left join role_permission rp on rp.role_id=r.id " +
                " left join permission p on p.id= rp.permission_id " +
                " left join business_entity be on be.id = r.business_entity_id" +
                " where be.email='"+userMail+"' " +
                " and r.name_en <>'Super admin' and r.name_ar <> 'Super admin'" +
                " and r.active <> '0'");

        if(searchDM.getEnglishName() != "")
        {
            query.append("and r.name_ar like '%"+searchDM.getEnglishName()+"%'");
        }

        if(searchDM.getPermissions() != "")
        {
            query.append("and p.name_en='"+searchDM.getPermissions()+"'");
        }

        // Execute query
        DBConnection conn = new DBConnection();
        ResultSet businessEntityRS = conn.executeQueryAndGetRS(dbConn, query.toString());

        // fill data returned from DB into data model
        ArrayList<RolesDM> rolesDMS = new ArrayList<>();
        RolesDM rolesDM = null;

        while (businessEntityRS.next())
        {
            rolesDM = new RolesDM();
            rolesDM.setEnglishName(businessEntityRS.getString(RolesDBTable.NAME_EN)==null?"":businessEntityRS.getString(RolesDBTable.NAME_EN));
            rolesDM.setDescription(businessEntityRS.getString(RolesDBTable.DESCRIPTION)==null?"":businessEntityRS.getString(RolesDBTable.DESCRIPTION));

            rolesDMS.add(rolesDM);
        }
        return rolesDMS;
    }

    public String getNotAssignedRoleName(Connection dbConn) throws SQLException {

        //Create DB Query to selected added/updated Category
        StringBuilder query = new StringBuilder();
        PropertiesFilesHandler propHandler = new PropertiesFilesHandler();
        Properties generalCofigsProps = propHandler.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);
        String userMail = generalCofigsProps.getProperty(GeneralConstants.VALID_MAIL);
        query.append("Select r.name_en as NAME_EN  " +
                " from Role r " +
                " left join business_entity be on be.id = r.business_entity_id " +
                " where r.id not in (select ur.role_id from user_role ur) " +
                " and be.email='"+userMail+"' " +
                " and r.name_en <> 'Super admin'" +
                " and r.active <> '0'");

        // Execute query
        DBConnection conn = new DBConnection();
        ResultSet businessEntityRS = conn.executeQueryAndGetRS(dbConn, query.toString());

        // fill data returned from DB into string
        String roleName = "";

        if (businessEntityRS.next())
        {
            roleName = businessEntityRS.getString(RolesDBTable.NAME_EN)==null?"":businessEntityRS.getString(RolesDBTable.NAME_EN);


        }
        return roleName;
    }

    public String getAssignedRoleName(Connection dbConn) throws SQLException {

        //Create DB Query to selected added/updated Category
        StringBuilder query = new StringBuilder();
        PropertiesFilesHandler propHandler = new PropertiesFilesHandler();
        Properties generalCofigsProps = propHandler.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);
        String userMail = generalCofigsProps.getProperty(GeneralConstants.VALID_MAIL);
        query.append("Select r.name_en as NAME_EN  " +
                " from Role r " +
                " left join business_entity be on be.id = r.business_entity_id " +
                " where r.id in (select ur.role_id from user_role ur) " +
                " and be.email='"+userMail+"' " +
                " and r.name_en <> 'Super admin'" +
                " and r.active <> '0'");

        // Execute query
        DBConnection conn = new DBConnection();
        ResultSet businessEntityRS = conn.executeQueryAndGetRS(dbConn, query.toString());

        // fill data returned from DB into string
        String roleName = null;

        if (businessEntityRS.next())
        {
            roleName = businessEntityRS.getString(RolesDBTable.NAME_EN)==null?"":businessEntityRS.getString(RolesDBTable.NAME_EN);


        }
        return roleName;
    }

    public String getRoleStatus(Connection dbConn ,String roleName) throws SQLException {

        //Create DB Query to selected added/updated Category
        StringBuilder query = new StringBuilder();

        query.append("select r.active as STATUS from Role r where r.name_en ='"+roleName+"'");

        // Execute query
        DBConnection conn = new DBConnection();
        ResultSet businessEntityRS = conn.executeQueryAndGetRS(dbConn, query.toString());

        // fill data returned from DB into string
        String status = "";

        if (businessEntityRS.next())
        {
            status = businessEntityRS.getString(RolesDBTable.STATUS)==null?"":businessEntityRS.getString(RolesDBTable.STATUS);


        }
        return status;
    }


}



