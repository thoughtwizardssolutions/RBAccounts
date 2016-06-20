(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('ProductController', ProductController);

    ProductController.$inject = ['$scope', '$state', 'Product'];

    function ProductController ($scope, $state, Product) {
        var vm = this;
        
        vm.products = [];

        loadAll();

        function loadAll() {
            Product.query(function(result) {
                vm.products = result;
            });
        }
    }
})();
