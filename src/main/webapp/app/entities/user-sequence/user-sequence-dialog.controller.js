(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('UserSequenceDialogController', UserSequenceDialogController);

    UserSequenceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserSequence'];

    function UserSequenceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserSequence) {
        var vm = this;

        vm.userSequence = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userSequence.id !== null) {
                UserSequence.update(vm.userSequence, onSaveSuccess, onSaveError);
            } else {
                UserSequence.save(vm.userSequence, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rbaccountsApp:userSequenceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
