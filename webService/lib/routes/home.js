'use strict';
const express = require('express');
const router = express.Router();
const AccessModel = require('../models').Access;
const LogModel = require('../models').Log;
const ConfigModel = require('../models').Config;

router.get('/access/:id', function(req, res) {
    AccessModel.findOne({
        card: req.params.id
    })
    .then((dataCard) => {
        if(dataCard) {
            let newLog = new LogModel({
                card: dataCard._id
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

router.get('/luminosidad', function(req, res) {
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

module.exports = router;
