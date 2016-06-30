(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('ProductTransactionDetailController', ProductTransactionDetailController);

    ProductTransactionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ProductTransaction'];

    function ProductTransactionDetailController($scope, $rootScope, $stateParams, entity, ProductTransaction) {
        var vm = this;

        vm.productTransaction = entity;

        var unsubscribe = $rootScope.$on('rbaccountsApp:productTransactionUpdate', function(event, result) {
            vm.productTransaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
