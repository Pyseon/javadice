package com.herejava.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.herejava.member.service.MemberService;
import com.herejava.member.vo.Member;

/**
 * Servlet implementation class MypageServlet
 */
@WebServlet(name = "Mypage", urlPatterns = { "/mypage.do" })
public class MypageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MypageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.인코딩
		request.setCharacterEncoding("utf-8");
		//2.값추출
		String memberId = request.getParameter("memberId");
		//3.비즈니스로직
		MemberService service = new MemberService();
		Member m = service.selecOneMember(memberId);
		System.out.println(m.getMemberId());
		System.out.println(m.getMemberName());
		System.out.println(m.getMemberNick());
		System.out.println(m.getMemberPhone());
		//4.결과처리

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}