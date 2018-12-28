 //控制层 
app.controller('orderController' ,function($scope, $controller, orderService, cartService, addressService){
	
	$controller('baseController',{$scope:$scope});//继承

	//定义寄送至联系人对象
	$scope.address = null;

	// 查询收件人列表地址
	$scope.findListByUserId = function() {
		addressService.findListByUserId().success(function (response) {
			$scope.addressList = response;
			for (var i = 0; i < $scope.addressList.length; i++) {
				if ($scope.addressList[i].isDefault == "1") {
					$scope.address = $scope.addressList[i];
					break;
				}
			}
			if ($scope.address == null) {
				$scope.address = $scope.addressList[0];
			}
		});
	};

	// 判断当前对象是否要勾选
	$scope.isSelected = function(addr) {
		if ($scope.address == addr) {
			return true;
		} else {
			return false;
		}
	};

	// 收件人地址切换
	$scope.updateSelected = function(addr) {
		$scope.address = addr;
	};

	// 定义订单实体对象
	$scope.entity = {paymentType: '1'};

	// 支付方式切换 默认使用在线支付
	$scope.updatePaymentType = function(type) {
		$scope.entity.paymentType = type;
	};


    //查询购物车列表
	$scope.findCartList=function(){
        cartService.findCartList().success(
			function(response){
				//接收购物车列表数据
				$scope.cartList=response;
                //统计商品总数量和总金额
				sum();
			}			
		);
	};


    //统计商品总数量和总金额
	sum=function () {
		//总数量
		$scope.totalNum=0;
		//总金额
		$scope.totalMoney=0.00;
		//遍历购物车列表
		for(var i=0;i<$scope.cartList.length;i++){
			//获取购物车对象
           var cart= $scope.cartList[i];
           //获取购物车商品明细列表
			var orderItemList =cart.orderItemList;
			for(var j=0;j<orderItemList.length;j++){
                $scope.totalNum+=orderItemList[j].num;
                $scope.totalMoney+=orderItemList[j].totalFee;
			}
		}
    };

	// 保存订单
	$scope.saveOrder = function () {
		$scope.entity.receiverAreaName = $scope.address.address;
		$scope.entity.receiverMobile = $scope.address.mobile;
		$scope.entity.receiver = $scope.address.contact;
		orderService.add($scope.entity).success(function (response) {
			if (response.success) {
				// 提交成功跳转支付页面
				location.href = "pay.html";
			}  else {
				alert(response.message);
			}
		});
	};
	
});
