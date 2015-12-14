'use strict';

angular.module('jwebApp')
    .factory('Cart', function ($resource, DateUtils) {
        return $resource('api/carts/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
