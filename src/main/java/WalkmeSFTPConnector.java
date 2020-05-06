import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
 
/**
 * This java program shows you how to make SFTP/SSH connection with public key using JSch java API
 * 
 * @author https://kodehelp.com
 */
public class WalkmeSFTPConnector {
 
	/**
     *  SFTP host
     */
	static String SFTPHOST = "partners-sftp.walkme.com";;
    
    /**
     *  SFTP port
     */
    static int SFTPPORT = 22;
    
    /**
     *  SFTP username
     */
    static String SFTPUSER = "";
    
    /**
     * Full path to SFTP private key
     */
    static String privateKey = "";
    
    /**
     * Remote directory path
     */
    static String SFTPWORKINGDIR = "/walkme-partners/" + SFTPUSER;
    
    /**
     * Full path to your local file
     */
    static String LOCAL_FILE = "";
	
	
    /**
     * @param args
     */
    public static void main(String[] args) {
        
    		ChannelSftp channelSftp = null;
        try {
        		//Create SFTP connection
        		channelSftp = connect();
        		
        		//Copy the File
        		if(channelSftp != null) {
        			copyFileToRemoteSftpDir(channelSftp);
        		}
        		
        } catch (SftpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Copy Local file to remote sftp directory
     * 
     * @param channelSftp sftp connection
     * @throws SftpException
     */
	private static void copyFileToRemoteSftpDir(ChannelSftp channelSftp) throws SftpException {
		
		System.out.println("Copy file to the directory...");
		channelSftp.put(LOCAL_FILE, SFTPWORKINGDIR);
		
		//Print the directory content
		System.out.println("Print the directory...");
		System.out.println(channelSftp.ls(SFTPWORKINGDIR));
	}

	/**
	 * Create SFTP connection.
	 * 
	 * @return SFTP connection
	 */
	private static ChannelSftp connect() {
		JSch jSch = new JSch();
        Session     session     = null;
        Channel     channel     = null;
        ChannelSftp channelSftp = null;
        try {
        		//Create the SFTP connection
            jSch.addIdentity(privateKey);
            System.out.println("Private Key Added.");
            session = jSch.getSession(SFTPUSER,SFTPHOST,SFTPPORT);
            java.util.Properties config = new java.util.Properties();
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("session created.");
            System.out.println("shell channel connected....");
            
            channelSftp = (ChannelSftp)channel;
            
        } catch (JSchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            if(channelSftp!=null){
                channelSftp.disconnect();
                channelSftp.exit();
            }
            if(channel!=null) channel.disconnect();
             
            if(session!=null) session.disconnect();
        }
        
        return channelSftp;
	}
 
}
	
	
