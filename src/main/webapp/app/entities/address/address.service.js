(function() {
    'use strict';
    angular
        .module('rbaccountsApp')
        .factory('Address', Address);

    Address.$inject = ['$resource'];

    function Address ($resource) {
        var resourceUrl =  'api/addresses/:id';

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
