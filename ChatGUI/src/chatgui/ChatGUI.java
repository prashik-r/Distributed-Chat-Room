
package chatgui;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ChatGUI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException{
        // TODO code application logic here
        Login app = new Login();
        app.setVisible(true); 
    }
   
}
