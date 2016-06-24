(function() {
    'use strict';
    angular
        .module('rbaccountsApp')
        .factory('UserName', UserName);

    UserName.$inject = ['$resource'];

    function UserName ($resource) {
        var resourceUrl =  'api/usernames';

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
