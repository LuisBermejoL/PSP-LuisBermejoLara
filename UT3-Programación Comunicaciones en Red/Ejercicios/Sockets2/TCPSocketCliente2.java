package Ejercicios.Sockets2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class TCPSocketCliente2 {
    public static void main(String[] args) throws IOException {
        // IP de la máquina donde está el servidor
        String ipServidor = "192.168.204.129"; 
        int puerto = 6666;

        Socket socket = new Socket(ipServidor, puerto);
        DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
        DataInputStream entrada = new DataInputStream(socket.getInputStream());

        Random ran = new Random();
        for (int i = 0; i < 10; i++) {
            salida.writeInt(ran.nextInt(100));
        }

        System.out.println("Suma: " + entrada.readInt());
        System.out.println("Máximo: " + entrada.readInt());
        System.out.println("Mínimo: " + entrada.readInt());

        socket.close();
    }
}