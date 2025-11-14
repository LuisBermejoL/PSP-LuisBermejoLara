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

// Clase principal que simula el supermercado
public class Supermercado3 {
    public static void main(String[] args) {
        int numeroCajas = 3;     // Número de cajas disponibles
        int numeroClientes = 10; // Número total de clientes
        Caja[] cajas = new Caja[numeroCajas];       // Array de cajas
        Thread[] clientes = new Thread[numeroClientes]; // Array de hilos clientes

        // Crear las cajas del supermercado
        for (int i = 0; i < numeroCajas; i++) {
            cajas[i] = new Caja("Caja " + (i + 1));
        }

        // Crear e iniciar los hilos de los clientes
        for (int i = 0; i < numeroClientes; i++) {
            int numCaja = i % numeroCajas; // Asigna el cliente a una caja de manera cíclica
            // Cada cliente es un hilo que atiende en la caja correspondiente
            clientes[i] = new Cliente(i + 1, cajas[numCaja]);
            clientes[i].start();
        }

        // Esperar a que todos los clientes terminen
        for (int i = 0; i < numeroClientes; i++) {
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