/*
  This program blinks pin 13 of the Arduino (the
  built-in LED)
*/
int codigoRFID;
bool respuestaServidor;
bool bloquearPuerta=true;
bool abrirPuerta=false;

void setup()
{
	pinMode(13, INPUT);
}

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
  
  
}