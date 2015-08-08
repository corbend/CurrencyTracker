(function() {"use strict";

    function Ctrl($scope, $http) {

        $scope.chart = {}
        $scope.charts = [];

        $scope.save = function() {
            $http.post("/charts", $scope.chart).success(function(data) {
                $scope.charts.push(angular.copy($scope.chart));
                $scope.chart = {};
            });
        }

        $scope.loadAll = function() {
            $http.get("/charts").success(function(data) {
                $scope.charts = data;
            })
        }

        $scope.loadAll();
    }

    Ctrl.$inject=["$scope", '$http'];

    angular.module('currency_manager.charts', [])
        .controller('chartFormCtrl', Ctrl)
})()