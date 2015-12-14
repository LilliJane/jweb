'use strict';

angular.module('jwebApp').controller('CartDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cart', 'User', 'Product',
        function($scope, $stateParams, $uibModalInstance, entity, Cart, User, Product) {

        $scope.cart = entity;
        $scope.users = User.query();
        $scope.products = Product.query();
        $scope.load = function(id) {
            Cart.get({id : id}, function(result) {
                $scope.cart = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jwebApp:cartUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.cart.id != null) {
                Cart.update($scope.cart, onSaveSuccess, onSaveError);
            } else {
                Cart.save($scope.cart, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
