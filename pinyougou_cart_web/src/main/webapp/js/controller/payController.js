 //控制层 
app.controller('payController' ,function($scope, $controller, $location, payService){
	
	$controller('baseController',{$scope:$scope});//继承

	// 微信支付二维码生成
	$scope.createNative = function () {
		payService.createNative().success(function (response) {
			// 接受订单号和支付金额
			$scope.outTradeNo = response.out_trade_no;
			$scope.totalFee = response.total_fee/100;
			new QRious({
				element: document.getElementById('qrious'),
				size: 250,
				value: response.code_url
			});
			$scope.queryPayStatus();
		});
	};

	// 查询支付状态
	$scope.queryPayStatus = function () {
		payService.queryPayStatus($scope.outTradeNo).success(function (response) {
			if (response.success) {
				// 支付失败
				location.href = "paysuccess.html#?money=" + $scope.totalFee;
			} else {
				if (response.success == "TimeOut") {
					$scope.createNative();
				} else {
					// 支付失败
					location.href = "payfail.html";
				}
			}
		})
	};

	// 获取支付金额
	$scope.getMoney = function () {
		console.log($location.search()['money']);
		$scope.money = $location.search()['money'];
	}
	
});
