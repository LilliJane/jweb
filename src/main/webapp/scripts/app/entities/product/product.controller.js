'use strict';

angular.module('jwebApp')
    .controller('ProductController', function ($scope, $state, Product) {

        $scope.products = [];
        $scope.loadAll = function() {
            Product.query(function(result) {
               $scope.products = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.product = {
                name: null,
                price: null,
                description: null,
                id: null
            };
        };
    });
