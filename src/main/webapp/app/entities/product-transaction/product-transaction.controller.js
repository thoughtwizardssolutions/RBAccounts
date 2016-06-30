(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('ProductTransactionController', ProductTransactionController);

    ProductTransactionController.$inject = ['$scope', '$state', 'ProductTransaction'];

    function ProductTransactionController ($scope, $state, ProductTransaction) {
        var vm = this;
        
        vm.productTransactions = [];

        loadAll();

        function loadAll() {
            ProductTransaction.query(function(result) {
                vm.productTransactions = result;
            });
        }
    }
})();
