(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('profile', {
            parent: 'entity',
            url: '/profile',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Profiles'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/profile/profiles.html',
                    controller: 'ProfileController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('profile-detail', {
            parent: 'entity',
            url: '/profile/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Profile'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/profile/profile-detail.html',
                    controller: 'ProfileDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Profile', function($stateParams, Profile) {
                    return Profile.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('profile.new', {
            parent: 'profile',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profile/profile-dialog.html',
                    controller: 'ProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                creationTime: null,
                                modificationTime: null,
                                firmName: null,
                                ownerName: null,
                                tin: null,
                                user: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('profile', null, { reload: true });
                }, function() {
                    $state.go('profile');
                });
            }]
        })
        .state('profile.edit', {
            parent: 'profile',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profile/profile-dialog.html',
                    controller: 'ProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Profile', function(Profile) {
                            return Profile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('profile', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('profile.delete', {
            parent: 'profile',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profile/profile-delete-dialog.html',
                    controller: 'ProfileDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Profile', function(Profile) {
                            return Profile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('profile', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
