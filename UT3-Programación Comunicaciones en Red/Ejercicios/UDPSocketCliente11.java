package Ejercicios;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPSocketCliente11 {
    private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
        int puerto = 6666; // Puerto por el que escucha el servidor
        InetAddress ipServidor = InetAddress.getByName("192.168.204.109"); // IP del servidor

		// Asocio el socket al puerto 6666 y la IP del cliente
		DatagramSocket clientSocket = new DatagramSocket(puerto, ipServidor);

		// Datos del servidor al que enviar mensaje
		InetAddress IPServidor = InetAddress.getByName("192.168.204.129");// IP del servidor
		int puertoServidor = 6666; // Puerto por el que escucha el servidor

        System.out.println("PROGRAMA CLIENTE 1 INICIADO....");

		// Introducir datos por teclado
		System.out.print("Escribe un número: ");
		int numero = sc.nextInt();

        // Convertir el número a bytes para enviarlo
		byte[] enviados = new byte[1024];
		enviados = String.valueOf(numero).getBytes();

		// Enviando datagrama al servidor
		DatagramPacket envio = new DatagramPacket(enviados, enviados.length, IPServidor, puertoServidor);
        System.out.println("Enviando el número " + numero + " al servidor desde el cliente 1");
		clientSocket.send(envio);

		// Recibiendo datagrama del servidor
		byte[] recibidos = new byte[1024];
		DatagramPacket mensajeS = new DatagramPacket(recibidos, recibidos.length);
		System.out.println("Esperando datagrama del servidor....");
		clientSocket.receive(mensajeS);

		// Convertir los datos recibidos a String y luego a long
		String mensajeTexto = new String(mensajeS.getData(), 0, mensajeS.getLength());
		mensajeTexto = mensajeTexto.trim();
        try {
            long resultado = Long.parseLong(mensajeTexto);
            System.out.println("Recibiendo del servidor el resultado: " + resultado);
        } catch (NumberFormatException e) {
            System.out.println("Error: Lo recibido no es un número válido. Recibido: " + mensajeTexto);
        }

		clientSocket.close();// Cerrar socket
	}
}