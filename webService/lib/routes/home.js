'use strict';
const express = require('express');
const router = express.Router();
const tarjetas = [{
    id: '20d23ca8'
}, {
    id: '2eadba79'
}]


router.get('/access/:id', function(req, res) {
    let tarjetasHabilitadas = tarjetas.filter((tarjetaElem) => {
        return tarjetaElem.id === req.params.id;
    });
    
    if(tarjetasHabilitadas.length) {
        res.status(200).send(true);
    } else {
        res.status(401).send(false);
    }
});

module.exports = router;
