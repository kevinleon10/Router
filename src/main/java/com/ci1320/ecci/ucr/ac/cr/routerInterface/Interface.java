package com.ci1320.ecci.ucr.ac.cr.routerInterface;

import com.ci1320.ecci.ucr.ac.cr.cacheTable.CacheTable;
import com.ci1320.ecci.ucr.ac.cr.cacheTable.Neighbour;
import com.ci1320.ecci.ucr.ac.cr.routingTable.RoutingTable;
import com.ci1320.ecci.ucr.ac.cr.sockets.Receiver;
import com.ci1320.ecci.ucr.ac.cr.sockets.Sender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Iterator;

/**
 * Created by lskev on 7-Nov-17.
 */
public class Interface {
    public static String myMacAddress;
    public static int myServerPort;
    public static String myIp;
    public static String falseIp;

    public static void main(String args[]) throws IOException {

        //Para leer de la entrada estándar
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader (inputStreamReader);

        //Obtengo mi dirección y puerto
        myMacAddress = "Bolinchas.Kevin";
        falseIp = "140.90.0.10";
        System.out.println("Digite su ip");
        myIp = bufferedReader.readLine();
        System.out.println("Digite su puerto");
        myServerPort = Integer.parseInt (bufferedReader.readLine());
        System.out.print("IP local: " + myIp + "\n" + "Direccion Fisica: " + myMacAddress + "\n" + "Direccion Falsa: " + falseIp + "\n");

        //Obtengo la dirección y puerto de la memoria compartida
        System.out.println("Digite la ip de su memoria compartida");
        String sharedMemoryIp = bufferedReader.readLine();
        System.out.println("Digite el puerto de su memoria compartida");
        int sharedMemoryPort = Integer.parseInt (bufferedReader.readLine());

        //Crear tabla local de enrutamiento.
        RoutingTable routingTable = RoutingTable.getInstance();

        //Crear Tabla de red local.
        CacheTable cacheTable = CacheTable.getInstance();
        cacheTable.addNeighbour(myMacAddress, myIp, myServerPort, falseIp);

        //Obtengo la IP y puerto del Dispatcher
        System.out.println("Digite la ip del dispatcher");
        cacheTable.dispatcherIp = bufferedReader.readLine();
        System.out.println("Digite el puerto del dispatcher");
        cacheTable.dispatcherPort = Integer.parseInt (bufferedReader.readLine());

        //Iniciar puerto para escuchar.
        (new Thread(new Receiver(myServerPort, myMacAddress, sharedMemoryPort, sharedMemoryIp, falseIp))).start();   //Servidor global.
        System.out.print("Servidor Global Iniciado en el puerto " + myServerPort + "\n");

        routingTable.showTable();

        //Decirle al dispatcher mi ip y puerto.
        (new Thread(new Sender(cacheTable.dispatcherPort, cacheTable.dispatcherIp, myMacAddress + ";" + myIp + ";" + myServerPort + ";" + falseIp))).start();
        System.out.print("Presentado con el dispatcher\n");

        cacheTable.displayTable();

    }
}
