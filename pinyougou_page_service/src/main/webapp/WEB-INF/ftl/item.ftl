<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
    <title>产品详情页</title>
    <link rel="icon" href="assets/img/favicon.ico">

    <link rel="stylesheet" type="text/css" href="css/webbase.css"/>
    <link rel="stylesheet" type="text/css" href="css/pages-item.css"/>
    <link rel="stylesheet" type="text/css" href="css/pages-zoom.css"/>
    <link rel="stylesheet" type="text/css" href="css/widget-cartPanelView.css"/>
    <script src="plugins/angularjs/angular.min.js"></script>

    <!-- 引入基础模块 -->
    <script type="text/javascript" src="js/base.js"></script>
    <!-- 引入公共controller模块 -->
    <script type="text/javascript" src="js/controller/baseController.js"></script>
    <!-- 引入controller模块 -->
    <script type="text/javascript" src="js/controller/pageController.js"></script>


    <script type="text/javascript">
        var spec = ${item.spec};

        var specList = [
            <#list goods.itemList as item>
            {id:${item.id?c},spec:${item.spec}},
            </#list>
        ];

        var itemId = ${item.id?c};
    </script>
</head>

<body ng-app="pinyougou" ng-controller="pageController">
<!-- 头部栏位 -->
<!--页面顶部-->
<div id="nav-bottom">
    <!--顶部-->
    <#include 'head.ftl'>
</div>

<div class="py-container">
    <div id="item">
        <div class="crumb-wrap">
            <ul class="sui-breadcrumb">
                <li>
                    <a href="#">${goods.categoryMap.category1Name}</a>
                </li>
                <li>
                    <a href="#">${goods.categoryMap.category2Name}</a>
                </li>
                <li>
                    <a href="#">${goods.categoryMap.category3Name}</a>
                </li>
                <li class="active">${goods.goods.goodsName}</li>
            </ul>
        </div>
        <!--product-info-->
        <div class="product-info">

            <#assign imageList = goods.goodsDesc.itemImages?eval>

            <div class="fl preview-wrap">
                <!--放大镜效果-->
                <div class="zoom">
                    <#if (imageList?size>0)>
                        <!--默认第一个预览-->
                        <div id="preview" class="spec-preview">
                        <span class="jqzoom">
                        <img jqimg="${imageList[0].url}" src="${imageList[0].url}" height="400px" width="400px"/>
                        </span>
                        </div>
                        <!--下方的缩略图-->
                        <div class="spec-scroll">
                        <a class="prev">&lt;</a>
                        <!--左右按钮-->
                        <div class="items">
                        <ul>
                        <#list imageList as image>
                            <li><img src="${image.url}" bimg="${image.url}" onmousemove="preview(this)"/></li>
                        </#list>
                        </ul>
                        </div>
                        <a class="next">&gt;</a>
                        </div>
                    </#if>
                </div>
            </div>
            <div class="fr itemInfo-wrap">
                <div class="sku-name">
                    <h4>${item.title}</h4>
                </div>
                <div class="news">
                    <span>${item.sellPoint!goods.goods.caption}</span>
                </div>
                <div class="summary">
                    <div class="summary-wrap">
                        <div class="fl title">
                            <i>价　　格</i>
                        </div>
                        <div class="fl price">
                            <i>¥</i>
                            <em>${item.price}</em>
                            <span>降价通知</span>
                        </div>
                        <div class="fr remark">
                            <i>累计评价</i><em>612188</em>
                        </div>
                    </div>
                    <div class="summary-wrap">
                        <div class="fl title">
                            <i>促　　销</i>
                        </div>
                        <div class="fl fix-width">
                            <i class="red-bg">加价购</i>
                            <em class="t-gray">满999.00另加20.00元，或满1999.00另加30.00元，或满2999.00另加40.00元，即可在购物车换 购热销商品</em>
                        </div>
                    </div>
                </div>
                <div class="support">
                    <div class="summary-wrap">
                        <div class="fl title">
                            <i>支　　持</i>
                        </div>
                        <div class="fl fix-width">
                            <em class="t-gray">以旧换新，闲置手机回收 4G套餐超值抢 礼品购</em>
                        </div>
                    </div>
                    <div class="summary-wrap">
                        <div class="fl title">
                            <i>配 送 至</i>
                        </div>
                        <div class="fl fix-width">
                            <em class="t-gray">满999.00另加20.00元，或满1999.00另加30.00元，或满2999.00另加40.00元，即可在购物车换购热销商品</em>
                        </div>
                    </div>
                </div>
                <div class="clearfix choose">
                    <div id="specification" class="summary-wrap clearfix">
                        <#assign specList=goods.goodsDesc.specificationItems?eval>
                        <#list specList as spec>
                            <dl>
                            <dt>
                            <div class="fl title">
                            <i>${spec.attributeName}</i>
                            </div>
                            </dt>
                            <dd>
                        <#--  class="selected" -->
                            <#list spec.attributeValue as value>
                                <a href="javascript:;" ng-click="updateSpecAttribute('${spec.attributeName}','${value}')" class="{{isSelected('${spec.attributeName}','${value}')?'selected':''}}">${value}
                                <span title="点击取消选择">&nbsp;</span>
                                </a>
                            </#list>
                            </dd>
                            </dl>
                        </#list>
                    </div>


                    <div class="summary-wrap">
                        <div class="fl title">
                            <div class="control-group">
                                <div class="controls">
                                    <input autocomplete="off" type="text" value="{{number}}" minnum="1" class="itxt"/>
                                    <a href="javascript:void(0)" ng-click="addNum(number + 1)"
                                       class="increment plus">+</a>
                                    <a href="javascript:void(0)" ng-click="addNum(number - 1)"
                                       class="increment mins">-</a>
                                </div>
                            </div>
                        </div>
                        <div class="fl">
                            <ul class="btn-choose unstyled">
                                <li>
                                    <a href="javaScript:;" ng-click="addItemToCartList()" target="_blank" class="sui-btn  btn-danger addshopcar">加入购物车</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--product-detail-->
        <#include 'left.ftl'>
            <!--like-->
        <#include 'like.ftl'>
    </div>
