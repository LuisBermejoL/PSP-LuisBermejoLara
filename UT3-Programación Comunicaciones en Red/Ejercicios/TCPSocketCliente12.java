package Ejercicios;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class TCPSocketCliente12 {
    public static void main(String[] args) throws Exception {
		String host = "192.168.204.129";
		int Puerto = 6666;//puerto remoto
		
		System.out.println("PROGRAMA CLIENTE 2 INICIADO....");
		Socket Cliente = new Socket(host, Puerto);

        // Creación flujo de entrada desde el servidor al cliente 1
		DataInputStream flujoEntrada = new DataInputStream(Cliente.getInputStream());

		// Recibiendo datos del cliente 1
		int numero = flujoEntrada.readInt();
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

		// Creación flujo de salida hacia el servidor
		DataOutputStream flujoSalida = new DataOutputStream(Cliente.getOutputStream());
		
        // Enviando el resultado del factorial del número al servidor
        System.out.println("Enviando el resultado " + resultado + " al servidor desde el cliente 2");
		flujoSalida.writeLong(resultado);

		// CERRAR STREAMS Y SOCKETS	
		flujoEntrada.close();
		flujoSalida.close();
		Cliente.close();
	}
}