/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servercontocorrente;

import java.util.Scanner;

/**
 *
 * @author Paolo
 */
public class ServerContoCorrete {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       // ContiManager cm = new ContiManager();
        
        new ServerInstance(8080).goSever();
        //System.out.println("lista conti\n"+cm.getListaConti());
    }
    
}