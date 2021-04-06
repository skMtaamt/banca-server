/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servercontocorrente;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author Paolo
 */
public class ServerManager implements Runnable{
    static Socket s;
    private String conti;

    public ServerManager(Socket s, String conti) {
        this.s = s;
        this.conti = conti;
    }
    
    
    @Override
    public void run() {
        String str;
        try {
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            // login
            boolean tf = true;
            do{
                JSONObject response = new JSONObject(in.readLine());
                JSONObject message = new JSONObject();
                System.out.println("autenting ..");
                FileReader fr = new FileReader(System.getProperty("user.dir") + "/db/clients.json");
                String users = "";
                int i;
                while ((i=fr.read()) != -1)
                  users = users + (char) i;
                JSONObject jsonObject = new JSONObject(users);
                Iterator<String> keys = jsonObject.keys();
                while(keys.hasNext()) {
                    String key = keys.next();
                    if (key.equals(response.getString("email"))) {
                        if(jsonObject.getJSONObject(key).getString("pass").equals(response.getString("password"))){
                            message.put("status", true).put("err", false);
                            ServerInstance.cm.addClient(s, jsonObject.getJSONObject(key).getString("id"));
                            tf = false;
                            break;
                        }else{
                            message.put("status", false).put("err", "wrong password");
                        }
                    }else{
                         message.put("status", false).put("err", "wrong email");
                    }
                }
                pw.println(message);  
            }while(tf);
        } catch (IOException ex) {
            Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
