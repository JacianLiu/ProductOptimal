app.controller("indexController",function ($scope,$controller,contentService) {

    $controller("baseController",{$scope:$scope});

    $scope.findByCategoryId = function (categoryId) {
        contentService.findByCategoryId(categoryId).success(function (response) {
            $scope.contentList = response;
        });
    }
});