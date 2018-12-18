//服务层
app.service('contentService', function ($http) {
    this.findByCategoryId = function (categoryId) {
        return $http.get('content/findByCategoryId.do?categoryId=' + categoryId);
    }
});
