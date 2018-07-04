'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var accessSchema = new Schema({
    card: {
        type: String,
        required: true,
        unique: true
    }
}, {
    timestamps: true
});


accessSchema.pre('save', function(next) {
    console.log('pre save');
    next();    
});

module.exports = accessSchema;
