<%@page import="java.util.ArrayList"%>
<%@page import="com.herejava.book.vo.BookData"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	ArrayList<BookData> list = (ArrayList<BookData>) request.getAttribute("list");
	String pageNavi = (String) request.getAttribute("pageNavi");
	String memberNick = (String) request.getAttribute("memberNick");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
		.notice-tbl a:hover {
			text-decoration: underline;
		}
		
		.notice-tbl tr {
			border-bottom: 1px solid #ccc;
		}
		
		.notice-tbl tr>th:first-child {
			width: 10%;
		}
		
		.notice-tbl tr>th:nth-child(2) {
			width: 45%;
		}
		
		.notice-tbl tr>td:nth-child(2) {
			text-align: left;
		}
		
		.notice-tbl tr>th:nth-child(3) {
			width: 15%;
		}
		
		.notice-tbl tr>th:nth-child(4) {
			width: 20%;
		}
		
		.notice-tbl tr>th:nth-child(5) {
			width: 10%;
		}
		
		#pageNavi {
			margin: 30px;
		}
		.img-0{
		width: 150px;
		height: 90px;
		}
</style>
</head>
<body>
	<%@ include file="/WEB-INF/views/common/header.jsp"%>
	<div class="page-content">
		<div class="flex-wrap">
			<%@ include file="/WEB-INF/views/admin/mypage_admin.jsp"%>
			<div class="mypage-content">
				<div class="mypage-content-title"><%=memberNick %>님의 예약내역</div>
				<table class="tbl tbl-hover">
					<%
					for (BookData bd : list) {
					%>
					<tr class="tr-1">
						<td><img class="img-0" src="/img/<%=bd.getFilePath()%>"></td>
						<td><%=bd.getRoomName()%></td>
						<td><%=bd.getCheckIn()%> - <%=bd.getCheckOut()%></td>
						<td><%=bd.getBookState()%></td>
					</tr>
					<%
					}
					%>
				</table>
				<div id="pageNavi"><%=pageNavi%></div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
</html>