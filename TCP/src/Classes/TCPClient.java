package Classes;

import Criptografia.Criptografia;
import Criptografia.SHA1;
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
            upperHash = Criptografia.criptografar(clientSentence.toUpperCase());
        } else{
            upperHash = SHA1.SHA1(clientSentence.toUpperCase());
        }
        
        return clientSentence.toUpperCase() + "/" + upperHash + "/" + TipoHash;
    }

    public static void main(String argv[]) throws Exception 
    { 
        String sentence = ""; 
        String hashMD5;
        String hashSHA1;
        String modifiedSentence;
        
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        
        while(!sentence.toLowerCase().equals("exit")){
            System.out.print("Informe uma sentença (exit para sair): ");
            sentence = inFromUser.readLine(); 
            hashMD5 = Criptografia.criptografar(sentence);
            hashSHA1 = SHA1.SHA1(sentence);

            List<String> mensagens = new ArrayList<>();
            mensagens.add(sentence + "/" + hashMD5 + '\n');
            mensagens.add(sentence + "/" + hashSHA1 + '\n');

            for(String m : mensagens){

                Socket clientSocket = new Socket("192.168.0.105", 10001); 
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); 
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                outToServer.writeBytes(m); 
                modifiedSentence = inFromServer.readLine();
                clientSocket.close();

                System.out.println("--------------------------------");
                System.out.println("FROM SERVER: " + modifiedSentence); 
                System.out.println("VERIFICACAO: " + VerificaHash(modifiedSentence, sentence));
                System.out.println("--------------------------------");
            }   
        }        
    } 
} 

        
