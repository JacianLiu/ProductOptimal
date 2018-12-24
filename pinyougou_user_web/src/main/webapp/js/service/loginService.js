//抽取服务
app.service("loginService", function($http) {
	// 获取用户登录名
	this.loadLoginName = function() {
		return $http.get("login/getName.do");
	};
	
});
