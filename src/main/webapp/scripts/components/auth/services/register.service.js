'use strict';

angular.module('jwebApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


