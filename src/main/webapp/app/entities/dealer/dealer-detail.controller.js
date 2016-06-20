(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('DealerDetailController', DealerDetailController);

    DealerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Dealer', 'Address'];

    function DealerDetailController($scope, $rootScope, $stateParams, entity, Dealer, Address) {
        var vm = this;

        vm.dealer = entity;

        var unsubscribe = $rootScope.$on('rbaccountsApp:dealerUpdate', function(event, result) {
            vm.dealer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
