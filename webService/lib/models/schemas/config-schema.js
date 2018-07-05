'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var configSchema = new Schema({
    label: {
        type: String,
        enum: ['luminosidad', 'abrirpuerta'],
        unique: true
    },
    value: String
}, {
    timestamps: true
});

module.exports = configSchema;
