'use strict';
const express = require('express');
const router = express.Router();
const AccessModel = require('../models').Access;
const LogModel = require('../models').Log;
const ConfigModel = require('../models').Config;
var lightConsulted = false;
router.get('/arduino/luminosidad', function(req, res) {
    ConfigModel.findOne({
        label: 'luminosidad'
    })
    .then((dataLuminosidad) => {
        if(dataLuminosidad) {
            lightConsulted = true;
            res.status(200).send('|' + dataLuminosidad.value + '|');
        }
    })
    .catch((err) => {
        if (err) {
            console.error(err);
            res.status(200).send('|-1|');
        }
    });
});

router.get('/arduino/abrirpuerta', function(req, res) {
    ConfigModel.findOne({
        label: 'abrirpuerta'
    })
    .then((dataAbrirPuerta) => {
        res.status(200).send('|' + dataAbrirPuerta.value + '|');
        dataAbrirPuerta.value = -1;
        dataAbrirPuerta.save();
    });
});

router.put('/toggleLuz' , function(req, res) {
    ConfigModel.findOne({
        label: 'luminosidad'
    })
    .then((dataLuz) => {

        if(lightConsulted) {
            if(dataLuz.value >= 1) {
                dataLuz.value = 0;
            } else {
                dataLuz.value = 100;
            }
            lightConsulted = false;
        }
        console.log('dataLuz', dataLuz)
        dataLuz.save()
        .then((dataSaved) => {
            console.log('dataSaved',dataSaved)
        })
        .catch((err) => {
            console.log('err', err);
        });
        res.status(200).send('|' + dataLuz.value + '|');
    })
    .catch((err) => {
        console.log('err', err);
    });
});

router.get('/arduino/:id', function(req, res) {
    AccessModel.findOne({
        card: req.params.id
    })
    .then((dataCard) => {
        if(dataCard) {
            let newLog = new LogModel({
                card: dataCard._id,
                action: 'access-room',
                value: 'valid'
            });

            return newLog.save();
        }
        throw new Error('unauthorized');
    })
    .then((logSaved) => {
        console.log('New Access, card Nº: ' + logSaved.card + ' At: ' + logSaved.createdAt);
        res.status(200).send('|1|');
    })
    .catch((err) => {
        if (err) {
            console.error(err);
            res.status(401).send('|0|');
        }
    });
});
var hayMovimiento = 0;
var valorLdr = 0;
// sensores/ldr/movimiento(1 o 0)/porcentaje encendido de luz
router.get('/sensores/:ldr/:movimiento/:encendidoLuz', function(req, res) {
    hayMovimiento = req.params.movimiento;
    valorLdr = req.params.ldr;    
    console.log(req.params.ldr, req.params.movimiento, req.params.encendidoLuz);
    res.status(200).send('ok');
});

router.get('/datosGenerales', function(req, res) {
    ConfigModel.findOne({
        label: 'luminosidad'
    })
    .then((dataLuz) => {
        res.status(200).json({
            hayMovimiento,
            valorLdr,
            luz: dataLuz.value
        });
    })
});

module.exports = router;
