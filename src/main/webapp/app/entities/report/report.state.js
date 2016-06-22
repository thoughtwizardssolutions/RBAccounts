(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('report', {
            parent: 'entity',
            url: '/report',
            data: {
                authorities: ['ROLE_USER','ROLE_ORG_ADMIN','ROLE_ADMIN'],
                pageTitle: 'Reports'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/report/reports.html',
                    controller: 'ReportController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        });
    }

})();
