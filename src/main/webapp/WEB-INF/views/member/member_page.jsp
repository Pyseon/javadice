<%@page import="com.herejava.member.vo.Member"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
.mypage-content{
	min-height: 800px;
}
</style>
<meta charset="UTF-8">
<title>:: 회원 페이지 ::</title>
</head>
<body>
	<%@include file="/WEB-INF/views/common/header.jsp" %>
	<div class="page-content">
		<div class="flex-wrap">
			<%@include file="/WEB-INF/views/common/mypage_member.jsp"%>
			
			<div class="mypage-content">
				<div class="mypage-content-title">회원목록</div>
			</div>

		</div>
		<!-- flex-wrap -->
	</div>
	<!-- page-content -->
</body>
	<%@include file="/WEB-INF/views/common/footer.jsp"%>
</html>