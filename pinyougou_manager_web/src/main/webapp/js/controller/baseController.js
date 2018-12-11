app.controller("baseController", function ($scope) {
// 分页配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList();
        }
    };

// 刷新列表
    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };

// 定义勾选的ID数组
    $scope.selectIds = [];

// 根据数据列表复选框选中状态
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {
            // 如果是选中状态则将ID加入到数组中
            $scope.selectIds.push(id);
        } else {
            // 如果取消选中则在数组中删除此ID
            var index = $scope.selectIds.indexOf(id);
            // 第一个参数从哪里删除 第二个参数 删除几个元素
            $scope.selectIds.splice(index, 1);
        }
    };


    // 定义方法提取名称
    $scope.jsonStringParse = function (jsonString, key) {
        // 定义空字符串存储解析的值
        var value = "";
        // 将json字符串转换成json数组
        var jsonArray = JSON.parse(jsonString);
        // 遍历数组拼接字符串
        for (var i = 0; i < jsonArray.length; i++) {
            if (i > 0) {
                value += "/" + jsonArray[i][key];
            } else {
                value += jsonArray[i][key];
            }
        }
        return value;
    }

    // 判断复选框是否选中
    $scope.isChecked = function (id) {
        //alert($scope.selectIds);
        //alert(id + ".." + $scope.selectIds.indexOf(id) != -1);
        if ($scope.selectIds.indexOf(id) != -1) {
            return true;
        }
        return false;
    }
});