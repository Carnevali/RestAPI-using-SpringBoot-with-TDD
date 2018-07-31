/**
 * Created by felipecarnevalli on 14/7/18.
 */


(function(){
    'use strict';

    angular.module('TestModule').factory('ContainerService', ['$localStorage', '$http', '$q', 'urls', function ($localStorage, $http, $q, urls) {
        var factory = {
            loadAllContainers: loadAllContainers,
            getAllContainer: getAllContainer
        };

        return factory;

        function loadAllContainers() {
            var deferred = $q.defer();
            $http.get(urls.SERVICE_API + '/container/').then(function (response) {
                    $localStorage.containers = response.data;
                    deferred.resolve(response);
                },
                function (errResponse) {
                    console.error('Error while loading containers');
                    deferred.reject(errResponse);
                }
            );

            return deferred.promise;
        }

        function getAllContainer(){
            return $localStorage.containers;
        }
    }]);
})();