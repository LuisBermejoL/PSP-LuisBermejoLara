package Ejercicios.Sockets1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPSocketServidor1 {
    public static void main(String[] arg) throws IOException {
        InetAddress ipServidor = InetAddress.getByName("192.168.204.129");
		int numeroPuerto = 6666;// Puerto
		ServerSocket servidor = new ServerSocket(numeroPuerto, 2, ipServidor);

        System.out.println("Servidor escuchando en " + ipServidor.getHostAddress() + ":" + numeroPuerto);
		
		// Creación de la conexion al cliente 1
		Socket cliente1Conectado = servidor.accept();
        System.out.println("Cliente1 conectado desde: " 
            + cliente1Conectado.getInetAddress().getHostAddress() + ":" + cliente1Conectado.getPort());

		// Creación de la conexion al cliente 2
		Socket cliente2Conectado = servidor.accept();
        System.out.println("Cliente2 conectado desde: " 
            + cliente2Conectado.getInetAddress().getHostAddress() + ":" + cliente2Conectado.getPort());

		// Creación flujo de entrada desde el servidor al cliente 1
		InputStream entrada1 = null;
		entrada1 = cliente1Conectado.getInputStream();
		DataInputStream flujoEntrada1 = new DataInputStream(entrada1);

		//Recibiendo datos del cliente 1
		int numero = flujoEntrada1.readInt();
		System.out.println("Recibiendo del cliente 1 el número: " + numero);

		// Creación flujo de salida hacia el cliente 2 desde el servidor
		OutputStream salida2 = null;
		salida2 = cliente2Conectado.getOutputStream();
		DataOutputStream flujoSalida2 = new DataOutputStream(salida2);

		//Enviando datos al cliente 2
		System.out.println("Enviando el número " + numero + " al cliente 2 desde el servidor");
		flujoSalida2.writeInt(numero);

		// Creación flujo de entrada desde el servidor al cliente 2
		InputStream entrada2 = null;
		entrada2 = cliente2Conectado.getInputStream();
		DataInputStream flujoEntrada2 = new DataInputStream(entrada2);

		//Recibiendo datos del cliente 2
        long resultado = flujoEntrada2.readLong();
		System.out.println("Recibiendo del cliente 2 el resultado: " + resultado);

		// Creación flujo de salida hacia el cliente 2 desde el servidor
		OutputStream salida1 = null;
		salida1 = cliente1Conectado.getOutputStream();
		DataOutputStream flujoSalida1 = new DataOutputStream(salida1);

		//Enviando datos al cliente 1
		System.out.println("Enviando el resultado " + resultado + " al cliente 1 desde el servidor");
		flujoSalida1.writeLong(resultado);

		// Cierre streams y sockets
		entrada1.close();
		flujoEntrada1.close();
		entrada2.close();
		flujoEntrada2.close();

		salida1.close();
		flujoSalida1.close();
		salida2.close();
		flujoSalida2.close();
		
        cliente1Conectado.close();
		cliente2Conectado.close();
		servidor.close();
	}// main
}