package Ejercicios;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSocketCliente12 {
    public static void main(String[] args) throws IOException {
        int puertoCliente = 6667; // Puerto por el que escucha el servidor
        InetAddress ipCliente = InetAddress.getByName("192.168.204.109"); // IP del servidor

		// Asocio el socket al puerto 6667 y la IP del cliente
		DatagramSocket clientSocket = new DatagramSocket(puertoCliente, ipCliente);
        
		// Datos del servidor al que enviar mensaje
		InetAddress IPServidor = InetAddress.getByName("192.168.204.129");// IP del servidor
		int puertoServidor = 6666; // Puerto por el que escucha el servidor

        System.out.println("PROGRAMA CLIENTE 2 INICIADO....");

        // Enviar un paquete de saludo al servidor para ser detectado como Cliente 2
        String saludo = "hola";
        byte[] saludoBytes = saludo.getBytes();
        DatagramPacket saludoPacket = new DatagramPacket(saludoBytes, saludoBytes.length, IPServidor, puertoServidor);
        System.out.println("Enviando saludo al servidor para ser detectado como Cliente 2");
        clientSocket.send(saludoPacket);

        // Recibiendo datagrama del servidor
		byte[] recibidos = new byte[1024];
		DatagramPacket mensajeS = new DatagramPacket(recibidos, recibidos.length);
		System.out.println("Esperando datagrama del servidor....");
		clientSocket.receive(mensajeS);

		// Convertir los datos recibidos a String y luego a long
		String mensajeTexto = new String(mensajeS.getData(), 0, mensajeS.getLength());
		mensajeTexto = mensajeTexto.trim();
        int numero = Integer.parseInt(mensajeTexto);
        System.out.println("Recibiendo del servidor el número: " + numero);
        
        // Calculo del factorial del número
        long resultado = 1;

        if (numero == 0) {
            resultado = 1;
        } else {
            for (int i = 1; i <= numero; i++) {
                resultado = resultado * i;
            }
        }

        // Convertir el resultado a bytes para enviarlo
		byte[] enviados = new byte[1024];
		enviados = String.valueOf(resultado).getBytes();

		// Enviando datagrama al servidor
		DatagramPacket envio = new DatagramPacket(enviados, enviados.length, IPServidor, puertoServidor);
        System.out.println("Enviando el resultado " + resultado + " al servidor desde el cliente 2");
		clientSocket.send(envio);

		clientSocket.close();// Cerrar socket
	}
}