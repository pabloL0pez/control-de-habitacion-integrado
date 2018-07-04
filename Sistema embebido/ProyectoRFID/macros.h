// definicion de los pines del arduino
#define DOOR_OUT 4
#define ALARM_OUT 5
#define LED_LIGHT_OUT 6
#define LED_GREEN_OUT 7
#define LED_RED_OUT 8
#define LED_PIR_OUT A4

#define RST_PIN  9    //Pin 9 para el reset del RC522
#define SS_PIN  10   //Pin 10 para el SS (SDA) del RC522

#define PIR_IN A1
#define LDR_IN A0
#define BUTTON_IN A2
#define DOOR_IN A3

// definiciones para el los intervalos de tiempo
#define TIME_INTERVAL_INFO 6000      //Tiempo de envio de informacion de sensores
#define TIME_INTERVAL_ACTION 6000    //Tiempo de consulta de acciones
#define TIME_INTERVAL_UNLOCK 20000   //Tiempo de espera para volver a bloquear puerta
#define TIME_INTERVAL_ALARM 30    //Tiempo de espera para sonar alarma
