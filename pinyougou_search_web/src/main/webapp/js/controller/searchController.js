app.controller("searchController", function ($scope, $controller, searchService) {

    $controller("baseController", {$scope: $scope});

    // 初始化搜索对象
    $scope.searchMap = {
        keywords: "",
        category: "",
        brand: "",
        spec: {}, // 规格数据 ( 由于规格数据参数提交时需要提交规格名称以及规格选项值 , 以对象格式组装 )
        price: "",
        sort: "ASC",
        sortField: "",
        pageNo: 1, // 当前页码
        pageSize: 60 // 每页显示条数
    };

    $scope.search = function () {
        searchService.search($scope.searchMap).success(function (response) {
            $scope.resultMap = response;
        });
    };

    // 封装品牌数据
    $scope.addFilterCondition = function (key, value) {
        if (key == "category" || key == "brand" || key == "price") {
            $scope.searchMap[key] = value;
        } else {
            $scope.searchMap.spec[key] = value;
        }
        // 指定查询条件后重新查询列表
        $scope.search();
    };

    $scope.removeSearchItem = function (key) {
        if (key == "category" || key == "brand" || key == "price") {
            $scope.searchMap[key] = "";
        } else {
            delete $scope.searchMap.spec[key];
        }
        // 指定查询条件后重新查询列表
        $scope.search();
    }
});