'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var accessSchema = new Schema({
    card: {
        type: Schema.Types.ObjectId,
        required: true,
        ref: 'Access'
    },
    action: {
        type: String,
        enum: [
            'change-luminosity', 
            'access-room'
        ]
    },
    value: String
}, {
    timestamps: true
});


accessSchema.pre('save', function(next) {
    console.log('pre save');
    next();
});

module.exports = accessSchema;
