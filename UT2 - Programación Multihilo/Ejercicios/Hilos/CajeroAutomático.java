// Clase CuentaBancaria
// Definición de la cuenta con un saldo inicial y de las operaciones para
// ingresar dinero (método ingresarDinero) y sacar dinero (método sacarDinero) de la cuenta
class CuentaBancaria {
    //Saldo inicial de la cuenta
    int saldo = 1000;

    int getSaldo() { return saldo; }
	void restar(int cantidad) { saldo=saldo-cantidad; }
    void sumar(int cantidad) { saldo=saldo+cantidad; }
    //método sacarDinero:
    //nombre -> persona que realiza la operación
    //importe -> cantidad a retirar
    synchronized void sacarDinero (String nombre, int importe) {
        if (getSaldo() >= importe) {
			//Después de la operación dormir el hilo
            try {
            Thread.sleep(1000);
            }
            catch (InterruptedException e) {
            e.printStackTrace();
            }

			restar(importe);		
			
			System.out.println(nombre+ " sacó de la cuenta= " + importe + "€");
            System.out.println("Saldo actual cuenta= " + getSaldo() + "€");
		} else {
			System.out.println(nombre+ " no puede sacar " + importe + "€ -> NO HAY SALDO SUFICIENTE" );
            System.out.println("Saldo actual cuenta= " + getSaldo() + "€");
        }
		if (getSaldo() < 0) {
			System.out.println("SALDO NEGATIVO => "+getSaldo());
		}
    }

    //método ingresarDinero
    //nombre -> persona que realiza la operación
    //importe -> cantidad a retirar
    synchronized void ingresarDinero (String nombre, int importe) {
        //Después de la operación dormir el hilo
        try {
        Thread.sleep(1000);
        }
        catch (InterruptedException e) {
        e.printStackTrace();
        }

        sumar(importe);

        System.out.println(nombre+" ingresó en la cuenta " + importe + "€");
        System.out.println("Saldo actual cuenta= " + getSaldo() + "€");
    }
}

// Clase HiloSacarDinero
class HiloSacarDinero extends Thread {
    private CuentaBancaria cuenta;
    private String nombre;
    private int cantidad;

    // Constructor de la clase
    HiloSacarDinero(CuentaBancaria micuenta, String nombre, int cantidad){
        this.cuenta = micuenta;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public void run() {
        for (int i = 0; i < 1; i++) {
            cuenta.sacarDinero(nombre, cantidad);
        }
    }
}

// Clase HiloIngresarDinero
class HiloIngresarDinero extends Thread {
    private CuentaBancaria cuenta;
    private String nombre;
    private int cantidad;
    // Constructor de la clase
    HiloIngresarDinero (CuentaBancaria micuenta, String nombre, int cantidad) {
        this.cuenta = micuenta;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }
    public void run() {
        for (int i = 0; i < 1; i++) {
            cuenta.ingresarDinero(nombre, cantidad);
        }
    }
}

// Clase principal
public class CajeroAutomático {
    public static void main(String[] args) {
        CuentaBancaria cuenta = new CuentaBancaria();
        HiloIngresarDinero hilo1 = new HiloIngresarDinero(cuenta, "Padre", 200);
        HiloSacarDinero hilo2 = new HiloSacarDinero(cuenta, "Madre", 800);
        HiloIngresarDinero hilo3 = new HiloIngresarDinero(cuenta, "Hijo1", 300);
        HiloSacarDinero hilo4 = new HiloSacarDinero(cuenta, "Hijo2", 800);
        HiloSacarDinero hilo5 = new HiloSacarDinero(cuenta, "Abuelo", 600);

        hilo1.start();
        hilo2.start();
        hilo3.start();
        hilo4.start();
        hilo5.start();
    }
}