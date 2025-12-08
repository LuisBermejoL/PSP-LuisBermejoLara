import java.util.LinkedList;
import java.util.Random;

// -------------------------------------------------------------------
// CLASE PRODUCTOR: Genera números
// -------------------------------------------------------------------
class Productor32 extends Thread {
    private int cantidadNumeros;
    private LinkedList<Integer> cola;
    private int numeroConsumidores;
    private int capacidadMaxima; 

    public Productor32(int cantidadNumeros, LinkedList<Integer> cola, int numeroConsumidores, int capacidadMaxima) {
        this.cantidadNumeros = cantidadNumeros;
        this.cola = cola;
        this.numeroConsumidores = numeroConsumidores;
        this.capacidadMaxima = capacidadMaxima;
    }

    public void run() {
        Random random = new Random();
        try {
            for (int i = 1; i <= cantidadNumeros; i++) {
                int n = random.nextInt(100);

                synchronized (cola) {
                    // AQUÍ ESTÁ EL LÍMITE DE 5
                    // Antes de meter nada, miramos si ya hay 5 elementos.
                    while (cola.size() == capacidadMaxima) { 
                        System.out.println("--- Cola LLENA (5 elementos). Productor espera... ---");
                        cola.wait(); // Si hay 5, se duerme y no mete nada.
                    }
                    
                    cola.add(n);
                    System.out.println("Productor inserta: " + n + " \t(Tamaño actual: " + cola.size() + ")");
                    cola.notifyAll();
                }
                Thread.sleep(500);
            }

            // Enviar FIN (-1)
            for (int i = 0; i < numeroConsumidores; i++) {
                synchronized (cola) {
                    // Incluso para meter el -1, respetamos el límite de 5
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

// -------------------------------------------------------------------
// CONSUMIDOR 1: Solo quiere PARES
// -------------------------------------------------------------------
class Consumidor32 extends Thread {
    private int cantidadNumeros;
    private int sumaTotal = 0;
    private LinkedList<Integer> cola;
    private int capacidadMaxima;

    public Consumidor32(int cantidadNumeros, LinkedList<Integer> cola, int capacidadMaxima) {
        this.cantidadNumeros = cantidadNumeros;
        this.cola = cola;
        this.capacidadMaxima = capacidadMaxima;
    }

    public void run() {
        try {
            for (int i = 1; i <= cantidadNumeros; i++) {
                int numero;

                // 1. INTENTAR SACAR UN NÚMERO
                synchronized (cola) {
                    while (cola.isEmpty()) {
                        cola.wait();
                    }
                    numero = cola.remove(0);
                    cola.notifyAll(); // Al sacar uno, avisamos (puede que el Productor estuviera esperando hueco)
                }

                if (numero == -1) break;

                // 2. VERIFICAR SI ES PAR
                if (numero % 2 == 0) { 
                    // ÉXITO: Es par, lo sumamos
                    sumaTotal += numero;
                    System.out.println("\t[PARES] Acepta: " + numero + " Total: " + sumaTotal);
                    Thread.sleep(200);
                }
                else { 
                    // FALLO: Es impar, hay que devolverlo
                    synchronized (cola) {
                        // OJO AQUÍ: Antes de devolverlo, verificamos el LÍMITE DE 5.
                        // Podría ser que mientras lo mirábamos, el Productor llenara el hueco que dejamos.
                        while (cola.size() == capacidadMaxima) {
                            cola.wait(); // Si la cola se llenó mientras pensábamos, esperamos.
                        }
                        cola.add(numero);
                        cola.notifyAll();
                    }
                    i--; // No contamos este ciclo
                }
            }
            System.out.println(">>> [PARES] TERMINADO. Suma: " + sumaTotal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// -------------------------------------------------------------------
// CONSUMIDOR 2: Solo quiere IMPARES
// -------------------------------------------------------------------
class Consumidor33 extends Thread {
    private int cantidadNumeros;
    private int sumaTotal = 0;
    private LinkedList<Integer> cola;
    private int capacidadMaxima;

    public Consumidor33(int cantidadNumeros, LinkedList<Integer> cola, int capacidadMaxima) {
        this.cantidadNumeros = cantidadNumeros;
        this.cola = cola;
        this.capacidadMaxima = capacidadMaxima;
    }

    public void run() {
        try {
            for (int i = 1; i <= cantidadNumeros; i++) {
                int numero;

                // 1. SACAR
                synchronized (cola) {
                    while (cola.isEmpty()) {
                        cola.wait();
                    }
                    numero = cola.remove(0);
                    cola.notifyAll();
                }

                if (numero == -1) break;

                // 2. VERIFICAR
                if (numero % 2 != 0) { 
                    // ÉXITO: Es impar
                    sumaTotal += numero;
                    System.out.println("\t[IMPARES] Acepta: " + numero + " Total: " + sumaTotal);
                    Thread.sleep(1000);
                }
                else { 
                    // FALLO: Es par, devolverlo
                    synchronized (cola) {
                        // AQUÍ TAMBIÉN SE RESPETA EL LÍMITE DE 5
                        while (cola.size() == capacidadMaxima) {
                            cola.wait();
                        }
                        cola.add(numero);
                        cola.notifyAll();
                    }
                    i--; 
                }
            }
            System.out.println(">>> [IMPARES] TERMINADO. Suma: " + sumaTotal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// -------------------------------------------------------------------
// CLASE PRINCIPAL
// -------------------------------------------------------------------
public class Productor_ConsumidorLinked3 {
    public static void main(String[] args) {
        int cantidadNumeros = 30;
        
        // AQUÍ DEFINIMOS QUE EL LÍMITE SERÁ 5
        int capacidadMaxima = 5; 

        LinkedList<Integer> cola = new LinkedList<>();

        // Le pasamos el '5' a todos los hilos para que sepan cuándo parar
        Productor32 productor = new Productor32(cantidadNumeros, cola, 2, capacidadMaxima);
        Consumidor32 consumidor1 = new Consumidor32(cantidadNumeros, cola, capacidadMaxima);
        Consumidor33 consumidor2 = new Consumidor33(cantidadNumeros, cola, capacidadMaxima);

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