/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatgui;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import lib1.ClientInterface;
import lib1.RMIInterface;
import lib1.RMIMainInterface;


public class Client extends UnicastRemoteObject implements ClientInterface  {
    private RMIMainInterface mainStub;
    private RMIInterface subStub;
    private String name, username, serverName;
    private Profile profile;

    public Client() throws RemoteException, NotBoundException, MalformedURLException {
        super();
        this.subStub = null;
        this.profile = new Profile(this);
        this.mainStub = (RMIMainInterface) Naming.lookup("//127.0.0.1:19999/hellton");
    }
    
    @Override
    public synchronized void doNothing() throws RemoteException {
        System.exit(0);
        return;
    }
    
    @Override
    public synchronized void refresh() throws RemoteException {
        this.load();
    }
    
    @Override
    public synchronized void reciveMessage(String groupName, String message) throws RemoteException {
        if(groupName.length() == 0){
            int index = message.indexOf(":");
            if(index < 0) {
                groupName = message;
            } else {
                groupName = message.substring(0, index);
            }
        }
        this.profile.addMessage(groupName, this.username, message);
    } 
    
    public void load() throws RemoteException {
        ArrayList<String> groups = this.subStub.fetchGroups(username);
        ArrayList<String> requests = this.subStub.displayRequests(this.username);
        for(int i = 0; i < groups.size(); i++) {
            String temp = groups.get(i);
            this.profile.setGroup(i, temp);
            this.profile.setChatOptions(i, this.username, temp);
        }
        for(int i = 0; i < requests.size(); i++) {
            String temp = requests.get(i);
            this.profile.setRequest(i, temp);
        }
        this.profile.display();
        return;
    }
    
    public synchronized void logout() throws RemoteException{
        this.profile.setVisible(false);
        this.subStub.logout(this.name, username);
    }
    
    public synchronized void sendPersonal(String to, String message) throws RemoteException{
        message += "  <-" + new Date(System.currentTimeMillis());
        this.subStub.send(this.username, to, message);
    }
    
    public synchronized void sendBroad(String message) throws RemoteException{
        message += "  <-" + new Date(System.currentTimeMillis());
        this.subStub.broadcast(this.username, message);    
    }
    
    public synchronized void sendGroup(String groupName, String message) throws RemoteException {
        ArrayList<String>res = this.subStub.display(groupName);
        String ret = this.subStub.sendGroup(this.username, groupName, res, message, 0);    
        this.reciveMessage(groupName, ret);
    }
    
    public synchronized void groupRequest(String groupName) throws RemoteException {
        String owner = this.subStub.groupRequest(username , groupName);
    }
    
    public synchronized void requestDecide(String groupName,String requser,int parity) throws RemoteException {
        this.subStub.requestDecide(parity, requser, groupName);
        this.profile.display();
        this.load();
    }
    
    public synchronized void groupCreateRequest(String groupName) throws RemoteException {
        this.subStub.groupCreateRequest(this.username, groupName);
    }
    
    public synchronized void leaveGroup(String groupName) throws RemoteException {
        this.subStub.leaveGroup(this.username, groupName);
        this.sendGroup(groupName, this.username + " has left the group");
    }
    
    public synchronized int login(String username , String password) throws RemoteException{
        this.username = username;
        this.name = "rmi://127.0.0.1:20000/" + username; 
        String val = this.mainStub.check(username, password);
        if(val.equals("0")){
           return 0;
        }
        this.serverName = val;
        try {
            Naming.rebind(this.name, this);
            this.subStub = (RMIInterface) Naming.lookup(val);
            this.subStub.topology(this.username, this.name);
        } catch (NotBoundException | MalformedURLException e) {
            System.out.println("Error: " + e);
        }
        return 1;
    }
    
    public void init() throws RemoteException {
        this.profile.init(username);
        this.load();
        this.profile.display();
    }
}
