'use strict';

angular.module('jwebApp')
    .controller('CartDetailController', function ($scope, $rootScope, $stateParams, entity, Cart, User, Product) {
        $scope.cart = entity;
        $scope.load = function (id) {
            Cart.get({id: id}, function(result) {
                $scope.cart = result;
            });
        };
        var unsubscribe = $rootScope.$on('jwebApp:cartUpdate', function(event, result) {
            $scope.cart = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.getTotal = function(){
            var total = 0;
            for(var i = 0; i < $scope.cart.products.length; i++){
                var product = $scope.cart.products[i];
                total += (product.price);
            }
            return total;
        }
    });
