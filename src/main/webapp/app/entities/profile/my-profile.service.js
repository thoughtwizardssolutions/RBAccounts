(function() {
    'use strict';
    angular
        .module('rbaccountsApp')
        .factory('MyProfile', MyProfile);

    MyProfile.$inject = ['$resource', 'DateUtils'];

    function MyProfile ($resource, DateUtils) {
        var resourceUrl =  'api/profiles/:user';

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
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.creationDate = DateUtils.convertLocalDateToServer(data.creationDate);
                    data.modificationDate = DateUtils.convertLocalDateToServer(data.modificationDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.creationDate = DateUtils.convertLocalDateToServer(data.creationDate);
                    data.modificationDate = DateUtils.convertLocalDateToServer(data.modificationDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
