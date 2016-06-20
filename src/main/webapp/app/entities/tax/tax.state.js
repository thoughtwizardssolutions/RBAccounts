(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tax', {
            parent: 'entity',
            url: '/tax',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Taxes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tax/taxes.html',
                    controller: 'TaxController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('tax-detail', {
            parent: 'entity',
            url: '/tax/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tax'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tax/tax-detail.html',
                    controller: 'TaxDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Tax', function($stateParams, Tax) {
                    return Tax.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('tax.new', {
            parent: 'tax',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tax/tax-dialog.html',
                    controller: 'TaxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                rate: null,
                                createdBy: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tax', null, { reload: true });
                }, function() {
                    $state.go('tax');
                });
            }]
        })
        .state('tax.edit', {
            parent: 'tax',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tax/tax-dialog.html',
                    controller: 'TaxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tax', function(Tax) {
                            return Tax.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tax', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tax.delete', {
            parent: 'tax',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tax/tax-delete-dialog.html',
                    controller: 'TaxDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tax', function(Tax) {
                            return Tax.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tax', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
