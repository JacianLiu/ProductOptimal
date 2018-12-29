//服务层
app.service('payService',function($http){
	    	
	// 生成二维码
	this.createNative=function(){
		return $http.get('pay/createNative.do');
	}

	// 查询订单状态
	this.queryPayStatus=function(outTradeNo){
		return $http.get('pay/queryPayStatus.do?outTradeNo=' + outTradeNo);
	}

});
