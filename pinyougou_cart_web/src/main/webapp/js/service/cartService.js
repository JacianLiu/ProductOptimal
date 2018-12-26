//服务层
app.service('cartService',function($http){
	    	
	//查询购物车列表
	this.findCartList=function(){
		return $http.get('cart/findCartList.do');
	}

	//添加商品到购物车列表
	this.addItemToCartList=function (itemId,num) {
        return $http.get('cart/addItemToCartList.do?itemId='+itemId+"&num="+num);
    }
});
