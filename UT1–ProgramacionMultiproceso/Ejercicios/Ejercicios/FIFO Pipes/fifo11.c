#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <time.h>

int main() {
    const char *fifo1 = "FIFO1";
    const char *fifo2 = "FIFO2";
    int numero, factorial = 1;
    time_t t;

    // Crear FIFO si no existe
    mkfifo(fifo1, 0666);
    mkfifo(fifo2, 0666);
    
    srand((unsigned) time(&t));
    numero = (rand() % 10) + 1;

    // Abrir FIFO para escritura (bloquea hasta que el lector abra)
    int fd1 = open(fifo1, O_WRONLY);
    if (fd1 == -1) {
        printf("escritor: error al abrir FIFO");
        exit(EXIT_FAILURE);
    }

    // Escribir el número en la FIFO
    if (write(fd1, &numero, sizeof(numero)) == -1) {
        printf("escritor: error al escribir");
    }

    close(fd1);
    
    // Abrir FIFO para lectura (bloquea hasta que el escritor escriba)
    int fd2 = open(fifo2, O_RDONLY);
    if (fd2 == -1) {
        printf("lector: error al abrir FIFO");
        exit(EXIT_FAILURE);
    }

    // Leer el número enviado
    if (read(fd2, &factorial, sizeof(factorial)) == -1) {
        printf("lector: error al leer");
        close(fd2);
        exit(EXIT_FAILURE);
    }
    
    printf("El factorial del numero %d es: %d", numero, factorial);
    
    close(fd1);
    
    unlink(fifo1);
    unlink(fifo2);
    
    return 0;
}
