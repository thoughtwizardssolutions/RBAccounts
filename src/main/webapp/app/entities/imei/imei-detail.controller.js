(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('ImeiDetailController', ImeiDetailController);

    ImeiDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Imei', 'InvoiceItem'];

    function ImeiDetailController($scope, $rootScope, $stateParams, entity, Imei, InvoiceItem) {
        var vm = this;

        vm.imei = entity;

        var unsubscribe = $rootScope.$on('rbaccountsApp:imeiUpdate', function(event, result) {
            vm.imei = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
