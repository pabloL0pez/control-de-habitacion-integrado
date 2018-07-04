'use strict';
const mongoose = require('mongoose');
const LogSchema = require('./schemas/log-schema');

module.exports = mongoose.model('Log', LogSchema);