</div>
<!-- 底部栏位 -->
<!--页面底部-->
<#include 'foot.ftl'>
<!--页面底部END-->

<!--侧栏面板开始-->
<div class="J-global-toolbar">
    <div class="toolbar-wrap J-wrap">
        <div class="toolbar">
            <div class="toolbar-panels J-panel">

                <!-- 购物车 -->
                <div style="visibility: hidden;" class="J-content toolbar-panel tbar-panel-cart toolbar-animate-out">
                    <h3 class="tbar-panel-header J-panel-header">
                        <a href="" class="title"><i></i><em class="title">购物车</em></a>
                        <span class="close-panel J-close" onclick="cartPanelView.tbar_panel_close('cart');"></span>
                    </h3>
                    <div class="tbar-panel-main">
                        <div class="tbar-panel-content J-panel-content">
                            <div id="J-cart-tips" class="tbar-tipbox hide">
                                <div class="tip-inner">
                                    <span class="tip-text">还没有登录，登录后商品将被保存</span>
                                    <a href="#none" class="tip-btn J-login">登录</a>
                                </div>
                            </div>
                            <div id="J-cart-render">
                                <!-- 列表 -->
                                <div id="cart-list" class="tbar-cart-list">
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 小计 -->
                    <div id="cart-footer" class="tbar-panel-footer J-panel-footer">
                        <div class="tbar-checkout">
                            <div class="jtc-number"><strong class="J-count" id="cart-number">0</strong>件商品</div>
                            <div class="jtc-sum"> 共计：<strong class="J-total" id="cart-sum">¥0</strong></div>
                            <a class="jtc-btn J-btn" href="#none" target="_blank">去购物车结算</a>
                        </div>
                    </div>
                </div>

                <!-- 我的关注 -->
                <div style="visibility: hidden;" data-name="follow" class="J-content toolbar-panel tbar-panel-follow">
                    <h3 class="tbar-panel-header J-panel-header">
                        <a href="#" target="_blank" class="title"> <i></i> <em class="title">我的关注</em> </a>
                        <span class="close-panel J-close" onclick="cartPanelView.tbar_panel_close('follow');"></span>
                    </h3>
                    <div class="tbar-panel-main">
                        <div class="tbar-panel-content J-panel-content">
                            <div class="tbar-tipbox2">
                                <div class="tip-inner"><i class="i-loading"></i></div>
                            </div>
                        </div>
                    </div>
                    <div class="tbar-panel-footer J-panel-footer"></div>
                </div>

                <!-- 我的足迹 -->
                <div style="visibility: hidden;" class="J-content toolbar-panel tbar-panel-history toolbar-animate-in">
                    <h3 class="tbar-panel-header J-panel-header">
                        <a href="#" target="_blank" class="title"> <i></i> <em class="title">我的足迹</em> </a>
                        <span class="close-panel J-close" onclick="cartPanelView.tbar_panel_close('history');"></span>
                    </h3>
                    <div class="tbar-panel-main">
                        <div class="tbar-panel-content J-panel-content">
                            <div class="jt-history-wrap">
                                <ul>
                                    <!--<li class="jth-item">
                                        <a href="#" class="img-wrap"> <img src=".portal/img/like_03.png" height="100" width="100" /> </a>
                                        <a class="add-cart-button" href="#" target="_blank">加入购物车</a>
                                        <a href="#" target="_blank" class="price">￥498.00</a>
                                    </li>
                                    <li class="jth-item">
                                        <a href="#" class="img-wrap"> <img src="portal/img/like_02.png" height="100" width="100" /></a>
                                        <a class="add-cart-button" href="#" target="_blank">加入购物车</a>
                                        <a href="#" target="_blank" class="price">￥498.00</a>
                                    </li>-->
                                </ul>
                                <a href="#" class="history-bottom-more" target="_blank">查看更多足迹商品 &gt;&gt;</a>
                            </div>
                        </div>
                    </div>
                    <div class="tbar-panel-footer J-panel-footer"></div>
                </div>

            </div>

            <div class="toolbar-header"></div>

            <!-- 侧栏按钮 -->
            <div class="toolbar-tabs J-tab">
                <div onclick="cartPanelView.tabItemClick('cart')" class="toolbar-tab tbar-tab-cart" data="购物车"
                     tag="cart">
                    <i class="tab-ico"></i>
                    <em class="tab-text"></em>
                    <span class="tab-sub J-count " id="tab-sub-cart-count">0</span>
                </div>
                <div onclick="cartPanelView.tabItemClick('follow')" class="toolbar-tab tbar-tab-follow" data="我的关注"
                     tag="follow">
                    <i class="tab-ico"></i>
                    <em class="tab-text"></em>
                    <span class="tab-sub J-count hide">0</span>
                </div>
                <div onclick="cartPanelView.tabItemClick('history')" class="toolbar-tab tbar-tab-history" data="我的足迹"
                     tag="history">
                    <i class="tab-ico"></i>
                    <em class="tab-text"></em>
                    <span class="tab-sub J-count hide">0</span>
                </div>
            </div>

            <div class="toolbar-footer">
                <div class="toolbar-tab tbar-tab-top"><a href="#"> <i class="tab-ico  "></i> <em
                                class="footer-tab-text">顶部</em> </a></div>
                <div class="toolbar-tab tbar-tab-feedback"><a href="#" target="_blank"> <i class="tab-ico"></i> <em
                                class="footer-tab-text ">反馈</em> </a></div>
            </div>

            <div class="toolbar-mini"></div>

        </div>

        <div id="J-toolbar-load-hook"></div>

    </div>
