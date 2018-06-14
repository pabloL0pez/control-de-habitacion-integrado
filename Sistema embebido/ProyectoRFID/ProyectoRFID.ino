/*
Control de Acceso

Pinout:
D0:   -----
D1:   -----
D2:   RX
D3:   TX  
D4:   Desbloqueo Puerta 
D5:   Sirena
D6:   Luz Habitacion
D7:   Luz Verde
D8:   Luz Roja
D9:   Reset RC522
D10:  SS (SDA) RC522
D11:  MOSI RC522
D12:  MISO RC522
D13:  -----
A0:   LDR
A1:   PIR
A2:   BUTTON
A3:   Magnetico
A4:   -----
A5:   -----
A6:   -----
A7:   -----




*/
#include <SPI.h>
#include "funciones.h"

// definicion de los pines del arduino
#define DOOR_OUT 4
#define ALARM_OUT 5
#define LED_LIGHT_OUT 6
#define LED_GREEN_OUT 7
#define LED_RED_OUT 8

#define RST_PIN  9    //Pin 9 para el reset del RC522
#define SS_PIN  10   //Pin 10 para el SS (SDA) del RC522

#define PIR_IN A1
#define LDR_IN A0
#define BUTTON_IN A2
#define DOOR_IN A3

// definiciones para el los intervalos de tiempo
#define TIME_INTERVAL_INFO 2      //Tiempo de envio de informacion de sensores
#define TIME_INTERVAL_ACTION 1    //Tiempo de consulta de acciones
#define TIME_INTERVAL_UNLOCK 30   //Tiempo de espera para volver a bloquear puerta
#define TIME_INTERVAL_ALARM 30    //Tiempo de espera para sonar alarma

MFRC522 mfrc522(SS_PIN, RST_PIN); //Creamos el objeto para el RC522

bool puertaAbierta = false;
bool puertaDesbloqueada = false;
// array con las acciones externas recividas. si es negativo no hago nada
//accionesExternas[0] -> Desbloqueo de puerta
//accionesExternas[1] -> Luz habitacion
byte accionesExternas[] = {-1, -1}; 

void setup() {
  // put your setup code here, to run once:
  SPI.begin();        //Iniciamos el Bus SPI
  mfrc522.PCD_Init(); // Iniciamos  el MFRC522
}

void loop() {

  unsigned long currentMillis = millis();
  // Leo si tengo alguna tarjeta para leer
  String lectura = leerRFID(mfrc522);
  if(lectura.length() > 1){
    if(consultarRFID(lectura)){
      desbloquearPuerta();
      puertaDesbloqueada = true;
    }
  }

  if(false){ //paso el tiempo para revisar las acciones
    if(consultarAccionesRemotas(accionesExternas)){
      if(accionesExternas[0] == 1 && !puertaDesbloqueada){
        desbloquearPuerta();
        puertaDesbloqueada = true;
      }
      if(accionesExternas[1]> -1 ){
        manejarLuz(accionesExternas[1]);
      }
    }
  }

  if(false){ //paso el tiempo para enviar informacion de sensores
    leerSensores();
  }

  bool lectPuerta = digitalRead(DOOR_IN);

  if(puertaDesbloqueada){
    
    if(!puertaAbierta){
      if(lectPuerta){ // si justo se abrio la puerta cuando se penso que estaba cerrada
        puertaAbierta = true;
        bloquearPuerta();
        puertaDesbloqueada = false
      }
    }else if(false){ // si despues de un tiempo de estar desbloqueada la puerta 
        
      }
    
  }

  

  

  
  
}
