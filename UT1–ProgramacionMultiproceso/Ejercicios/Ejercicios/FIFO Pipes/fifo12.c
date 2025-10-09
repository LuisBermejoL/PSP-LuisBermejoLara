#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>

int main() {
    const char *fifo1 = "FIFO1";
    const char *fifo2 = "FIFO2";
    int numero, factorial = 1;

    // Crear FIFO si no existe
    mkfifo(fifo1, 0666);
    mkfifo(fifo2, 0666);

    // Abrir FIFO para lectura (bloquea hasta que el escritor escriba)
    int fd1 = open(fifo1, O_RDONLY);
    if (fd1 == -1) {
        printf("lector: error al abrir FIFO");
        exit(EXIT_FAILURE);
    }

    // Leer el número enviado
    if (read(fd1, &numero, sizeof(numero)) == -1) {
        printf("lector: error al leer");
        close(fd1);
        exit(EXIT_FAILURE);
    }
    
    // Calcular factorial
    for (int i = 1; i <= numero; i++) {
        factorial *= i;
    }
    
    // Abrir FIFO para escritura (bloquea hasta que el lector abra)
    int fd2 = open(fifo2, O_WRONLY);
    if (fd2 == -1) {
        printf("escritor: error al abrir FIFO");
        exit(EXIT_FAILURE);
    }

    // Escribir el número en la FIFO
    if (write(fd2, &factorial, sizeof(factorial)) == -1) {
        printf("escritor: error al escribir");
    }

    close(fd2);
    return 0;
}
