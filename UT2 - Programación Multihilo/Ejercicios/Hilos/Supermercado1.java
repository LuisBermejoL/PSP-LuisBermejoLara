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
    }
}

public class Supermercado1 {
    public static void main(String[] args) {
        Caja caja = new Caja("Caja 1");

        Thread cliente1 = new Thread(() -> caja.atenderCliente("cliente 1"));
        Thread cliente2 = new Thread(() -> caja.atenderCliente("cliente 2"));
        Thread cliente3 = new Thread(() -> caja.atenderCliente("cliente 3"));
        cliente1.start();
        cliente2.start();
        cliente3.start();

        try {
            cliente1.join();
            cliente2.join();
            cliente3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Supermercado cerrado.");
    }
}