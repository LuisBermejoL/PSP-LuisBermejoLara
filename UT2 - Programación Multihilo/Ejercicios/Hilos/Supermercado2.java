// Clase que representa una caja del supermercado
class Caja {
    // Indica si la caja está disponible para atender a un cliente
    private boolean disponible = true;
    // Nombre identificador de la caja
    private String nombreCaja;

    // Constructor que asigna un nombre a la caja
    public Caja(String nombreCaja) {
        this.nombreCaja = nombreCaja;
    }

    // Método sincronizado para atender a un cliente
    public synchronized void atenderCliente(int clienteId) {
        // Mientras la caja no esté disponible, el hilo espera
        while (!disponible) {
            try {
                wait(); // Hace que el hilo espere hasta que la caja esté libre
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Marca la caja como ocupada
        disponible = false;
        System.out.println(nombreCaja + " atendiendo al " + clienteId);

        // Simula el tiempo de atención al cliente (5 segundos)
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Indica que terminó de atender al cliente
        System.out.println(nombreCaja + " terminó con el " + clienteId);
        // Marca la caja como disponible nuevamente
        disponible = true;
    }
}

// Clase que representa un cliente como hilo
class Cliente extends Thread {
    private final int id;   // Identificador del cliente
    private final Caja caja; // Caja en la que será atendido

    // Constructor que asigna id y caja al cliente
    public Cliente(int id, Caja caja) {
        this.id = id;
        this.caja = caja;
    }

    // Método que se ejecuta al iniciar el hilo
    public void run() {
        System.out.println("Cliente " + id + " entra en la cola de espera.");
        caja.atenderCliente(id); // Llama al método de la caja para ser atendido
    }
}

// Clase principal que simula el supermercado con una sola caja
public class Supermercado2 {
    public static void main(String[] args) {
        // Crear una sola caja
        Caja caja = new Caja("Caja 1");

        // Crear un array de hilos que representarán a los clientes
        Thread[] clientes = new Thread[15];

        // Crear e iniciar los hilos de los clientes
        for (int i = 0; i < clientes.length; i++) {
            // Cada hilo representa a un cliente y llama al método atenderCliente de la misma caja
            clientes[i] = new Cliente(i + 1, caja);
            clientes[i].start();
        }

        // Esperar a que todos los clientes terminen
        for (int i = 0; i < clientes.length; i++) {
            try {
                clientes[i].join(); // Espera a que el hilo termine
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Indica que el supermercado ha cerrado
        System.out.println("Supermercado cerrado.");
    }
}