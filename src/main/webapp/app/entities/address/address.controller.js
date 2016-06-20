(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('AddressController', AddressController);

    AddressController.$inject = ['$scope', '$state', 'Address'];

    function AddressController ($scope, $state, Address) {
        var vm = this;
        
        vm.addresses = [];

        loadAll();

        function loadAll() {
            Address.query(function(result) {
                vm.addresses = result;
            });
        }
    }
})();
