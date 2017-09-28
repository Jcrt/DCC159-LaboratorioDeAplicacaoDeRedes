package Classes;

import Criptografia.Criptografia;
import Criptografia.SHA1;
import java.io.*; 
import java.net.*; 
import java.security.NoSuchAlgorithmException;
class TCPClient { 
    
    public static String VerificaHash(String serverHash, String clientSentence) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        String[] str = serverHash.split("/");
        String TipoHash = str[2];
        String upperHash;
        
        if(TipoHash.toUpperCase().equals("MD5")){
            upperHash = Criptografia.criptografar(clientSentence.toUpperCase());
        } else{
            upperHash = SHA1.SHA1(clientSentence.toUpperCase());
        }
        
        return clientSentence.toUpperCase() + "/" + upperHash + "/" + TipoHash;
    }

    public static void main(String argv[]) throws Exception 
    { 
        String sentence; 
        String hashMD5;
        String hashSHA1;
        String modifiedSentence;
        String modifiedSentence1;
        
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); 

        Socket clientSocket = new Socket("192.168.56.1", 10001); 

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); 
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 

        sentence = inFromUser.readLine(); 
        hashMD5 = Criptografia.criptografar(sentence);
        hashSHA1 = SHA1.SHA1(sentence);
        
        //outToServer.writeBytes(sentence + "/" + hashMD5 + '\n'); 
        outToServer.writeBytes(sentence + "/" + hashSHA1 + '\n'); 

        modifiedSentence = inFromServer.readLine();
        //modifiedSentence1 = inFromServer.readLine();

        System.out.println("FROM SERVER: " + modifiedSentence); 
        System.out.println("VERIFICACAO: " + VerificaHash(modifiedSentence, sentence));
        
        clientSocket.close(); 
                   
    } 
} 

        
