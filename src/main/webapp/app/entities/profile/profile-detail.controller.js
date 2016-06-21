(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('ProfileDetailController', ProfileDetailController);

    ProfileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Profile', 'Address'];

    function ProfileDetailController($scope, $rootScope, $stateParams, entity, Profile, Address) {
        var vm = this;

        vm.profile = entity;

        var unsubscribe = $rootScope.$on('rbaccountsApp:profileUpdate', function(event, result) {
            vm.profile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
