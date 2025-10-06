#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>

void main() {
  pid_t vpid1, vpid2, vpid3, vpid4, vpid5, Hijo_pid, Abuelo_pid1, Abuelo_pid2, Abuelo_pid3;
  
  Abuelo_pid1 = getppid();
  vpid1 = fork();
  Abuelo_pid2 = getppid();

  if (vpid1 == -1 ) //Ha ocurrido un error 
  {
    printf("No se ha podido crear el proceso hijo...");
    exit(-1);       
  }
  if (vpid1 == 0 )  //Nos encontramos en Proceso hijo 
  {    
    vpid2 = fork();
    Abuelo_pid3 = getppid();
    
    if (vpid2 == 0)
    {
      vpid4 = fork();
      Abuelo_pid3 = getppid();
      
      if (vpid4 == 0)
      {
        printf("Soy el proceso P5: Mi PID es %d, El PID de mi abuelo: %d\n",  getpid(), Abuelo_pid3);
      }
      else
      {
        Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
        
        printf("Soy el proceso P3: Mi PID es %d, El PID de mi abuelo: %d\n",  getpid(), Abuelo_pid2);
      }
    }
    else
    {
      vpid3 = fork();
      
      if (vpid3 == 0)
      {
        vpid5 = fork();
        Abuelo_pid3 = getppid();
      
        if (vpid5 == 0)
        {
          printf("Soy el proceso P6: Mi PID es %d, El PID de mi abuelo: %d\n",  getpid(), Abuelo_pid3);
        }
        else
        {
          Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
        
          printf("Soy el proceso P4: Mi PID es %d, El PID de mi abuelo: %d\n",  getpid(), Abuelo_pid3);
        }
      }
      else
      {
      Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
      Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
      
      printf("Soy el proceso P2: Mi PID es %d, El PID de mi abuelo: %d\n",  getpid(), Abuelo_pid1);
      }
    }
  }
  else    //Nos encontramos en Proceso padre 
  { 
    Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
    
    printf("Soy el proceso P1: Mi PID es %d, No tengo el PID de mi abuelo\n",  getpid());
  }
  exit(0);
}
