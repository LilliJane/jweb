'use strict';

angular.module('jwebApp')
	.controller('NewsDeleteController', function($scope, $uibModalInstance, entity, News) {

        $scope.news = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            News.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
