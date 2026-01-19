package Ejercicios.Sockets3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSocketServidor3 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        // Configuración del servidor UDP
        InetAddress ipServidor = InetAddress.getByName("192.168.1.43");
        int puertoServidor = 6666;
        
        try (DatagramSocket servidor = new DatagramSocket(puertoServidor, ipServidor)) {
            System.out.println("Servidor UDP esperando facturas en el puerto " + puertoServidor + "...");

            byte[] buffer = new byte[1024]; // Buffer para recibir el objeto

            // 1. Recibir el paquete del cliente (El programa se detiene aquí hasta que llegue algo)
            DatagramPacket recibo = new DatagramPacket(buffer, buffer.length);
            servidor.receive(recibo);

            // 2. Deserializar: Convertir bytes a objeto Factura
            Factura factura = (Factura) deserializar(recibo.getData());
            System.out.println("Factura recibida: " + factura.getNumeroFactura());

            // 3. Lógica de Negocio (Calcular IVA y Total)
            calcularFactura(factura);

            // 4. Serializar: Convertir objeto modificado a bytes
            byte[] datosVuelta = serializar(factura);

            // 5. Enviar de vuelta al cliente
            InetAddress ipCliente = recibo.getAddress();
            int puertoCliente = recibo.getPort();
            DatagramPacket envio = new DatagramPacket(datosVuelta, datosVuelta.length, ipCliente, puertoCliente);
            servidor.send(envio);
            
            System.out.println("Factura procesada y enviada. Apagando servidor...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void calcularFactura(Factura f) {
        double porcentaje = 0;
        
        // Lógica solicitada en el enunciado
        switch (f.getTipoIVA().toUpperCase()) {
            case "IGC": porcentaje = 0.07; break;
            case "ESP": porcentaje = 0.21; break;
            case "EUR": porcentaje = 0.15; break;
            default:    porcentaje = 0.21; break; // Por defecto
        }

        double ivaCalculado = f.getImporteFactura() * porcentaje;
        double total = f.getImporteFactura() + ivaCalculado;

        f.setIVA(ivaCalculado);
        f.setImporteTotal(total);
    }

    // --- Métodos Auxiliares para convertir Objetos a Bytes y viceversa ---
    
    private static byte[] serializar(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();
        return baos.toByteArray();
    }

    private static Object deserializar(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }
}