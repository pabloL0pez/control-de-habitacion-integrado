#include <SPI.h>
#include "src/MFRC522/MFRC522.h"
#include "src/ESP8266/ESP8266.h"
#include "macros.h"
#include <SoftwareSerial.h>

extern unsigned long doorUnlockMillis;
//extern unsigned long sensorSendMillis;
extern unsigned long actionReciveMillis; 

extern unsigned long currentMillis;
extern bool puertaAbierta;
extern bool puertaDesbloqueada;
extern int accionesExternas[];
extern ESP8266 wifi;
extern byte luzPorciento;



String leerRFID(MFRC522 mfrc522);

bool consultarRFID(String idTarjeta);

void desbloquearPuerta();

void bloquearPuerta();

void leerSensores();

void enviarSensores(byte movimiento, byte lux, byte luz);

bool consultarAccionesRemotas(int accionesExternas[2]);

bool manejarLuz(byte porcentaje);

bool pasoTiempoDoorUnlock();

bool pasoTiempoEnviarSensores();

bool pasoTiempoRecibirAcciones();

int sendWiFiRequest(String request);


