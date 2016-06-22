(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('UserSequenceController', UserSequenceController);

    UserSequenceController.$inject = ['$scope', '$state', 'UserSequence'];

    function UserSequenceController ($scope, $state, UserSequence) {
        var vm = this;
        
        vm.userSequences = [];

        loadAll();

        function loadAll() {
            UserSequence.query(function(result) {
                vm.userSequences = result;
            });
        }
    }
})();
