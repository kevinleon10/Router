package com.ci1320.ecci.ucr.ac.cr.sockets;

import com.ci1320.ecci.ucr.ac.cr.cacheTable.CacheTable;
import com.ci1320.ecci.ucr.ac.cr.routingTable.RoutingTable;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by lskev on 6-Nov-17.
 */
public class Receiver implements Runnable{
    //variables necesarias para recibir mensajes
    protected String serverMessage;
    protected ServerSocket serverSocket;
    protected Socket clientSocket;
    protected int port;
    protected String myMacAddress;
    protected int sharedMemoryPort;
    protected String sharedMemoryIp;
    protected String falseIp;
    RoutingTable routingTable = RoutingTable.getInstance();
    CacheTable cacheTable = CacheTable.getInstance();

    /**
     * Establece los valores del servidor
     * @param port
     * @param macAddress
     * @param sharedMemoryPort
     * @param sharedMemoryIp
     * @param falseIp
     * @throws IOException
     */
    public Receiver(int port, String macAddress, int sharedMemoryPort, String sharedMemoryIp, String falseIp) throws IOException {
        this.port = port;
        this.myMacAddress = macAddress;
        this.sharedMemoryPort = sharedMemoryPort;
        this.sharedMemoryIp = sharedMemoryIp;
        this.falseIp = falseIp;
    }

    /**
     * Empieza a recibir
     * @throws IOException
     */
    public void startReceiver() throws IOException {
        while (true) {
            clientSocket = serverSocket.accept();
            Thread t = new ListenToClientThread(clientSocket);
            t.start();
        }
    }

