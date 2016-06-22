(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('UserSequenceDeleteController',UserSequenceDeleteController);

    UserSequenceDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserSequence'];

    function UserSequenceDeleteController($uibModalInstance, entity, UserSequence) {
        var vm = this;

        vm.userSequence = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserSequence.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
