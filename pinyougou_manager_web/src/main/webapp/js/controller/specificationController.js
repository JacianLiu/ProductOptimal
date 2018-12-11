app.controller("specificationController", function ($scope, $controller, specificationService) {

    $controller("baseController", {$scope: $scope});

    //声明searchEntity
    $scope.searchEntity = {};

    $scope.search = function (pageNum, pageSize) {
        specificationService.search(pageNum, pageSize, $scope.searchEntity).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        });
    };

    // 新增和修改
    $scope.saveOrUpdate = function () {
        var method = null;
        if ($scope.entity.tbSpecification.id != null) {
            // 修改
            method = specificationService.update($scope.entity);
        } else {
            // 插入
            method = specificationService.add($scope.entity);
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
        specificationService.findOne(id).success(function (response) {
            $scope.entity = response;
        });
    };

    // 批量删除品牌
    $scope.dele = function () {
        if (confirm("您确定要删除选中的品牌信息吗?")) {
            specificationService.dele($scope.selectIds).success(function (response) {
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

    // 声明entity
    $scope.entity = {tbSpecification:{}, tbSpecificationOptions:[]};

    $scope.addRow = function () {
        $scope.entity.tbSpecificationOptions.push({});
    };

    $scope.deleRow = function (index) {
        $scope.entity.tbSpecificationOptions.splice(index, 1);
    };
});