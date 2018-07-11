'use strict';
function configDecorator(controller) {
    controller.request('put', function(req, res, next) {
        console.log(req.body);        
        next();
    });
}
module.exports = configDecorator;
