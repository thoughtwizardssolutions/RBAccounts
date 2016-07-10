(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('DealerManagementDeleteController', DealerManagementDeleteController);

    DealerManagementDeleteController.$inject = ['$uibModalInstance', 'entity', 'User'];

    function DealerManagementDeleteController ($uibModalInstance, entity, User) {
        var vm = this;

        vm.user = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (login) {
            User.delete({login: login},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
