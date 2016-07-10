(function() {
    'use strict';
    angular
        .module('rbaccountsApp')
        .factory('User', User);

    User.$inject = ['$resource'];

    function User ($resource) {
        var resourceUrl =  'api/users/:login';

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
