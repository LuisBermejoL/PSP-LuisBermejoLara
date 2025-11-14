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
        System.out.println(nombreCaja + " atendiendo al cliente " + clienteId);

        // Simula el tiempo de atención al cliente (5 segundos)
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Indica que terminó de atender al cliente
        System.out.println(nombreCaja + " terminó con el cliente " + clienteId);
        // Marca la caja como disponible nuevamente
        disponible = true;
        // Nota: No se llama a notify(); los hilos se despiertan automáticamente al liberar el monitor
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

// Clase principal que simula el supermercado con una sola caja y tres clientes
public class Supermercado1 {
    public static void main(String[] args) {
        // Crear una sola caja
        Caja caja = new Caja("Caja 1");

        // Crear tres hilos, cada uno representando un cliente
        Cliente cliente1 = new Cliente(1, caja);
        Cliente cliente2 = new Cliente(2, caja);
        Cliente cliente3 = new Cliente(3, caja);

        // Iniciar los hilos de los clientes
        cliente1.start();
        cliente2.start();
        cliente3.start();

        // Esperar a que todos los clientes terminen
        try {
            cliente1.join();
            cliente2.join();
            cliente3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Indica que el supermercado ha cerrado
        System.out.println("Supermercado cerrado.");
    }
}