'use strict';

angular.module('jwebApp').controller('NewsDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'News',
        function($scope, $stateParams, $uibModalInstance, entity, News) {

        $scope.news = entity;
        $scope.load = function(id) {
            News.get({id : id}, function(result) {
                $scope.news = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jwebApp:newsUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.news.id != null) {
                News.update($scope.news, onSaveSuccess, onSaveError);
            } else {
                News.save($scope.news, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
