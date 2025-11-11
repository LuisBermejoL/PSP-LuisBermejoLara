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

public class Supermercado2 {
    public static void main(String[] args) {
        Caja caja = new Caja("Caja 1");

        Thread cliente1 = new Thread(() -> caja.atenderCliente("cliente 1"));
        Thread cliente2 = new Thread(() -> caja.atenderCliente("cliente 2"));
        Thread cliente3 = new Thread(() -> caja.atenderCliente("cliente 3"));
        Thread cliente4 = new Thread(() -> caja.atenderCliente("cliente 4"));
        Thread cliente5 = new Thread(() -> caja.atenderCliente("cliente 5"));
        Thread cliente6 = new Thread(() -> caja.atenderCliente("cliente 6"));
        Thread cliente7 = new Thread(() -> caja.atenderCliente("cliente 7"));
        Thread cliente8 = new Thread(() -> caja.atenderCliente("cliente 8"));
        Thread cliente9 = new Thread(() -> caja.atenderCliente("cliente 9"));
        Thread cliente10 = new Thread(() -> caja.atenderCliente("cliente 10"));
        Thread cliente11 = new Thread(() -> caja.atenderCliente("cliente 11"));
        Thread cliente12 = new Thread(() -> caja.atenderCliente("cliente 12"));
        Thread cliente13 = new Thread(() -> caja.atenderCliente("cliente 13"));
        Thread cliente14 = new Thread(() -> caja.atenderCliente("cliente 14"));
        Thread cliente15 = new Thread(() -> caja.atenderCliente("cliente 15"));
        cliente1.start();
        cliente2.start();
        cliente3.start();
        cliente4.start();
        cliente5.start();
        cliente6.start();
        cliente7.start();
        cliente8.start();
        cliente9.start();
        cliente10.start();
        cliente11.start();
        cliente12.start();
        cliente13.start();
        cliente14.start();
        cliente15.start();

        try {
            cliente1.join();
            cliente2.join();
            cliente3.join();
            cliente4.join();
            cliente5.join();
            cliente6.join();
            cliente7.join();
            cliente8.join();
            cliente9.join();
            cliente10.join();
            cliente11.join();
            cliente12.join();
            cliente13.join();
            cliente14.join();
            cliente15.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Supermercado cerrado.");
    }
}