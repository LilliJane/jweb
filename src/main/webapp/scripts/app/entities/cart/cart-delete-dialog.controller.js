'use strict';

angular.module('jwebApp')
	.controller('CartDeleteController', function($scope, $uibModalInstance, entity, Cart) {

        $scope.cart = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Cart.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
