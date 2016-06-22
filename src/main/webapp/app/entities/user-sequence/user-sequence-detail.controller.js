(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('UserSequenceDetailController', UserSequenceDetailController);

    UserSequenceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'UserSequence'];

    function UserSequenceDetailController($scope, $rootScope, $stateParams, entity, UserSequence) {
        var vm = this;

        vm.userSequence = entity;

        var unsubscribe = $rootScope.$on('rbaccountsApp:userSequenceUpdate', function(event, result) {
            vm.userSequence = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