</div>
<!--购物车单元格 模板-->
<script type="text/template" id="tbar-cart-item-template">
    <div class="tbar-cart-item">
        <div class="jtc-item-promo">
            <em class="promo-tag promo-mz">满赠<i class="arrow"></i></em>
            <div class="promo-text">已购满600元，您可领赠品</div>
        </div>
        <div class="jtc-item-goods">
            <span class="p-img"><a href="#" target="_blank"><img src="{2}" alt="{1}" height="50" width="50"/></a></span>
            <div class="p-name">
                <a href="#">{1}</a>
            </div>
            <div class="p-price"><strong>¥{3}</strong>×{4}</div>
            <a href="#none" class="p-del J-del">删除</a>
        </div>
    </div>
</script>
<!--侧栏面板结束-->


<script type="text/javascript" src="js/plugins/jquery/jquery.min.js"></script>
<script type="text/javascript">
    $(function () {
        $("#service").hover(function () {
            $(".service").show();
        }, function () {
            $(".service").hide();
        });
        $("#shopcar").hover(function () {
            $("#shopcarlist").show();
        }, function () {
            $("#shopcarlist").hide();
        });

    })
</script>
<script type="text/javascript" src="js/model/cartModel.js"></script>
<script type="text/javascript" src="js/plugins/jquery.easing/jquery.easing.min.js"></script>
<script type="text/javascript" src="js/plugins/sui/sui.min.js"></script>
<script type="text/javascript" src="js/plugins/jquery.jqzoom/jquery.jqzoom.js"></script>
<script type="text/javascript" src="js/plugins/jquery.jqzoom/zoom.js"></script>
<script type="text/javascript" src="index/index.js"></script>
</body>

</html>