#include <stdio.h>   // Para printf() y operaciones con ficheros (FILE, fopen, fprintf, fclose)
#include <unistd.h>  // Para getpid() y pause()
#include <signal.h>  // Para signal() y manejo de señales (SIGINT, SIG_DFL)
#include <time.h>    // Para time(), localtime(), struct tm

/*
   Manejador de la señal SIGINT (Ctrl+C)
   - Se ejecuta automáticamente cuando el usuario presiona Ctrl+C.
   - Guarda en un fichero "salidas.txt" la hora exacta en que se recibió la señal.
*/
void fun_signal(int s) {
    // Abrimos el fichero "salidas.txt" en modo añadir ("a")
    // Si no existe, se crea automáticamente
    FILE *f = fopen("salidas.txt", "a");

    // Obtenemos la hora actual del sistema
    time_t h = time(NULL);        // Hora en segundos desde 1970
    struct tm *t = localtime(&h); // Convertimos a estructura con hora local

    // Guardamos en el fichero el PID del proceso y la hora exacta
    // %d -> número entero, %02d -> entero con dos dígitos rellenado con 0 si hace falta
    fprintf(f, "Señal SIGINT recibida a las %d:%02d:%02d:%02d\n",
            getpid(), t->tm_hour, t->tm_min, t->tm_sec);

    // Cerramos el fichero para asegurar que se guarden los cambios
    fclose(f);

    // Restauramos el comportamiento por defecto de SIGINT
    // Si el usuario presiona Ctrl+C otra vez, el programa terminará
    signal(SIGINT, SIG_DFL);
}

int main() {
    // Registramos el manejador fun_signal para la señal SIGINT (Ctrl+C)
    signal(SIGINT, fun_signal);

    // Bucle principal que mantiene el programa activo
    // pause() suspende el programa hasta que llegue cualquier señal
    while (1) {
        pause();
    }

    return 0;
}
