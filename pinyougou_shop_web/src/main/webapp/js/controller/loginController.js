//定义控制器
app.controller("loginController", function($scope,loginService) {
	
	//获取用户登录名
	$scope.loadLoginName = function(){
		
		//调用服务层方法
		loginService.loadLoginName().success(
				function(data){
					$scope.loginName = data.loginName;
				}
		);
		
	};
	
});