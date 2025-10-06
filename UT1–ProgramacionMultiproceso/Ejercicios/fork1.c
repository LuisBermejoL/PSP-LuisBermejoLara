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
    if (getpid() % 2 == 0)
    {
      printf("\nSoy el proceso P2: Mi PID es %d, El PID de mi padre es: %d ",  getpid(), getppid());
    }
    else
    {
      printf("\nSoy el proceso P2: Mi PID es %d",  getpid());
    }
  }
  else    //Nos encontramos en Proceso padre 
  { 
    Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
    vpid2 = fork();
    
    if (vpid2 == 0)
    {
      vpid3 = fork();
      
      if (vpid3 == 0)
      {
        if (getpid() % 2 == 0)
        {
          printf("\nSoy el proceso P4: Mi PID es %d, El PID de mi padre es: %d ",  getpid(), getppid());
        }
        else
        {
          printf("\nSoy el proceso P4: Mi PID es %d",  getpid());
        }
      }
      else
      {
        Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
        
        if (getpid() % 2 == 0)
        {
          printf("\nSoy el proceso P3: Mi PID es %d, El PID de mi padre es: %d ",  getpid(), getppid());
        }
        else
        {
          printf("\nSoy el proceso P3: Mi PID es %d",  getpid());
        }
      }
    }
    else
    {
      Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
      
      if (getpid() % 2 == 0)
      {
        printf("\nSoy el proceso P1: Mi PID es %d, El PID de mi padre es: %d ",  getpid(), getppid());
      }
      else
      {
        printf("\nSoy el proceso P1: Mi PID es %d",  getpid());
      }
    }
  }
  exit(0);
}
