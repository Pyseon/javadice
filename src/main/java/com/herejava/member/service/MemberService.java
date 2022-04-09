package com.herejava.member.service;

import java.sql.Connection;

import com.herejava.member.dao.MemberDao;
import com.herejava.member.vo.Member;

import common.JDBCTemplate;

public class MemberService {

	public Member selectOneMember(String memberId) {
		Connection conn = JDBCTemplate.getConnection();
		MemberDao dao = new MemberDao();
		Member m = dao.selecOneMember(conn, memberId);
		JDBCTemplate.close(conn);
		return m;
	}

	public Member selectOneMember(Member member) {
		Connection conn = JDBCTemplate.getConnection();
		MemberDao dao = new MemberDao();
		Member m = dao.selectOneMember(conn,member);
		JDBCTemplate.close(conn);
		return m;
	}

	
	
	
}//MemberService class
