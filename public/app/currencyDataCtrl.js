(function() {"use strict";

    function Ctrl($scope, $http) {

        $scope.list = [];

        $scope.loadAll = function() {
            $http.get("/data")
                .success(function(data) {
                    $scope.list = data;
                })
        }

        $scope.loadAll();
    }

    Ctrl.$inject=["$scope", '$http'];

    angular.module('currency_manager.data', [])
        .controller('currencyDataCtrl', Ctrl)
})()