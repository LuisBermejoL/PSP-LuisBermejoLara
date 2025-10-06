#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>

void main() {
  pid_t vpid1, vpid2, vpid3, Hijo_pid;
  
  vpid1 = fork();

  if (vpid1 == -1 ) //Ha ocurrido un error 
  {
    printf("No se ha podido crear el proceso hijo...");
    exit(-1);       
  }
  if (vpid1 == 0 )  //Nos encontramos en Proceso hijo 
  {    
    vpid2 = fork();
    
    if (vpid2 == 0)
    {
      vpid3 = fork();
      
      if (vpid3 == 0)
      {
        printf("Soy el proceso P4: Mi PID es %d, El PID de mi padre es: %d y la suma de ambos PID es: %d\n",  getpid(), getppid(), getpid() + getppid());
      }
      else
      {
        Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
      
        printf("Soy el proceso P3: Mi PID es %d, El PID de mi padre es: %d y la suma de ambos PID es: %d\n",  getpid(), getppid(), getpid() + getppid());
      }
    }
    else
    {
      Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
      
      printf("Soy el proceso P2: Mi PID es %d, El PID de mi padre es: %d y la suma de ambos PID es: %d\n",  getpid(), getppid(), getpid() + getppid());
    }
  }
  else    //Nos encontramos en Proceso padre 
  { 
    Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
    
    printf("Soy el proceso P1: Mi PID es %d, El PID de mi padre es: %d y la suma de ambos PID es: %d\n",  getpid(), getppid(), getpid() + getppid());
  }
  exit(0);
}
