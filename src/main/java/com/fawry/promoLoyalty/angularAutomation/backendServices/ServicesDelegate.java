package com.fawry.promoLoyalty.angularAutomation.backendServices;

import com.fawry.promoLoyalty.angularAutomation.backendServices.database.services.*;
import com.fawry.promoLoyalty.angularAutomation.dataModels.*;
import com.fawry.promoLoyalty.angularAutomation.utils.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicesDelegate {

    public ServicesDelegate()
    {
        Log.info(" *******   Create an instance of ServiceDelegate to access backend DB    ******** ");
    }



    /*public List<LookUpModel> getSKUProgs(String partnerMail)
    {
        List<LookUpModel> loyaltyProgs = null;
        try {
            LoyaltyProgService lpService = new LoyaltyProgService();
            loyaltyProgs = lpService.getSKUProgs(partnerMail);

        }

        catch (Exception e)
        {
            Log.error("ERROR occured in " + new Object() {}
                    .getClass().getName() + "." + new Object() {}
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
        }
        return loyaltyProgs;
    }*/




}
