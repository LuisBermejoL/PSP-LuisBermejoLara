#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>

void main() {
  pid_t vpid, Hijo_pid;
  
  vpid = fork();

  if (vpid == -1 ) //Ha ocurrido un error 
  {
    printf("No se ha podido crear el proceso hijo...");
    exit(-1);       
  }
  if (vpid == 0 )  //Nos encontramos en Proceso hijo 
  {        
    printf("Soy Luis Bermejo");
    
  }
  else    //Nos encontramos en Proceso padre 
  { 
   Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
   
   printf("\nSoy el proceso padre: Mi PID es %d, El PID de mi hijo es: %d ",  getpid(), vpid);
  }
   exit(0);
}
