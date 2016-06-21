(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('ProfileController', ProfileController);

    ProfileController.$inject = ['$scope', '$state', 'Profile'];

    function ProfileController ($scope, $state, Profile) {
        var vm = this;
        
        vm.profiles = [];

        loadAll();

        function loadAll() {
            Profile.query(function(result) {
                vm.profiles = result;
            });
        }
    }
})();
