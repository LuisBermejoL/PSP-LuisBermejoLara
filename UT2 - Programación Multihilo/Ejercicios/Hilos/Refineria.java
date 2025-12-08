import java.util.Random;

// Clase Deposito: Actúa como el recurso compartido (Monitor)
class Deposito {
    // Variable para almacenar los litros
    private double litrosDeposito;
    private boolean estaLleno = false; // Bandera para controlar el estado del depósito

    // Método para que el camión cargue combustible
    public synchronized double llenarDepositoCamion() {
        // Espera mientras el depósito NO esté lleno
        while (!estaLleno) {
            try {
                wait(); // El camión espera a que el productor llene el depósito
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Solo un camión carga a la vez (garantizado por synchronized)
        double litrosCargados = this.litrosDeposito;
        this.litrosDeposito = 0;
        this.estaLleno = false; // Marcamos como vacío para que el productor pueda trabajar

        // Notificamos al productor que ya se vació el depósito
        notifyAll(); 

        return litrosCargados;
    }

    // Método para que el productor llene el depósito
    public synchronized void prodLlenaDep(double litros) {
        // Mientras el camión carga (o está lleno), el productor espera
        while (estaLleno) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Realiza el llenado
        this.litrosDeposito = litros;
        this.estaLleno = true; // Marcamos como lleno para que pasen los camiones

        System.out.println("Productor llena el depósito con => " + litros + " Litros");

        // Notificamos a los camiones (uno aleatorio despertará y ganará el monitor)
        notifyAll();
    }
}

// Clase Camion
class Camion extends Thread {
    private Deposito deposito;
    private int idCamion;
    private double totalCargado = 0;

    // Constructor
    public Camion(Deposito dep, int n) {
        this.deposito = dep;
        this.idCamion = n;
    }

    // Método run
    public void run() {
        try {
            // Cada camión realiza exactamente 5 cargas
            for (int i = 0; i < 5; i++) {
                // Llama al método sincronizado del depósito
                double litros = deposito.llenarDepositoCamion();
                
                totalCargado += litros;
                System.out.println("Camión " + idCamion + " carga: " + litros + " litros");
                
                // Pequeña pausa para simular el tiempo de operación
                Thread.sleep(new Random().nextInt(500)); 
            }
            // Mensaje final al terminar sus 5 cargas
            System.out.println("Camión " + idCamion + " => Volumen Total Recogido = " + totalCargado + " Operación carga finalizada!!");
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Clase Productor
class Productor extends Thread {
    private Deposito deposito;
    private Random random;

    // Constructor
    public Productor(Deposito dep) {
        this.deposito = dep;
        this.random = new Random();
    }

    // Método run
    public void run() {
        try {
            // Se realizarán 15 llenados del depósito
            // (15 llenados totales coinciden con 3 camiones * 5 cargas cada uno)
            for (int i = 0; i < 15; i++) {
                // Genera números aleatorios entre 0 y 1000
                double litros = Math.round((random.nextDouble() * 1000) * 100.0) / 100.0; // Redondeo a 2 decimales para estética
                
                // Llama al método sincronizado
                deposito.prodLlenaDep(litros);
                
                // Simulación de tiempo de llenado
                Thread.sleep(500);
            }
            System.out.println("Productor ha finalizado sus 15 cargas.");
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Clase Principal Refineria
public class Refineria {
    public static void main(String[] args) {
        // Instancia del recurso compartido
        Deposito deposito = new Deposito();

        // Creación del Productor
        Productor productor = new Productor(deposito);

        // Creación de los 3 Camiones
        Camion camion1 = new Camion(deposito, 1);
        Camion camion2 = new Camion(deposito, 2);
        Camion camion3 = new Camion(deposito, 3);

        // Inicio de los hilos
        productor.start();
        camion1.start();
        camion2.start();
        camion3.start();

        // Esperar a que terminen (Opcional, pero recomendable)
        try {
            productor.join();
            camion1.join();
            camion2.join();
            camion3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Simulación de Refinería finalizada.");
    }
}