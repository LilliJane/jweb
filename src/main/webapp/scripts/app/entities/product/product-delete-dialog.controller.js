'use strict';

angular.module('jwebApp')
	.controller('ProductDeleteController', function($scope, $uibModalInstance, entity, Product) {

        $scope.product = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Product.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
