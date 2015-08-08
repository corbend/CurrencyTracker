(function() {"use strict";

    function Ctrl($scope, $http) {

        $scope.entity = {}
        $scope.list = [];

        $scope.save = function() {
            $http.post("/collectors", $scope.entity)
                .success(function(data) {
                    $scope.list.push(angular.copy($scope.entity));
                    $scope.entity = {};
                });
        }

        $scope.loadAll = function() {
            $http.get("/collectors")
                .success(function(data) {
                    $scope.list = data;
                })
        }

        $scope.loadAll();
    }

    Ctrl.$inject=["$scope", '$http'];

    angular.module('currency_manager.collectors', [])
        .controller('collectorFormCtrl', Ctrl)
})()