'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var accessSchema = new Schema({
    card: {
        type: Schema.Types.ObjectId,
        required: true,
        ref: 'Access'        
    }
}, {
    timestamps: true
});


accessSchema.pre('save', function(next) {
    console.log('pre save');
    next();    
});

module.exports = accessSchema;
