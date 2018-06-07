#include "funciones.h"

String leerRFID(MFRC522 mfrc522){
  String retorno = "";
  if ( mfrc522.PICC_IsNewCardPresent()) 
        {  
      //Seleccionamos una tarjeta
            if ( mfrc522.PICC_ReadCardSerial()) 
            {
                  // Enviamos serialemente su UID
                  //Serial.print("Card UID:");
                  for (byte i = 0; i < mfrc522.uid.size; i++) {
                          //Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
                          //Serial.print(mfrc522.uid.uidByte[i], HEX);
                          retorno = String(retorno + String(mfrc522.uid.uidByte[i], HEX));   
                  } 
                  //Serial.println();
                  // Terminamos la lectura de la tarjeta  actual
                  mfrc522.PICC_HaltA();         
            }      
  }
  return retorno;
}

bool consultarRFID(String idTarjeta){
  return true;
}

