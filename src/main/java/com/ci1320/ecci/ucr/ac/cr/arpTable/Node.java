package com.ci1320.ecci.ucr.ac.cr.arpTable;

/**
 * Created by GrupoBolinchas on 5-Nov-17.
 */
public class Node {
    private String macAddress;
    private String ip;
    private int port;
    private String falseIp;

    /**
     * Establece los valores de un vecino
     * @param macAddress
     * @param ip
     * @param port
     * @param falseIp
     */
    public Node(String macAddress, String ip, int port, String falseIp) {
        this.macAddress = macAddress;
        this.ip = ip;
        this.falseIp = falseIp;
        this.port = port;
    }

    /**
     * @return Devuelve la dirección física de un vecino
     */
    public String getMacAddress() {
        return this.macAddress;
    }

    /**
     * @return Devuelve la ip real de un vecino
     */
    public String getIp() {
        return this.ip;
    }


    /**
     * Establece la ip real de un vecino
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return Devuelve el puerto de un vecino
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Establece el puerto de un vecino
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Devuelve la direccion falsa de un vecino
     * @return
     */
    public String getFalseIp() {
        return falseIp;
    }

    /**
     * Establece la ip falsa de un vecino
     * @param falseIp
     */
    public void setFalseIp(String falseIp) {
        this.falseIp = falseIp;
    }
}
