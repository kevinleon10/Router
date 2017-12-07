package com.ci1320.ecci.ucr.ac.cr.routingTable;

/**
 * Created by GrupoBolinchas on 3-Nov-17.
 */
public class Route {
    private String networkName;
    private String ip;
    private String through;
    private int distance;

    /**
     * Establece los valores de la red
     * @param networkName
     * @param ip
     * @param through
     * @param distance
     */
    public Route(String networkName, String ip, String through, int distance){
        this.networkName = networkName;
        this.ip = ip;
        this.through = through;
        this.distance = distance;
    }

    /**
     * @return Devuelve el nombre de una red
     */
    public String getNetworkName() {
        return this.networkName;
    }


    /**
     * @return Devuelve la ip de una red
     */
    public String getIp() {
        return this.ip;
    }

    /**
     * @return Devuelve a través de quién se llega a esa ruta
     */
    public String getThrough() {
        return this.through;
    }

    /**
     * @return Devuelve la distancia hacia una red
     */
    public int getDistance() {
        return this.distance;
    }
}
