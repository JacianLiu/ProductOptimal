app.controller("baseController",function ($scope) {

    //分页配置
    $scope.paginationConf = {
        currentPage:1,  				//当前页
        totalItems:10,					//总记录数
        itemsPerPage:10,				//每页记录数
        perPageOptions:[10,20,30,40,50], //分页选项，下拉选择一页多少条记录
        onChange:function(){			//页面变更后触发的方法
            $scope.reloadList();		//启动就会调用分页组件
        }
    };

    $scope.reloadList=function () {
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    }

    //定义勾选的id数组
    $scope.selectIds=[];

    //更新数据列表复选框选中状态
    $scope.updateSelection=function ($event,id) {
        //判断复选框选中状态  $event.target事件源对象
        if($event.target.checked){
            //选中状态
            $scope.selectIds.push(id);
        }else{
            //取消勾选，从数组中移除该元素  //参数一：移除位置的元素的索引值 参数二：从该位置移除几个元素
            var index= $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(index,1);
        }
    }

    //解析json字符串为json数组，获取数组中的对象，根据对象的属性名获取属性值，做字符串拼接
    //jsonString要解析的json字符串   key 数组中的对象属性名
    $scope.jsonStringParse=function (jsonString,key) {

        var value="";

        // jsonArray= [{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        var jsonArray =JSON.parse(jsonString);
        for(var i=0;i<jsonArray.length;i++){
            //获取数组中的对象，根据对象的属性名获取属性值，做字符串拼接
            //根据json对象的属性名取属性值的两种方式
            //方式一：当属性名是确定值，  对象.属性名
            //方式一：当属性名是不确定的值，  对象[属性名]
            if(i>0){
                value+=","+jsonArray[i][key];
            }else {
                value+=jsonArray[i][key];
            }
        }
        return value;
    }
    //是否选中为了翻页后回来还能勾选上
    $scope.isChecked = function(id){
        //alert($scope.selectIds.indexOf(id));
        if($scope.selectIds.indexOf(id)!= -1){
            return true;
        }
        return false;
    }
    
})