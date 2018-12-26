//定义服务层  发起请求
app.service("loginService",function ($http) {
    //获取用户信息
    this.getName=function () {
        return $http.get("login/getName.do");
    }
})
