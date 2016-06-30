(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('product-transaction', {
            parent: 'entity',
            url: '/product-transaction',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProductTransactions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-transaction/product-transactions.html',
                    controller: 'ProductTransactionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('product-transaction-detail', {
            parent: 'entity',
            url: '/product-transaction/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProductTransaction'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-transaction/product-transaction-detail.html',
                    controller: 'ProductTransactionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ProductTransaction', function($stateParams, ProductTransaction) {
                    return ProductTransaction.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('product-transaction.new', {
            parent: 'product-transaction',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-transaction/product-transaction-dialog.html',
                    controller: 'ProductTransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                creationTime: null,
                                modificationTime: null,
                                quantity: null,
                                newStock: null,
                                returnStock: null,
                                invoicedStock: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('product-transaction', null, { reload: true });
                }, function() {
                    $state.go('product-transaction');
                });
            }]
        })
        .state('product-transaction.edit', {
            parent: 'product-transaction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-transaction/product-transaction-dialog.html',
                    controller: 'ProductTransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProductTransaction', function(ProductTransaction) {
                            return ProductTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-transaction', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('product-transaction.delete', {
            parent: 'product-transaction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-transaction/product-transaction-delete-dialog.html',
                    controller: 'ProductTransactionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProductTransaction', function(ProductTransaction) {
                            return ProductTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-transaction', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
