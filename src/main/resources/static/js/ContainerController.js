/**
 * Created by felipecarnevalli on 14/7/18.
 */

(function(){
    'use strict';

    angular.module('TestModule').controller('ContainerController', ['ContainerService', '$interval', function(ContainerService, $interval) {
        var vm = this;

        var REFRESH_INTERVAL = 2 * 1000;

        vm.containers = [];

        activate();

        function activate() {
            $interval(refreshContainers, REFRESH_INTERVAL);
            getAllContainer();
            vm.isReady = true;
        }

        function getAllContainer(){
            vm.containers = ContainerService.getAllContainer();
        }

        function refreshContainers(){
            ContainerService.loadAllContainers().then(function(result) {
                getAllContainer();
            });
        }
    }]);
})();