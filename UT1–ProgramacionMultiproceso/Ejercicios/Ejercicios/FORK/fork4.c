#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>

void main() {
  pid_t vpid1, vpid2, vpid3, vpid4, Hijo_pid;
  
  int acumulado = (int) getpid();
  
  vpid1 = fork();

  if (vpid1 == -1 ) //Ha ocurrido un error 
  {
    printf("No se ha podido crear el proceso hijo...");
    exit(-1);       
  }
  if (vpid1 == 0 )  //Nos encontramos en Proceso hijo 
  {    
    vpid3 = fork();
    
    if (vpid3 == 0)
    {
      if (getpid() %2 == 0)
      {
      	 acumulado = acumulado + 10;
      }
      else
      {
      	 acumulado = acumulado - 100;
      }
      
      printf("Soy el proceso P5: Mi PID es %d, La variable acumulado es: %d\n", getpid(), acumulado);
    }
    else
    {
      Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
      
      if (getpid() %2 == 0)
      {
        acumulado = acumulado + 10;
      }
      else
      {
      	 acumulado = acumulado - 100;
      }
      
      printf("Soy el proceso P2: Mi PID es %d, La variable acumulado es: %d\n", getpid(), acumulado);
    }
  }
  else    //Nos encontramos en Proceso padre 
  { 
    vpid2 = fork();
      
    if (vpid2 == 0)
    {
      vpid4 = fork();
      
      if (vpid4 == 0)
      {
        if (getpid() %2 == 0)
      	{
      	  acumulado = acumulado + 10;
      	}
      	else
      	{
      	  acumulado = acumulado - 100;
      	}
      	
        printf("Soy el proceso P4: Mi PID es %d, La variable acumulado es: %d\n", getpid(), acumulado);
      }
      else
      { 
        Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
        
      	if (getpid() %2 == 0)
      	{
      	  acumulado = acumulado + 10;
      	}
      	else
      	{
      	  acumulado = acumulado - 100;
      	}
      	
        printf("Soy el proceso P3: Mi PID es %d, La variable acumulado es: %d\n", getpid(), acumulado);
      }
    }
    else
    {
      Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
    
      printf("Soy el proceso P1: Mi PID es %d, La variable acumulado es: %d\n", getpid(), acumulado);
    }
  }
  exit(0);
}
