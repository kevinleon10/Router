package com.ci1320.ecci.ucr.ac.cr.cacheTable;

import com.ci1320.ecci.ucr.ac.cr.routingTable.Route;
import com.ci1320.ecci.ucr.ac.cr.routingTable.RoutingTable;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lskev on 5-Nov-17.
 */
public class CacheTable {
    //Variables únicas
    public static CacheTable cacheTableInstance;
    //Variables del dispatcher
    public static String dispatcherIp;
    public static int dispatcherPort;

    //Solo una instancia
    static {
        try {
            cacheTableInstance = new CacheTable();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    //Tabla de enrutamiento
    RoutingTable routingTable = RoutingTable.getInstance();

    //Lista de vecinos
    private List<Neighbour> localNetwork;

    /**
     * Crea la tabla caché
     * @throws UnknownHostException
     */
    private CacheTable() throws UnknownHostException {
        localNetwork = new ArrayList<Neighbour>();
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
        Neighbour newNode = new Neighbour(macAddress, ip, port, falseIp);
        localNetwork.add(newNode);
    }

    /**
     * Imprime la tabla caché
     */
    public synchronized void displayTable() {
        Iterator<Neighbour> iterator = localNetwork.iterator();
        System.out.print("\nNodos de Red Local\n");
        Neighbour actualObject;
        while (iterator.hasNext()) {
            actualObject = iterator.next();
            System.out.println("||" + actualObject.getMacAddress() + "||" + actualObject.getIp() + "||" + actualObject.getPort() + "||\n");
        }
    }

    /**
     * @return Devuelve la única instancia de tabla caché
     * @throws UnknownHostException
     */
    public static synchronized CacheTable getInstance() throws UnknownHostException {
        if (cacheTableInstance == null) {
            cacheTableInstance = new CacheTable();
        }
        return cacheTableInstance;
    }

    /**
     * @param macAddress
     * @return Devuelve un booleano que indica si un vecino existe o no
     */
    public boolean neighbourExists(String macAddress) {
        Iterator<Neighbour> iterator = localNetwork.iterator();
        Neighbour actualObject;
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
        Iterator<Neighbour> iterator = localNetwork.iterator();
        Neighbour actualObject;
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
        Iterator<Neighbour> iterator = localNetwork.iterator();
        Neighbour actualObject;
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
        Iterator<Neighbour> iterator = localNetwork.iterator();
        Neighbour actualObject;
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
        Iterator<Neighbour> iterator = localNetwork.iterator();
        Neighbour actualObject;
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
        Iterator<Neighbour> iterator = localNetwork.iterator();
        Neighbour actualObject;
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
