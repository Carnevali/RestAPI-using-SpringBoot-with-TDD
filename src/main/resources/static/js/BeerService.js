/**
 * Created by felipecarnevalli on 14/7/18.
 */


(function(){
    'use strict';

    angular.module('TestModule').factory('BeerService', ['$http', '$q', 'urls', function ($http, $q, urls) {
        var factory = {
            createDefault: createDefault
        };

        return factory;

        function createDefault() {
            var deferred = $q.defer();
            $http.get(urls.SERVICE_API + '/beer/default/').then(function (response) {
                    deferred.resolve(response);
                },
                function (errResponse) {
                    console.error('Error while trying start thread');
                    deferred.reject(errResponse);
                }
            );

            return deferred.promise;
        }
    }]);
})();