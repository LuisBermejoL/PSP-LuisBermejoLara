#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <time.h>

int main() {
    const char *fifo = "PIPE02";
    int numero, factorial = 1;
    time_t t;

    // Crear FIFO si no existe
    mkfifo(fifo, 0666);
    
    srand((unsigned) time(&t));
    numero = (rand() % 10) + 1;

    // Abrir PIPE02 para escritura (bloquea hasta que el lector abra)
    int fd1 = open(fifo, O_WRONLY);
    if (fd1 == -1) {
        printf("escritor: error al abrir FIFO");
        exit(EXIT_FAILURE);
    }

    // Escribir el número en PIPE02
    if (write(fd1, &numero, sizeof(numero)) == -1) {
        printf("escritor: error al escribir");
    }
    
    // Abrir PIPE02 para lectura (bloquea hasta que el escritor escriba)
    int fd2 = open(fifo, O_RDONLY);
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
    
    unlink(fifo);
    
    return 0;
}
