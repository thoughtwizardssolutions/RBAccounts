(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dealer-management', {
            parent: 'home',
            url: 'dealer-management',
            data: {
                authorities: ['ROLE_ADMIN', 'ROLE_ORG_ADMIN'],
                pageTitle: 'rbaccounts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dealer-management/dealer-management.html',
                    controller: 'DealerManagementController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('dealer-management-detail', {
            parent: 'dealer-management',
            url: '/user/:login',
            data: {
                authorities: ['ROLE_ADMIN', 'ROLE_ORG_ADMIN'],
                pageTitle: 'rbaccounts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dealer-management/dealer-management-detail.html',
                    controller: 'DealerManagementDetailController',
                    controllerAs: 'vm'
                },                   
            resolve: {
                    entity: ['User', function(User) {
                        return User.get({login : $stateParams.login});
                    }]
                }
            }
        })
        .state('dealer-management.new', {
            parent: 'dealer-management',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN', 'ROLE_ORG_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dealer-management/dealer-management-dialog.html',
                    controller: 'DealerManagementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null, login: null, firstName: null, lastName: null, email: null,
                                activated: true, langKey: null, createdBy: null, createdDate: null,
                                lastModifiedBy: null, lastModifiedDate: null, resetDate: null,
                                resetKey: null, authorities: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dealer-management', null, { reload: true });
                }, function() {
                    $state.go('dealer-management');
                });
            }]
        })
        .state('dealer-management.edit', {
            parent: 'dealer-management',
            url: '/{login}/edit',
            data: {
                authorities: ['ROLE_ADMIN', 'ROLE_ORG_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dealer-management/dealer-management-dialog.html',
                    controller: 'DealerManagementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['User', function(User) {
                            return User.get({login : $stateParams.login});
                        }]
                    }
                }).result.then(function() {
                    $state.go('dealer-management', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dealer-management.delete', {
            parent: 'dealer-management',
            url: '/{login}/delete',
            data: {
                authorities: ['ROLE_ADMIN', 'ROLE_ORG_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dealer-management/dealer-management-delete-dialog.html',
                    controller: 'DealerManagementDeleteController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['User', function(User) {
                            return User.get({login : $stateParams.login});
                        }]
                    }
                }).result.then(function() {
                    $state.go('dealer-management', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
