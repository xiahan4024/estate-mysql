<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	pageContext.setAttribute("APP_PATH", request.getContextPath());
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>爬虫结果展示</title>
<link href="${APP_PATH}/static/css/plugins/sweetalert/sweetalert.css" th:href="@{/css/plugins/sweetalert/sweetalert.css}" rel="stylesheet">
<link href="${APP_PATH}/static/css/bootstrap1.min.css" th:href="@{/css/bootstrap1.min.css}" rel="stylesheet">
<script type="text/javascript" src="${APP_PATH}/static/js/jquery-1.12.4.min.js" th:src="@{/js/jquery-1.12.4.min.js}"></script>
<script src="${APP_PATH}/static/js/bootstrap1.min.js" th:src="@{/js/bootstrap1.min.js}"></script>
<script src="${APP_PATH}/static/js/plugins/sweetalert/sweetalert.min.js" th:src="@{/js/plugins/sweetalert/sweetalert.min.js}"></script>
</head>
<body class="gray-bg">

<div class="ibox-content m-b-sm border-bottom">
                    <div class="text-center p-lg">
                        <h2>新闻动态</h2>
                    </div>
                </div>
	<!-- 搭建显示页面 -->
	<div class="wrapper container wrapper-content animated fadeInUp">
		<!-- 标题 -->
		<div class="row">
			
		</div>
		<div class="col-md-3 col-md-offset-8">
           <div class="input-group">
               <input type="text" placeholder="请输入关键字" class="input-md form-control" value="" name="input" /> <span class="input-group-btn">
                   <button type="button" class="btn btn-md btn-primary"> 搜索</button> </span>
           </div>
       </div>
       <div>
            	<p style="height: 50px"></p>
            </div>
       <div class="col-md-3 col-md-offset-10">
       		<div class="input-group">
                <button class="btn btn-danger" id="emp_delete_all_btn">批量删除</button>
           </div>
       </div>
       <br><br><br>
		<!-- 显示表格数据 -->
		<div class="row">
			<div class="col-md-12">
				<table class="table table-hover" id="emps_table">
					<thead>
						<tr>
							<th>
								<input type="checkbox" id="check_all"/>
							</th>
							<th>#</th>
							
							<th>标题</th>
							
							<th>来源</th>
							
							<th>状态</th>
							
							<th>时间</th>
							
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
					
					</tbody>
				</table>
			</div>
		</div>

		<!-- 显示分页信息 -->
		<div class="row">
			<!--分页文字信息  -->
			<div class="col-md-4" id="page_info_area"></div>
			<!-- 分页条信息 -->
			<div class="col-md-5 col-md-offset-3" id="page_nav_area">
				
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
	
	//当前页码
	var currentPage;
	//总工页码
	var totalRecord;
	//输入框
	var inputText="";
	//是否输入关键字  0:否     1:是
	var flagKey = 0;
	
	//进入第一页
	$(function(){
		$("#emp_delete_all_btn").show();
		to_page(1);
	});
	
	//输入框点击函数
	$(document).on("click","span.input-group-btn",function(){
		inputText = $("input[name='input']").first().val();
		flagKey = 1;
		to_page(1);
	})
	
	//查看详细页面  
	/* $(document).on("click","a[newsid]",function(){
		var newid = $(this).attr("newsid");
		window.blank.location.href="${APP_PATH}/news/gotoNewPage/"+newid;  
	}); */
	
	/*
			全选、全部选
	*/
	$("#check_all").click(function() {
		$(".check_item").prop("checked",$(this).prop("checked"));
	});
	
	$(document).on("click",".check_item",function(){
		var flag = $(".check_item:checked").length == $(".check_item").length;
		$("#check_all").prop("checked",flag);
	});
		
	//跳转到第几页
	function to_page(pn){
		$("#emp_delete_all_btn").show();
		$.ajax({
			url:"${APP_PATH}/news/gotopage/"+ pn,
		 	data:"text=" + inputText + "&flag=" + flagKey, 
			type:"POST",
			success:function(result){
				//1.解析显示新闻
				build_news_table(result);
				//2.解析显示分页信息
				build_page_info(result);
				//3.解析显示分页数据
				build_page_nav(result);
				//4.输入框
				build_news_input(result);
				//5.赋值
				currentPage = result.result.pageInfo.pageNum;
				totalRecord = result.result.pageInfo.total;
				/* if(result.result.text != ""){
					text = result.result.text;
				} */
				var flag = $(".check_item:checked").length == $(".check_item").length;
				$("#check_all").prop("checked",flag);
			}
		});
	}
		
		//4. 输入框显示
		function build_news_input(result) {
			//$("input[name='input']").first().val(result.result.text);
			$("input[name='input']").first().val(result.result.key);
		}
		
		//1.解析显示新闻
		function build_news_table(result) {
			$("#emps_table tbody").empty();
			var newsList = result.result.pageInfo.list;
			var key = result.result.key;
		//	console.log(key);
			var keyTitle = "";
			var KeySource = "";
			$.each(newsList,function(idnex,item){
				var checkBoxTd = "";
				var checkBoxInput = $("<input type='checkbox' class='check_item'/>");
				/* var checkBoxTd = $("<td>  </td>").append(checkBoxInput); */
				var news_id = $("<td></td>").append(item.id);
				/* href='javascript:;' onclick='detailA()'   */
				if(key == ""){
					//console.log("kong ");
					keyTitle = item.title;
					if(keyTitle.length > 25){
						keyTitle = keyTitle.substr(0,25) + "……"; 
					}
					KeySource = item.source;
				}else{
					//console.log(key);
					//<div style="width: 65px;height: 20px;border: 1px solid;">测试元素</div>
					var re = new RegExp(key,"g");
					var varKey = "<span style='color:red;'>"+key +"</span>";
					keyTitle = item.title.replace(re,varKey);
					if(keyTitle.length > (  25 + varKey.length  ) ){
						keyTitle = keyTitle.substr(0,25 + varKey.length) + "……"; 
					}
					KeySource = item.source.replace(re,varKey);
				}
				
				var news_title = $("<td></td>").append($("<a></a>").attr("href","${APP_PATH}/news/gotonewpage/"+item.id).attr("target","_blank").attr("newsid",item.id).append(keyTitle));
				var new_come = $("<td></td>").append(KeySource);
				var flag = $("<button></button>").addClass("btn");
				
				var delBtn =  $("<button></button>").addClass("btn btn-danger btn-sm delete_btn")
					.append($("<span></span>").addClass("glyphicon glyphicon-trash")).append("删除");
				
				if(item.flag == 1){
					checkBoxTd = $("<td>  </td>");
					flag.append($("<span></span>")).append("已审核").addClass("btn-success");
					var editBtn = $("<button></button>").addClass("btn delete-single btn-danger btn-sm")
						.append($("<span></span>").addClass("glyphicon glyphicon-pencil")).append("取消");
					var btnTd = $("<td></td>").append(editBtn);
					$("#emp_delete_all_btn").show();
				}else{
					checkBoxTd = $("<td>  </td>").append(checkBoxInput); 
					flag.append($("<span></span>")).append("待审核").addClass("btn-primary");
					var editBtn = $("<button></button>").addClass("btn btn-primary btn-sm edit_btn")
								.append($("<span></span>").addClass("glyphicon glyphicon-pencil")).append("发布");
					var btnTd = $("<td></td>").append(editBtn).append(" ").append(delBtn);
				}
				
				var new_flag = $("<td></td>").append(flag);
				var news_date = $("<td></td>").append(item.date);
				//为编辑按钮添加一个自定义的属性，来表示当前员工id
				editBtn.attr("edit-id",item.id);
				//为删除按钮添加一个自定义的属性来表示当前删除的员工id
				delBtn.attr("del-id",item.id);	
				
				$("<tr></tr>").append(checkBoxTd)
					.append(news_id)
					.append(news_title)
					.append(new_come)
					.append(new_flag)
					.append(news_date)
					.append(btnTd)
					.appendTo("#emps_table tbody");	
			});
			
		}
		
		//2.解析显示分页信息
		function build_page_info(result){
			$("#page_info_area").empty();
			$("#page_info_area").append("当前 " +result.result.pageInfo.pageNum + " 页，总" +
					result.result.pageInfo.pages+ " 页,总 "+ 
					result.result.pageInfo.total+" 条记录");			
		}
		
		//3.解析显示分页数据
		function build_page_nav(result){
			$("#page_nav_area").empty();
			
			var pageInfo = result.result.pageInfo;
			
			var ul = $("<ul></ul>").addClass("pagination");
			//构建元素
			var firstPageLi = $("<li></li>").append($("<a></a>").append("首页"));
			var prePageLi = $("<li></li>").append($("<a></a>").append("&laquo;"));
			
			if(result.result.pageInfo.isFirstPage == true){
				$(firstPageLi).hide();
				$(prePageLi).hide();
			}else{
				firstPageLi.click(function name() {
					to_page(1);
				//	window.location.href="${APP_PATH}/news/" + 1 + "/page"
				});
				prePageLi.click(function name() {
					to_page(result.result.pageInfo.pageNum - 1);
				//	window.location.href="${APP_PATH}/news/" + currentPage - 1 + "/page"
				});
			}
			
			var nextPageLi = $("<li></li>").append($("<a></a>").append("&raquo;"));
			var lastPageLi = $("<li></li>").append($("<a></a>").append("末页"));
			
			if(result.result.pageInfo.isLastPage == true){
				$(nextPageLi).hide();
				$(lastPageLi).hide();
			}else{
				nextPageLi.click(function() {
					to_page(result.result.pageInfo.pageNum + 1);
				//	window.location.href="${APP_PATH}/news/" + currentPage + 1  + "/page"
				});
				lastPageLi.click(function() {
					to_page(result.result.pageInfo.pages);
				var pageNO = result.result.pageInfo.pages 
				//	window.location.href="${APP_PATH}/news/" + pageNO + "/page"
				});
			}
			
			ul.append(firstPageLi).append(prePageLi);
			$.each(pageInfo.navigatepageNums,function(index,item){
				 var itemLi = $("<li></li>").append($("<a></a>").append(item));
				 if(pageInfo.pageNum == item){
					 itemLi.addClass("active");
				 }
				 itemLi.click(function() {
					to_page(item);
				//	 window.location.href="${APP_PATH}/news/" + item + "/page"
				});
				 ul.append(itemLi);
			});
			ul.append(nextPageLi).append(lastPageLi);
			var navEle = $("<nav></nav>").append(ul);
			navEle.appendTo("#page_nav_area");
		}
		
		//删除单条信息
		$(document).on("click",".delete_btn",function(){
			var news_title = $(this).parent().parent().find("td:eq(2)").text();
			var news_hotId = $(this).attr("del-id");
			swal({
                title: "确认删除这条新闻吗？",
                text: news_title,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "是的，我要删除！",
                cancelButtonText: "让我再考虑一下…",
                closeOnConfirm: false,
                closeOnCancel: false
            },
            function (isConfirm) {
                if (isConfirm) {
                	$.ajax({
    					url:"${APP_PATH}/news/dele/"+news_hotId,
    					type:"DELETE",
    					success:function(){ 
    						to_page(currentPage);
    					}
    				});
                    swal("删除成功！", "您已经永久删除了这条信息。", "success");
                } else {
                    swal("已取消", "您取消了删除操作！", "error");
                }
            })
		});
		
		//删除多条信息
		$("#emp_delete_all_btn").click(function() {
			var news_name = "";
			var news_id = "";
			var temp_name = "";
			$.each($(".check_item:checked"),function(index,item) {
				temp_name = $(this).parents("tr").find("td:eq(2)").text().substr(0,20) + "……";
			//	news_name += (index +1) + "  :"+ $(this).parents("tr").find("td:eq(2)").text()+ "\n";
				news_name += (index +1) + "  :"+ temp_name + "\n";
				news_id +=$(this).parents("tr").find("td:eq(1)").text()+"-";			
			});
			news_name = news_name.substring(0,news_name.length - 1);
			news_id= news_id.substring(0 , news_id.length - 1);
			if(news_id == ""){
				swal({
                    title: "提示",
                    text: "删除项为空，请重新选择"
                });
				return ;
			}
			
			swal({
                title: "确认删除这些新闻吗？",
                text: news_name,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "是的，我要删除！",
                cancelButtonText: "让我再考虑一下…",
                closeOnConfirm: false,
                closeOnCancel: false
            },
            function (isConfirm) {
            	console.log(news_id);
                if (isConfirm) {
                	$.ajax({
    					url: "${APP_PATH}/news/dele/"+news_id,
    					type:"DELETE",
    					success:function(){
    						to_page(currentPage);
    						$("#check_all").prop("checked",false);
    					}
    				});
                    swal("删除成功！", "您已经永久删除了这些信息。", "success");
                } else {
                    swal("已取消", "您取消了删除操作！", "error");
                }
            })
		});
		
		//取消发布单条信息
		$(document).on("click",".delete-single",function(){ 
			var news_title = $(this).parents("tr").find("td:eq(2)").text();
			var news_id= $(this).parents("tr").find("td:eq(1)").text();
			swal({
                title: "确认取消发布这条新闻吗？",
                text: news_title,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "是的，我要取消！",
                cancelButtonText: "让我再考虑一下…",
                closeOnConfirm: false,
                closeOnCancel: false
            },
            function (isConfirm) {
                if (isConfirm) {
                	$.ajax({
    					url:"${APP_PATH}/news/release/"+news_id,
    					type:"DELETE",
    					success:function(msg){ 
    						to_page(currentPage);
    					}
    				});
                    swal("取消成功！", "您已经取消发布了这条信息。", "success");
                } else {
                    swal("已取消", "您取消了发布操作！", "error");
                }
            })
		});
	  	
		//发布单条信息
		$(document).on("click",".edit_btn",function(){ 
			var news_title = $(this).parents("tr").find("td:eq(2)").text();
			var news_id= $(this).parents("tr").find("td:eq(1)").text();
			
			swal({
                title: "确认发布这条新闻吗？",
                text: news_title,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "是的，我要发布！",
                cancelButtonText: "让我再考虑一下…",
                closeOnConfirm: false,
                closeOnCancel: false
            },
            function (isConfirm) {
                if (isConfirm) {
                	$.ajax({
    					url:"${APP_PATH}/news/release/"+news_id,
    					type:"GET",
    					success:function(){ 
    						to_page(currentPage);
    					}
    				});
                    swal("发布成功！", "您已经发布了这条信息。", "success");
                } else {
                    swal("已取消", "您取消了发布操作！", "error");
                }
            })
		});
		
		//发布多条信息
		$("#emp_add_modal_btn").click(function() {
			var news_name ="";
			var news_id ="";
			$.each($(".check_item:checked"),function(index,item){
				news_name += $(item).parents("tr").find("td:eq(2)").text()+","; 
				news_id += $(item).parents("tr").find("td:eq(1)").text()+"-"; 
			});
			news_name = news_name.substring(0,news_name.length -1);
			news_id = news_id.substring(0,news_id.length -1);
			
			if(news_id == ""){
				swal({
                    title: "提示",
                    text: "发布项为空，请重新选择"
                });
				return ;
			}
			
			swal({
                title: "确认发布这些新闻吗？",
                text: news_name,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "是的，我要发布！",
                cancelButtonText: "让我再考虑一下…",
                closeOnConfirm: false,
                closeOnCancel: false
            },
            function (isConfirm) {
                if (isConfirm) {
                	$.ajax({
    					url: "${APP_PATH}/news/release/"+news_id,
    					type:"GET",
    					success:function(){
    						to_page(currentPage);
    						$("#check_all").prop("checked",false);
    					}
    				});
                    swal("发布成功！", "您已经发布了这些信息。", "success");
                } else {
                    swal("已取消", "您取消了发布操作！", "error");
                }
            })
		});
	</script>
</body>
</html>