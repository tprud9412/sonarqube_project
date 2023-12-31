<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<ul>
    <c:forEach var="listview" items="${listview}" varStatus="status">
        <li>
            <a href="boardRead?brdno=<c:out value="${listview.brdno}" />">
                <div>
                    <em class="fa fa-<c:out value="${listview.extfield1}" /> fa-fw"></em>
                    <c:out value="${listview.brdtitle}" />
                    <span class="pull-right text-muted small">
                        <c:out value="${listview.brddate}" />
                    </span>
                </div>
            </a>
        </li>
        <li class="divider"></li>
    </c:forEach>
    <li>
        <a class="text-center" href="alertList">
            <strong>See All Alerts</strong>
            <em class="fa fa-angle-right"></em>
        </a>
    </li>
</ul>