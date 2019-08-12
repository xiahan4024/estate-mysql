<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	pageContext.setAttribute("APP_PATH", request.getContextPath());
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="${APP_PATH}/static/css/bootstrap.min.css?v=3.3.6" th:href="@{/css/bootstrap.min.css?v=3.3.6}" rel="stylesheet">
<link href="${APP_PATH}/static/css/font-awesome.css?v=4.4.0" th:href="@{/css/font-awesome.css?v=4.4.0}" rel="stylesheet">
<link href="${APP_PATH}/static/css/animate.css" th:href="@{/css/animate.css}" rel="stylesheet">
<link href="${APP_PATH}/static/css/style.css?v=4.1.0" th:href="@{/css/style.css?v=4.1.0}" rel="stylesheet">
<script type="text/javascript" src="${APP_PATH}/static/js/jquery-1.12.4.min.js" th:src="@{/js/jquery-1.12.4.min.js}"></script>
</head>
<body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight article">
        <div class="row">
            <div class="col-lg-10 col-lg-offset-1">
                <div class="ibox">
                    <div class="ibox-content">
						<div class="text-center article-title">
                            <h1></h1>
                        </div>
						<hr width="50%">
						<center>
							<span id="date"></span>
						</center>
						<hr width="50%">
						 <div  id = "message"></div>
						 <hr>
						 <span>
						 	<a id="original" class="btn btn-success" target="_blank">
                            	<i class="fa fa-link"> </i> 查看原文
                        	</a>
                        </span>			
					</div>
				</div>
			</div>
		</div>
	</div>
	
	
<script type="text/javascript">
	$(function(){
		var urlStr = window.location.href;
		$.ajax({
			url:urlStr+"/detail",
			type:"GET",
			success:function(result){
				var str  = result.result.result.a;
				console.log(str)
				$("#original").attr("href",str); 
				console.log(result)
				var key = result.result.key;
				var title = result.result.result.title;
				var message = result.result.result.message;
				$("title").first().text(title);
				if(key != ""){
					var re = new RegExp(key,"g");
					var varKey = "<span style='color:red;'>"+key +"</span>";
					title = title.replace(re,varKey);
					console.log(title)
					message = message.replace(re,varKey);
				}
				var h1 = $("h1").first().append(title);
				var span = $("#date").append(result.result.result.date);
				$("#message").append(message);
			}
		});
	});
</script>
</body>
</html>