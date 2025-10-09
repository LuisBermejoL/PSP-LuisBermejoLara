#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <time.h>
#include <stdbool.h>  // <-- Necesario para usar bool

int main() {
    int fd1[2];  // Pipe del padre al hijo
    int fd2[2];  // Pipe del hijo al padre
    pid_t pid;

    // Crear los pipes
    pipe(fd1);
    pipe(fd2);

    pid = fork();

    if (pid == 0) {
        // --- PROCESO HIJO ---
        close(fd1[1]); // Cierra el extremo de escritura del primer pipe
        close(fd2[0]); // Cierra el extremo de lectura del segundo pipe

        int numero;
        int factorial = 1;

        // Leer el número del padre
        read(fd1[0], &numero, sizeof(int));

        // Calcular factorial
        for (int i = 1; i <= numero; i++) {
            factorial *= i;
        }

        // Enviar el resultado al padre
        write(fd2[1], &factorial, sizeof(int));

    } else {
        // --- PROCESO PADRE ---
        close(fd1[0]); // Cierra el extremo de lectura del primer pipe
        close(fd2[1]); // Cierra el extremo de escritura del segundo pipe

        int numero;
        int factorial;
        time_t t;

        srand((unsigned) time(&t));
        numero = (rand() % 10) + 1; // Evita el 0

        printf("El proceso padre genera el número %d y lo envía al hijo.\n", numero);

        // Enviar número al hijo
        write(fd1[1], &numero, sizeof(int));

        // Esperar al hijo
        wait(NULL);

        // Leer factorial desde el hijo
        read(fd2[0], &factorial, sizeof(int));

        printf("El factorial calculado por el proceso hijo: %d != %d\n", numero, factorial);
    }

    return 0;
}
