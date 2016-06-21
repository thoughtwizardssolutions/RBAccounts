(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('MyProfileController', MyProfileController);

    MyProfileController.$inject = ['$timeout', '$scope', '$stateParams', '$q', 'MyProfile'];

    function MyProfileController ($timeout, $scope, $stateParams, $q, MyProfile) {
        var vm = this;

        vm.profile = {};
        vm.profile.id = null;
        vm.clear = clear;
        vm.save = save;
        
        loadProfile();
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
        	// state go
            // $uibModalInstance.dismiss('cancel');
        }

        function loadProfile() {
        	MyProfile.query(function(result) {
                vm.profile = result[0];
            });
        	
        }
        function save () {
        	
            vm.isSaving = true;
            if (vm.profile.id !== null) {
            	MyProfile.update(vm.profile, onSaveSuccess, onSaveError);
            	console.log('saving profile.');
            } else {
            	MyProfile.save(vm.profile, onSaveSuccess, onSaveError);
            	console.log('saving profile changes.');
            }
        }

        function onSaveSuccess (result) {
            // $scope.$emit('rbaccountsApp:myProfileUpdate', result);
            // $uibModalInstance.close(result);
            vm.isSaving = false;
            vm.success = true;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
