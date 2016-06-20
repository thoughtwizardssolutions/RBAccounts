(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('UserController', UserController);

    UserController.$inject = ['$scope', '$state', 'User'];

    function UserController ($scope, $state, User) {
        var vm = this;
        
        vm.users = [];

        loadAll();

        function loadAll() {
            User.query(function(result) {
                vm.users = result;
            });
        }
    }
})();
