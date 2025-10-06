#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <time.h>
#include <string.h>

int main() {
    int fd[2];
    pid_t pid;

    pipe(fd);

    pid = fork();

    if (pid == 0) {
        // --- HIJO ---
        close(fd[0]); // Cierra lectura

        char numStr[10];
        time_t t;
        int numero1, numero2;

        // Inicializar semilla para números aleatorios
        srand((unsigned) time(&t));

        // Generar números aleatorios entre 1 y 50
        numero1 = rand() % 50;
        numero2 = rand() % 50;

        // Convertir los números a cadena y enviarlos al pipe con salto de línea
        sprintf(numStr, "%d\n", numero1);
        write(fd[1], numStr, strlen(numStr));

        sprintf(numStr, "%d\n", numero2);
        write(fd[1], numStr, strlen(numStr));

        close(fd[1]); // Cerramos escritura
        exit(0);

    } else {
        // --- PADRE ---
        close(fd[1]); // Cierra escritura

        char buffer[10];
        int numeros[2];
        int i = 0;
        char c;
        int j = 0;

        // Leer carácter por carácter hasta obtener dos números
        while (read(fd[0], &c, 1) > 0 && j < 2) {
            if (c == '\n') {
                buffer[i] = '\0';           // Terminamos cadena
                numeros[j] = atoi(buffer);  // Convertimos a número
                j++;
                i = 0;                      // Reiniciamos buffer
            } else {
                buffer[i++] = c;
            }
        }

        close(fd[0]);
        wait(NULL); // Espera al hijo

        // Mostrar operaciones
        printf("%d + %d = %d\n", numeros[0], numeros[1], numeros[0]+numeros[1]);
        printf("%d - %d = %d\n", numeros[0], numeros[1], numeros[0]-numeros[1]);
        printf("%d * %d = %d\n", numeros[0], numeros[1], numeros[0]*numeros[1]);
        printf("%d / %d = %d\n", numeros[0], numeros[1], numeros[0]/numeros[1]);
    }

    return 0;
}
