'use strict';
const express = require('express');
const router = express.Router();
const AccessModel = require('../models').Access;
const LogModel = require('../models').Log;

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
        res.status(200).send(true);
    })
    .catch((err) => {
        if (err) {
            console.error(err);            
            res.status(401).send(false);
        }
    });
});

module.exports = router;
