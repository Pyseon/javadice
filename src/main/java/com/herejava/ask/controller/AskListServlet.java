package com.herejava.ask.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.herejava.ask.service.AskService;
import com.herejava.ask.vo.AskPageData;
import com.herejava.book.vo.Book;
import com.herejava.member.vo.Member;

/**
 * Servlet implementation class AskListServlet
 */
@WebServlet(name = "AskList", urlPatterns = { "/askList.do" })
public class AskListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AskListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		int reqPage= Integer.parseInt(request.getParameter("reqPage"));
		int memberNo = Integer.parseInt(request.getParameter("memberNo"));
		System.out.println("memberNo");
		AskService service = new AskService();
		AskPageData apd = service.selecetAllAsk(reqPage);
		
		
		Member m1 = service.selecetOneNickName(memberNo);
		
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/views/ask/askList.jsp");
		request.setAttribute("list", apd.getList());
		request.setAttribute("pageNavi", apd.getPageNavi());
		request.setAttribute("m1", m1);
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
