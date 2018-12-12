//服务层
app.service('uploadService', function ($http) {

    // 文件上传
    this.uploadFile = function () {
        // 结合AngularJS+HTML5对象完成表单上传文件操作
        var formData = new FormData();
        // 获取上传文件对象
        // 参数1:后台接受表单文件数据的参数名称
        // 参数2 : file.files[0] 获取上传文件表单项,上传文件对象
        formData.append("file", file.files[0]);
        return $http({
            // 指定提交方式为 post
            method: "POST",
            // 指定提交路径
            url: "../upload/uploadFile.do",
            data: formData,
            headers: {'Content-Type': undefined}, // 作用:指定enctype
            transformRequest: angular.identity // 序列化表单对象
        });
    }
});
