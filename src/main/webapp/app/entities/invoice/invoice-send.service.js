(function() {
    'use strict';
    angular
        .module('rbaccountsApp')
        .factory('InvoiceSend', InvoiceSend);

    InvoiceSend.$inject = ['$resource'];

    function InvoiceSend ($resource) {
        var resourceUrl =  'api/invoices/send';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET',
            }
        });
    }
})();