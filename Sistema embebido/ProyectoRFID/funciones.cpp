#include "funciones.h"


#define HOST_NAME "192.168.1.37"
#define HOST_PORT 80

uint8_t buffer[700] = { 0 };

void desbloquearPuerta(){
  digitalWrite(DOOR_OUT, HIGH);
  doorUnlockMillis = currentMillis;
  puertaDesbloqueada = true;
}

bool manejarLuz(byte porcentaje){
  if(porcentaje == 0){
    analogWrite(LED_LIGHT_OUT, 0);
    return false;
  }
  analogWrite(LED_LIGHT_OUT, (porcentaje*255)/100);
}

void bloquearPuerta(){
  digitalWrite(DOOR_OUT, LOW);
  puertaDesbloqueada = false;
}

bool pasoTiempoDoorUnlock(){
  return currentMillis - doorUnlockMillis > TIME_INTERVAL_UNLOCK;
}


bool pasoTiempoEnviarSensores(){
  //return currentMillis - sensorSendMillis > TIME_INTERVAL_INFO;
  return currentMillis - actionReciveMillis > TIME_INTERVAL_ACTION;
}

bool pasoTiempoRecibirAcciones(){
  return currentMillis - actionReciveMillis > TIME_INTERVAL_ACTION;
}

String leerRFID(MFRC522 mfrc522){
  String retorno = F("");
  if ( mfrc522.PICC_IsNewCardPresent()) 
        {  
      //Seleccionamos una tarjeta
            if ( mfrc522.PICC_ReadCardSerial()) 
            {
                  //Serial.println("Se detecto una tarjeta");
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
  
  String request = String("GET /arduino/"+idTarjeta+"/ HTTP/1.1\r\nHost: 192.168.1.37\r\nConnection: close\r\n\r\n");
  int retorno = sendWiFiRequest(request);
//  Serial.println("termino consultar RFID");
  if(retorno == 1)
    return true;
  return false;
/*  
  if (wifi.createTCP(HOST_NAME, HOST_PORT)) {
      //Serial.print("create tcp ok\r\n");
   }
   else {
      Serial.print("create tcp err\r\n");
      return false;
   }
 
   String request = String("GET /arduino/"+idTarjeta+"/ HTTP/1.1\r\nHost: 192.168.1.37\r\nConnection: close\r\n\r\n");

   wifi.send((const uint8_t*)request.c_str(), request.length());
 
   uint32_t len = wifi.recv(buffer, sizeof(buffer), 10000);
   if (len > 0) 
   {
      //Serial.print("Received:\r\n");
      for (uint32_t i = 0; i < len; i++) 
      {
         char c = (char)buffer[i];
         //Serial.print(c);
         if (c == '|')
         {
          if ((char)buffer[i + 1] == '1'){
            return true;
          }else{
            return false;
          }
         }
      }
      //Serial.print("\r\n");
      
}
return false;*/
}


bool consultarAccionesRemotas(int accionesExternas[2]){

  //return true;
  //consulto por abrir la puerta

  int retorno = sendWiFiRequest( F("GET /arduino/abrirpuerta/ HTTP/1.1\r\nHost: 192.168.1.37\r\nConnection: close\r\n\r\n"));
  //Serial.println("termino consultar si abre la puerta");
  if(retorno < 0){
    accionesExternas[0] = -1;
  }
  accionesExternas[0] = retorno;
  delay(300);
  
  
  
  //consulto por la luz

  retorno = sendWiFiRequest(F("GET /arduino/luminosidad/ HTTP/1.1\r\nHost: 192.168.1.37\r\nConnection: close\r\n\r\n"));
  if(retorno < 0){
    accionesExternas[1] = -1;
  }
  accionesExternas[1] = retorno;

  return true;
}

int sendWiFiRequest(String request){
  
    String retorno = "";
  if (wifi.createTCP(HOST_NAME, HOST_PORT)) {
      //Serial.print("create tcp ok\r\n");
   }
   else {
      Serial.print(F("create tcp err\r\n"));
      //Serial.print(request);
      return false;
   }

   wifi.send((const uint8_t*)request.c_str(), request.length());
 
   uint32_t len = wifi.recv(buffer, sizeof(buffer), 10000);
   
   if (len > 0) 
   {
      //Serial.print("Received:\r\n");
      for (uint32_t i = 0; i < len; i++) 
      {
         char c = (char)buffer[i];
         //Serial.print(c);
         if (c == '|')
         {
            for (uint32_t j = i + 1; j < len; j++)
            {
               c = (char)buffer[j];
               if (c == '|') break;
               retorno = String(retorno + String(c));;
            }
            break;
         }
      }
      if(retorno == "")
        return -1000;
      return retorno.toInt();
      
}
return -1000; // hubo un problema

}

void leerSensores(){
  String request = String("GET /sensores/"+String(analogRead(LDR_IN))+"/"+digitalRead(PIR_IN)+"/"+luzPorciento+"/HTTP/1.1\r\nHost: 192.168.1.37\r\nConnection: close\r\n\r\n");
  int retorno = sendWiFiRequest(request);
}

