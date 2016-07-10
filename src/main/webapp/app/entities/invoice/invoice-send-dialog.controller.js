(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('InvoiceSendDialogController',InvoiceSendDialogController);

    InvoiceSendDialogController.$inject = ['$uibModalInstance', 'entity' , 'InvoiceSend'];

    function InvoiceSendDialogController($uibModalInstance ,entity , InvoiceSend) {
        var vm = this;

        vm.clear = clear;
        vm.send = send;
        vm.email = null;
        vm.invoice = entity;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function send () {
        	InvoiceSend.get({
        		id: vm.invoice.id,
        		email : vm.email
        	},function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
