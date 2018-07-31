/**
 * Created by felipecarnevalli on 14/7/18.
 */

var app = angular.module('TestModule',['ui.router','ngStorage']);

app.constant('urls', {
    BASE: 'http://localhost:8080/',
    SERVICE_API : 'http://localhost:8080/api/'
});

app.config(['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {

        $stateProvider
            .state('home', {
                url: '/',
                templateUrl: 'partials/list',
                controller:'ContainerController',
                controllerAs:'vm',
                resolve: {
                    containers: function ($q, ContainerService, BeerService) {
                        var deferred = $q.defer();
                        BeerService.createDefault().then(function(result) {
                            ContainerService.loadAllContainers().then(function () {
                                return deferred.promise;
                            });
                        });
                    }
                }
            });
        $urlRouterProvider.otherwise('/');
    }]);