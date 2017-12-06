package com.ci1320.ecci.ucr.ac.cr.routingTable;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lskev on 3-Nov-17.
 */
public class RoutingTable {

    //Solo hay una instancia de tabla de enrutamiento para todos
    public static RoutingTable routingTableInstance = new RoutingTable();

    //Tabla de enrutamiento
    private List<Route> routingTable;


    /**
     * Crea la tabla de enrutamiento
     */
    public RoutingTable(){
        this.routingTable = new ArrayList<Route>();
        this.createRoutingTable();
    }


    /**
     * Hardcode agrega los valores de las rutas a la tabla de enrutamiento
     */
    public void createRoutingTable(){
        Route carritos = new Route("Carritos", "165.8.0.0", "LEGO3", 2);
        routingTable.add(carritos);
        Route paletas = new Route("Paletas", "200.5.0.0", "Bolinchas.Daniel", 1);
        routingTable.add(paletas);
        Route luces = new Route("Luces", "25.0.0.0", "LEGO3", 1);
        routingTable.add(luces);
        Route legos = new Route("Legos", "201.6.0.0", "lEGO3", 0);
        routingTable.add(legos);
        Route banderas = new Route("Banderas", "12.0.0.0", "Bolinchas.Daniel", 2);
        routingTable.add(banderas);
        Route bolinchas = new Route("Bolinchas", "140.90.0.0", "Bolinchas.Jorge", 0);
        routingTable.add(bolinchas);
    }

    /**
     * Imprime la tabla de enrutamiento
     */
    public void showTable(){
        Iterator<Route> iterator = routingTable.iterator();
        System.out.print("\nTabla de Enrutamiento Local\n");
        Route actualObject;
        while (iterator.hasNext()) {
            actualObject = iterator.next();
            System.out.println("||" + actualObject.getNetworkName() + "||" + actualObject.getIp() + "||" + actualObject.getThrough() + "||" + actualObject.getDistance() + "||\n");
        }
    }

    /**
     * @param ip
     * @return Devuelve un booleano que indica si una red es alcanzable o no
     */
    public boolean isReachable(String ip) {
        Iterator<Route> iterator = routingTable.iterator();
        Route actualObject;
        boolean found = false;
        while (iterator.hasNext() && !found) {
            actualObject = iterator.next();
            if (actualObject.getIp().equals(ip)) {
                found = true;
            }
        }
        return found;
    }

    /**
     * @param ip
     * @return Devuelve a través de quién se llega a una red
     */
    public String getThrough(String ip) {
        Iterator<Route> iterator = routingTable.iterator();
        Route actualObject;
        boolean found = false;
        String through = "";
        while (iterator.hasNext() && !found) {
            actualObject = iterator.next();
            if (actualObject.getIp().equals(ip)) {
                found = true;
                through = actualObject.getThrough();
            }
        }
        return through;
    }

    /**
     * @param ip
     * @return Devuelve la distancia a la que se encuentra una red
     */
    public int getDistance(String ip) {
        Iterator<Route> iterator = routingTable.iterator();
        Route actualObject;
        boolean found = false;
        int distance = -1;
        while (iterator.hasNext() && !found) {
            actualObject = iterator.next();
            if (actualObject.getIp().equals(ip)) {
                found = true;
                distance = actualObject.getDistance();
            }
        }
        return distance;
    }


    /**
     * @return Devuelve la instancia de la tabla de enrutamiento, la cual es única
     */
    public static synchronized RoutingTable getInstance() {
        if (routingTableInstance == null) {
            routingTableInstance = new RoutingTable();
        }
        return routingTableInstance;
    }

    /**
     * @param ip
     * @return Devuelve la red a la cual pertenece una ip, según su tipo
     */
    public String equal(String ip){
        String[] Ip = ip.split("\\.");
        String network;

        int firstValue = Integer.parseInt(Ip[0]);

        if(firstValue > 0 && firstValue <=127 ){
            network = Ip[0]+".0.0.0";
        }else if(firstValue > 127 && firstValue <=192){
            network = Ip[0]+"."+Ip[1]+".0.0";
        }else{
            network = Ip[0]+"."+Ip[1]+"."+Ip[2]+".0";
        }
        return network;
    }
}
