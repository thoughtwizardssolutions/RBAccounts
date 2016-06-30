'use strict';

describe('Controller Tests', function() {

    describe('ProductTransactions Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProductTransactions, MockProduct;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProductTransactions = jasmine.createSpy('MockProductTransactions');
            MockProduct = jasmine.createSpy('MockProduct');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ProductTransactions': MockProductTransactions,
                'Product': MockProduct
            };
            createController = function() {
                $injector.get('$controller')("ProductTransactionsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rbaccountsApp:productTransactionsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
