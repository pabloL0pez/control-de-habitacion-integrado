#include "src/MFRC522/MFRC522.h"


String leerRFID(MFRC522 mfrc522);

bool consultarRFID(String idTarjeta);

void desbloquearPuerta();

void bloquearPuerta();

void leerSensores();

void enviarSensores(byte movimiento, byte lux, byte luz);

bool consultarAccionesRemotas();

bool manejarLuz(byte porcentaje);


