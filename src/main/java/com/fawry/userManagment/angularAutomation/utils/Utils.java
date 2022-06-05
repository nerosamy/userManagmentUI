package com.fawry.userManagment.angularAutomation.utils;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public String convertArrayToStringWithDelimeter()
    {
        return "";
    }

    public static List convertStringToList(String stringWithDelimeter, String delimeter)
    {
        List<String> stringsInArrayList = new ArrayList<>();

        if(stringWithDelimeter != null && !stringWithDelimeter.isEmpty()) {
            if(stringWithDelimeter.contains(delimeter))
            {
                String[] textsToBeAddedToList = stringWithDelimeter.split(delimeter);
                for (int i = 0; i < textsToBeAddedToList.length; i++)
                    stringsInArrayList.add(textsToBeAddedToList[i].trim());
            }
            else
                stringsInArrayList.add(stringWithDelimeter.trim());
        }

        return stringsInArrayList;
    }


    public static List<String[]> readCSVFile(String filePath)
    {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> fileContents = reader.readAll();
            fileContents.forEach(x -> System.out.println(Arrays.toString(x)));
            return fileContents;
        }
        catch (Exception e)
        {
            Log.error("Error occured in " + new Object() {}
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return null;
        }
    }

}
