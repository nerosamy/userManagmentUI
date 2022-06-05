package com.fawry.userManagment.angularAutomation.utils;

import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.mailslurp.api.api.EmailControllerApi;
import com.mailslurp.api.api.InboxControllerApi;
import com.mailslurp.api.api.WaitForControllerApi;
import com.mailslurp.client.ApiClient;
import com.mailslurp.client.ApiException;
import com.mailslurp.client.Configuration;
import com.mailslurp.models.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailsHandler
{
    //Initialize instances of properties files to get the previously created API Key to be able to create random mail accounts
    PropertiesFilesHandler propHandler = new PropertiesFilesHandler();
    Properties generalCofigsProps = propHandler.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);

    // You can signup and your own API Key at https://app.mailslurp.com/sign-up/
    private final String API_KEY = generalCofigsProps.getProperty(GeneralConstants.MAILSLURP_API_KEY);
    // set a timeout as fetching emails might take time
    private static final Long TIMEOUT_MILLIS = 30000L;

    // setup mailslurp
    public ApiClient setupMailSlurp()
    {
        // setup mailslurp
        ApiClient mailslurpClient = Configuration.getDefaultApiClient();
        mailslurpClient.setApiKey(API_KEY);

        mailslurpClient.setConnectTimeout(TIMEOUT_MILLIS.intValue());
        return mailslurpClient;
    }


    //Create XXX@mailslurp.com mail account
    public Inbox createMailInbox(ApiClient mailslurpClient) throws ApiException
    {
        // create a real, randomized email address with MailSlurp to represent a user
        InboxControllerApi inboxControllerApi = new InboxControllerApi(mailslurpClient);
        Inbox inbox = inboxControllerApi.createInbox(null,null,null,null, null, null);

        // check that inbox was created
        if(inbox != null && inbox.getEmailAddress().contains("@mailslurp.com"))
            return inbox;
        return null;

    }




    //Send mail to list of email addresses
    public void sendEmail(ApiClient mailslurpClient, Inbox inbox, List<String> toMails, String subject, String body) throws ApiException {
        InboxControllerApi inboxControllerApi = new InboxControllerApi(mailslurpClient);
        SendEmailOptions sendEmailOptions = new SendEmailOptions()
                .to(toMails)
                .subject(subject)
                .body(body);
        inboxControllerApi.sendEmail(inbox.getId(), sendEmailOptions);
    }

    //get latest unread email
    public Email getLatestReceivedMail(ApiClient mailslurpClient, Inbox inbox) throws ApiException
    {
        WaitForControllerApi waitForControllerApi = new WaitForControllerApi(mailslurpClient);
        Email email = waitForControllerApi.waitForLatestEmail(inbox.getId(), TIMEOUT_MILLIS, true);

        // You can get mail's subject and body as follows
        //    email.getSubject();
        //    email.getBody();

        return email;
    }


    //Extract data from mail's body using RegEX
    public String extractDataFromMailBody(Email email, String regEX)
    {
        String requiredData = "";
        // create a regex for matching the required data we expect in the email body
        // sample reqex  --->  (".*verification code is (\\d+).*")
        //               --->  ("Your code is: ([0-9]{3})");
        Pattern p = Pattern.compile(regEX);
        Matcher matcher = p.matcher(email.getBody());

        // find first occurrence and extract

        boolean found = false;
        while (matcher.find()) {
            requiredData = matcher.group();
            System.out.println("I found the text "+matcher.group()+" starting at index "+
                    matcher.start()+" and ending at index "+matcher.end());
            found = true;
        }
        if(!found)
            return GeneralConstants.FAILED;

        return requiredData;
    }

    // Search inbox for all mails that its subject contains the sent text
    public List<EmailPreview> searchInbox(ApiClient mailClient, Inbox inbox, String subjectSearchText) throws ApiException {
        // wait for email matching the one we sent
        WaitForControllerApi waitForControllerApi = new WaitForControllerApi(mailClient);
        MatchOptions matchOptions = new MatchOptions();
        MatchOption matchOption = new MatchOption();

    // match for emails where subject contains
        matchOption.setField(MatchOption.FieldEnum.SUBJECT);
        matchOption.setShould(MatchOption.ShouldEnum.CONTAIN);
        matchOption.setValue(subjectSearchText);
        matchOptions.addMatchesItem(matchOption);

    // wait time for conditions to match, recommended as emails can take several seconds to send and arrive
        Integer expectedCount = 1;
        List<EmailPreview> results = waitForControllerApi.waitForMatchingEmail(matchOptions, expectedCount, inbox.getId(), TIMEOUT_MILLIS, null);
        return results;
    }


    //fetch metadata for an email's attachments or fetch the attachment files themselves as byte streams
    public String getAttachmentFromEmail(ApiClient mailslurpClient, Email email) throws ApiException
    {
        String attachmentFileContents = "";
        EmailControllerApi emailControllerApi = new EmailControllerApi(mailslurpClient);
        List<AttachmentMetaData> attachments = emailControllerApi.getAttachments(email.getId());

        AttachmentMetaData attachment = attachments.get(0);
        attachment.getContentType(); // content type of attachment
        attachment.getContentLength(); // size in bytes of attachment

    // get the attachment id for fetching the content bytes
        String attachmentId  = attachment.getId();

    // get the bytes for the attachment
        byte[] attachmentBytes = emailControllerApi.downloadAttachment(attachmentId, email.getId(), API_KEY);
    // convert byte[] to string
        attachmentFileContents = new String(attachmentBytes, StandardCharsets.UTF_8);

        return attachmentFileContents;
    }

    //Delete Email
    public void deleteEmail(ApiClient defaultClient, Email email) throws ApiException
    {
        EmailControllerApi emailControllerApi = new EmailControllerApi(defaultClient);
        emailControllerApi.deleteEmail(email.getId());
    }






}
