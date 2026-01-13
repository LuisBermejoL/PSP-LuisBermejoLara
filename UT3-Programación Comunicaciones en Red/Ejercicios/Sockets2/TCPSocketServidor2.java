package Ejercicios.Sockets2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPSocketServidor2 {
    public static void main(String[] args) throws IOException {
        // El servidor escucha en SU PROPIA IP
        String ipServidor = "192.168.204.129";
        int puerto = 6666;
        
        try (ServerSocket servidor = new ServerSocket(puerto, 50, InetAddress.getByName(ipServidor))) {
            System.out.println("Servidor TCP iniciado en " + ipServidor + ":" + puerto);

            while (true) {
                Socket cliente = servidor.accept();
                DataInputStream entrada = new DataInputStream(cliente.getInputStream());
                DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());

                int suma = 0, max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;

                for (int i = 0; i < 10; i++) {
                    int n = entrada.readInt();
                    suma += n;
                    if (n > max) max = n;
                    if (n < min) min = n;
                }

                salida.writeInt(suma);
                salida.writeInt(max);
                salida.writeInt(min);
                cliente.close();
            }
        }
    }
}