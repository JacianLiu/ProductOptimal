// $location 内置对象 , 完成路由传参 ( 参数的获取 )
app.controller("searchController",function($scope,$controller,$location,searchService){
	
	//控制器继承
	$controller("baseController",{$scope:$scope});
	
	//查询条件封装对象
	$scope.searchMap={
		"keywords":"",
		"category":"",
		"brand":"",
		"spec":{}, 
		"price":"",
		"sort":"ASC",//排序方式  升序  降序
		"sortField":"", //排序字段
		"pageNo":1,//当前页
		"pageSize":60 //每页记录数
			
	};
	
	//获取门户网站传递的参数
	var keywords =$location.search()["keywords"];
	
	if(keywords!="" && keywords!=null && keywords != "undefined"){
		//门户网站输入了搜索关键字
		$scope.searchMap.keywords=keywords;
	}else{
		//门户网站未输入了搜索关键字
		$scope.searchMap.keywords="";
	}
	
	//商品查询方法
	$scope.search=function(){
		
		searchService.search($scope.searchMap).success(
			function(response){
				$scope.resultMap=response;
				
				//每次查询后，需要重新构建分页构建栏
				buildPageLabel();
			}
		);
		
	}
	
	//条件查询
	$scope.addFilterCondition=function(key,value){
		if(key=="brand" || key=="category" || key=="price"){
			$scope.searchMap[key]=value;
		}else{
			//封装规格数据
			//{spec:{网络:移动3G}}
			$scope.searchMap.spec[key]=value;
		}
		
		//调用查询
		$scope.search();
	}
	
	//移除搜索条件
	$scope.removeSearchItem=function(key){
		if(key=="brand" || key=="category" || key=="price"){
			$scope.searchMap[key]="";
		}else{
			//删除规格数据
			//{spec:{网络:移动3G}}
			//delete删除对象
			delete $scope.searchMap.spec[key];
		}
		
		//调用查询
		$scope.search();
		
	}
	
	//排序操作
	$scope.sortSearch=function(sortField,sort){
		
		if(sortField=="price"){
			//基于价格排序
			$scope.searchMap.sortField=sortField;
			$scope.searchMap.sort=sort;
		}
		
		if(sortField=="updatetime"){
			//基于时间排序
			$scope.searchMap.sortField=sortField;
			$scope.searchMap.sort=sort;
		}
		
		//调用查询
		$scope.search();
	}
	
	//构建分页工具条代码
	buildPageLabel=function(){
		$scope.pageLabel = [];// 新增分页栏属性
		var maxPageNo = $scope.resultMap.totalPages;// 得到最后页码
		
		// 定义属性,显示省略号
		$scope.firstDot = true;
		$scope.lastDot = true;

		var firstPage = 1;// 开始页码
		var lastPage = maxPageNo;// 截止页码

		if ($scope.resultMap.totalPages > 5) { // 如果总页数大于5页,显示部分页码
			if ($scope.resultMap.pageNo <= 3) {// 如果当前页小于等于3
				lastPage = 5; // 前5页
				// 前面没有省略号
				$scope.firstDot = false;

			} else if ($scope.searchMap.pageNo >= lastPage - 2) {// 如果当前页大于等于最大页码-2
				firstPage = maxPageNo - 4; // 后5页
				// 后面没有省略号
				$scope.lastDot = false;
			} else {// 显示当前页为中心的5页
				firstPage = $scope.searchMap.pageNo - 2;
				lastPage = $scope.searchMap.pageNo + 2;
			}
		} else {
			// 页码数小于5页  前后都没有省略号
			$scope.firstDot = false;
			$scope.lastDot = false;
		}
		// 循环产生页码标签
		for (var i = firstPage; i <= lastPage; i++) {
			$scope.pageLabel.push(i);
		}
	}
	
	
	//分页查询
	$scope.queryForPage=function(pageNo){
		$scope.searchMap.pageNo=pageNo;
		
		//执行查询操作
		$scope.search();
		
	}
	
	//分页页码显示逻辑分析：
	// 1,如果页面数不足5页,展示所有页号
	// 2,如果页码数大于5页
		// 1) 如果展示最前面的5页,后面必须有省略号.....
		// 2) 如果展示是后5页,前面必须有省略号
		// 3) 如果展示是中间5页,前后都有省略号
	
	// 定义函数,判断是否是第一页
	$scope.isTopPage = function() {
		if ($scope.searchMap.pageNo == 1) {
			return true;
		} else {
			return false;
		}
	}
	// 定义函数,判断是否最后一页
	$scope.isLastPage = function() {
		if ($scope.searchMap.pageNo == $scope.resultMap.totalPages) {
			return true;
		} else {
			return false;
		}
	}
	
});