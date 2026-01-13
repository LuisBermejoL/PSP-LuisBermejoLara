package Ejercicios.Sockets1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSocketServidor1 {
    public static void main(String[] args) throws IOException {
        
        // Configuración del servidor UDP
        InetAddress ipServidor = InetAddress.getByName("192.168.204.129");
        int puertoServidor = 6666;
        
        // Crear el socket UDP del servidor
        DatagramSocket servidor = new DatagramSocket(puertoServidor, ipServidor);
        System.out.println("Servidor UDP escuchando en " + ipServidor.getHostAddress() + ":" + puertoServidor);

        byte[] buffer = new byte[1024];

        // ----------------------------------------------------------------
        // PASO 1: Recibir el NÚMERO del Cliente 1
        // ----------------------------------------------------------------
        System.out.println("Esperando al Cliente 1...");
        
        DatagramPacket paqueteCliente1 = new DatagramPacket(buffer, buffer.length);
        servidor.receive(paqueteCliente1); // BLOQUEO: Espera mensaje

        // Guardamos la dirección del Cliente 1 para responderle luego
        InetAddress ipCliente1 = paqueteCliente1.getAddress();
        int puertoCliente1 = paqueteCliente1.getPort();
        
        // Extraer el número enviado por el Cliente 1
        String mensajeC1 = new String(paqueteCliente1.getData(), 0, paqueteCliente1.getLength());
        int numero = Integer.parseInt(mensajeC1.trim());
        
        System.out.println("Recibido del Cliente 1 (" + ipCliente1 + ":" + puertoCliente1 + ") el número: " + numero);

        // ----------------------------------------------------------------
        // PASO 2: Detectar al Cliente 2
        // ----------------------------------------------------------------
        System.out.println("Esperando conexión (handshake) del Cliente 2...");
        
        // Reutilizamos el buffer o creamos uno nuevo
        DatagramPacket paqueteCliente2 = new DatagramPacket(buffer, buffer.length);
        servidor.receive(paqueteCliente2); // BLOQUEO: Espera a que el Cliente 2 diga "hola"

        InetAddress ipCliente2 = paqueteCliente2.getAddress();
        int puertoCliente2 = paqueteCliente2.getPort();
        
        System.out.println("Cliente 2 detectado en: " + ipCliente2 + ":" + puertoCliente2);

        // ----------------------------------------------------------------
        // PASO 3: Enviar el número al Cliente 2
        // ----------------------------------------------------------------
        System.out.println("Reenviando número " + numero + " al Cliente 2");
        
        byte[] datosParaC2 = String.valueOf(numero).getBytes();
        DatagramPacket envioC2 = new DatagramPacket(datosParaC2, datosParaC2.length, ipCliente2, puertoCliente2);
        servidor.send(envioC2);

        // ----------------------------------------------------------------
        // PASO 4: Recibir el resultado del Cliente 2
        // ----------------------------------------------------------------
        System.out.println("Esperando resultado del cálculo del Cliente 2...");
        
        // Usamos un paquete nuevo o reutilizamos
        DatagramPacket respuestaC2 = new DatagramPacket(buffer, buffer.length);
        servidor.receive(respuestaC2); // Esperamos el factorial

        String mensajeRespuesta = new String(respuestaC2.getData(), 0, respuestaC2.getLength());
        long resultado = Long.parseLong(mensajeRespuesta.trim()); // Parseamos a long

        System.out.println("Recibido resultado: " + resultado);

        // ----------------------------------------------------------------
        // PASO 5: Enviar el resultado al Cliente 1
        // ----------------------------------------------------------------
        System.out.println("Enviando resultado final al Cliente 1");
        
        byte[] datosParaC1 = String.valueOf(resultado).getBytes();
        DatagramPacket envioC1 = new DatagramPacket(datosParaC1, datosParaC1.length, ipCliente1, puertoCliente1);
        servidor.send(envioC1);

        // Cerrar socket
        System.out.println("Proceso terminado. Cerrando servidor.");
        servidor.close();
    }
}