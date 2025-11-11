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
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(nombreCaja + " terminó con el " + nombre);
        disponible = true;
    }
}

public class Supermercado2 {
    public static void main(String[] args) {
        Caja caja = new Caja("Caja 1");

        Thread[] clientes = new Thread[15];
        for (int i = 0; i < clientes.length; i++) {
            final String nombreCliente = "cliente " + (i+1);
            clientes[i] = new Thread(() -> caja.atenderCliente(nombreCliente));
            clientes[i].start();
        }

        for (int i = 0; i < clientes.length; i++) {
            try {
                clientes[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Supermercado cerrado.");
    }
}