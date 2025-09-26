#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
void main() {
 pid_t pid1, pid2, Hijo_pid;
 printf("AAA \n");
 pid1 = fork();
 
 if (pid1==0)
 {
   printf("BBB \n");
 }
 else
 {
   Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
   pid2 = fork();
   
   if (pid2 == 0)
    {
      printf("CCC \n");
    }
    else
    {
      Hijo_pid = wait(NULL); //espera la finalización del proceso hijo
      printf("CCC \n");
    }
 }
 
 exit(0);
}
