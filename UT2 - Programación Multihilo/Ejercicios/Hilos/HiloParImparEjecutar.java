class HiloParImpar implements Runnable {
    private int tipo;

    public HiloParImpar(int tipo) {
        this.tipo = tipo;
    }

    public void run() {
        int contadorPares = 1;
        int contadorImpares = 101;
        if (tipo == 1) {
            while (contadorPares <= 100) {
                System.out.println("Hilo XX generando número par: " + contadorPares);
                if (contadorPares % 2 == 0) {
                    contadorPares += 2;
                } else {
                    contadorPares += 1;
                }
            }
        }
        if (tipo == 2) {
            while (contadorImpares <= 200) {
                System.out.println("Hilo YY generando número impar: " + contadorImpares);
                if (contadorImpares % 2 == 0) {
                    contadorImpares += 1;
                } else {
                    contadorImpares += 2;
                }
            }
        }
    }
}

public class HiloParImparEjecutar {
    public static void main(String[] args) {
        Thread t1 = new Thread(new HiloParImpar(1));
        Thread t2 = new Thread(new HiloParImpar(2));
        
        t2.start();

        try {
            t2.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        t1.start();
    }
}