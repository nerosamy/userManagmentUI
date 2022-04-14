package com.fawry.promoLoyalty.angularAutomation.utils;

import com.fawry.promoLoyalty.angularAutomation.constants.GeneralConstants;
import com.google.common.io.CharStreams;
import com.jcraft.jsch.*;
import org.apache.commons.io.input.ReversedLinesFileReader;

import static java.util.Arrays.asList;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class ServerLog {

    //Initialize instances of properties files to be used in all tests
    PropertiesFilesHandler propHandler = new PropertiesFilesHandler();
    Properties generalCofigsProps = propHandler.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);

    public String getLocalLogFilePath(String testCaseName) throws IOException, SftpException, JSchException {

        Log.info("Initializing remote connection to server of log file...");
        Session session = null;
        ChannelSftp sftpChannel = null;
        OutputStream os = null;
        String fullLocalLogFileName = "";

        String tempLogFilePath = "";

        try
        {
            // check if log file placed in container, so we must get the current container instance to get file path placed in container's directory
            String isLogFileInContainer = generalCofigsProps.getProperty(GeneralConstants.LOG_SERVER_IN_CONTAINER);
            if(isLogFileInContainer.equalsIgnoreCase(GeneralConstants.TRUE))
            {
                ///var/lib/docker/containers/<container_id>/<container_id>-json.log
                String containerID = runCommand(generalCofigsProps.getProperty(GeneralConstants.SSH_CMD_TO_GET_CONTAINER_ID)).get(0);
                tempLogFilePath = generalCofigsProps.getProperty(GeneralConstants.LOG_SERVER_FILE_PATH_IN_CONTAINER).replaceAll("<container_id>", containerID);
            }
            //Get log file path directly from config file
            else
                tempLogFilePath = generalCofigsProps.getProperty(GeneralConstants.LOG_SERVER_FILE_PATH);

            final String logFilePathOnServer = tempLogFilePath;
            Log.info("Log file path on server: " + logFilePathOnServer);
            //Get local log file configs to be created on local machine
            String localLogFileDir = generalCofigsProps.getProperty(GeneralConstants.LOG_LOCAL_FILE_PATH);
            String localLogFileSize = generalCofigsProps.getProperty(GeneralConstants.LOG_LOCAL_FILE_SIZE);

            // Add date pattern not to overwrite the previously created log file
            String dateTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            String localLogFileName = testCaseName + dateTime + ".log";

            localLogFileDir = System.getProperty("user.dir") + localLogFileDir;
            fullLocalLogFileName = localLogFileDir + GeneralConstants.FILE_DELIMETER + localLogFileName;

            //Create log file
            File bugLogFile = fileWithDirectoryAssurance(localLogFileDir, localLogFileName);
            //Create outputstream that will be used to copy log file from server to local machine
            os = new FileOutputStream(bugLogFile.getAbsolutePath());

            //Create session to the application's remote server
            session = setupSshSession();
            session.connect();
            if (session.isConnected() == true) {
                Log.info("Connection to SFTP server is successfully");
            }

            //Open sftp channel to server to securly transfer files. MAKE SURE THAT SFTP SERVICE IS UP AND RUNNING ON SERVER
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            // Get remote log file size
            long logFileSize = sftpChannel.stat(logFilePathOnServer).getSize();
            Log.info( "Remote log file size "+ logFileSize);

            // Get log file size to be skipped, so we can only get the required bytes from the end of the file
            long fileSizeToBeSkiped = logFileSize - Integer.parseInt(localLogFileSize);

            //Create monitor object to be used while copying log file from server
            SftpProgressMonitor progress = new SftpProgressMonitor() {

                @Override public void init(int arg0, String arg1, String arg2, long arg3)
                {
                    Log.info("File transfer begin..");
                }

                @Override public void end()
                {
                    Log.info("Download of file "+ logFilePathOnServer +" succeeded.");
                }

                @Override public boolean count(long i) { return false; }
            };

            //Get the require size from the end of the remote log file and copy it to the local log file
            sftpChannel.get(logFilePathOnServer, os, progress , ChannelSftp.RESUME, fileSizeToBeSkiped);

            if(os != null)
                os.close();
        }
        finally {

            // Validating if channel sftp is not null to exit
            if (sftpChannel != null) {
                sftpChannel.exit();
            }

            // Validating if session instance is not null to disconnect
            if (session != null) {
                session.disconnect();
            }
        }
        return fullLocalLogFileName;
    }

    /** Creates parent directories if necessary. Then returns file */
    private static File fileWithDirectoryAssurance(String directory, String filename) {
        File dir = new File(directory);
        if (!dir.exists())
            dir.mkdirs();
        return new File(directory + GeneralConstants.FILE_DELIMETER + filename);
    }

//        public static void main() {
//            System.out.println(runCommand("pwd"))
//            System.out.println(runCommand("ls -la"));
//        }

        private  List<String> runCommand(String command) throws JSchException, IOException
        {
            Session session = null;
            ChannelExec channel = null;


            session = setupSshSession();
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");

            channel.setCommand(command);
            channel.setInputStream(null);
            InputStream output = channel.getInputStream();
            channel.connect();

            String result = CharStreams.toString(new InputStreamReader(output));
            return asList(result.split("\n"));

        }

        private Session setupSshSession() throws JSchException {
            JSch jsch = new JSch();

            //Get server credentials from config file
            String rmUsername = generalCofigsProps.getProperty(GeneralConstants.LOG_SERVER_USERNAME);
            String rmHost = generalCofigsProps.getProperty(GeneralConstants.LOG_SERVER_IP);
            String rmPassword = generalCofigsProps.getProperty(GeneralConstants.LOG_SERVER_PASSWRD);
            String rmPort = generalCofigsProps.getProperty(GeneralConstants.LOG_SERVER_PORT);

            //Create session to the application's remote server
            Session session = jsch.getSession(rmUsername, rmHost, Integer.parseInt(rmPort));
            session.setPassword(rmPassword);
            java.util.Properties config = new java.util.Properties();
            config.put("PreferredAuthentications", "publickey,keyboard-interactive,password");
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            return session;
        }


        private static void closeConnection(ChannelExec channel, Session session) {
            try {
                channel.disconnect();
            } catch (Exception ignored) {
            }
            session.disconnect();
        }



    public static List<String> readLastLine(File file, int numLastLineToRead) {

        List<String> result = new ArrayList<>();

        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(file, StandardCharsets.UTF_8)) {

            String line = "";
            while ((line = reader.readLine()) != null && result.size() < numLastLineToRead) {
                result.add(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }





    /**
     *
     * @param session
     */
    private static void isSftpProxyEnabled(Session session) {
        // Fetching the sftp proxy flag set as part of the properties file
        boolean isSftpProxyEnabled = Boolean.valueOf("<sftp.proxy.enable>");
        // Validating if proxy is enabled to access the sftp
        if (isSftpProxyEnabled) {
            // Setting host and port of the proxy to access the SFTP
            session.setProxy(new ProxyHTTP("<sftp.proxy.host>", Integer.valueOf("<sftp.proxy.port>")));
        }
        System.out.println("Proxy status: " + isSftpProxyEnabled);
    }
}
