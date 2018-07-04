'use strict';
const mongoose = require('mongoose');
const ConfigSchema = require('./schemas/config-schema');

module.exports = mongoose.model('Config', ConfigSchema);
