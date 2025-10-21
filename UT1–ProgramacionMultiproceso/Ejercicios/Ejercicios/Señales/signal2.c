#include <stdio.h>    // Para printf()
#include <signal.h>   // Para signal() y manejo de señales (SIGALRM)
#include <unistd.h>   // Para pause() y alarm()
#include <time.h>     // Para time(), si quisieras registrar hora exacta

// Contador global que guarda los segundos transcurridos desde el inicio
int segundos = 0;

// ----------------------------------------------------------
// Manejador de la señal SIGALRM
// Esta función se ejecuta automáticamente cada vez que llega la alarma
// ----------------------------------------------------------
void tiempo_transcurrido(int s) {
    segundos += 5; // Sumamos 5 segundos cada vez que llega la alarma

    // Mostramos en pantalla los segundos transcurridos
    printf("Han transcurrido %d segundos\n", segundos);

    // Programamos la siguiente alarma para dentro de 5 segundos
    alarm(5);
}

int main() {
    // Asociamos la señal SIGALRM al manejador tiempo_transcurrido
    signal(SIGALRM, tiempo_transcurrido);

    // Programamos la primera alarma a 5 segundos
    alarm(5);

    // Bucle principal del programa
    // pause() suspende la ejecución hasta que llegue cualquier señal
    // De esta forma el programa no consume CPU mientras espera
    while (1) {
        pause();
    }

    return 0;
}
