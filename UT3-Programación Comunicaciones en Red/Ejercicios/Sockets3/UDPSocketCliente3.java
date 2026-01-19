package Ejercicios.Sockets3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UDPSocketCliente3 {
    public static void main(String[] args) {
        // Configuración
        String ipDestino = "192.168.1.43"; // localhost o la IP "192.168.1.43" si es otra máquina
        int puertoDestino = 6666;
        try (Scanner teclado = new Scanner(System.in)) {
            try (DatagramSocket clientSocket = new DatagramSocket()) {
                
                // 1. Pedir datos al usuario
                System.out.println("--- GENERAR FACTURA ---");
                System.out.print("Número Factura (ej: F-2023-01): ");
                String numero = teclado.next();

                System.out.print("Fecha (dd/MM/yyyy): ");
                String fechaStr = teclado.next();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate fecha = LocalDate.parse(fechaStr, formatter);

                System.out.print("Importe (€): ");
                double importe = teclado.nextDouble();
                teclado.nextLine(); // Consumir el salto de línea

                System.out.print("Tipo IVA (IGC, ESP, EUR): ");
                String tipoIva = teclado.nextLine();

                // 2. Crear objeto Factura
                Factura factura = new Factura(numero, fecha, importe, tipoIva);

                // 3. Serializar (Objeto -> Bytes) y Enviar
                byte[] msg = serializar(factura);
                InetAddress ipServidor = InetAddress.getByName(ipDestino);
                DatagramPacket envio = new DatagramPacket(msg, msg.length, ipServidor, puertoDestino);
                clientSocket.send(envio);
                System.out.println("Factura enviada al servidor...");

                // 4. Recibir respuesta
                byte[] buffer = new byte[1024];
                DatagramPacket recibo = new DatagramPacket(buffer, buffer.length);
                clientSocket.receive(recibo);

                // 5. Deserializar (Bytes -> Objeto)
                Factura facturaRecibida = (Factura) deserializar(recibo.getData());
                // 6. Mostrar resultados
                System.out.println("Recibo la factura " + facturaRecibida.getNumeroFactura() + " con IVA: " + facturaRecibida.getIVA() + " y importe total: " + facturaRecibida.getImporteTotal());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // --- Mismos métodos auxiliares que en el servidor ---

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