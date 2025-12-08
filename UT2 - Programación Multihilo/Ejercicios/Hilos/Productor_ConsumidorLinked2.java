import java.util.LinkedList;
import java.util.Random;

// Productor: Es el único que llena, así que es el único que vigila que no pase de 5
class Productor22 extends Thread {
    private int cantidadNumeros;
    private LinkedList<Integer> cola;
    private int numeroConsumidores;
    private int capacidadMaxima; 

    public Productor22(int cantidadNumeros, LinkedList<Integer> cola, int numeroConsumidores, int capacidadMaxima) {
        this.cantidadNumeros = cantidadNumeros;
        this.cola = cola;
        this.numeroConsumidores = numeroConsumidores;
        this.capacidadMaxima = capacidadMaxima;
    }

    public void run() {
        Random random = new Random();
        try {
            // Generar números
            for (int i = 1; i <= cantidadNumeros; i++) {
                int n = random.nextInt(100);

                synchronized (cola) {
                    // SI ESTÁ LLENA (5), ESPERAR
                    while (cola.size() == capacidadMaxima) {
                        cola.wait();
                    }
                    cola.add(n);
                    System.out.println("Productor inserta: " + n + " (Cola: " + cola.size() + ")");
                    cola.notifyAll(); // Despierta a los consumidores
                }
                Thread.sleep(500);
            }

            // Generar señales de fin (-1)
            for (int i = 0; i < numeroConsumidores; i++) {
                synchronized (cola) {
                    // TAMBIÉN DEBE ESPERAR SI ESTÁ LLENA PARA METER EL -1
                    while (cola.size() == capacidadMaxima) {
                        cola.wait();
                    }
                    cola.add(-1); 
                    System.out.println("Productor inserta FIN (-1)");
                    cola.notifyAll();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Consumidor Sumador
class Consumidor22 extends Thread {
    private int cantidadNumeros;
    private int sumaTotal = 0;
    private LinkedList<Integer> cola;

    public Consumidor22(int cantidadNumeros, LinkedList<Integer> cola) {
        this.cantidadNumeros = cantidadNumeros;
        this.cola = cola;
    }

    public void run() {
        try {
            for (int i = 1; i <= cantidadNumeros; i++) {
                int numero;

                synchronized (cola) {
                    // Solo vigila si está vacía
                    while (cola.isEmpty()) {
                        cola.wait();
                    }
                    numero = cola.remove(0);
                    // Al sacar, avisa al Productor que hay hueco libre
                    cola.notifyAll(); 
                }

                if (numero == -1) break;

                sumaTotal += numero;
                System.out.println("\t[Sumador] tomó: " + numero + ", Total: " + sumaTotal);
                Thread.sleep(8000);
            }
            System.out.println(">>> [Sumador] FIN. Suma: " + sumaTotal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Consumidor Multiplicador
class Consumidor23 extends Thread {
    private int cantidadNumeros;
    private long multiplicacionTotal = 1;
    private LinkedList<Integer> cola;

    public Consumidor23(int cantidadNumeros, LinkedList<Integer> cola) {
        this.cantidadNumeros = cantidadNumeros;
        this.cola = cola;
    }

    public void run() {
        try {
            for (int i = 1; i <= cantidadNumeros; i++) {
                int numero;

                synchronized (cola) {
                    while (cola.isEmpty()) {
                        cola.wait();
                    }
                    numero = cola.remove(0);
                    cola.notifyAll();
                }

                if (numero == -1) break;

                multiplicacionTotal *= numero;
                System.out.println("\t[Multiplicador] tomó: " + numero + ", Total: " + multiplicacionTotal);
                Thread.sleep(5000);
            }
            System.out.println(">>> [Multiplicador] FIN. Multi: " + multiplicacionTotal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Productor_ConsumidorLinked2 {
    public static void main(String[] args) {
        int cantidadNumeros = 10;
        int capacidadMaxima = 5; // Límite estricto

        LinkedList<Integer> cola = new LinkedList<>();

        Productor22 productor = new Productor22(cantidadNumeros, cola, 2, capacidadMaxima);
        Consumidor22 consumidor1 = new Consumidor22(cantidadNumeros, cola);
        Consumidor23 consumidor2 = new Consumidor23(cantidadNumeros, cola);

        productor.start();
        consumidor1.start();
        consumidor2.start();

        try {
            productor.join();
            consumidor1.join();
            consumidor2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Productor-Consumidor terminado.");
    }
}