    /**
     * Método para correr el hilo
     */
    public void run() {
        try {
            this.serverSocket = new ServerSocket(port);
            this.clientSocket = new Socket();
            this.startReceiver();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clase que se encarga de procesar los mensajes que llegan
     */
    class ListenToClientThread extends Thread {

        private Socket socket;

        /**
         * Establece el socket
         * @param socket
         */
        ListenToClientThread(Socket socket) {
            this.socket = socket;
        }

        /**
         * Metodo para correr el hilo y ver que hacer para cada caso
         */
        @Override
        public void run() {

            //System.out.println("Cliente " + socket.getInetAddress().getHostAddress() + " conectado al servidor\n");

            BufferedReader input = null;
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                while ((serverMessage = input.readLine()) != null) {
                    String[] msg = serverMessage.split(";");
                    switch (msg.length) {
                        case 3: //Broadcast
                            if (msg[1].equals("*")) { //Broadcast, mensaje = Bolinchas.Jorge;*;IpDistanciaSolicitada
                                //System.out.print("Broadcast " + msg[2]);
                                String ip = routingTable.equal(msg[2]); //se obtiene la red de esa ip
                                System.out.print("Broadcast " + ip + "\n");
                                if (routingTable.isReachable(ip)) { //si es alcanzable se manda la distancia
                                    int distance = routingTable.getDistance(ip);
                                    //Respuesta a broadcast = Bolinchas.Kevin;*;IpDistanciaSolicitada;distancia
                                    (new Thread(new Sender(cacheTable.getNeighbourPort(msg[0]), cacheTable.getNeighbourIp(msg[0]), myMacAddress + ";*;" + msg[2] + ";" + distance))).start();
                                }
                                else if(msg[2].equals(falseIp)){ //si pregunta por mí
                                    (new Thread(new Sender(cacheTable.getNeighbourPort(msg[0]), cacheTable.getNeighbourIp(msg[0]), myMacAddress + ";*;" + msg[2] + ";" + 0))).start();

                                }else { //si no es alcanzable se manda -1 en la distancia
                                    (new Thread(new Sender(cacheTable.getNeighbourPort(msg[0]), cacheTable.getNeighbourIp(msg[0]), myMacAddress + ";*;" + msg[2] + ";" + -1))).start();
                                }
                            }
                            else{
                                System.out.println("Nuevo Mensaje con formato desconocido: " + serverMessage+"\n");
                            }
                            break;
                        case 4: //shareMemory o dispatcher
                            //System.out.println(msg[0]+msg[1]+msg[2]+msg[3]);
                            //msg[0]="share";
                            if (msg[0].equals("share")) { //Mensaje de memoria compartida, mensaje = share;ipSender;ipReceiver;message
                                //Es mensaje de sharedMemory.
                                String ip = routingTable.equal(msg[2]);
                                // if(msg[2].equals("123.45.67.8")){ //si es para mi
                                if(msg[2].equals(falseIp)){ //si es para mi
                                    System.out.println("Nuevo mensaje para "+myMacAddress+" desde la memoria compartida\n");
                                    System.out.println("El mensaje es: "+msg[3]+" y fue enviado por "+msg[1]+"\n");
                                }
                                else if(routingTable.isReachable(ip)){
                                    String through = routingTable.getThrough(ip);
                                    if(cacheTable.neighbourExists(through)){ //si conozco ese atraves
                                        //Respuesta a mensaje de memoria compartida = Bolinchas.Kevin;Bolinchas;ipSender;ipReceiver;message
                                        (new Thread(new Sender(cacheTable.getNeighbourPort(through), cacheTable.getNeighbourIp(through), myMacAddress+";"+through+";"+msg[1] + ";" + msg[2] + ";" + msg[3]))).start();
                                        System.out.println("Mensaje recibido desde la memoria compartida enviado a "+through);
                                    }
                                    else{ //si no lo pongo de nuevo en la memoria compartida
                                        (new Thread(new Sender(sharedMemoryPort, sharedMemoryIp, "share;" + msg[1] + ";" + msg[2] + ";" + msg[3]))).start();
                                        System.out.println("Mensaje recibido desde la memoria compartida enviado a "+through);
                                    }
                                }
                                else{ //si no es alcanzable
                                    System.out.println("La red "+ip+" no es alcanzable por parte del nodo "+myMacAddress+"\n");
                                }
                            }
                            else{ //Dispatcher, mensaje = macAddress;ip;port;falseIp
                                if (cacheTable.neighbourExists(msg[0])) {
                                    //Editar datos del vecino.
                                    cacheTable.editNeighbour(msg[0], msg[1], Integer.parseInt(msg[2]), msg[3]);
                                    //System.out.println("Se ha actualizado la tabla de red local.");
                                    //cacheTable.displayTable();
                                } else {
                                    //Agregar vecino
                                    cacheTable.addNeighbour(msg[0], msg[1], Integer.parseInt(msg[2]), msg[3]);
                                    System.out.println("Se ha agregado "+msg[0]+" a la tabla de red local\n");
                                    //cacheTable.displayTable();
                                }
                                //Respuesta al dispatcher = Bolinchas.Kevin;macAddress;ok
                                (new Thread(new Sender(cacheTable.dispatcherPort, cacheTable.dispatcherIp, myMacAddress + ";" + msg[0] + ";" + "ok"))).start();
                            }
                            break;
                        case 5: //Mensaje de Daniel o Jorge, mensaje = macAddressSender;macAddressReceiver;ipSender;ipReceiver;message
                            if (msg[1].equals(myMacAddress)) {
                                //Es mensaje normal.
                                String ip = routingTable.equal(msg[3]);
                                String network = routingTable.equal(falseIp);
                                if(ip.equals(network)){ // si es para la red interna
                                    if(msg[3].equals(falseIp)){ //si es para mi
                                        System.out.println("Nuevo mensaje para "+myMacAddress+"\n");
                                        System.out.println("El mensaje es: "+msg[4]+" y fue enviado por "+msg[2]+"\n");
                                    }
                                    else if(cacheTable.isNeighbour(msg[3])){ //si es par Daniel o Jorge
                                        String through = cacheTable.getThrough(msg[3]);
                                        (new Thread(new Sender(cacheTable.getNeighbourPort(through), cacheTable.getNeighbourIp(through), myMacAddress+";"+through+";"+msg[2] + ";" + msg[3] + ";" + msg[4]))).start();
                                        System.out.println("Mensaje recibido desde "+msg[2]+" enviado a "+through);
                                    }
                                }
                                else { //si es para los que conoce Pao
                                    System.out.println("Se escribio mensaje desde "+myMacAddress+" a la memoria compartida\n");
                                    (new Thread(new Sender(sharedMemoryPort, sharedMemoryIp, "share;" + msg[2] + ";" + msg[3] + ";" + msg[4]))).start();
                                }
                            }
                            else{
                                System.out.println("Nuevo Mensaje con formato desconocido: " + serverMessage+"\n");
                            }
                            break;
                        /* Deprecated, ya el dispatcher no responde al router, solo manda los vecinos sin haberselo solicitado
                        case 2:
                            //Respuesta negativa del dispatcher
                            System.out.print(serverMessage + "\n");
                            (new Thread(new Sender(cacheTable.dispatcherPort, cacheTable.dispatcherIp, msg[0] + ";" + "*", "error"))).start();
                            break;
                        */
                        default:
                            System.out.println("Nuevo Mensaje con formato desconocido: " + serverMessage+"\n");
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
