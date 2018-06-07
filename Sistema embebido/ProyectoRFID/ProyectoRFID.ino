#include <SPI.h>
#include "funciones.h"

#define RST_PIN  9    //Pin 9 para el reset del RC522
#define SS_PIN  10   //Pin 10 para el SS (SDA) del RC522
#define PUERTA_OUT 5
#define LED_GREEN_OUT 6
#define LED_RED_OUT 7
#define LED_LIGHT_OUT 8
#define ALARM_OUT 11
#define PIR_IN 12
#define LDR_IN 13
#define BUTTON_IN 4

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
