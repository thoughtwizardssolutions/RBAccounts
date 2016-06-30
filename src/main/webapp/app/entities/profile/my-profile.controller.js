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
            angular.element('.form-group:eq(0)>input').focus();
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
            if (vm.profile.id) {
            	MyProfile.update(vm.profile, onSaveSuccess, onSaveError);
            	console.log('updating profile.');
            } else {
            	MyProfile.save(vm.profile, onSaveSuccess, onSaveError);
            	console.log('saving profile.');
            }
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            vm.success = true;
            $state.go('my-profile', null, { reload: true });
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
