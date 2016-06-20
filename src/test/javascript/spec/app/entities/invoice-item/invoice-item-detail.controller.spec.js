'use strict';

describe('Controller Tests', function() {

    describe('InvoiceItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInvoiceItem, MockImei, MockInvoice;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInvoiceItem = jasmine.createSpy('MockInvoiceItem');
            MockImei = jasmine.createSpy('MockImei');
            MockInvoice = jasmine.createSpy('MockInvoice');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InvoiceItem': MockInvoiceItem,
                'Imei': MockImei,
                'Invoice': MockInvoice
            };
            createController = function() {
                $injector.get('$controller')("InvoiceItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rbaccountsApp:invoiceItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
