package servercontocorrente;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author Paolo
 */
public class ContiManager {
    private HashMap<String, ContoCorrente> lista;

    public ContiManager() {
        lista = new HashMap();
        genera();
    }
    
    private void genera(){
        FileReader fr = null;
        try {
            fr = new FileReader(System.getProperty("user.dir") + "/db/conti.json");
            String users = "";
            int i;
            while ((i=fr.read()) != -1)
                users = users + (char) i;
            JSONObject jsonObject = new JSONObject(users);
            Iterator<String> keys = jsonObject.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                lista.put(key, new ContoCorrente(key));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ContiManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ContiManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(ContiManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String getListaConti(){
        String l="";
        for(String s : lista.keySet()){
            l = l +"::"+s;
        }
        return l;
    }
    
    public boolean addClient(Socket s, String nconto){
        lista.get(nconto).addClient(s);
        new Thread(new CommunicationManager(s, lista.get(nconto))).start();
        return true;
    }
    
}
