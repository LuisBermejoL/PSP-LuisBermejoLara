#include <stdio.h>    // Para printf() y scanf()
#include <signal.h>   // Para signal() y manejo de señales (SIGALRM)
#include <unistd.h>   // Para pause() y alarm()
#include <time.h>     // Para time(), localtime() y struct tm

// Variables globales
int n_alrm; // Número de veces que sonará la alarma
int s_alrm; // Intervalo de segundos entre alarmas

// ----------------------------------------------------------
// Manejador de la señal SIGALRM
// Esta función se ejecuta automáticamente cada vez que llega la alarma
// ----------------------------------------------------------
void alarma(int s) {
    // Si ya se cumplieron todas las repeticiones, se desactiva la alarma
    if (n_alrm <= 0) { 
        printf("Alarma desactivada\n");
        return;
    }

    // Obtenemos la hora actual del sistema
    time_t h = time(NULL);        // Hora en segundos desde 1970
    struct tm *t = localtime(&h); // Convertimos a estructura con hora local

    // Mostramos en pantalla la hora exacta en que sonó la alarma
    printf("Señal de alarma recibida a las %02d:%02d:%02d\n",
            t->tm_hour, t->tm_min, t->tm_sec);

    // Programamos la siguiente alarma para dentro de s_alrm segundos
    alarm(s_alrm);

    // Disminuimos el contador de repeticiones restantes
    n_alrm -= 1;
}

int main() {
    // Solicitamos al usuario el número de veces que sonará la alarma
    printf("¿Cuántas veces sonará la alarma?\n");
    scanf("%d", &n_alrm);

    // Solicitamos al usuario cada cuántos segundos se repetirá la alarma
    printf("¿Cada cuántos segundos se repetirá la alarma?\n");
    scanf("%d", &s_alrm);

    // Registramos el manejador de SIGALRM
    signal(SIGALRM, alarma);

    // Programamos la primera alarma
    alarm(s_alrm);

    // Mensaje informativo
    printf("Alarma activada\n");

    // Bucle principal que mantiene el programa activo
    // pause() suspende la ejecución hasta que llegue cualquier señal
    while (1) {
        pause();
    }

    return 0;
}
