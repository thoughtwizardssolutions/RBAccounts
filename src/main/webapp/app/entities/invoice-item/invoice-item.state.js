(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('invoice-item', {
            parent: 'entity',
            url: '/invoice-item?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'InvoiceItems'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/invoice-item/invoice-items.html',
                    controller: 'InvoiceItemController',
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
        .state('invoice-item-detail', {
            parent: 'entity',
            url: '/invoice-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'InvoiceItem'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/invoice-item/invoice-item-detail.html',
                    controller: 'InvoiceItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'InvoiceItem', function($stateParams, InvoiceItem) {
                    return InvoiceItem.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('invoice-item.new', {
            parent: 'invoice-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invoice-item/invoice-item-dialog.html',
                    controller: 'InvoiceItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                mrp: null,
                                description: null,
                                amount: null,
                                taxType: null,
                                taxRate: null,
                                discount: null,
                                quantity: null,
                                creationTime: null,
                                modificationTime: null,
                                color: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('invoice-item', null, { reload: true });
                }, function() {
                    $state.go('invoice-item');
                });
            }]
        })
        .state('invoice-item.edit', {
            parent: 'invoice-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invoice-item/invoice-item-dialog.html',
                    controller: 'InvoiceItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InvoiceItem', function(InvoiceItem) {
                            return InvoiceItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('invoice-item', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('invoice-item.delete', {
            parent: 'invoice-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invoice-item/invoice-item-delete-dialog.html',
                    controller: 'InvoiceItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InvoiceItem', function(InvoiceItem) {
                            return InvoiceItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('invoice-item', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
