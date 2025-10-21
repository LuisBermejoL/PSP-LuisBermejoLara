#include <stdio.h>    // printf()
#include <unistd.h>   // getpid(), pause()
#include <signal.h>   // signal(), SIGINT, SIG_DFL
#include <time.h>     // time(), localtime(), struct tm

/* 
   Manejador de la señal SIGINT (Ctrl+C)
   - Se ejecuta automáticamente cuando el usuario presiona Ctrl+C.
   - Muestra la hora exacta en formato dd/mm/yyyy HH:MM:SS.
*/
void fun_signal(int s) {
  struct tm *t;          // Puntero a estructura con la hora local desglosada
  time_t h = time(NULL); // Obtiene la hora actual del sistema (en segundos desde 1970)
  t = localtime(&h);     // Convierte la hora a formato local (día, mes, hora, etc.)

  // Muestra el PID y la hora en el formato deseado
  printf("Fin del proceso %d: %02d/%02d/%d %02d:%02d:%02d\n",
         getpid(), t->tm_mday, t->tm_mon + 1, t->tm_year + 1900,
         t->tm_hour, t->tm_min, t->tm_sec);

  signal(SIGINT, SIG_DFL); // Restaura el comportamiento por defecto (terminar el programa)
}

/*
   Función principal:
   - Muestra la hora de inicio del proceso.
   - Registra el manejador de señal.
   - Mantiene el programa activo esperando señales.
*/
int main() {
  struct tm *t;          // Puntero a estructura con hora local
  time_t h = time(NULL); // Obtiene la hora actual
  t = localtime(&h);     // Convierte a formato local

  // Muestra el PID y la hora de inicio
  printf("Inicio del proceso %d: %02d/%02d/%d %02d:%02d:%02d\n",
         getpid(), t->tm_mday, t->tm_mon + 1, t->tm_year + 1900,
         t->tm_hour, t->tm_min, t->tm_sec);

  signal(SIGINT, fun_signal); // Asocia la señal SIGINT (Ctrl+C) a la función fun_signal()

  while (1) {
    pause();          // Pausa el proceso indefinidamente hasta recibir una señal
  }
  
  return 0;
}
