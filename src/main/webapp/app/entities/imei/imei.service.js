(function() {
    'use strict';
    angular
        .module('rbaccountsApp')
        .factory('Imei', Imei);

    Imei.$inject = ['$resource'];

    function Imei ($resource) {
        var resourceUrl =  'api/imeis/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
