(function() {
    'use strict';
    angular
        .module('rbaccountsApp')
        .factory('Tax', Tax);

    Tax.$inject = ['$resource'];

    function Tax ($resource) {
        var resourceUrl =  'api/taxes/:id';

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
