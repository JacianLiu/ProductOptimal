//控制层
app.controller('typeTemplateController', function ($scope, $controller, typeTemplateService, brandService, specificationService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        typeTemplateService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        typeTemplateService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        typeTemplateService.findOne(id).success(
            function (response) {
                $scope.entity = response;
                $scope.entity.brandIds = JSON.parse(response.brandIds);
                $scope.entity.customAttributeItems = JSON.parse(response.customAttributeItems);
                $scope.entity.specIds = JSON.parse(response.specIds);
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = typeTemplateService.update($scope.entity); //修改
        } else {
            serviceObject = typeTemplateService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        typeTemplateService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        typeTemplateService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    // 初始化品牌信息
    $scope.brandList = {
        data: []
    };

    $scope.specList = {
        data: []
    };

    // 查询所有品牌信息
    $scope.selectBrandList = function () {
        brandService.selectBrandList().success(function (response) {
            $scope.brandList.data = response;
        });
    };

    $scope.selectSpecList = function () {
        specificationService.selectSpecList().success(function (response) {
            $scope.specList.data = response;
        });
    };

    //初始化entity对象
    $scope.entity={customAttributeItems:[]};

    // 添加一行
    $scope.addRow = function () {

        $scope.entity.customAttributeItems.push({});
    };

    // 删除一行
    $scope.deleRow = function (index) {
        $scope.entity.customAttributeItems.splice(index, 1);
    };

});	
