(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('UserDetailController', UserDetailController);

    UserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'User'];

    function UserDetailController($scope, $rootScope, $stateParams, entity, User) {
        var vm = this;

        vm.user = entity;

        var unsubscribe = $rootScope.$on('rbaccountsApp:userUpdate', function(event, result) {
            vm.user = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
