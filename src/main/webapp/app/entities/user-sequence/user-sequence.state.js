(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-sequence', {
            parent: 'entity',
            url: '/user-sequence',
            data: {
                authorities: ['ROLE_USER','ROLE_ORG_ADMIN','ROLE_ADMIN'],
                pageTitle: 'UserSequences'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-sequence/user-sequences.html',
                    controller: 'UserSequenceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('user-sequence-detail', {
            parent: 'entity',
            url: '/user-sequence/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UserSequence'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-sequence/user-sequence-detail.html',
                    controller: 'UserSequenceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'UserSequence', function($stateParams, UserSequence) {
                    return UserSequence.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('user-sequence.new', {
            parent: 'user-sequence',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-sequence/user-sequence-dialog.html',
                    controller: 'UserSequenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createdBy: null,
                                prefix: null,
                                currentSequence: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-sequence', null, { reload: true });
                }, function() {
                    $state.go('user-sequence');
                });
            }]
        })
        .state('user-sequence.edit', {
            parent: 'user-sequence',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-sequence/user-sequence-dialog.html',
                    controller: 'UserSequenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserSequence', function(UserSequence) {
                            return UserSequence.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-sequence', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-sequence.delete', {
            parent: 'user-sequence',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-sequence/user-sequence-delete-dialog.html',
                    controller: 'UserSequenceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserSequence', function(UserSequence) {
                            return UserSequence.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-sequence', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
