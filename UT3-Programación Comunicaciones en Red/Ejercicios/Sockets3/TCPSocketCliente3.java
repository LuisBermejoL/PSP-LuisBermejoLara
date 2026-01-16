package Ejercicios.Sockets3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Scanner;
public class TCPSocketCliente3 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        try (Scanner teclado = new Scanner(System.in)) {
            // IP de la máquina donde está el servidor
            String ipServidor = "192.168.204.129"; 
            int puerto = 6666;

            System.out.println("PROGRAMA CLIENTE INICIADO....");
            Socket cliente = new Socket(ipServidor, puerto);	

            System.out.println("Escribe el numero de la factura a enviar: ");
            String numeroFactura = teclado.next();

            System.out.println("Escribe el dia de la fecha de la factura a enviar: ");
            int dia = teclado.nextInt();
            System.out.println("Escribe el mes de la fecha de la factura a enviar: ");
            int mes = teclado.nextInt();
            System.out.println("Escribe el año de la fecha de la factura a enviar: ");
            int anio = teclado.nextInt();

            System.out.println("Escribe el importe de la factura a enviar: ");
            int importeFactura = teclado.nextInt();

            System.out.println("Escribe el tipo de IVA de la factura a enviar (IGC, ESP, EUR): ");
            String tipoIVA = teclado.next();

            //Creo el objeto Factura para enviar
            Factura factura = new Factura(numeroFactura, LocalDate.of(anio, mes, dia), importeFactura, tipoIVA);

            //Flujo de salida para objetos
            ObjectOutputStream perSal = new ObjectOutputStream(cliente.getOutputStream());

            // Se envia el objeto
            perSal.writeObject(factura);
            System.out.println("Envio la factura: " + factura.getNumeroFactura());
            
            //Flujo de entrada para objetos
            ObjectInputStream perEnt = new ObjectInputStream(cliente.getInputStream());

            // Se recibe un objeto
            Factura facturaRecibida = (Factura) perEnt.readObject();
            System.out.println("Recibo la factura " + facturaRecibida.getNumeroFactura() + " con IVA: " + facturaRecibida.getIVA() + " y importe total: " + facturaRecibida.getImporteTotal());
                
            // CERRAR STREAMS Y SOCKETS
            perEnt.close();
            perSal.close();
            cliente.close();
        }	catch (Exception e) {
            e.printStackTrace();
        }
    }
}