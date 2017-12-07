package com.ci1320.ecci.ucr.ac.cr.arpTable;

import com.ci1320.ecci.ucr.ac.cr.routingTable.RoutingTable;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by GrupoBolinchas on 5-Nov-17.
 */
public class ARPTable {

    //Variables únicas
    public static ARPTable cacheTableInstance;
    //Variables del dispatcher
    public static String dispatcherIp;
    public static int dispatcherPort;

    //Solo una instancia
    static {
        try {
            cacheTableInstance = new ARPTable();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    //Tabla de enrutamiento
    RoutingTable routingTable = RoutingTable.getInstance();

    //Lista de vecinos
    private List<Node> localNetwork;

    /**
     * Crea la tabla caché
     * @throws UnknownHostException
     */
    private ARPTable() throws UnknownHostException {
        localNetwork = new ArrayList<Node>();
        dispatcherIp = "";
        dispatcherPort = 1024;
    }


    /**
     * Agrega un vecino
     * @param macAddress
     * @param ip
     * @param port
     * @throws UnknownHostException
     */
    public void addNeighbour(String macAddress, String ip, int port, String falseIp) throws UnknownHostException {
        Node newNode = new Node(macAddress, ip, port, falseIp);
        localNetwork.add(newNode);
    }

    /**
     * Imprime la tabla caché
     */
    public synchronized void displayTable() {
        Iterator<Node> iterator = localNetwork.iterator();
        System.out.print("\nNodos de Red Local\n");
        Node actualObject;
        while (iterator.hasNext()) {
            actualObject = iterator.next();
            System.out.println("||" + actualObject.getMacAddress() + "||" + actualObject.getIp() + "||" + actualObject.getPort() + "||\n");
        }
    }

    /**
     * @return Devuelve la única instancia de tabla caché
     * @throws UnknownHostException
     */
    public static synchronized ARPTable getInstance() throws UnknownHostException {
        if (cacheTableInstance == null) {
            cacheTableInstance = new ARPTable();
        }
        return cacheTableInstance;
    }

    /**
     * @param macAddress
     * @return Devuelve un booleano que indica si un vecino existe o no
     */
    public boolean neighbourExists(String macAddress) {
        Iterator<Node> iterator = localNetwork.iterator();
        Node actualObject;
        boolean found = false;
        while (iterator.hasNext() && !found) {
            actualObject = iterator.next();
            if (actualObject.getMacAddress().equals(macAddress)) {
                found = true;
            }
        }
        return found;
    }

    /**
     * @param falseIp
     * @return Devuelve un booleano que indica si un vecino está o no
     */
    public boolean isNeighbour(String falseIp) {
        Iterator<Node> iterator = localNetwork.iterator();
        Node actualObject;
        boolean found = false;
        while (iterator.hasNext() && !found) {
            actualObject = iterator.next();
            if (actualObject.getFalseIp().equals(falseIp)) {
                found = true;
            }
        }
        return found;
    }

    public String getThrough(String falseIp){
        Iterator<Node> iterator = localNetwork.iterator();
        Node actualObject;
        String through = "";
        while (iterator.hasNext() && through.equals("")) {
            actualObject = iterator.next();
            if (actualObject.getFalseIp().equals(falseIp)) {
                through = actualObject.getMacAddress();
            }
        }
        return through;
    }

    /**
     * @param macAddress
     * @return Devuelve la ip de un vecino
     */
    public String getNeighbourIp(String macAddress) {
        Iterator<Node> iterator = localNetwork.iterator();
        Node actualObject;
        boolean found = false;
        String ipNodo = "";
        while (iterator.hasNext() && !found) {
            actualObject = iterator.next();
            if (actualObject.getMacAddress().equals(macAddress)) {
                found = true;
                ipNodo = actualObject.getIp();
            }
        }
        return ipNodo;
    }

    /**
     * @param macAddress
     * @return Devuelve el puerto de un vecino
     */
    public int getNeighbourPort(String macAddress) {
        Iterator<Node> iterator = localNetwork.iterator();
        Node actualObject;
        boolean found = false;
        int portNode = 0;
        while (iterator.hasNext() && !found) {
            actualObject = iterator.next();
            if (actualObject.getMacAddress().equals(macAddress)) {
                found = true;
                portNode = actualObject.getPort();
            }
        }
        return portNode;
    }

    /**
     * Edita un vecino
     * @param sender
     * @param ip
     * @param port
     */
    public void editNeighbour(String sender, String ip, int port, String falseIp) {
        Iterator<Node> iterator = localNetwork.iterator();
        Node actualObject;
        boolean found = false;
        while (iterator.hasNext() && !found) {
            actualObject = iterator.next();
            if (actualObject.getMacAddress().equals(sender)) {
                actualObject.setIp(ip);
                actualObject.setPort(port);
                actualObject.setFalseIp(falseIp);
                found=true;
            }
        }
    }
}
