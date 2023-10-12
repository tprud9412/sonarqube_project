<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <a class="navbar-brand" href="index"><img src="/images/logo.png" alt="logo" style="max-width:200px" ></a>
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>

	            <ul class="nav navbar-top-links navbar-right">
	                <!-- /.dropdown -->
                    <c:if test="${alertcount>0}">
		                <li class="dropdown">
		                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" onclick="showAlertList()">
		                        <em class="fa fa-bell fa-fw"></em>  <i class="fa fa-caret-down"></i>
		                        	<div class="msgAlert"><c:out value="${alertcount}"/></div>
		                    </a>
		                    <script>
		                    	function showAlertList(){
		                    		$.ajax({
		                    			url: "alertList4Ajax", 
		                    			dataType: "html",
		                    			type:"post", 
		                    			success: function(result){
		                    				if (result!=="") {
		                    					$("#alertlist").html(result);
		                    				}
		                    			}
		                    		})		                    		
		                    	}
		                    </script>
		                    <ul id="alertlist" class="dropdown-menu dropdown-alerts">
		                    </ul>
		                    <!-- /.dropdown-alerts -->
		                </li>
                    </c:if>
	                <!-- /.dropdown -->
	                <li class="dropdown">
	                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
	                        <em class="fa fa-user fa-fw"></em>  <em class="fa fa-caret-down"></em>
	                    </a>
	                    <ul class="dropdown-menu dropdown-user">
	                        <li><a href="memberForm"><em class="fa fa-user fa-fw"></em> <c:out value="${sessionScope.usernm}"/></a></li>
	                        <li><a href="searchMember"><em class="fa fa-users fa-fw"></em> <s:message code="memu.users"/></a></li>
	                        <li class="divider"></li>
	                        <li><a href="memberLogout"><em class="fa fa-sign-out fa-fw"></em> Logout</a>
	                        </li>
	                    </ul>
	                    <!-- /.dropdown-user -->
	                </li>
	                <!-- /.dropdown -->
	            </ul>
	            <!-- /.navbar-top-links -->
            </div>
            <!-- /.navbar-header -->

            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        <li class="sidebar-search">
                           	<form id="searchForm" name="searchForm"  method="post" action="boardList">
                                <input type="hidden" name="searchType" value="brdtitle,brdmemo">
								<div class="input-group custom-search-form">
	                                <input class="form-control" type="text" name="globalKeyword" id="globalKeyword" placeholder="Search...">
	                                <span class="input-group-btn">
	                                    <button class="btn btn-default" type="button" onclick="fn_search()">
	                                        <em class="fa fa-search"></em>
	                                    </button>
	                                </span>
	                            </div>                           	
                            </form>
	                                <script>
	                                	function fn_search(){
	                                		if ($("#globalKeyword").val()!=="") {
		                                		$("#searchForm").submit();
	                                		}
	                                	}
	                                </script>                            <!-- /input-group -->
                        </li>
                        <li>
                            <a href="boardList"><em class="fa fa-files-o fa-fw"></em> <s:message code="board.boardName"/></a>
                        </li>
                        <li>
                            <a href="#"><em class="fa fa-edit fa-fw"></em> 전자결재<span class="fa arrow"></span></a>
							<ul class="nav nav-second-level">
                                <li>
                                    <a href="signDocTypeList">기안하기</a>
                                </li>
                                <li>
		                            <a href="signListTobe">결재 받을(은) 문서 </a>
                                </li>
                                <li>
		                            <a href="signListTo">결재 할(한) 문서</a>
                                </li>
	                        </ul>                             
                        </li>                        
                        <li>
                            <a href="schList"><em class="fa fa-calendar fa-fw"></em> 일정관리</a>
                        </li>                        
                        <li>
                            <a href="#"><em class="fa fa-envelope-o fa-fw"></em> 메일<span class="fa arrow"></span></a>
							<ul class="nav nav-second-level">
                                <li>
                                    <a href="mailForm">새메일</a> 
                                </li>
                                <li>
		                            <a href="receiveMails">받은 메일</a>
                                </li>
                                <li>
		                            <a href="sendMails">보낸 메일</a>
                                </li>
	                        </ul>                             
                        </li>                        
                       
                        <c:if test='${sessionScope.userrole == "A"}'>
	                        <li>
	                            <a href="#"> <s:message code="memu.admin"/></a>
	                        </li>
	                        <li>
	                            <a href="adBoardGroupList"><em class="fa fa-files-o fa-fw"></em> <s:message code="memu.board"/></a>
	                        </li>
	                        <li>
	                            <a href="#"><em class="fa fa-sitemap fa-fw"></em> <s:message code="memu.organ"/><span class="fa arrow"></span></a>
	                            <ul class="nav nav-second-level">
	                                <li>
	                                    <a href="adDepartment"><s:message code="memu.dept"/></a>
	                                </li>
	                                <li>
	                                    <a href="adUser"><s:message code="memu.user"/></a>
	                                </li>
	                            </ul>
	                        </li>
                             <li>
                                 <a href="adSignDocTypeList"><em class="fa fa-edit fa-fw"></em> 결재문서양식</a>
                             </li>
	                	</c:if>	        
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>

