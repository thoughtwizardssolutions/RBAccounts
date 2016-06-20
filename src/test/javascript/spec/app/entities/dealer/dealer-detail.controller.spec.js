'use strict';

describe('Controller Tests', function() {

    describe('Dealer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDealer, MockAddress;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDealer = jasmine.createSpy('MockDealer');
            MockAddress = jasmine.createSpy('MockAddress');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Dealer': MockDealer,
                'Address': MockAddress
            };
            createController = function() {
                $injector.get('$controller')("DealerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'rbaccountsApp:dealerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
