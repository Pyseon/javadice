<%@page import="com.herejava.room.vo.Room"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <% ArrayList<Room> list = (ArrayList<Room>)request.getAttribute("list");%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>객실 관리</title>
<style>
	.page-content{
		float: left;
	}
	.tbl tr>th{
		border-top: 1px solid #ccc;
		background-color: rgb(204,204,204,0.4);
	}
	.tbl-hover{
		margin-top: 20px;
	}
	.tbl-hover th, .tbl-hover td{
		border-bottom: 1px solid #A7A7A7;
	}
	#roomPrice{
		width:100px;
	}
	.mypage-content{
	margin-bottom: 100px;
	}
</style>
</head>
<body>
	<%@include file="/WEB-INF/views/common/header.jsp" %>
	<div class="page-content">
	<div class="flex-wrap">
		<%@include file="/WEB-INF/views/admin/mypage_admin.jsp"%>
			
			<div class="mypage-content">
			<div class="mypage-content-title">객실 목록</div>
			
				<table class="tbl tbl-hover">
					<tr class="tr-3">
						<th>객실 이미지</th><th>객실명</th><th>가격</th><th>방갯수</th><th>기준인원/최대인원</th><th>가격수정</th>
					<%for(Room r : list) {%>
						<tr class="tr-1">
							<form action="priceChange.do">
								<input type="hidden" id="roomNo" name="roomNo" value="<%=r.getRoomNo()%>">
								<td><%=r.getFilepath() %></td>
								<td><%=r.getRoomName()%></td>
								<td><input id="roomPrice" name="roomPrice" type="number" value="<%=r.getRoomPrice()%>" required> 원</td>
								<td><%=r.getRoomCount() %></td>
								<td>기준인원/<%=r.getRoomCapacity()%>인</td>
								<td><button class="btn bc2">수정</button></td>
							</form>
						</tr>
					<%} %>
				</table>
			</div>
	</div>
	</div>
	<%@include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>