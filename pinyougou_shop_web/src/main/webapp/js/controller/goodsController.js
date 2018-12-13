//控制层
app.controller('goodsController', function ($scope, $controller, goodsService, itemCatService, typeTemplateService, uploadService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.goods.id != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            $scope.entity.goodsDesc.introduction = editor.html();
            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.entity = {};

                    editor.html("");
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
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
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    // 查询一级分类
    $scope.selectItemCat1List = function () {
        itemCatService.findByParentId(0).success(function (response) {
            $scope.itemCat1List = response;
        })
    };

    // 查询二级分类
        $scope.$watch("entity.goods.category1Id", function (newValue, oldValue) {
            itemCatService.findByParentId(newValue).success(function (response) {
                $scope.itemCat2List = response;
                $scope.itemCat3List = [];
                $scope.entity.goods.typeTemplateId = "";
                $scope.brandList = [];

            })
        })

    // 查询三级分类
        $scope.$watch("entity.goods.category2Id", function (newValue, oldValue) {
            itemCatService.findByParentId(newValue).success(function (response) {
                $scope.itemCat3List = response;
            })
        })

    // 查询模板数据
        $scope.$watch("entity.goods.category3Id", function (newValue, oldValue) {
            itemCatService.findOne(newValue).success(function (response) {
                $scope.entity.goods.typeTemplateId = response.typeId;
                $scope.entity.goodsDesc.specificationItems = [];
            })
        })

    // 查询模板关联数据
    $scope.$watch("entity.goods.typeTemplateId", function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(function (response) {
            $scope.brandList = JSON.parse(response.brandIds);
            $scope.entity.goodsDesc.customAttributeItems = JSON.parse(response.customAttributeItems);//扩展属性
        });

        typeTemplateService.selectSpecList(newValue).success(function (response) {
            $scope.specList = response;
        })
    });

    $scope.imageEntity = {};
    $scope.entity = {goods: {}, goodsDesc: {itemImages: [], specificationItems: []}};

    // 图片上传功能
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (response) {
            if (response.success) {
                $scope.imageEntity.url = response.message;
                angular.element("input[type = file]")[0].value = ""
            } else {
                alert(response.message);
            }
        })
    };

    // 添加图片到列表
    $scope.addImageEntity = function () {
        $scope.entity.goodsDesc.itemImages.push($scope.imageEntity);
    }

    // 删除列表中图片
    $scope.deleImage = function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index, 1)
    };

    // 规格勾选和取消勾选
    $scope.updateSpecAttribute = function ($event, specName, specOption) {
        // 调用方法获取到当前规格对象
        var specObject = $scope.getObjectByKey($scope.entity.goodsDesc.specificationItems, "attributeName", specName);
        // 如果对象为空直接创建对象,并添加指定规格以及规格选项
        if (specObject == null) {
            $scope.entity.goodsDesc.specificationItems.push({"attributeName": specName, "attributeValue": [specOption]})
        } else {
            // 如果对象不为空,判断复选框是否为选中状态
            if ($event.target.checked) {
                // 如果是选中则直接追加
                specObject.attributeValue.push(specOption);
            } else {
                // 如果是取消勾选 , 从数组中移除这个规格选项
                var index = specObject.attributeValue.indexOf(specOption);
                specObject.attributeValue.splice(index);
                // 判断移除后是否还存在元素 , 如果不存在直接删除这个对象
                if (specObject.attributeValue.length <= 0) {
                    var indexOf = $scope.entity.goodsDesc.specificationItems.indexOf(specObject);
                    $scope.entity.goodsDesc.specificationItems.splice(indexOf);
                }
            }
        }
    };

    $scope.status = ["未审核", "审核通过", "审核未通过", "关闭"];

    $scope.itemCatList = [];

    $scope.selectItemCatList = function () {
        itemCatService.findAll().success(function (response) {
            for (var i = 0; i < response.length; i++) {
                $scope.itemCatList[response[i].id] = response[i].name;
            }
        })
    }

});	
