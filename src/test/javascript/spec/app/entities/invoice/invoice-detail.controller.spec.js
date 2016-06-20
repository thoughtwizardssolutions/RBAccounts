'use strict';

describe('Controller Tests', function() {

    describe('Invoice Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInvoice, MockInvoiceItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInvoice = jasmine.createSpy('MockInvoice');
            MockInvoiceItem = jasmine.createSpy('MockInvoiceItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Invoice': MockInvoice,
                'InvoiceItem': MockInvoiceItem
            };
            createController = function() {
                $injector.get('$controller')("InvoiceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rbaccountsApp:invoiceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
