class HiloSumasRestas implements Runnable {
    private int número = 1000;
    private int numveces;
    private String operación;

    public HiloSumasRestas(int numveces, String operación) {
        this.numveces = numveces;
        this.operación = operación;
    }

    public int incrementar(int numveces) {
        return this.número += numveces;
    }

    public int decrementar(int numveces) {
        return this.número -= numveces;
    }

    public void run() {
        if (operación.equals("+")) {
            incrementar(numveces);
            System.out.println("Número después de sumar " + numveces + ": " + this.número);
        }
        if (operación.equals("-")) {
            decrementar(numveces);
            System.out.println("Número después de restar " + numveces + ": " + this.número);
        }
    }
}

public class HiloSumasRestasEjecutar {
    public static void main(String[] args) {
        Thread HiloSuma1 = new Thread(new HiloSumasRestas(100, "+"));
        Thread HiloResta2 = new Thread(new HiloSumasRestas(100, "-"));
        Thread HiloSuma3 = new Thread(new HiloSumasRestas(100, "+"));
        Thread HiloResta4 = new Thread(new HiloSumasRestas(100, "-"));

        HiloSuma1.start();
        HiloResta2.start();
        HiloSuma3.start();

        try {
            HiloSuma1.join();
            HiloResta2.join();
            HiloSuma3.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        HiloResta4.start();
    }
}