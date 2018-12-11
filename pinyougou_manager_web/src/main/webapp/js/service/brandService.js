
app.service("brandService", function ($http) {
    // 查询所有
    this.findAll = function () {
        return $http.get('../brand/findAll.do');
    };

    // 分页查询所有
    this.findPage = function () {
        return $http.get('../brand/findPage.do?pageNum=' + pageNum + '&pageSize=' + pageSize);
    };

    // 新增
    this.add = function (entity) {
        return $http.post("../brand/add.do", entity);
    };

    // 更新
    this.update = function (entity) {
        return $http.post("../brand/update.do", entity);
    };

    // 根据ID查询品牌信息
    this.findOne = function (id) {
        return $http.get('../brand/findOne.do?id=' + id);
    };

    // 根据ID删除信息
    this.dele = function (selectIds) {
        return $http.get("../brand/delete.do?ids=" + selectIds);
    };

    // 分页条件查询
    this.search = function (pageNum, pageSize, searchParameter) {
        return $http.post("../brand/search.do?pageNum=" + pageNum + "&pageSize=" + pageSize, searchParameter);
    };

    // 查询所有品牌信息
    this.selectBrandList = function () {
        return $http.get("../brand/selectBrandList.do");
    }

});
