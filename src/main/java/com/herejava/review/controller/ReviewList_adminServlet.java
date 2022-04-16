package com.herejava.review.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.herejava.review.service.ReviewService;
import com.herejava.review.vo.ReviewListAdmin;

/**
 * Servlet implementation class ReviewList_adminServlet
 */
@WebServlet(name = "ReviewList_admin", urlPatterns = { "/reviewList_admin.do" })
public class ReviewList_adminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReviewList_adminServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		ReviewService service = new ReviewService();
		ArrayList<ReviewListAdmin> reviewList = service.getAllReview();
		//4.결과처리
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/views/admin/admin_review.jsp");
		request.setAttribute("reviewList", reviewList);
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