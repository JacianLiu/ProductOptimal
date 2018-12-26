 //控制层 
app.controller('cartController' ,function($scope,$controller   ,cartService){
	
	$controller('baseController',{$scope:$scope});//继承
	
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
	} 
	
	//添加商品到购物车列表
	$scope.addItemToCartList=function (itemId,num) {
        cartService.addItemToCartList(itemId,num).success(function (response) {
			if(response.success){//添加商品到购物车列表成功
				//查询购物车列表
                $scope.findCartList();
			}else {
				alert(response.message);
			}
        })
    }
    
    
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
    }
	
});
