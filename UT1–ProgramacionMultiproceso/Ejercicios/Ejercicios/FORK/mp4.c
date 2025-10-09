#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
void main() {

 printf("CCC \n");
 
 if (fork()!=0)
 {
   printf("BBB \n");
 } 
 else 
 {
   printf("AAA \n");
 }
 
 exit(0);
}
