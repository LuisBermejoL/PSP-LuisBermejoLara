class SalaGimnasio {
    // Nombre identificador de la caja
    private String nombreSala;
    // Aforo máximo de la sala
    private int aforoSala = 5;

    // Constructor que asigna un nombre a la caja
    public SalaGimnasio(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    // Método sincronizado para atender a un cliente
    public synchronized void entrarEnSala(int clienteId) {
        // Mientras la sala no esté disponible, el hilo espera
        while (aforoSala == 0) {
            try {
                wait(); // Hace que el hilo espere hasta que un hueco de la sala esté libre
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Restamos un espacio libre en la sala
        aforoSala--;

        System.out.println(clienteId + " entrenando en la " + nombreSala + ". Aforo restante: " + aforoSala);
    }

    public synchronized void salirDeSala(int clienteId) {
        // Sumamos un espacio libre en la sala
        aforoSala++;

        // Indica que terminó de atender al cliente
        System.out.println(clienteId + " termino de entrenar en la " + nombreSala + ". Aforo restante: " + aforoSala);

        notifyAll();
    }
}

class ClienteGimnasio extends Thread {
    private final int id;   // Identificador del cliente
    private final SalaGimnasio sala; // Sala en la que será atendido

    // Constructor que asigna id y sala al cliente
    public ClienteGimnasio(int id, SalaGimnasio sala) {
        this.id = id;
        this.sala = sala;
    }

    // Método que se ejecuta al iniciar el hilo
    public void run() {
        System.out.println("Cliente " + id + " entra en la cola de espera.");
        sala.entrarEnSala(id); // Llama al método de la sala para ser atendido

        // Simula el tiempo de atención al cliente (5 segundos)
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sala.salirDeSala(id);
    }
}

public class Gimnasio {
    public static void main(String[] args) {
        SalaGimnasio sala = new SalaGimnasio("Sala 1");
        int numeroClientes = 20; // Número total de clientes
        Thread[] clientes = new Thread[numeroClientes]; // Array de hilos clientes

        // Crear e iniciar los hilos de los clientes
        for (int i = 0; i < numeroClientes; i++) {
            // Cada cliente es un hilo que atiende en la sala correspondiente
            clientes[i] = new ClienteGimnasio(i + 1, sala);
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
        System.out.println("Gimnasio cerrado.");
    }
}