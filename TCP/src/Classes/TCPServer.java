package Classes;

import Criptografia.Criptografia;
import Criptografia.SHA1;
import java.io.*; 
import java.net.*; 
import java.security.NoSuchAlgorithmException;


class TCPServer { 
    
    public static String ValidaMensagens(String ClientSentence) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        
        String[] parts = ClientSentence.split("/");
        String sentence = parts[0];
        String hash = parts[1];
        
        String TipoHash = "";
        
        String hashMD5 = Criptografia.criptografar(sentence);
        String hashSHA1 = SHA1.SHA1(sentence);
        
        if(hash.equals(hashMD5)){
            TipoHash = "MD5";
        } else if(hash.equals(hashSHA1)) {
            TipoHash = "SHA1";
        } 
        
        return TipoHash;
    }
    
    public static String HashString(String TipoHash, String sentence, boolean isUpperCase) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        String hash = "";
        
        String[] parts = sentence.split("/");
        sentence = parts[0];
        
        if(isUpperCase)
            sentence = sentence.toUpperCase();
        else
            sentence = sentence.toLowerCase();
        
        if(TipoHash.toUpperCase().equals("MD5")){
            hash = Criptografia.criptografar(sentence);
        } else if(TipoHash.toUpperCase().equals("SHA1")){
            hash = SHA1.SHA1(sentence);
        }
        
        return sentence + "/" + hash + "/" + TipoHash;
    }

    public static void main(String argv[]) throws Exception 
    { 
      String clientSentence;
      ServerSocket welcomeSocket = new ServerSocket(10001); 
      welcomeSocket.setReuseAddress(true);
      while(true) {
           Socket connectionSocket = welcomeSocket.accept();
           
           BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); 
	   DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream());
           clientSentence = inFromClient.readLine(); 
           
           if(clientSentence != null && clientSentence.length() > 0){
                String TipoHash = ValidaMensagens(clientSentence);
                if(TipoHash.length() > 0){
                    String hash = HashString(TipoHash, clientSentence, true);
                    outToClient.writeBytes(hash + "\n"); 
                    outToClient.flush();
                }
           }
           
           inFromClient.ready();
        } 
    }
} 
 
