/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servercontocorrente;
import java.io.IOException;
import java.net.*;
import java.util.logging.*;

/**
 *
 * @author Paolo
 */
public class ServerInstance {
    private ServerSocket ss;
    private Socket s;
    private String lista ="";
    private ServerManager sm;
    public static final ContiManager cm = new ContiManager();

    public ServerInstance(int port) {
        try {
            ss = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void goSever(){
        do{
            try {
                s = ss.accept();
                sm = new ServerManager(s, cm.getListaConti());
                Thread t = new Thread(sm);
                t.start();
            } catch (IOException ex) {
                Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }while(true);
    }
    
}
