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
A3:   -----
A4:   -----
A5:   -----
A6:   -----
A7:   -----




*/
#include <SPI.h>
#include "funciones.h"

#define PUERTA_OUT 4
#define ALARM_OUT 5
#define LED_LIGHT_OUT 6
#define LED_GREEN_OUT 7
#define LED_RED_OUT 8

#define RST_PIN  9    //Pin 9 para el reset del RC522
#define SS_PIN  10   //Pin 10 para el SS (SDA) del RC522

#define PIR_IN A1
#define LDR_IN A0
#define BUTTON_IN A2

MFRC522 mfrc522(SS_PIN, RST_PIN); //Creamos el objeto para el RC522

void setup() {
  // put your setup code here, to run once:
  SPI.begin();        //Iniciamos el Bus SPI
  mfrc522.PCD_Init(); // Iniciamos  el MFRC522
}

void loop() {
  // put your main code here, to run repeatedly:
  String lectura = leerRFID(mfrc522);
  if(lectura.length() > 1){
    consultarRFID(lectura);
  }

  
  
}
