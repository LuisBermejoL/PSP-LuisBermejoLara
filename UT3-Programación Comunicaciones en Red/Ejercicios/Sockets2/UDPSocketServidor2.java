package Ejercicios.Sockets2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSocketServidor2 {
    public static void main(String[] args) throws IOException {
        
        // Configuración del servidor UDP
        InetAddress ipServidor = InetAddress.getByName("192.168.204.129");
        int puertoServidor = 6666;
        
        try (DatagramSocket servidor = new DatagramSocket(puertoServidor, ipServidor)) {
            System.out.println("Servidor UDP escuchando en " + ipServidor.getHostAddress() + ":" + puertoServidor);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket recibo = new DatagramPacket(buffer, buffer.length);
                servidor.receive(recibo); // Espera array

                String datos = new String(recibo.getData(), 0, recibo.getLength());
                String[] partes = datos.split(",");
                int suma = 0, max = -1, min = 101;

                for (String s : partes) {
                    int n = Integer.parseInt(s.trim());
                    suma += n;
                    if (n > max) max = n;
                    if (n < min) min = n;
                }

                // Respuesta: Suma;Max;Min
                String res = suma + ";" + max + ";" + min;
                byte[] bOut = res.getBytes();
                
                // Enviamos de vuelta a la IP y puerto que envió el paquete
                DatagramPacket envio = new DatagramPacket(bOut, bOut.length, recibo.getAddress(), recibo.getPort());
                servidor.send(envio);
            }
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}