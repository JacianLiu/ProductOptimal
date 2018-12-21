app.controller("indexController",function ($scope,$controller,contentService) {

    $controller("baseController",{$scope:$scope});

    $scope.findByCategoryId = function (categoryId) {
        contentService.findByCategoryId(categoryId).success(function (response) {
            $scope.contentList = response;
        });
    };

    // 门户网站与搜索页对接
    $scope.search = function () {
        location.href = "http://search.pinyougou.com/search.html#?keywords=" + $scope.keywords;
    }
});