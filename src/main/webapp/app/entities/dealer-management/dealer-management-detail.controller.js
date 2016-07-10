(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('DealerManagementDetailController', DealerManagementDetailController);

    DealerManagementDetailController.$inject = ['$stateParams', 'User'];

    function DealerManagementDetailController ($stateParams, User) {
        var vm = this;

        vm.load = load;
        vm.user = {};

        vm.load($stateParams.login);

        function load (login) {
            User.get({login: login}, function(result) {
                vm.user = result;
            });
        }
    }
})();
