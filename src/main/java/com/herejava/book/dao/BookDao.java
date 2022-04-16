package com.herejava.book.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.herejava.book.vo.Book;
import com.herejava.book.vo.BookData;
import com.herejava.book.vo.BookPay;
import com.herejava.book.vo.BookPayData;
import com.herejava.member.vo.Member;

import common.JDBCTemplate;

public class BookDao {

	// 예약번호로 예약 1개 가져오는 메소드
	public Book selectOneBook(Connection conn, long bookNo) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Book b = null;
		String query = "select * from book where book_no=?";

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, bookNo);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				b = new Book();
				b.setBookNo(rset.getLong("book_no"));
				b.setRoomNo(rset.getInt("room_no"));
				b.setMemberNo(rset.getInt("member_no"));
				b.setBookPeople(rset.getInt("book_people"));
				b.setBookName(rset.getString("book_name"));
				b.setBookPhone(rset.getString("book_phone"));
				b.setBookDay(rset.getString("book_day"));
				b.setBookState(rset.getInt("book_state"));
				b.setCheckIn(rset.getString("check_in"));
				b.setCheckOut(rset.getString("check_out"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return b;
	}

	public int searchBookCount(Connection conn, int roomNo, String dayToStr) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int result = 0;
		String query = "select count(*) from book where room_no = ? and check_in <= ? and check_out > ?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, roomNo);
			pstmt.setString(2, dayToStr);
			pstmt.setString(3, dayToStr);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				result = rset.getInt("count(*)");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	// 멤버번호&요청페이지로 예약리스트 + 페이지번호 가져오는 메소드 (최신 예약 날짜 순으로 정렬) -관리자
	public ArrayList<BookData> selectBookList(Connection conn, int memberNo, int start, int end, int chk) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<BookData> list = new ArrayList<BookData>();
		String query ="";
		switch(chk) {
			case 0:
				query = "select * from (select rownum rnum, b.* from (select * from book join room using(room_no) order by check_in)b where member_no=? and book_state=0 order by book_day desc) where rnum between ? and ?";
				break;
			case 1:
				query = "select * from (select rownum rnum, b.* from (select * from book join room using(room_no) order by check_in)b where member_no=? and book_state=1 order by book_day desc) where rnum between ? and ?";
				break;
			case 2: 
				query = "select * from (select rownum rnum, b.* from (select * from book join room using(room_no) order by check_in)b where member_no=? and book_state=2 order by book_day desc) where rnum between ? and ?";
				break;
			case 3:
				query = "select * from (select rownum rnum, b.* from (select * from book join room using(room_no) order by check_in)b where member_no=? order by book_day desc) where rnum between ? and ?";
				break;
		}
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, memberNo);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				BookData bd = new BookData();
				bd.setBookNo(rset.getLong("book_no"));
				bd.setFilePath(rset.getString("filepath"));
				bd.setRoomName(rset.getString("room_name"));
				bd.setCheckIn(rset.getString("check_in"));
				bd.setCheckOut(rset.getString("check_out"));
				bd.setBookState(rset.getInt("book_state"));
				bd.setBookPeople(rset.getInt("book_people"));
				bd.setBookName(rset.getString("book_name"));
				bd.setBookPhone(rset.getString("book_phone"));
				bd.setBookDay(rset.getString("book_day"));
				list.add(bd);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}

	// 멤버번호로 예약리스트 가져오는 메소드(최신순 정렬)
	public ArrayList<BookData> selectAllBook(Connection conn, int memberNo) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<BookData> list = new ArrayList<BookData>();
		String query = "SELECT * FROM \r\n"
				+ "(SELECT ROWNUM AS RNUM,N.* \r\n"
				+ "FROM \r\n"
				+ "(SELECT B.BOOK_NO, R.FILEPATH, R.ROOM_NAME, B.CHECK_IN, B.CHECK_OUT, B.BOOK_STATE, B.BOOK_PEOPLE, B.BOOK_NAME, B.BOOK_PHONE, NVL(V.REVIEW_NO,0) AS REVIEW_NO \r\n"
				+ "FROM BOOK B\r\n"
				+ "INNER JOIN ROOM R\r\n"
				+ "ON B.ROOM_NO = R.ROOM_NO \r\n"
				+ "LEFT JOIN REVIEW V\r\n"
				+ "ON B.BOOK_NO = V.BOOK_NO\r\n"
				+ "WHERE B.MEMBER_NO=?\r\n"
				+ "ORDER BY B.BOOK_NO DESC)N)";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, memberNo);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				BookData bd = new BookData();
				bd.setBookNo(rset.getLong("book_no"));
				bd.setFilePath(rset.getString("filepath"));
				bd.setRoomName(rset.getString("room_name"));
				bd.setCheckIn(rset.getString("check_in"));
				bd.setCheckOut(rset.getString("check_out"));
				bd.setBookState(rset.getInt("book_state"));
				bd.setBookPeople(rset.getInt("book_people"));
				bd.setBookName(rset.getString("book_name"));
				bd.setBookPhone(rset.getString("book_phone"));
				bd.setReviewNo(rset.getInt("review_no"));
				list.add(bd);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}
	
	
	// 회원한명 전체예약갯수 세는 dao 메소드
	public int totalBookCount(Connection conn, int memberNo) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int result = 0;
		String query = "select count(*) as cnt from book where member_no = ?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, memberNo);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				result = rset.getInt("cnt");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	// 회원한명 전체예약갯수 세는 dao 메소드 - 관리자
	public int totalBookCount(Connection conn, int memberNo, int chk) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int result = 0;
		String query ="";
		switch(chk) {
			case 0:
				query = "select count(*) as cnt from book where member_no = ? and book_state=0";
				break;
			case 1:
				query = "select count(*) as cnt from book where member_no = ? and book_state=1";
				break;
			case 2: 
				query = "select count(*) as cnt from book where member_no = ? and book_state=2";
				break;
			case 3:
				query = "select count(*) as cnt from book where member_no = ?";
				break;
		}
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, memberNo);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				result = rset.getInt("cnt");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return result;
	}	
	// 전체 예약갯수 세는 dao 메소드
	public int totalBookCount(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int result = 0;
		String query = "select count(*) as cnt from book";
		try {
			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				result = rset.getInt("cnt");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	// 회원번호로 예약 1개 가져오는 메소드
	public Book selectOneBook(Connection conn, int memberNo) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Book b = null;
		String query = "select * from book where member_no=?";

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, memberNo);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				b = new Book();
				b.setBookNo(rset.getLong("book_no"));
				b.setRoomNo(rset.getInt("room_no"));
				b.setMemberNo(rset.getInt("member_no"));
				b.setBookPeople(rset.getInt("book_people"));
				b.setBookName(rset.getString("book_name"));
				b.setBookPhone(rset.getString("book_phone"));
				b.setBookDay(rset.getString("book_day"));
				b.setBookState(rset.getInt("book_state"));
				b.setCheckIn(rset.getString("check_in"));
				b.setCheckOut(rset.getString("check_out"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return b;
	}

	// 예약리스트 최신순으로 전체 가져오는 메소드
	public ArrayList<Book> selectAllBook(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Book> list = new ArrayList<Book>();
		String query = "select rownum as rnum,n.*from(select * from book order by book_no desc)n";
		try {
			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				Book b = new Book();
				b.setBookNo(rset.getLong("book_no"));
				b.setRoomNo(rset.getInt("room_no"));
				b.setMemberNo(rset.getInt("member_no"));
				b.setBookPeople(rset.getInt("book_people"));
				b.setBookName(rset.getString("book_name"));
				b.setBookPhone(rset.getString("book_phone"));
				b.setBookDay(rset.getString("book_day"));
				b.setBookState(rset.getInt("book_state"));
				b.setCheckIn(rset.getString("check_in"));
				b.setCheckOut(rset.getString("check_out"));
				list.add(b);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}

	// 예약번호로 예약취소(update)하는 메소드
	public int updateBook(Connection conn, long bookNo) {
		PreparedStatement pstmt = null;
		int result = 0;
		String query = "update Book set book_state = 2 where book_no=?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, bookNo);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	// 예약번호로 예약(방사진/방이름/체크인/체크아웃/예약상태/이용자숫자/예약자명/예약자전화번호) 1개 가져오는 메소드
	public BookData getBook(Connection conn, long bookNo) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		BookData bd = null;
		String query = "SELECT BOOK_NO, FILEPATH, ROOM_NAME, CHECK_IN, CHECK_OUT, BOOK_STATE, BOOK_PEOPLE, BOOK_NAME, BOOK_PHONE FROM BOOK JOIN ROOM USING(ROOM_NO) WHERE BOOK_NO=?";

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, bookNo);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				bd = new BookData();
				bd.setBookNo(rset.getLong("book_no"));
				bd.setFilePath(rset.getString("filepath"));
				bd.setRoomName(rset.getString("room_name"));
				bd.setCheckIn(rset.getString("check_in"));
				bd.setCheckOut(rset.getString("check_out"));
				bd.setBookState(rset.getInt("book_state"));
				bd.setBookPeople(rset.getInt("book_people"));
				bd.setBookName(rset.getString("book_name"));
				bd.setBookPhone(rset.getString("book_phone"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return bd;
	}

	public ArrayList<Book> selectAllBook1(Connection conn, int start, int end) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Book> list = new ArrayList<Book>();
		String query = "select * from (select * from (select rownum as rnum,n. * from (select * from book order by book_no desc) n) where rnum between ? and ?)";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				Book b = new Book();
				b.setBookNo(rset.getLong("book_no"));
				b.setRoomNo(rset.getInt("room_no"));
				b.setMemberNo(rset.getInt("member_no"));
				b.setBookPeople(rset.getInt("book_people"));
				b.setBookName(rset.getString("book_name"));
				b.setBookPhone(rset.getString("book_phone"));
				b.setBookDay(rset.getString("book_day"));
				b.setBookState(rset.getInt("book_state"));
				b.setCheckIn(rset.getString("check_in"));
				b.setCheckOut(rset.getString("check_out"));
				list.add(b);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
			JDBCTemplate.close(rset);
		}
		
		return list;
	}
	
	//현재날짜 기준 예약상태(숙박완료로) 최신화 시켜주는 메소드
	public int updateBookState(Connection conn) {
		PreparedStatement pstmt = null;
		int result = 0;
		String query ="update book set book_state = 1 where book_state = 0 and (to_date(check_in) < SYSDATE)";
		try {
			pstmt = conn.prepareStatement(query);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	
	// 멤버번호&요청페이지로 예약리스트 + 페이지번호 가져오는 메소드 (최신 예약 날짜 순으로 정렬)
	public ArrayList<BookData> selectBookList(Connection conn, int memberNo, int start, int end) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<BookData> list = new ArrayList<BookData>();
		String query ="select * from (select rownum rnum, b.* from (select * from book join room using(room_no) order by check_in)b where member_no=? order by book_day desc) where rnum between ? and ?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, memberNo);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				BookData bd = new BookData();
				bd.setBookNo(rset.getLong("book_no"));
				bd.setFilePath(rset.getString("filepath"));
				bd.setRoomName(rset.getString("room_name"));
				bd.setCheckIn(rset.getString("check_in"));
				bd.setCheckOut(rset.getString("check_out"));
				bd.setBookState(rset.getInt("book_state"));
				bd.setBookPeople(rset.getInt("book_people"));
				bd.setBookName(rset.getString("book_name"));
				bd.setBookPhone(rset.getString("book_phone"));
				bd.setReviewNo(rset.getInt("review_no"));
				list.add(bd);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}
	
	// 예약번호로 리뷰갯수 리턴하는 메소드
	public int getReview(Connection conn, long bookNo) {
		//리뷰 존재여부 리턴받음
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int result = 0;
		String query = "select count(*) from review where book_no= ?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, bookNo);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				result = rset.getInt("count(*)");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	// 멤버번호&요청페이지로 예약리스트 + 페이지번호 가져오는 메소드 (최신 예약 날짜 순으로 정렬) - 이신영(reviewNo 추가)
	public ArrayList<BookData> selectBookList2(Connection conn, int memberNo, int start, int end) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<BookData> list = new ArrayList<BookData>();
		String query = "SELECT * FROM \r\n"
				+ "(SELECT ROWNUM AS RNUM,N.* \r\n"
				+ "FROM \r\n"
				+ "(SELECT B.BOOK_NO, R.FILEPATH, R.ROOM_NAME, B.CHECK_IN, B.CHECK_OUT, B.BOOK_STATE, B.BOOK_PEOPLE, B.BOOK_NAME, B.BOOK_PHONE, NVL(V.REVIEW_NO,0) AS REVIEW_NO \r\n"
				+ "FROM BOOK B\r\n"
				+ "INNER JOIN ROOM R\r\n"
				+ "ON B.ROOM_NO = R.ROOM_NO \r\n"
				+ "LEFT JOIN REVIEW V\r\n"
				+ "ON B.BOOK_NO = V.BOOK_NO\r\n"
				+ "WHERE B.MEMBER_NO=?\r\n"
				+ "ORDER BY B.BOOK_NO DESC)N) where rnum between ? and ?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, memberNo);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				BookData bd = new BookData();
				bd.setBookNo(rset.getLong("book_no"));
				bd.setFilePath(rset.getString("filepath"));
				bd.setRoomName(rset.getString("room_name"));
				bd.setCheckIn(rset.getString("check_in"));
				bd.setCheckOut(rset.getString("check_out"));
				bd.setBookState(rset.getInt("book_state"));
				bd.setBookPeople(rset.getInt("book_people"));
				bd.setBookName(rset.getString("book_name"));
				bd.setBookPhone(rset.getString("book_phone"));
				bd.setReviewNo(rset.getInt("review_no"));
				list.add(bd);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}

	public int insertBook(Connection conn, BookPay bpay) {
		PreparedStatement pstmt = null;
		int result = 0;
		String query ="INSERT INTO BOOK VALUES(BOOK_SEQ.NEXTVAL,?,?,?,?,?, TO_CHAR(SYSDATE,'YYYY-MM-DD'),0,?,?)";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, bpay.getRoomNo());
			pstmt.setInt(2, bpay.getMemberNo());
			pstmt.setInt(3, bpay.getBookPeople());
			pstmt.setString(4, bpay.getBookName());
			pstmt.setString(5, bpay.getBookPhone());
			pstmt.setString(6, bpay.getCheckIn());
			pstmt.setString(7, bpay.getCheckOut());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public int insertPay(Connection conn, BookPay bpay, BookPayData bpd) {
		PreparedStatement pstmt = null;
		int result = 0;
		String query ="insert into pay VALUES(pay_SEQ.NEXTVAL,?,?,?,0,?,?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, bpd.getBookNo());
			pstmt.setInt(2, bpay.getRoomNo());
			pstmt.setString(3, bpay.getRoomName());
			pstmt.setString(4, bpd.getBookDay());
			pstmt.setInt(5, bpay.getPayAmount());
			pstmt.setInt(6, bpay.getMinusPoint());
			pstmt.setInt(7, bpay.getPayStayDay());
			pstmt.setInt(8, bpay.getMemberNo());
			pstmt.setInt(9, bpay.getPayRoomPrice());
			
			
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public BookPayData searchBookNo(Connection conn, BookPay bpay) {
		//결제후 예약번호(시퀀스)와 예약날짜 데이터 받아오기
				PreparedStatement pstmt = null;
				ResultSet rset = null;
				BookPayData bpd = new BookPayData();
				String query = "select book_no, book_day from book where room_no=? and member_no =? and book_people = ? and book_name = ? and book_phone = ? and check_in = ? and check_out=?  ;";
				try {
					pstmt = conn.prepareStatement(query);
					pstmt.setInt(1, bpay.getRoomNo());
					pstmt.setInt(2, bpay.getMemberNo());
					pstmt.setInt(3, bpay.getBookPeople());
					pstmt.setString(4, bpay.getBookName());
					pstmt.setString(5, bpay.getBookPhone());
					pstmt.setString(6, bpay.getCheckIn());
					pstmt.setString(7, bpay.getCheckOut());
					rset = pstmt.executeQuery();
					if (rset.next()) {
						bpd.setBookNo(rset.getLong("bookNo"));
						bpd.setBookDay(rset.getString("bookDay"));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					JDBCTemplate.close(rset);
					JDBCTemplate.close(pstmt);
				}
				return bpd;
	}

}
