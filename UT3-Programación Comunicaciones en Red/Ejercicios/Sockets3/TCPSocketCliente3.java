package Ejercicios.Sockets3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
public class TCPSocketCliente3 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // IP de la máquina donde está el servidor
        String ipServidor = "192.168.204.129"; 
        int puerto = 6666;

        System.out.println("PROGRAMA CLIENTE INICIADO....");
        Socket cliente = new Socket(ipServidor, puerto);	
        
        //Creo el objeto Factura para enviar
        Factura factura = new Factura("F-2023-01", LocalDate.of(2023, 2, 25), 250, "ESP");

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
    }
}