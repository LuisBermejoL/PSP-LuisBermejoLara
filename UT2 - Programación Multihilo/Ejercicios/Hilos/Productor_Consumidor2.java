import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// Clase Productor21: genera números aleatorios y los inserta en la cola compartida
class Productor21 extends Thread {
    private int cantidadNumeros;          // Cantidad de números que debe producir
    BlockingQueue<Integer> cola;         // Cola compartida entre productor y consumidores
    private int numeroConsumidores;       // Número de consumidores que esperan datos

    // Constructor: recibe la cantidad de números, la cola y el número de consumidores
    public Productor21(int cantidadNumeros, BlockingQueue<Integer> cola, int numeroConsumidores) {
        this.cantidadNumeros = cantidadNumeros;
        this.cola = cola;
        this.numeroConsumidores = numeroConsumidores;
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
                Thread.sleep(500);             // Pausa de 0.5 segundos para simular producción
            }

            // Inserta un valor especial (-1) por cada consumidor
            // Esto sirve como "señal de terminación" para que cada consumidor sepa cuándo detenerse
            for (int i = 0; i < numeroConsumidores; i++) {
                cola.put(-1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Clase Consumidor21: extrae números de la cola y acumula su suma
class Consumidor21 extends Thread {
    private int cantidadNumeros;          // Cantidad máxima de números a consumir
    private int sumaTotal = 0;            // Acumulador de la suma
    BlockingQueue<Integer> cola;          // Cola compartida

    public Consumidor21(int cantidadNumeros, BlockingQueue<Integer> cola) {
        this.cantidadNumeros = cantidadNumeros;
        this.cola = cola;
    }

    public synchronized void run() {
        try {
            for (int i = 1; i <= cantidadNumeros; i++) {
                int numero = cola.take();      // Extrae un número de la cola (bloquea si está vacía)
                if (numero == -1) {            // Señal de terminación
                    break;
                }
                sumaTotal += numero;           // Suma el número al acumulador
                System.out.println("Consumidor tomó: " + numero + ", suma acumulada: " + sumaTotal);
                Thread.sleep(500);             // Pausa de 0.5 segundos para simular consumo
            }

            // Al terminar, muestra la suma total
            System.out.println("Suma total de todos los números: " + sumaTotal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Clase Consumidor22: extrae números de la cola y acumula su multiplicación
class Consumidor22 extends Thread {
    private int cantidadNumeros;          // Cantidad máxima de números a consumir
    private int multiplicacionTotal = 1;  // Acumulador de la multiplicación
    BlockingQueue<Integer> cola;          // Cola compartida

    public Consumidor22(int cantidadNumeros, BlockingQueue<Integer> cola) {
        this.cantidadNumeros = cantidadNumeros;
        this.cola = cola;
    }

    public synchronized void run() {
        try {
            for (int i = 1; i <= cantidadNumeros; i++) {
                int numero = cola.take();      // Extrae un número de la cola
                if (numero == -1) {            // Señal de terminación
                    break;
                }
                multiplicacionTotal *= numero; // Multiplica el número al acumulador
                System.out.println("Consumidor tomó: " + numero + ", multiplicación acumulada: " + multiplicacionTotal);
                Thread.sleep(500);             // Pausa de 0.5 segundos
            }

            // Al terminar, muestra la multiplicación total
            System.out.println("Multiplicación total de todos los números: " + multiplicacionTotal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Clase principal: arranca el programa Productor-Consumidor con múltiples consumidores
public class Productor_Consumidor2 {
    public static void main(String[] args) {
        int cantidadNumeros = 10;   // Número de elementos que se producirán

        // Cola con capacidad de 5 elementos
        BlockingQueue<Integer> cola = new ArrayBlockingQueue<>(5);

        // Se crea el productor y dos consumidores
        Productor21 productor = new Productor21(cantidadNumeros, cola, 2);
        Consumidor21 consumidor1 = new Consumidor21(cantidadNumeros, cola);
        Consumidor22 consumidor2 = new Consumidor22(cantidadNumeros, cola);

        // Se inician los hilos
        productor.start();
        consumidor1.start();
        consumidor2.start();

        // Espera a que todos los hilos terminen
        try {
            productor.join();
            consumidor1.join();
            consumidor2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Mensaje final
        System.out.println("Productor-Consumidor terminado.");
    }
}