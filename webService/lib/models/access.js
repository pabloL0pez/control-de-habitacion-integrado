'use strict';
const mongoose = require('mongoose');
const AccessSchema = require('./schemas/access-schema');

module.exports = mongoose.model('Access', AccessSchema);
