package com.herejava.book.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.herejava.book.service.BookService;
import com.herejava.book.vo.Book;
import com.herejava.room.service.RoomService;
import com.herejava.room.vo.Room;

/**
 * Servlet implementation class BookSelectServlet
 */
@WebServlet(name = "BookSelect", urlPatterns = { "/bookSelect.do" })
public class BookSelectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookSelectServlet() {
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
		Long bookNo = Long.parseLong(request.getParameter("bookNo"));
		String bookName = request.getParameter("bookName");
		/*
		System.out.println(bookNo);
		System.out.println(bookName);
		*/
		//3.비즈니스로직
		BookService service = new BookService();
		Book b = service.selectOneBook(bookNo, bookName);
		RoomService rService = new RoomService();
		Room r = rService.selectOneRoom(b.getRoomNo());
		//System.out.println(b.getBookName());
		//4.결과처리
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/views/book/bookSelect.jsp");
		request.setAttribute("b", b);
		request.setAttribute("roomName", r.getRoomName());
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
