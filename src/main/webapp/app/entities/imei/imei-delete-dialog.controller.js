(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('ImeiDeleteController',ImeiDeleteController);

    ImeiDeleteController.$inject = ['$uibModalInstance', 'entity', 'Imei'];

    function ImeiDeleteController($uibModalInstance, entity, Imei) {
        var vm = this;

        vm.imei = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Imei.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
