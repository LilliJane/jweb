'use strict';

angular.module('jwebApp')
    .controller('NewsController', function ($scope, $state, News) {

        $scope.newss = [];
        $scope.loadAll = function() {
            News.query(function(result) {
               $scope.newss = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.news = {
                title: null,
                content: null,
                date: null,
                id: null
            };
        };
    });
