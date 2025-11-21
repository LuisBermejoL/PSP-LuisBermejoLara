import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// Clase Productor31: genera números aleatorios y los inserta en la cola compartida
class Productor31 extends Thread {
    private int cantidadNumeros;          // Cantidad de números que debe producir
    BlockingQueue<Integer> cola;         // Cola compartida entre productor y consumidores
    private int numeroConsumidores;       // Número de consumidores que esperan datos

    // Constructor: recibe la cantidad de números, la cola y el número de consumidores
    public Productor31(int cantidadNumeros, BlockingQueue<Integer> cola, int numeroConsumidores) {
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

// Clase Consumidor31: procesa solo números pares
class Consumidor31 extends Thread {
    private int cantidadNumeros;          // Cantidad máxima de números a consumir
    private int sumaTotal = 0;            // Acumulador de la suma de números pares
    BlockingQueue<Integer> cola;          // Cola compartida

    public Consumidor31(int cantidadNumeros, BlockingQueue<Integer> cola) {
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
                else if (numero % 2 == 0) {    // Si es par, lo procesa
                    sumaTotal += numero;
                    System.out.println("Consumidor1 tomó: " + numero + ", número par, suma acumulada: " + sumaTotal);
                    Thread.sleep(500);
                }
                else if (numero % 2 != 0) {    // Si es impar, lo devuelve a la cola
                    cola.put(numero);
                    i--;                       // No cuenta este ciclo, espera un número válido
                }
            }

            System.out.println("Suma total de todos los números pares: " + sumaTotal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Clase Consumidor32: procesa solo números impares
class Consumidor32 extends Thread {
    private int cantidadNumeros;          // Cantidad máxima de números a consumir
    private int sumaTotal = 0;            // Acumulador de la suma de números impares
    BlockingQueue<Integer> cola;          // Cola compartida

    public Consumidor32(int cantidadNumeros, BlockingQueue<Integer> cola) {
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
                else if (numero % 2 == 0) {    // Si es par, lo devuelve a la cola
                    cola.put(numero);
                    i--;                       // No cuenta este ciclo, espera un número válido
                }
                else if (numero % 2 != 0) {    // Si es impar, lo procesa
                    sumaTotal += numero;
                    System.out.println("Consumidor2 tomó: " + numero + ", número impar, suma acumulada: " + sumaTotal);
                    Thread.sleep(500);
                }
            }

            System.out.println("Suma total de todos los números impares: " + sumaTotal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Clase principal: arranca el programa Productor-Consumidor con dos consumidores especializados
public class Productor_Consumidor3 {
    public static void main(String[] args) {
        int cantidadNumeros = 30;   // Número de elementos que se producirán

        // Cola con capacidad de 5 elementos
        BlockingQueue<Integer> cola = new ArrayBlockingQueue<>(5);

        // Se crea el productor y dos consumidores especializados
        Productor31 productor = new Productor31(cantidadNumeros, cola, 2);
        Consumidor31 consumidor1 = new Consumidor31(cantidadNumeros, cola);
        Consumidor32 consumidor2 = new Consumidor32(cantidadNumeros, cola);

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