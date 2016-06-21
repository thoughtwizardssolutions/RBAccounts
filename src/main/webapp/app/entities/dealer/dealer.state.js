(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dealer', {
            parent: 'entity',
            url: '/dealer?page&sort&search',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ORG_ADMIN'],
                pageTitle: 'My Contacts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dealer/dealers.html',
                    controller: 'DealerController',
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
        .state('dealer-detail', {
            parent: 'entity',
            url: '/dealer/{id}',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ORG_ADMIN'],
                pageTitle: 'Contact details'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dealer/dealer-detail.html',
                    controller: 'DealerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Dealer', function($stateParams, Dealer) {
                    return Dealer.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('dealer.new', {
            parent: 'dealer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ORG_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dealer/dealer-dialog.html',
                    controller: 'DealerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                creationDate: null,
                                modificationDate: null,
                                firmName: null,
                                ownerName: null,
                                tin: null,
                                termsAndConditions: null,
                                openingBalance: null,
                                currentBalance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dealer', null, { reload: true });
                }, function() {
                    $state.go('dealer');
                });
            }]
        })
        .state('dealer.edit', {
            parent: 'dealer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ORG_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dealer/dealer-dialog.html',
                    controller: 'DealerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dealer', function(Dealer) {
                            return Dealer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dealer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dealer.delete', {
            parent: 'dealer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ORG_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dealer/dealer-delete-dialog.html',
                    controller: 'DealerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Dealer', function(Dealer) {
                            return Dealer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dealer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
