(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('InvoiceItemDetailController', InvoiceItemDetailController);

    InvoiceItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InvoiceItem', 'Imei', 'Invoice'];

    function InvoiceItemDetailController($scope, $rootScope, $stateParams, entity, InvoiceItem, Imei, Invoice) {
        var vm = this;

        vm.invoiceItem = entity;

        var unsubscribe = $rootScope.$on('rbaccountsApp:invoiceItemUpdate', function(event, result) {
            vm.invoiceItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
