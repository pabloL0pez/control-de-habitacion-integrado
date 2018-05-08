// Acá van los pin de entrada/salida (todavía hay que definirlos, los actuales son temporales).
const int SNSR_MAGNETICO = 10;
const int PESTILLO = 11; // No me acuerdo si usábamos esto o no.

// Acá van valores de control
const long T_MAX_PUERTA_ABIERTA = 5000;
const long T_MAX_PUERTA_DESBLOQUEADA = 5000;
const long T_MAX_CHICHARRA = 15000;

// Acá van los tiempos que llevan activas ciertas cosas (por ejemplo la chicharra, hay que apagarla si sonó durante más de 15 segundos).
unsigned long t_puerta_desbloqueada;
unsigned long t_puerta_abierta;
unsigned long t_chicharra;

// Banderas
bool respuestaServidor;
bool puertaBloqueada = true;
bool puertaAbierta = false;
bool chicharraActiva = false;

int codigoRFID;

void setup()
{
	pinMode(SNSR_MAGNETICO,INPUT);
  pinMode(PESTILLO,OUTPUT);
}

void loop()
{
  // Si el servidor da como válido el código RFID, se desbloquea la puerta.
  if(respuestaServidor == true)
  {
    desbloquearPuerta();
    puertaBloqueada = false;
    t_puerta_desbloqueada = millis(); // Marco el instante en el que se desbloqueó la puerta.
  }

  // Si el sensor magnético se activa, es porque la puerta ha sido abierta.
  if(digitalRead(SNSR_MAGNETICO) == HIGH) {
    puertaAbierta = true;
    t_puerta_abierta = millis(); // Marco el instante en el que se abrió la puerta.
  } else {
    puertaAbierta = false;
    chicharraActiva = false;
  }

  // Si la puerta permaneció desbloqueada mucho tiempo sin abrirse, o la puerta está cerrada, la bloqueo.
  if((!puertaBloqueada && !puertaAbierta && (millis() - t_puerta_desbloqueada) > T_MAX_PUERTA_DESBLOQUEADA)) {
    bloquearPuerta();
    puertaBloqueada = true;    
  }

  // Si la puerta permaneció abierta mucho tiempo, activo la chicharra.
  if(puertaAbierta && (millis() - t_puerta_abierta > T_MAX_PUERTA_ABIERTA)) {
    activarChicharra();
    chicharraActiva = true;
    t_chicharra = millis(); // Marco el instante en el que se activó la chicharra
  }

  // Si la chicharra estuvo activa mas de cierto tiempo, la apago.
  if(chicharraActiva && (millis() - t_chicharra) > T_MAX_CHICHARRA) {
    chicharraActiva = false;
    t_puerta_abierta = millis(); // Reinicio el tiempo de puerta abierta para que no se me prenda de nuevo la chicharra (igual para mí la chicharra hay que apagarla solamente cuando se cierra la puerta).
  }
}

void desbloquearPuerta() {
 // No me acuerdo que usamos para bloquear/desbloquear la puerta
}

void bloquearPuerta() {
 // No me acuerdo que usamos para bloquear/desbloquear la puerta
}

void activarChicharra() {
  digitalWrite(CHICHARRA, HIGH);
  delay(1000);
  digitalWrite(CHICHARRA, LOW);
  delay(500);
}
/*
void loop()
{
  //leer Tarjeta SPI
  // turn the LED on (HIGH is the voltage level)
  
  //leer dato
  if(respuestaServidor == true)
  {
     bloquearPuerta=false;
    delay(10000);
    //abrirPuerta=INput. leer estado de puerta
    
    if(abrirPuerta == false && bloquearPuerta == false)
    {
      bloquearPuerta=true;      
    }
    delay(5000);
    if(abrirPuerta == true){
       //sonar chicharra durante 10 segundos.
    }
    
    //LUces.
  }
*/



