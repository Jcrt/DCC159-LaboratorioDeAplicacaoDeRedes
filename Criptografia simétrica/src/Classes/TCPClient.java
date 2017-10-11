package Classes;

import Criptografia.AES;
import Criptografia.MD5;
import Criptografia.SHA1;
import Criptografia.TripleDES;
import java.io.*; 
import java.net.*; 
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
class TCPClient { 
    
    public static String VerificaHash(String serverHash, String clientSentence) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        String[] str = serverHash.split("/");
        String TipoHash = str[2];
        String upperHash;
        
        if(TipoHash.toUpperCase().equals("MD5")){
            upperHash = MD5.criptografar(clientSentence.toUpperCase());
        } else{
            upperHash = SHA1.SHA1(clientSentence.toUpperCase());
        }
        
        return clientSentence.toUpperCase() + "/" + upperHash + "/" + TipoHash;
    }

    public static void main(String argv[]) throws Exception 
    { 
        
        String key = "Bar12345Bar12345";
        String initVector = "RandomInitVector"; 
        String sentence = "";
        String modifiedSentence;
        TripleDES td = new TripleDES();
        
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        
        while(!sentence.toLowerCase().equals("exit")){
            System.out.print("Informe uma sentença (exit para sair): ");
            sentence = inFromUser.readLine(); 
            
            List<String> mensagens = new ArrayList<>();
            
            //mensagens.add(AES.encrypt(key, initVector, sentence));
            mensagens.add(td.encrypt(sentence).toString());
            
            for(String m : mensagens){

                Socket clientSocket = new Socket("192.168.0.105", 9999); 
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); 
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                outToServer.writeBytes(m + "\n"); 
                modifiedSentence = inFromServer.readLine();
                
                //modifiedSentence = AES.decrypt(key, initVector, modifiedSentence);
                modifiedSentence = td.decrypt(modifiedSentence.getBytes("UTF-8"));
                
                System.out.println("Client side com UPPER: " + modifiedSentence);
                
                clientSocket.close();
            }   
        }        
    } 
} 

        
