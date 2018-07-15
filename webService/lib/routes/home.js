'use strict';
const express = require('express');
const router = express.Router();
const AccessModel = require('../models').Access;
const LogModel = require('../models').Log;
const ConfigModel = require('../models').Config;
router.get('/arduino/luminosidad', function(req, res) {
    ConfigModel.findOne({
        label: 'luminosidad'
    })
    .then((dataLuminosidad) => {
        if(dataLuminosidad) {
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
    });
});

router.post('/toggleLuz' , function(req, res) {
    console.log('aca');
    ConfigModel.findOne({
        label: 'luminosidad'
    })
    .then((dataAbrirPuerta) => {
        if(dataAbrirPuerta.value >= 1) {
            dataAbrirPuerta.value = 0;
        }
        dataAbrirPuerta.save();
        res.status(200).send('|' + dataAbrirPuerta.value + '|');
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
                action: 'access_room',
                value: 'valid'
            });

            return newLog.save();
        } 

        let newLog = new LogModel({
            card: dataCard._id,
            action: 'access_room',
            value: 'invalid'
        });

        newLog.save();
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

// sensores/ldr/movimiento(1 o 0)/porcentaje encendido de luz
router.get('/sensores/:ldr/:movimiento/:encendidoLuz', function(req, res) {
    console.log(req.params.ldr, req.params.movimiento, req.params.encendidoLuz);
    res.status(200).send('ok');
});

module.exports = router;
