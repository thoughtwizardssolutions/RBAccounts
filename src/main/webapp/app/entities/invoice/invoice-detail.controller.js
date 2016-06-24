(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('InvoiceDetailController', InvoiceDetailController);

    InvoiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Invoice', 'InvoiceItem', 'Dealer'];

    function InvoiceDetailController($scope, $rootScope, $stateParams, entity, Invoice, InvoiceItem, Dealer) {
        var vm = this;

        vm.invoice = entity;

        var unsubscribe = $rootScope.$on('rbaccountsApp:invoiceUpdate', function(event, result) {
            vm.invoice = result;
        });
        $scope.$on('$destroy', unsubscribe);
        Dealer.get({id : vm.invoice.dealerId}, onSuccess, onError);
		function onSuccess(data, headers) {
			vm.selectedContact = data;
		}
        
		function onError(error) {
			AlertService.error(error.data.message);
		}
		
    }
})();
