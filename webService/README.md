# WebService

### Configuracion

* Instalar nodejs: https://nodejs.org/es/download/package-manager/

### Ejecucion
Ejecutar los siguientes comandos en la terminal para poner en marcha el webservice

* npm install (solo la primera vez)
* npm start (para dar de alta el servidor)

### Endpoints

* GET /api/access :Obtiene los ids de las tarjetas que pueden acceder
* GET /access/[cardId] :Dice si se puede acceder o no
* POST /api/access :Utilizado para la creacion de la tarjeta, enviar un json con el siguiente formato
```json
{
    "card": "[id de la card]"
}
```
* GET /api/logs :Se puede ver el log de acceso, se ve la referencia a la card y en que horario accedio
* POST /api/configs :Para crear el endpoint de luminosidad enviar el siguiente json
```json
{
    "label": "luminosidad",
    "value": "-1"
}
```
* GET /api/configs?conditions={"value": "luminosidad"} :Para obtener el valor de luminosidad (pensado para telefono)
* PUT /api/configs/[_id_de_label_luminosidad] : Enviando el siguiente json se puede modificar el valor de la luminosidad
```json
{
    "value": "[nivel de luminosidad]"
}
```


