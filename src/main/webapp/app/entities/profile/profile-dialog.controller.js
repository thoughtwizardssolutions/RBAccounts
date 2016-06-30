(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('ProfileDialogController', ProfileDialogController);

    ProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Profile', 'Address'];

    function ProfileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Profile, Address) {
        var vm = this;

        vm.profile = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.addresss = Address.query({filter: 'profile-is-null'});
        $q.all([vm.profile.$promise, vm.addresss.$promise]).then(function() {
            if (!vm.profile.address || !vm.profile.address.id) {
                return $q.reject();
            }
            return Address.get({id : vm.profile.address.id}).$promise;
        }).then(function(address) {
            vm.addresses.push(address);
        });

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.profile.id !== null) {
                Profile.update(vm.profile, onSaveSuccess, onSaveError);
            } else {
                Profile.save(vm.profile, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rbaccountsApp:profileUpdate', result);
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
