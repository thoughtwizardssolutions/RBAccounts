(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('imei', {
            parent: 'entity',
            url: '/imei?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Imeis'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/imei/imeis.html',
                    controller: 'ImeiController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('imei-detail', {
            parent: 'entity',
            url: '/imei/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Imei'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/imei/imei-detail.html',
                    controller: 'ImeiDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Imei', function($stateParams, Imei) {
                    return Imei.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('imei.new', {
            parent: 'imei',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/imei/imei-dialog.html',
                    controller: 'ImeiDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                imei1: null,
                                imei2: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('imei', null, { reload: true });
                }, function() {
                    $state.go('imei');
                });
            }]
        })
        .state('imei.edit', {
            parent: 'imei',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/imei/imei-dialog.html',
                    controller: 'ImeiDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Imei', function(Imei) {
                            return Imei.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('imei', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('imei.delete', {
            parent: 'imei',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/imei/imei-delete-dialog.html',
                    controller: 'ImeiDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Imei', function(Imei) {
                            return Imei.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('imei', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
