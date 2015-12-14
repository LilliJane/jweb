'use strict';

describe('Cart Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCart, MockUser, MockProduct;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCart = jasmine.createSpy('MockCart');
        MockUser = jasmine.createSpy('MockUser');
        MockProduct = jasmine.createSpy('MockProduct');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Cart': MockCart,
            'User': MockUser,
            'Product': MockProduct
        };
        createController = function() {
            $injector.get('$controller')("CartDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'jwebApp:cartUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
