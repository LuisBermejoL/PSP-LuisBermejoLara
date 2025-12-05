package Ejercicios;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class TCPSocketCliente11 {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
		String host = "192.168.204.129";
		int Puerto = 6666;//puerto remoto
		
		System.out.println("PROGRAMA CLIENTE 1 INICIADO....");
		Socket Cliente = new Socket(host, Puerto);

		// Creación flujo de salida hacia el servidor
		DataOutputStream flujoSalida = new DataOutputStream(Cliente.getOutputStream());

        System.out.println("Escribe un número: ");
        int numero = sc.nextInt();
		
        System.out.println("Enviando el número " + numero + " al servidor desde el cliente 1");
		flujoSalida.writeInt(numero);

		// Creación flujo de entrada desde el servidor
		DataInputStream flujoEntrada = new  DataInputStream(Cliente.getInputStream());

		//Recibiendo datos del servidor
        long resultado = flujoEntrada.readLong();
		System.out.println("Recibiendo del servidor el resultado: " + resultado);

		// CERRAR STREAMS Y SOCKETS	
		flujoEntrada.close();
		flujoSalida.close();
		Cliente.close();
	}
}