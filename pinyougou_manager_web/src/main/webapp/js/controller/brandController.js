app.controller("brandController", function ($scope, $controller, brandService) {

    $controller("baseController", {$scope: $scope});

    // 查询所有
    $scope.findAll = function () {
        brandService.findAll.success(function (response) {
            $scope.list = response;
        });
    };

    // 分页查询
    $scope.findPage = function (pageNum, pageSize) {
        brandService.findPage.success(function (response) {
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    };

    // 新增和修改
    $scope.saveOrUpdate = function () {
        var method = null;
        if ($scope.entity.id != null) {
            // 修改
            method = brandService.update($scope.entity);
        } else {
            // 插入
            method = brandService.add($scope.entity);
        }

        method.success(function (response) {
            if (response.success) {
                // 新增或修改成功后重新加载页面
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        });
    };

    // 根据ID查询品牌信息
    $scope.findOne = function (id) {
        brandService.findOne(id).success(function (response) {
            $scope.entity = response;
        });
    };

    // 批量删除品牌
    $scope.dele = function () {
        if (confirm("您确定要删除选中的品牌信息吗?")) {
            brandService.dele($scope.selectIds).success(function (response) {
                if (response.success) {
                    // 删除成功后重新加载页面
                    $scope.reloadList();
                } else {
                    // 删除失败弹框提示
                    alert(response.message);
                }
            });
        }
    };

    //声明searchEntity
    $scope.searchParameter = {};
    $scope.search = function (pageNum, pageSize) {
        brandService.search(pageNum, pageSize, $scope.searchParameter).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        });
    };
});