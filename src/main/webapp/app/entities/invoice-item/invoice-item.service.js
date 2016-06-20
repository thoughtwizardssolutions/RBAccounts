(function() {
    'use strict';
    angular
        .module('rbaccountsApp')
        .factory('InvoiceItem', InvoiceItem);

    InvoiceItem.$inject = ['$resource', 'DateUtils'];

    function InvoiceItem ($resource, DateUtils) {
        var resourceUrl =  'api/invoice-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationTime = DateUtils.convertDateTimeFromServer(data.creationTime);
                        data.modificationTime = DateUtils.convertDateTimeFromServer(data.modificationTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
