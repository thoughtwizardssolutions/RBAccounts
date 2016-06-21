(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user', {
            parent: 'entity',
            url: '/user',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Users'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user/users.html',
                    controller: 'UserController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('user-detail', {
            parent: 'entity',
            url: '/user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'User'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user/user-detail.html',
                    controller: 'UserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'User', function($stateParams, User) {
                    return User.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('user.new', {
            parent: 'user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user/user-dialog.html',
                    controller: 'UserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user', null, { reload: true });
                }, function() {
                    $state.go('user');
                });
            }]
        })
        .state('user.edit', {
            parent: 'user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user/user-dialog.html',
                    controller: 'UserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['User', function(User) {
                            return User.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user.delete', {
            parent: 'user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user/user-delete-dialog.html',
                    controller: 'UserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['User', function(User) {
                            return User.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
