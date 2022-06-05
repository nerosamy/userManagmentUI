package com.fawry.userManagment.angularAutomation.backendServices;


import com.fawry.userManagment.angularAutomation.backendServices.database.services.RolesService;
import com.fawry.userManagment.angularAutomation.backendServices.database.services.UserService;
import com.fawry.userManagment.angularAutomation.dataModels.RolesDM;
import com.fawry.userManagment.angularAutomation.dataModels.UsersDM;
import com.fawry.userManagment.angularAutomation.utils.Log;

import java.util.ArrayList;

public class ServicesDelegate {

    public ServicesDelegate()
    {
        Log.info(" *******   Create an instance of ServiceDelegate to access backend DB    ******** ");
    }



    public ArrayList<RolesDM> getRolesDetails(String roleName)
    {
        ArrayList<RolesDM> rolesDMS = null;
        try {
            RolesService rolesService = new RolesService();
            rolesDMS  = rolesService.getRolesDetails(roleName);

        }

        catch (Exception e)
        {
            Log.error("ERROR occurred in " + new Object() {}
                    .getClass().getName() + "." + new Object() {}
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
        }
        return rolesDMS;
    }



    public ArrayList<RolesDM> getRolesList(RolesDM searchDM)
    {
        ArrayList<RolesDM> rolesDMS = null;
        try {
            RolesService rolesService = new RolesService();
            rolesDMS  = rolesService.getRolesList(searchDM);

        }

        catch (Exception e)
        {
            Log.error("ERROR occurred in " + new Object() {}
                    .getClass().getName() + "." + new Object() {}
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
        }
        return rolesDMS;
    }



    public String getNotAssignedRoleName()
    {
        String roleName = null;
        try {
            RolesService rolesService = new RolesService();
            roleName  = rolesService.getNotAssignedRoleName();

        }

        catch (Exception e)
        {
            Log.error("ERROR occurred in " + new Object() {}
                    .getClass().getName() + "." + new Object() {}
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
        }
        return roleName;
    }


    public String getAssignedRoleName()
    {
        String roleName = null;
        try {
            RolesService rolesService = new RolesService();
            roleName  = rolesService.getAssignedRoleName();

        }

        catch (Exception e)
        {
            Log.error("ERROR occurred in " + new Object() {}
                    .getClass().getName() + "." + new Object() {}
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
        }
        return roleName;
    }
    public String getRoleStatus(String roleName)
    {
        String status = null;
        try {
            RolesService rolesService = new RolesService();
            status  = rolesService.getRoleStatus(roleName);

        }

        catch (Exception e)
        {
            Log.error("ERROR occurred in " + new Object() {}
                    .getClass().getName() + "." + new Object() {}
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
        }
        return status;
    }

    public UsersDM getUserDetails(String userMail)
    {
        UsersDM usersDM = null;
        try {
            UserService userService = new UserService();
            usersDM  = userService.getUserDetails(userMail);

        }

        catch (Exception e)
        {
            Log.error("ERROR occurred in " + new Object() {}
                    .getClass().getName() + "." + new Object() {}
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
        }
        return usersDM;
    }

    public ArrayList<UsersDM> getUsersList(UsersDM usersDM)
    {
        ArrayList<UsersDM> usersDMS = new ArrayList<>();
        try {
            UserService userService = new UserService();
            usersDMS  = userService.getUsersList(usersDM);

        }

        catch (Exception e)
        {
            Log.error("ERROR occurred in " + new Object() {}
                    .getClass().getName() + "." + new Object() {}
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
        }
        return usersDMS;
    }


}
