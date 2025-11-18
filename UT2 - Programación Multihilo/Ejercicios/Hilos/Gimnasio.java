class SalaGimnasio {
    // Nombre identificador de la sala
    private String nombreSala;
    // Capacidad máxima de la sala
    private int capacidadSala;

    // Constructor que asigna un nombre y capacidad a la sala
    public SalaGimnasio(String nombreSala, int capacidadSala) {
        this.nombreSala = nombreSala;
        this.capacidadSala = capacidadSala;
    }

    // Método sincronizado para atender a un cliente
    public synchronized void entrarEnSala(int clienteId) {
        // Mientras la sala no esté disponible, el hilo espera
        while (capacidadSala == 0) {
            try {
                System.out.println("Cliente " + clienteId + " entra en la cola de espera.");
                wait(); // Hace que el hilo espere hasta que un hueco de la sala esté libre
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Restamos un espacio libre en la sala
        capacidadSala--;

        // Indica que el cliente empezo a entrenar
        System.out.println(clienteId + " entrenando en la " + nombreSala + ". Capacidad restante: " + capacidadSala);
    }

    public synchronized void salirDeSala(int clienteId) {
        // Sumamos un espacio libre en la sala
        capacidadSala++;

        // Indica que el cliente termino de entrenar
        System.out.println(clienteId + " termino de entrenar en la " + nombreSala + ". Capacidad restante: " + capacidadSala);

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
        sala.entrarEnSala(id); // Llama al método de la sala para entrar

        // Simula el tiempo de atención al cliente (5 segundos)
        try {
            if (id<=2) Thread.sleep(5000);
            else Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sala.salirDeSala(id); // Llama al método de la sala para salir
    }
}

public class Gimnasio {
    public static void main(String[] args) {
        SalaGimnasio sala = new SalaGimnasio("Sala 1", 5); // Sala del gimnasio creada
        int numeroClientes = 20; // Número total de clientes
        Thread[] clientes = new Thread[numeroClientes]; // Array de hilos clientes

        // Crear e iniciar los hilos de los clientes
        for (int i = 0; i < numeroClientes; i++) {
            // Cada cliente es un hilo que entrena en la sala correspondiente
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

        // Indica que el gimnasio ha cerrado
        System.out.println("Gimnasio cerrado.");
    }
}