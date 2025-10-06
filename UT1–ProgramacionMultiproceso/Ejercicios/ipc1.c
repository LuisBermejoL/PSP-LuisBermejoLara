#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <time.h>   // Necesario para obtener la fecha/hora

void main(){
     int fd[2]; 
     char buffer[80];
     pid_t pid;
     time_t t;       // Variable para almacenar el tiempo actual
    
     // Creamos el pipe
     pipe(fd); 
     
     //Se crea un proceso hijo
     pid = fork();

     if (pid==0)
     {
       close(fd[1]); // Cierra el descriptor de escritura
       printf("Soy el proceso hijo con pid: %d\n", getpid());
       read(fd[0], buffer, 80);
       printf("Fecha/hora: %s", buffer);
     }
     else
     {
       close(fd[0]); // Cierra el descriptor de lectura
       
       // Obtenemos la fecha/hora actual
      time(&t);
      char *fecha = ctime(&t); // Devuelve un string tipo "Mon Oct 10 18:38:39 2022\n"
       
       write(fd[1], fecha, 80);  
       wait(NULL);    
     }
}
