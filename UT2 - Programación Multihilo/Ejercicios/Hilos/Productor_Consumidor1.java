import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// Clase Productor11: genera números aleatorios y los inserta en la cola compartida
class Productor11 extends Thread {
    private int cantidadNumeros;          // Cantidad de números que debe producir
    BlockingQueue<Integer> cola;         // Cola compartida entre productor y consumidor

    // Constructor: recibe la cantidad de números y la cola
    public Productor11(int cantidadNumeros, BlockingQueue<Integer> cola) {
        this.cantidadNumeros = cantidadNumeros;
        this.cola = cola;
    }

    // Método run: se ejecuta cuando el hilo comienza
    public synchronized void run() {
        Random random = new Random();    // Generador de números aleatorios

        try {
            // Produce 'cantidadNumeros' números
            for (int i = 1; i <= cantidadNumeros; i++) {
                int n = random.nextInt(100);   // Número aleatorio entre 0 y 99
                cola.put(n);                   // Inserta el número en la cola (bloquea si está llena)
                System.out.println("Productor inserta: " + n);
                Thread.sleep(500);             // Pausa de 0.5 segundos para simular tiempo de producción
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Clase Consumidor11: extrae números de la cola y acumula su suma
class Consumidor11 extends Thread {
    private int cantidadNumeros;          // Cantidad de números que debe consumir
    private int sumaTotal = 0;            // Acumulador de la suma de los números consumidos
    BlockingQueue<Integer> cola;          // Cola compartida

    // Constructor: recibe la cantidad de números y la cola
    public Consumidor11(int cantidadNumeros, BlockingQueue<Integer> cola) {
        this.cantidadNumeros = cantidadNumeros;
        this.cola = cola;
    }

    // Método run: se ejecuta cuando el hilo comienza
    public synchronized void run() {
        try {
            // Consume 'cantidadNumeros' números
            for (int i = 1; i <= cantidadNumeros; i++) {
                int numero = cola.take();      // Extrae un número de la cola (bloquea si está vacía)
                sumaTotal += numero;           // Suma el número al acumulador
                System.out.println("Consumidor tomó: " + numero + ", suma acumulada: " + sumaTotal);
                Thread.sleep(500);             // Pausa de 0.5 segundos para simular tiempo de consumo
            }

            // Al terminar, muestra la suma total
            System.out.println("Suma total de todos los números: " + sumaTotal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Clase principal: arranca el programa Productor-Consumidor
public class Productor_Consumidor1 {
    public static void main(String[] args) {
        int cantidadNumeros = 10;   // Número de elementos que se producirán y consumirán

        // Cola con capacidad de 5 elementos (bloquea si se llena o vacía)
        BlockingQueue<Integer> cola = new ArrayBlockingQueue<>(5);

        // Se crean los hilos productor y consumidor
        Productor11 productor = new Productor11(cantidadNumeros, cola);
        Consumidor11 consumidor = new Consumidor11(cantidadNumeros, cola);

        // Se inician los hilos
        productor.start();
        consumidor.start();

        // Espera a que ambos hilos terminen antes de continuar
        try {
            productor.join();
            consumidor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Mensaje final cuando todo el proceso ha terminado
        System.out.println("Productor-Consumidor terminado.");
    }
}