package com.herejava.room.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.herejava.room.dao.RoomDao;
import com.herejava.room.vo.Room;

import common.JDBCTemplate;

public class RoomService {
	
	public ArrayList<Room> selectAllRoom() {
		Connection conn = JDBCTemplate.getConnection();
		RoomDao dao = new RoomDao();
		ArrayList<Room> list = dao.selectAllRoom(conn);
		JDBCTemplate.close(conn);
		
		return list;
	}
	
	//가격변경 메소드
	public int priceChange(int roomPrice,String roomNo) {
		Connection conn = JDBCTemplate.getConnection();
		RoomDao dao = new RoomDao();
		int result = dao.priceChange(conn,roomPrice,roomNo);
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		return result;
	}

}
