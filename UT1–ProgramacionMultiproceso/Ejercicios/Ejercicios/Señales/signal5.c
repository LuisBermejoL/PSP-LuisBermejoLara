#include <stdio.h>      // Entrada/salida estándar
#include <stdlib.h>     // Funciones generales (exit, etc.)
#include <unistd.h>     // Funciones POSIX (fork, pipe, read, write, close, sleep)
#include <signal.h>     // Manejo de señales
#include <sys/wait.h>   // Espera de procesos hijos

int fd[2];             // Array para el pipe: fd[0] lectura, fd[1] escritura
pid_t p2, p3;          // PIDs de los procesos hijos P2 y P3

// Función para terminar todos los procesos al recibir SIGINT (Ctrl+C)
void terminar(int s) {
    printf("Mandando señal de terminación a P2 con pid %d\n", p2);
    kill(p2, SIGTERM);   // Envía señal de terminación a P2
    printf("Mandando señal de terminación a P3 con pid %d\n", p3);
    kill(p3, SIGTERM);   // Envía señal de terminación a P3
    close(fd[0]);         // Cierra el descriptor de lectura del pipe
    close(fd[1]);         // Cierra el descriptor de escritura del pipe
    printf("Fin proceso padre con pid %d\n", getpid());
    exit(0);             // Termina el proceso padre
}

// Función manejadora de P2 para recibir números pares
void num_par(int s) {
    int num;
    read(fd[0], &num, sizeof(int)); // Lee número del pipe
    printf("Número par %d recibido por P2 con pid %d\n", num, getpid());
}

// Función manejadora de P3 para recibir números impares
void num_impar(int s) {
    int num;
    read(fd[0], &num, sizeof(int)); // Lee número del pipe
    printf("Número impar %d recibido por P3 con pid %d\n", num, getpid());
}

int main() {
    pipe(fd); // Crea el pipe para comunicación entre padre e hijos

    p2 = fork(); // Crea el primer hijo P2
    if (p2 == 0) { // Código del hijo P2
        signal(SIGUSR1, num_par); // Asocia la señal SIGUSR1 a la función num_par
        while(1) pause();         // Espera señales indefinidamente
    }

    p3 = fork(); // Crea el segundo hijo P3
    if (p3 == 0) { // Código del hijo P3
        signal(SIGUSR2, num_impar); // Asocia la señal SIGUSR2 a la función num_impar
        while(1) pause();           // Espera señales indefinidamente
    }

    // Código del padre P1
    signal(SIGINT, terminar); // Ctrl+C terminará todos los procesos

    int numero;
    while(1) {
        printf("Introduce número: ");
        scanf("%d", &numero); // Lee número del usuario

        if (numero == 0) {    // Si es 0, termina todos los procesos
            terminar(0);
        }

        write(fd[1], &numero, sizeof(int)); // Escribe el número en el pipe

        if (numero % 2 == 0) {
            kill(p2, SIGUSR1); // Si es par, envía señal a P2
        } else {
            kill(p3, SIGUSR2); // Si es impar, envía señal a P3
        }
        sleep(1); // Pequeña pausa para que los hijos procesen el número
    }

    return 0;
}