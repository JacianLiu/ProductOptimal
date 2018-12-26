app.controller("indexController",function ($scope,$controller,loginService) {

    //控制器继承代码实现  参数一：继承的父控制器名称  参数二：$scope资源共享写法
    $controller("baseController",{$scope:$scope});

    //获取用户信息
    $scope.getName=function () {
        loginService.getName().success(function (response) {
            //{loginName:"admin"}
            $scope.loginName=response.loginName;
            //console.log(loginName);
        })
    }
})