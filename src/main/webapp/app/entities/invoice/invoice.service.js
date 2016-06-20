(function() {
    'use strict';
    angular
        .module('rbaccountsApp')
        .factory('Invoice', Invoice);

    Invoice.$inject = ['$resource', 'DateUtils'];

    function Invoice ($resource, DateUtils) {
        var resourceUrl =  'api/invoices/:id';

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
