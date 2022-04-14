package com.fawry.promoLoyalty.angularAutomation.tests;

import com.fawry.promoLoyalty.angularAutomation.utils.EmailsHandler;
import com.fawry.promoLoyalty.angularAutomation.utils.Log;
import com.mailslurp.client.ApiClient;
import com.mailslurp.client.ApiException;
import com.mailslurp.models.Email;
import com.mailslurp.models.EmailPreview;
import com.mailslurp.models.Inbox;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class TestingElementsToBeDeletedClass extends BaseTest{


    @Test
    public void testMail()
    {
        try {
            //Create extent test to be logged in report using test case title
            test = extent.createTest("Test mail");
            Log.test = test;
            Log.startTestCase("Test mail");

            //setup mailslurp
            EmailsHandler emailsHandler =  new EmailsHandler();
            ApiClient mailClient =  emailsHandler.setupMailSlurp();

            //create inbox
            Inbox emailInbox = emailsHandler.createMailInbox(mailClient);
            System.out.println(emailInbox.getEmailAddress());

            //send mail
            List<String> toMails = new ArrayList<>();
            toMails.add(emailInbox.getEmailAddress());
            emailsHandler.sendEmail(mailClient, emailInbox, toMails, "Test subject", "Test body welcome Wessam");

            //get received mail
            Email receivedMail = emailsHandler.getLatestReceivedMail(mailClient, emailInbox);
            System.out.println(receivedMail.getSubject() + "  *** " + receivedMail.getBody());

            // extract data from mail body
            String extractedData = emailsHandler.extractDataFromMailBody(receivedMail, ".*Wessam");
            System.out.println("extractedData  *** " + extractedData);

            //Search inbox using a text in subject
            List<EmailPreview> searchResults = emailsHandler.searchInbox(mailClient, emailInbox, "Test");
            System.out.println("searchResults: " + searchResults.get(0).getSubject());

        }
        catch (ApiException e)
        {
            Log.error("Error occured in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
        }
    }

}
