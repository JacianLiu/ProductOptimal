app.controller("indexController",function ($scope,$controller,loginService) {

    $controller("baseController",{$scope:$scope});

    $scope.getName = function () {
        loginService.loadLoginName().success(function (response) {
            $scope.loginName = response.loginName;
        });
    }
});