#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <string.h>

int main() {
    int fd[2];
    pid_t pid;

    pipe(fd);

    pid = fork();

    if (pid == 0) {
        // --- HIJO ---
        close(fd[1]); // Cierra escritura
        char buffer[10];
        int suma = 0;
        int n;

        while ((n = read(fd[0], buffer, sizeof(buffer)-1)) > 0) {
            buffer[n] = '\0';

            // Procesamos línea por línea
            char *line = strtok(buffer, "\n");
            while (line != NULL) {
                if (line[0] == '+') {
                    printf("Recibido carácter +\n");
                    printf("La suma total es igual a: %d\n", suma);
                    close(fd[0]);
                    exit(0);
                }

                int num = atoi(line);
                printf("Numero a sumar: %d\n", num);
                suma += num;

                line = strtok(NULL, "\n");
            }
        }

        close(fd[0]);
        exit(0);

    } else {
        // --- PADRE ---
        close(fd[0]); // Cierra lectura

        write(fd[1], "25\n", 3);
        write(fd[1], "18\n", 3);
        write(fd[1], "67\n", 3);
        write(fd[1], "+\n", 2); // Indicamos fin

        close(fd[1]);
        wait(NULL); // Espera al hijo
    }

    return 0;
}
