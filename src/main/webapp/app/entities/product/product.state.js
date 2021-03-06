(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('product', {
            parent: 'entity',
            url: '/product',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ORG_ADMIN'],
                pageTitle: 'Products'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product/products.html',
                    controller: 'ProductController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('product-detail', {
            parent: 'entity',
            url: '/product/{id}',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ORG_ADMIN'],
                pageTitle: 'Product'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product/product-detail.html',
                    controller: 'ProductDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Product', function($stateParams, Product) {
                    return Product.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('product.new', {
            parent: 'product',
            url: '/new',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ORG_ADMIN'],
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product/product-dialog.html',
                    controller: 'ProductDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                mrp: null,
                                color: null,
                                creationTime: null,
                                modificationTime: null,
                                createdBy: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('product', null, { reload: true });
                }, function() {
                    $state.go('product');
                });
            }]
        })
        .state('product.edit', {
            parent: 'product',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ORG_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product/product-dialog.html',
                    controller: 'ProductDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Product', function(Product) {
                            return Product.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('product', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('product.delete', {
            parent: 'product',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ORG_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product/product-delete-dialog.html',
                    controller: 'ProductDeleteController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Product', function(Product) {
                            return Product.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('product', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('add-stock', {
            parent: 'product',
            url: '/{id}/addStock',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ORG_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-transaction/product-add-transaction-dialog.html',
                    controller: 'ProductAddTransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Product', function(Product) {
                            return Product.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('product', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('product-upload', {
            parent: 'product',
            url: '/productUpload',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ORG_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product/product-upload-dialog.html',
                    controller: 'ProductUploadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                    }
                }).result.then(function() {
                    $state.go('product', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
