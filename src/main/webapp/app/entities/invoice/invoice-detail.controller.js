(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('InvoiceDetailController', InvoiceDetailController);

    InvoiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Invoice', 'InvoiceItem'];

    function InvoiceDetailController($scope, $rootScope, $stateParams, entity, Invoice, InvoiceItem) {
        var vm = this;

        vm.invoice = entity;

        var unsubscribe = $rootScope.$on('rbaccountsApp:invoiceUpdate', function(event, result) {
            vm.invoice = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
