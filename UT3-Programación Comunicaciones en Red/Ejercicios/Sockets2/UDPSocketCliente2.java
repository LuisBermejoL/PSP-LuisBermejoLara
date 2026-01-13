package Ejercicios.Sockets2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class UDPSocketCliente2 {
	public static void main(String[] args) throws Exception {
        int puertoCliente = 6667; // Puerto por el que escucha el servidor
        InetAddress ipCliente = InetAddress.getByName("192.168.204.109"); // IP del servidor

		// Asocio el socket al puerto 6667 y la IP del cliente
		DatagramSocket clientSocket = new DatagramSocket(puertoCliente, ipCliente);
        
		// Datos del servidor al que enviar mensaje
		InetAddress ipServidor = InetAddress.getByName("192.168.204.129");// IP del servidor
		int puertoServidor = 6666; // Puerto por el que escucha el servidor

        // Generar 10 números
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for(int i=0; i<10; i++) {
            sb.append(r.nextInt(100)).append(i<9 ? "," : "");
        }

        // Enviar al servidor
        byte[] msg = sb.toString().getBytes();
        DatagramPacket paquete = new DatagramPacket(msg, msg.length, ipServidor, puertoServidor);
        clientSocket.send(paquete);

        // Recibir resultados
        byte[] bufferIn = new byte[1024];
        DatagramPacket respuesta = new DatagramPacket(bufferIn, bufferIn.length);
        clientSocket.receive(respuesta);

        System.out.println("Resultados (Suma;Max;Min): " + new String(respuesta.getData(), 0, respuesta.getLength()));
        clientSocket.close();
    }
}