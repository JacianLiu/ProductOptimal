//定义过滤器，处理字符串为HTML标签
//使用方式：<em ng-bind-html="带标签的数据 | trustHtml"></em>
app.filter("trustHtml",function($sce){
	return function(data){
		return $sce.trustAsHtml(data);
	};
});