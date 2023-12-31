<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title><s:message code="common.pageTitle"/></title>
    <link href="css/sb-admin/bootstrap.min.css" rel="stylesheet">
    <link href="css/sb-admin/metisMenu.min.css" rel="stylesheet">
    <link href="css/sb-admin/sb-admin-2.css" rel="stylesheet">
    <link href="css/sb-admin/font-awesome.min.css" rel="stylesheet" type="text/css">
	<link href="js/dynatree/ui.dynatree.css" rel="stylesheet" id="skinSheet"/>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script src="js/jquery-2.2.3.min.js"></script>
	<script src="js/jquery-ui.js"></script>
	<script src="js/dynatree/jquery.dynatree.js"></script>    
    <script src="css/sb-admin/bootstrap.min.js"></script>
    <script src="css/sb-admin/metisMenu.min.js"></script>
    <script src="css/sb-admin/sb-admin-2.js"></script>
	<script src="js/project9.js"></script>    
<script>


function fn_formSubmit(){
	document.form1.submit();	
}

function showBoardList(ev){
	if( $('#boardlistDiv').is(':visible') ) {
		$("#boardlistDiv").hide();
		return;
	}
	var pos = $( "#boardlistBtn" ).position();
	$("#boardlistDiv").css({
		   "top" : parseInt(pos.top)+30 + "px",
		   "left" : pos.left
	}).show();
	
	var node = $("#tree").dynatree("getRoot");
	
	if (node.childList) return;
	
	$.ajax({
		url: "boardListByAjax",
		type:"post", 
		dataType: "json",
		success: function(result){
			$("#tree").dynatree({children: result});
		    $("#tree").dynatree("getTree").reload();
		    $("#tree").dynatree("getRoot").visit(function(node){
		        node.expand(true);
		    });
		}
	})	
	
}

$(function(){
	$("#tree").dynatree({
		onActivate: TreenodeActivate
	});
});

function TreenodeActivate(node) {
	// bgno 값이 1일 때는 boardList로 요청, 그 외에는 boardList?bgno=<bgno>로 요청
	var bgno = node.data.key;
	var url = (bgno === "1") ? "boardList" : "boardList?bgno=" + bgno;
	location.href = url;
}

</script>
    
</head>

<body>

    <div id="wrapper">

		<jsp:include page="../common/navigation.jsp" />
		
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header"><em class="fa fa-files-o fa-fw"></em> <s:message code="board.boardName"/></h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
	                <button id="boardlistBtn" type="button" class="btn btn-default" onclick="showBoardList()"><em class="fa  fa-files-o fa-fw"></em> 전체</button>
	                <div id="boardlistDiv" style="width: 250px; height:300px; display: none;" class="popover fade bottom in" role="tooltip">
	                	<div style="left:15%;" class="arrow"></div>
	                	<div class="popover-content">
             				<div id="tree"></div>	
	                	</div>
	                </div>
                </div>
            </div>
            <!-- /.row -->
            <div class="panel panel-default">
            	<div class="panel-body">
					<div class="listHead">
						<div class="listHiddenField pull-left field60"><s:message code="board.no"/></div>
						<div class="listHiddenField pull-right field100"><s:message code="board.locate"/></div>
						<div class="listHiddenField pull-right field60"><s:message code="board.attach"/></div>
						<div class="listHiddenField pull-right field60"><s:message code="board.hitCount"/></div>
						<div class="listHiddenField pull-right field100"><s:message code="board.date"/></div>
						<div class="listHiddenField pull-right field100"><s:message code="board.writer"/></div>
						<div class="listTitle"><s:message code="board.title"/></div>
					</div>
					<c:forEach var="listview" items="${noticelist}" varStatus="status">
						<c:set var="listitem" value="${listview}" scope="request" />	
						<c:set var="listitemNo" value="" />	
						<jsp:include page="BoardListAllSub.jsp" >
							<jsp:param name="listitemNo" value="${listitemNo}" />
							<jsp:param name="listitem" value="${listitem}" />
						</jsp:include>
					</c:forEach>					
					<c:if test="${listview.size()==0}">
						<div class="listBody height200">
						</div>
					</c:if>					
					<c:forEach var="listview" items="${listview}" varStatus="status">
						<c:set var="listitem" value="${listview}" scope="request" />	
						<c:set var="listitemNo" value="${searchVO.totRow-((searchVO.page-1)*searchVO.displayRowCount + status.index)}" scope="request" />	
						<jsp:include page="BoardListAllSub.jsp" >
							<jsp:param name="listitemNo" value="${listitemNo}" />
							<jsp:param name="listitem" value="${listitem}" />
						</jsp:include>
					</c:forEach>	
					<br/>
					<form role="form" id="form1" name="form1"  method="post">
					    <jsp:include page="../common/pagingforSubmit.jsp" />
				    
						<div class="form-group">
							<div class="checkbox col-lg-3 pull-left">
							 	<label class="pull-right">
		                        	<input type="checkbox" name="searchType" value="brdmemo" <c:if test="${fn:indexOf(searchVO.searchType, 'brdmemo')!=-1}">checked="checked"</c:if>/>
		                        	<s:message code="board.contents"/>
		                        </label>
							 	<label class="pull-right">
		                        	<input type="checkbox" name="searchType" value="brdtitle" <c:if test="${fn:indexOf(searchVO.searchType, 'brdtitle')!=-1}">checked="checked"</c:if>/>
		                        	<s:message code="board.title"/>
		                        </label>
							 	<label class="pull-right">
							 		<input type="checkbox" name="searchType" value="usernm" <c:if test="${fn:indexOf(searchVO.searchType, 'usernm')!=-1}">checked="checked"</c:if>/>
		                        	<s:message code="board.writer"/>
		                        </label>
		                   </div>
		                   <div class="input-group custom-search-form col-lg-3">
	                                <input class="form-control" placeholder="Search..." type="text" name="searchKeyword" 
	                                	   value='<c:out value="${searchVO.searchKeyword}"/>' >
	                                <span class="input-group-btn">
	                                <button class="btn btn-default" onclick="fn_formSubmit()">
	                                    <em class="fa fa-search"></em>
	                                </button>
	                            </span>
	                       </div>
						</div>
					</form>	
            	</div>    
            </div>            
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->
</body>

</html>
