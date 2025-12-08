import java.util.LinkedList;
import java.util.Random;

// Productor: Debe respetar el límite de capacidad
class Productor11 extends Thread {
    private int cantidadNumeros;
    private LinkedList<Integer> cola;
    private int capacidadMaxima; // Variable para el límite (5)

    public Productor11(int cantidadNumeros, LinkedList<Integer> cola, int capacidadMaxima) {
        this.cantidadNumeros = cantidadNumeros;
        this.cola = cola;
        this.capacidadMaxima = capacidadMaxima;
    }

    public void run() {
        Random random = new Random();
        try {
            for (int i = 1; i <= cantidadNumeros; i++) {
                int n = random.nextInt(100);

                synchronized (cola) {
                    // CONTROL DE LÍMITE: Si hay 5, espera.
                    while (cola.size() == capacidadMaxima) {
                        System.out.println("--- Cola LLENA. Productor espera... ---");
                        cola.wait();
                    }
                    cola.add(n);
                    System.out.println("Productor inserta: " + n + " (Cola: " + cola.size() + ")");
                    cola.notifyAll(); // Avisa al consumidor que hay datos
                }
                Thread.sleep(500); 
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Consumidor: No necesita saber el límite máximo, solo si está vacía
class Consumidor11 extends Thread {
    private int cantidadNumeros;
    private int sumaTotal = 0;
    private LinkedList<Integer> cola;

    public Consumidor11(int cantidadNumeros, LinkedList<Integer> cola) {
        this.cantidadNumeros = cantidadNumeros;
        this.cola = cola;
    }

    public void run() {
        try {
            for (int i = 1; i <= cantidadNumeros; i++) {
                int numero;

                synchronized (cola) {
                    // CONTROL DE VACÍO: Si hay 0, espera.
                    while (cola.isEmpty()) {
                        System.out.println("--- Cola VACÍA. Consumidor espera... ---");
                        cola.wait();
                    }
                    numero = cola.remove(0); 
                    // Al sacar un dato, liberamos espacio, así que avisamos al Productor
                    // por si estaba bloqueado esperando hueco.
                    cola.notifyAll(); 
                }

                sumaTotal += numero;
                System.out.println("Consumidor tomó: " + numero + ", suma: " + sumaTotal);
                Thread.sleep(2000); 
            }
            System.out.println("Suma total final: " + sumaTotal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Productor_ConsumidorLinked1 {
    public static void main(String[] args) {
        int cantidadNumeros = 10;
        int capacidadMaxima = 5; // Límite estricto

        LinkedList<Integer> cola = new LinkedList<>();

        Productor11 productor = new Productor11(cantidadNumeros, cola, capacidadMaxima);
        Consumidor11 consumidor = new Consumidor11(cantidadNumeros, cola);

        productor.start();
        consumidor.start();

        try {
            productor.join();
            consumidor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Productor-Consumidor terminado.");
    }
}