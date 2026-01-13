package Ejercicios.Sockets3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPSocketServidor3 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // El servidor escucha en SU PROPIA IP
        InetAddress ipServidor = InetAddress.getByName("192.168.204.129");
        int puerto = 6666;
        
        ServerSocket servidor = new ServerSocket(puerto, 1, ipServidor);
        System.out.println("Esperando al cliente.....");   
        Socket cliente = servidor.accept();

        // Se obtiene un stream para leer objetos
        ObjectInputStream inObjeto = new ObjectInputStream(cliente.getInputStream());
        
        // Se recibe un objeto
        Factura dato = (Factura) inObjeto.readObject();
        System.out.println("Recibo la factura: " + dato.getNumeroFactura());

        // Procesar la factura
        if (dato.getTipoIVA().equals("IGC")) {
            dato.setIVA(dato.getImporteFactura() * 0.07);
        } else if (dato.getTipoIVA().equals("ESP")) {
            dato.setIVA(dato.getImporteFactura() * 0.21);
        } else if (dato.getTipoIVA().equals("EUR")) {
            dato.setIVA(dato.getImporteFactura() * 0.15);
        }

        dato.setImporteTotal(dato.getImporteFactura() + dato.getIVA());
        
        // Se prepara un flujo de salida para objetos
        ObjectOutputStream outObjeto = new ObjectOutputStream(cliente.getOutputStream());

        // Se envia el objeto
        outObjeto.writeObject(dato);
        System.out.println("Envio la factura " + dato.getNumeroFactura() + " con IVA: " + dato.getIVA() + " y importe total: " + dato.getImporteTotal());

        // CERRAR STREAMS Y SOCKETS
        inObjeto.close();
        cliente.close();
        servidor.close();
    }
}