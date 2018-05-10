'use strict';
const express = require('express');
const router = express.Router();

router.get('/access', function(req, res) {
    let random = Math.random();
    if(random > 0.5) {
        res.status(200).send(true);
    } else {
        res.status(401).send(false);
    }
});

module.exports = router;
