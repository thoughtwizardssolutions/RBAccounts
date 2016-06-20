(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('DealerDialogController', DealerDialogController);

    DealerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Dealer', 'Address'];

    function DealerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Dealer, Address) {
        var vm = this;

        vm.dealer = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dealer.id !== null) {
                Dealer.update(vm.dealer, onSaveSuccess, onSaveError);
            } else {
                Dealer.save(vm.dealer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rbaccountsApp:dealerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationTime = false;
        vm.datePickerOpenStatus.modificationTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
