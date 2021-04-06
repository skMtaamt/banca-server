
package servercontocorrente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author Paolo
 */
public class CommunicationManager implements Runnable{
    private final ContoCorrente cc;
    private Socket s;
    //stream    
    
    public CommunicationManager(Socket s, ContoCorrente cc) {
        this.s = s;
        this.cc = cc;
    }

    //aka main 2 fare versa e preleva
    @Override
    public void run() {
        try {
             String input = "";
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            JSONObject risposta = null;
            do{
                JSONObject jsonObject = new JSONObject(reader.readLine());
                switch(jsonObject.getString("action")){
                    case "versa":
                        try {
                            cc.versa(jsonObject.getFloat("jsonObject"), jsonObject.getString("casuale"));
                            risposta = new JSONObject()
                                    .put("status", true)
                                    .put("err", false);
                        } catch (Exception ex) {
                            risposta = new JSONObject()
                                    .put("status", false)
                                    .put("err", ex.toString());
                        }
                        pw.println(risposta.toString());
                        break;
                    case "preleva":
                        try {
                            if(cc.preleva(jsonObject.getFloat("jsonObject"), jsonObject.getString("casuale"))){
                                risposta = new JSONObject()
                                    .put("status", true)
                                    .put("err", false);
                            }else{
                                 risposta = new JSONObject()
                                    .put("status", false)
                                    .put("err", "saldo insufficente");
                            }
                        } catch (Exception ex) {
                            risposta = new JSONObject()
                                    .put("status", false)
                                    .put("err", ex.toString());
                        }
                        pw.println(risposta.toString());
                        break;
                    case "saldo":
                        pw.println(cc.saldo());
                    case "storico":
                        pw.println(cc.storico().toString());
                }
            }while(input != "logout");
        } catch (IOException ex) {
            Logger.getLogger(CommunicationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
