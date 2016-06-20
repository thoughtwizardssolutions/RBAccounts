'use strict';

describe('Controller Tests', function() {

    describe('Imei Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockImei, MockInvoiceItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockImei = jasmine.createSpy('MockImei');
            MockInvoiceItem = jasmine.createSpy('MockInvoiceItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Imei': MockImei,
                'InvoiceItem': MockInvoiceItem
            };
            createController = function() {
                $injector.get('$controller')("ImeiDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rbaccountsApp:imeiUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
