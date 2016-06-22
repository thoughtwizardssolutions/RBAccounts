(function() {
    'use strict';
    angular
        .module('rbaccountsApp')
        .factory('UserSequence', UserSequence);

    UserSequence.$inject = ['$resource'];

    function UserSequence ($resource) {
        var resourceUrl =  'api/:user/user-sequences/';

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
