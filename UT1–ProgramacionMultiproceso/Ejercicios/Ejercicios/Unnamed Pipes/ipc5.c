#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <stdbool.h>

int main() {
    int fd1[2];  // Pipe: hijo -> padre
    int fd2[2];  // Pipe: padre -> hijo
    pid_t pid;

    // Crear los pipes
    pipe(fd1);
    pipe(fd2);

    pid = fork();

    if (pid == 0) {
        // --- PROCESO HIJO ---
        close(fd1[0]); // No lee del pipe hijo->padre
        close(fd2[1]); // No escribe en el pipe padre->hijo

        int dni;
        char letra;

        printf("Introduce el número de tu DNI: ");
        scanf("%d", &dni);  // <-- falta el & en tu código

        // Enviar el número al padre
        write(fd1[1], &dni, sizeof(int));

        // Esperar la letra del padre
        read(fd2[0], &letra, sizeof(char));

        printf("La letra del DNI es: %c\n", letra);
    } else {
        // --- PROCESO PADRE ---
        close(fd1[1]); // No escribe en pipe hijo->padre
        close(fd2[0]); // No lee del pipe padre->hijo

        int dni;
        char letra;
        char letras[] = "TRWAGMYFPDXBNJZSQVHLCKE";

        // Leer el número enviado por el hijo
        read(fd1[0], &dni, sizeof(int));

        // Calcular la letra
        letra = letras[dni % 23];

        // Enviar la letra al hijo
        write(fd2[1], &letra, sizeof(char));

        // Esperar a que el hijo termine
        wait(NULL);
    }

    return 0;
}
