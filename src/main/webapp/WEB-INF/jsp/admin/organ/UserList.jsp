<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

					 <table class="table table-striped table-bordered table-hover">
						 <caption>This table displays user information</caption>
						<colgroup>
							<col width='10%' />
							<col width='25%' />
							<col width='25%' />
							<col width='20%' />
							<col width='5%' />
						</colgroup>
						<thead>
							<tr>
								<th scope="col"><s:message code="board.no"/></th>
								<th scope="col"><s:message code="common.id"/></th>
								<th scope="col"><s:message code="common.name"/></th>
								<th scope="col"><s:message code="common.role"/></th>
								<th scope="col"></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="listview" items="${listview}" varStatus="status">	
								<tr>
									<td><c:out value="${status.index+1}"/></td>
									<td><a href="javascript:fn_UserRead(<c:out value="${listview.userno}"/>)"><c:out value="${listview.userid}"/></a></td>
									<td><a href="javascript:fn_UserRead(<c:out value="${listview.userno}"/>)"><c:out value="${listview.usernm}"/></a></td>
									<td><c:out value="${listview.userrole}"/></td>
									<td><a href="javascript:fn_UserDelete(<c:out value="${listview.userno}"/>)"><em class="fa fa-times fa-fw"></em></a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
