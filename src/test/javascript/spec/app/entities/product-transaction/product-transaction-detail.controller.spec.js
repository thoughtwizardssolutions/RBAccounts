'use strict';

describe('Controller Tests', function() {

    describe('ProductTransaction Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProductTransaction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProductTransaction = jasmine.createSpy('MockProductTransaction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ProductTransaction': MockProductTransaction
            };
            createController = function() {
                $injector.get('$controller')("ProductTransactionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rbaccountsApp:productTransactionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
