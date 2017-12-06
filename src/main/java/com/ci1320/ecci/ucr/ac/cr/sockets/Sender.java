package com.ci1320.ecci.ucr.ac.cr.sockets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by lskev on 6-Nov-17.
 */
public class Sender implements Runnable{
    //variables del emisor
    protected Socket clientSocket;
    protected DataOutputStream outServer = null;
    protected String message;
    protected boolean connected = false;
    protected int port;
    protected String host;

    /**
     * Establece los parámetros de a quién se le va a enviar
     * @param port
     * @param host
     * @param message
     * @throws IOException
     */
    public Sender(int port, String host, String message) throws IOException {
        this.message = message;
        this.port = port;
        this.host = host;
    }

    /**
     * Intenta conectarse y enviar el mensaje
     * @throws IOException
     * @throws InterruptedException
     */
    public void startSender() throws IOException, InterruptedException {
        Boolean trying = false;
        /* Deprecated, ya no hay mensaje de error
        /*if(type.equals("error")){
            Thread.sleep(15000);
        }*/
        while (!connected) {
            try {
                if (trying) {
                    System.out.print("Reintentando conectarse al servidor " + host + "\n");
                }
                clientSocket = new Socket(host, port);
                outServer = new DataOutputStream(clientSocket.getOutputStream());
                outServer.write(message.getBytes(), 0, message.length());
                connected = true;
            } catch (Exception e) {
                connected = false;
                Thread.sleep(150000);
            }
        }
        //System.out.print("Final de conexion exitosa como cliente con: " + host + "\n");
        clientSocket.close();
    }

    /**
     * Método para correr hilo
     */
    public void run() {
        try {
            startSender();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
