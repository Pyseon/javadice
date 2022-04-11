package com.herejava.notice.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.herejava.notice.controller.NoticeViewData;
import com.herejava.notice.dao.NoticeDao;
import com.herejava.notice.vo.Notice;
import com.herejava.notice.vo.NoticePageData;

import common.JDBCTemplate;



public class NoticeService {

	public NoticePageData selecetNoticeList(int reqPage) {
			Connection conn = JDBCTemplate.getConnection();
			NoticeDao dao = new NoticeDao();
			
			//1. 결정사항 : 한 페이지당 게시물 수
			int numPerPage = 10;
			/*
			 reqPage=1 				- > 1 and 10
			 reqPage=2 				- > 11 and 20
			 reqPage=3 				- > 21 and 30
			 게시물 rownum 범위 계산
			 */
			int end = reqPage*numPerPage;
			int start = end - numPerPage + 1;
			ArrayList<Notice> list = dao.selectNoticeList(conn,start,end);
			//페이징처리
			//전체 페이지곗나을 위한 전체 게시물 수 조회
			int totalCount = dao.totalNoticeCount(conn);
			//전체 페이지 수
			int totalPage = 0;
			if(totalCount%numPerPage == 0) {
				totalPage = totalCount/numPerPage;
			}else {
				totalPage = totalCount/numPerPage + 1;
			}
			//페이지 네비게이션의 길이 지정
			int pageNaviSize = 5;
			//페이지 모양 지정
			// 1~5 페이지 요청 시 - > 1 2 3 4 5
			// 6 ~ 10 페이지 요청 시 -> 6 7 8 9 10
			
			//페이지 네비게이션 시작 번호 계산
			int pageNo = ((reqPage-1)/pageNaviSize)*pageNaviSize + 1;
			
			//페이지 네비게이션 제작 시작
			String pageNavi = "<ul class='pagination circle-style'>";
			//이전버튼
			if(pageNo != 1) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/noticeList.do?reqPage="+(pageNo-1)+"'>";
				pageNavi += "<span class='material-icons'>chevron_left</span>";
				pageNavi += "</a></li>";
			}
			//페이지숫자
			for(int i=0;i<pageNaviSize;i++) {
				if(pageNo == reqPage) {
					pageNavi += "<li>";
					pageNavi += "<a class='page-item active-page' href='/noticeList.do?reqPage="+pageNo+"'>";
					pageNavi += pageNo;
					pageNavi += "</a></li>";
				}else {
					pageNavi += "<li>";
					pageNavi += "<a class='page-item' href='/noticeList.do?reqPage="+pageNo+"'>";
					pageNavi += pageNo;
					pageNavi += "</a></li>";
				}
				pageNo++;
				if(pageNo > totalPage) {
					break;
				}
			}
			//다음버튼
			if(pageNo <= totalPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/noticeList.do?reqPage="+pageNo+"'>";
				pageNavi += "<span class='material-icons'>chevron_right</span>";
				pageNavi += "</a></li>";
			}
			pageNavi += "</ul>";
			NoticePageData npd = new NoticePageData(list, pageNavi);
			JDBCTemplate.close(conn);
			return npd;
		}

	public NoticeViewData selecetNoticeView(int noticeNo) {
		// TODO Auto-generated method stub
		return null;
	}
	}
	

