class Caja {
    private boolean disponible = true;
    private String nombreCaja;

    public Caja(String nombreCaja) {
        this.nombreCaja = nombreCaja;
    }

    public synchronized void atenderCliente(String nombre) {
        while (!disponible) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        disponible = false;
        System.out.println(nombreCaja + " atendiendo al " + nombre);
        // Simular tiempo de atención al cliente
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(nombreCaja + " terminó con el " + nombre);
        disponible = true;
        notifyAll();
    }
}

public class Supermercado3 {
    public static void main(String[] args) {
        int numeroCajas = 3;
        int numeroClientes = 10;
        Caja[] cajas = new Caja[numeroCajas];
        Thread[] clientes = new Thread[numeroClientes];

        for (int i = 0; i < numeroCajas; i++) {
            cajas[i] = new Caja("Caja " + (i+1));
        }

        for (int i = 0; i < numeroClientes; i++) {
            final String nombreCliente = "cliente " + (i+1);
            int numCaja = i % numeroCajas;
            clientes[i] = new Thread(() -> cajas[numCaja].atenderCliente(nombreCliente));
            clientes[i].start();
        }

        for (int i = 0; i < numeroClientes; i++) {
            try {
                clientes[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Supermercado cerrado.");
    }
}