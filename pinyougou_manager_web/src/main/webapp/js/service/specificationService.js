
app.service("specificationService", function ($http) {

    // 分页条件查询
    this.search = function (pageNum, pageSize, searchParameter) {
        return $http.post("../specification/search.do?pageNum=" + pageNum + "&pageSize=" + pageSize, searchParameter);
    };

    // 新增
    this.add = function (entity) {
        return $http.post("../specification/add.do", entity);
    };

    // 更新
    this.update = function (entity) {
        return $http.post("../specification/update.do", entity);
    };

    // 根据ID查询品牌信息
    this.findOne = function (id) {
        return $http.get('../specification/findOne.do?id=' + id);
    };

    // 根据ID删除信息
    this.dele = function (selectIds) {
        return $http.get("../specification/delete.do?ids=" + selectIds);
    };

    this.selectSpecList = function () {
        return $http.get("../specification/selectSpecificationList.do");
    }
});
