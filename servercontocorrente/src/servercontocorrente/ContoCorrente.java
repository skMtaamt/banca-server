/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servercontocorrente;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Paolo
 */
public class ContoCorrente {
    private String id;
    private Vector<CommunicationManager> listaClient;
    public JSONObject jsonObject;
    
    public ContoCorrente(String id) {
        this.id = id;
        FileReader fr = null;
        try {
            fr = new FileReader(System.getProperty("user.dir") + "/db/conti.json");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ContoCorrente.class.getName()).log(Level.SEVERE, null, ex);
        }
        String cache = "";
        int i;
        try {
            while ((i=fr.read()) != -1)
                cache = cache + (char) i;
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(ContoCorrente.class.getName()).log(Level.SEVERE, null, ex);
        }
        jsonObject = new JSONObject(cache);
        listaClient = new Vector<CommunicationManager>();
    }

    public void addClient(Socket s){
        listaClient.add(new CommunicationManager(s, this));
        new Thread(listaClient.elementAt(listaClient.size()-1)).start();
    }
    
    public boolean versa(float cifra, String casuale) throws Exception {
        jsonObject.getJSONObject(id).put("saldo", jsonObject.getJSONObject(id).getFloat("saldo") + cifra);
        jsonObject.getJSONObject(id).put("movimenti", jsonObject.getJSONObject(id).getJSONArray("movimenti")
                .put(new JSONObject()
                        .put("casuale", casuale)
                        .put("transazione", cifra)
                        .put("status", "positivo")));
        update(jsonObject);
        return true;
    }
    
    public boolean preleva(float cifra, String casuale) throws Exception {
        if(jsonObject.getJSONObject(id).getFloat("saldo") - cifra >= 0){
            jsonObject.getJSONObject(id).put("saldo", jsonObject.getJSONObject(id).getFloat("saldo") - cifra);
            jsonObject.getJSONObject(id).put("movimenti", jsonObject.getJSONObject(id).getJSONArray("movimenti")
                    .put(new JSONObject()
                            .put("casuale", casuale)
                            .put("transazione", cifra)
                            .put("status", "negativo")));
            update(jsonObject);
            return true;
        }
        return false;
    }
    
    public JSONArray storico(){
        return jsonObject.getJSONObject(id).getJSONArray("movimenti");
    }
    
    public float saldo(){
        return jsonObject.getJSONObject(id).getFloat("saldo");
    }
    
    private void update(JSONObject newJson) throws Exception {
        FileWriter fw = new FileWriter(System.getProperty("user.dir") + "/db/conti.json");
        fw.write(newJson.toString());
        fw.close();
    }
    
    //getSaldo()
}
