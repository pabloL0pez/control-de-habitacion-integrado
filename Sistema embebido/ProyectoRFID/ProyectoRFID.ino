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
A4:   Testigo Pir
A5:   -----
A6:   -----
A7:   -----
*/


#include "funciones.h"

#define SSIDNAME "SOA-ACCESO"
#define PASSWORD "soa123456"

MFRC522 mfrc522(SS_PIN, RST_PIN); //Creamos el objeto para el RC522

SoftwareSerial softSerial(2, 3); // RX | TX
ESP8266 wifi(softSerial);



bool sirenaSonando = false;
bool puertaAbierta = false;
bool puertaDesbloqueada = false;
// array con las acciones externas recividas. si es negativo no hago nada
//accionesExternas[0] -> Desbloqueo de puerta
//accionesExternas[1] -> Luz habitacion
//int accionesExternas[] = {-1, -1}; 
int accionesExternas[] = {-1, -1}; 
unsigned long doorUnlockMillis = 0;
unsigned long actionReciveMillis = 3000;
//unsigned long sensorSendMillis = 3000;
unsigned long currentMillis = 0;
byte luzPorciento = 0;

void setup() {
  pinMode(DOOR_OUT,OUTPUT);
  pinMode(ALARM_OUT,OUTPUT);
  pinMode(LED_LIGHT_OUT,OUTPUT);
  pinMode(LED_GREEN_OUT,OUTPUT);
  pinMode(LED_RED_OUT,OUTPUT);
  pinMode(LED_PIR_OUT,OUTPUT); // PIR Testigo

  pinMode(PIR_IN,INPUT);
  pinMode(LDR_IN,INPUT);
  pinMode(BUTTON_IN,INPUT);
  pinMode(DOOR_IN,INPUT);

  
  // put your setup code here, to run once:
  SPI.begin();        //Iniciamos el Bus SPI
  mfrc522.PCD_Init(); // Iniciamos  el MFRC522
   softSerial.begin(9600);
  Serial.begin(9600);
/*
   if (wifi.setOprToStationSoftAP()) {
      Serial.print("to station + softap ok\r\n");
   }
   else {
      Serial.print("to station + softap err\r\n");
   }
 
   if (wifi.joinAP(SSIDNAME, PASSWORD)) {
      Serial.print("Join AP success\r\n");

   }
   else {
      Serial.print("Join AP failure\r\n");
   }
 
   if (wifi.disableMUX()) {
      Serial.print("single ok\r\n");
   }
   else {
      Serial.print("single err\r\n");
   }
  */
   delay(5000);
   Serial.print(F("setup end\r\n"));
}

void loop() {
  digitalWrite(LED_PIR_OUT, digitalRead(PIR_IN));
  bool lectPuerta = digitalRead(DOOR_IN);
  bool lectBotonPuerta = digitalRead(BUTTON_IN);
  currentMillis = millis();
  // Leo si tengo alguna tarjeta para leer
  //Serial.println("Preparo para la lectura");
  String lectura = leerRFID(mfrc522);
  if(lectura.length() > 1){
    
   // Serial.print("Recibi desde el main:\r\n");
    if(consultarRFID(lectura)){
     // Serial.print("Debo abrir la puerta\r\n");      
      //Serial.println(lectura);
      desbloquearPuerta();
      
    }else{
      //Serial.print("NO debo abrir la puerta\r\n");
    }
  }
delay(200);
  if(lectBotonPuerta){
    desbloquearPuerta();
  }

  if(pasoTiempoRecibirAcciones()){ //paso el tiempo para revisar las acciones
    actionReciveMillis = currentMillis;
    if(consultarAccionesRemotas(accionesExternas)){
      if(accionesExternas[0] == 1 && !puertaDesbloqueada){
        desbloquearPuerta(); 
      }
      if(accionesExternas[1]> -1 ){
        luzPorciento = (byte)accionesExternas[1];
        manejarLuz(luzPorciento);
      }
    }
    leerSensores();
  }
/*
  if(pasoTiempoEnviarSensores()){ //paso el tiempo para enviar informacion de sensores
    sensorSendMillis = currentMillis;
//    leerSensores();
  }
*/


  if(puertaDesbloqueada){
    
    if(!puertaAbierta){
      if(lectPuerta){ // si justo se abrio la puerta cuando se penso que estaba cerrada
        puertaAbierta = true;
        bloquearPuerta();
        
      }
    } 
    if(pasoTiempoDoorUnlock()){ // si despues de un tiempo de estar desbloqueada la puerta la bloqueamos
        bloquearPuerta();      
    }
    
  }
  if(lectPuerta){ // si la puerta esta actualmente abierta
  
  if(!sirenaSonando && pasoTiempoDoorUnlock()){ //y despues de un tiempo de estar la puerta desbloqueada y la alarma no esta sonando hacemos sonar la alarma
    //suena alarma
    digitalWrite(ALARM_OUT, HIGH);
    sirenaSonando = true;
  }

  } else{
    if(sirenaSonando){ //y la sirena suena apagamos la puerta
      digitalWrite(ALARM_OUT, LOW);
      sirenaSonando = false;
      puertaAbierta = false;
    }else{
      puertaAbierta = false;
    }
  }

  

  
  
}
