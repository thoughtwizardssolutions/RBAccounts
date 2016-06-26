(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('my-profile', {
            parent: 'home',
            url: 'my-profile',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ORG_ADMIN', 'ROLE_ADMIN'],
                pageTitle: 'My Profile'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/profile/my-profile.html',
                    controller: 'MyProfileController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('my-profile.edit', {
            parent: 'home',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ORG_ADMIN', 'ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profile/my-profile.html',
                    controller: 'MyProfileController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Profile', function(Profile) {
                            return Profile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('my-profile', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
