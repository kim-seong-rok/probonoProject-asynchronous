package probono.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import probono.model.ProbonoService;
import probono.model.dto.ActivistDTO;
import probono.model.dto.RecipientDTO;

@WebServlet("/probono")
public class ProbonoFrontController extends HttpServlet {
	
	private static ProbonoService probonoService = ProbonoService.getInstance();
	
	//command를 통해 실행로직과 service로직 분리
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String command = request.getParameter("command");
		
		try{
			if(command.equals("probonoProjectAll")){//모든 probono project 정보 검색
				probonoProjectAll(request, response);
			}else if(command.equals("activistAll")){//모든 재능 기부자 검색
				activistAll(request, response);
			}else if(command.equals("activist")){//특정 재능 기부자 정보 검색
				activist(request, response);
			}else if(command.equals("activistInsert")){//재능 기부자 추가 등록
				activistInsert(request, response);
			}else if(command.equals("activistUpdateReq")){//재능 기부자 정보 수정요청
				activistUpdateReq(request, response);
			}else if(command.equals("activistUpdate")){//재능 기부자 정보 수정
				activistUpdate(request, response);
			}else if(command.equals("activistDelete")){//재능 기부자 탈퇴[삭제]
				activistDelete(request, response);
			}else if(command.equals("recipientAll")){//모든 재능 수혜자 검색
				recipientAll(request, response);
			}else if(command.equals("recipient")){//특정 재능 수혜자 정보 검색
				recipient(request, response);
			}else if(command.equals("recipientInsert")){//재능 수혜자 추가 등록
				recipientInsert(request, response);
			}else if(command.equals("recipientUpdateReq")){//재능 수혜자 정보 수정요청
				recipientUpdateReq(request, response);
			}else if(command.equals("recipientUpdate")){//재능 수혜 정보 수정
				recipientUpdate(request, response);
			}else if(command.equals("recipientDelete")){//재능 수혜자 탈퇴[삭제]
				recipientDelete(request, response);
			}
		}catch(Exception s){
			request.setAttribute("errorMsg", s.getMessage());
			request.getRequestDispatcher("showError.jsp").forward(request, response);
			s.printStackTrace();
		}
	}
	

	//모두 ProbonoProject 검색 메소드
	public void probonoProjectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "showError.jsp";
		try {
			request.setAttribute("probonoProjectAll", probonoService.getAllProbonoProjects());
			url = "probonoProjectList.jsp";
		}catch(Exception s){
			request.setAttribute("errorMsg", s.getMessage());
			s.printStackTrace();
		}
		request.getRequestDispatcher(url).forward(request, response);
	}
	
	//모든 재능 기부자 검색 - 검색된 데이터 출력 화면[activistList.jsp]
	public void activistAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "showError.jsp";
		try {
			request.setAttribute("activistAll", probonoService.getAllActivists());
			url = "activist/activistList.jsp";
		}catch(Exception s){
			request.setAttribute("errorMsg", s.getMessage());
			s.printStackTrace();
		}
		request.getRequestDispatcher(url).forward(request, response);
	}
	
	//재능 기부자 검색 
	public void activist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "showError.jsp";
		try {
			ActivistDTO a = probonoService.getActivist(request.getParameter("activistId"));
			if(a != null) {
				request.setAttribute("activist", a);
				url = "activist/activistDetail.jsp";
			}else {
				request.setAttribute("errorMsg", "존재하지 않는 기부자입니다.");
			}
		}catch(Exception s){
			request.setAttribute("errorMsg", s.getMessage());
			s.printStackTrace();
		}
		request.getRequestDispatcher(url).forward(request, response);
	}
	

	//재능 기부자 가입 메소드
	protected void activistInsert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "showError.jsp";
		
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String pw = request.getParameter("pw");
		String major = request.getParameter("major");
		
		//해킹등으로 불합리하게 요청도 될수 있다는 가정하에 모든 데이터가 제대로 전송이 되었는지를 검증하는 로직
		if(id != null && id.length() !=0 && name != null) {
		
			ActivistDTO activist = new ActivistDTO(id, name, pw, major);
			try{
				boolean result = probonoService.addActivist(activist);
				if(result){
					request.setAttribute("activist", activist);
					request.setAttribute("successMsg", "가입 완료");
					url = "activist/activistDetail.jsp";
				}else{
					request.setAttribute("errorMsg", "다시 시도하세요");
				}
			}catch(Exception s){
				request.setAttribute("errorMsg", s.getMessage());
			}
			request.getRequestDispatcher(url).forward(request, response);
		}
	}
	
	//재능 기부자 수정 요구
	public void activistUpdateReq(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "showError.jsp";
		try {
			request.setAttribute("activist", probonoService.getActivist(request.getParameter("activistId")));
			url = "activist/activistUpdate.jsp";
		}catch(Exception s){
			request.setAttribute("errorMsg", s.getMessage());
			s.printStackTrace();
		}
		request.getRequestDispatcher(url).forward(request, response);
	}

	//???
	//재능 기부자 수정 - 상세정보 확인 jsp[activistDetail.jsp]
	public void activistUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "showError.jsp";
		try {
			if(probonoService.updateActivist(request.getParameter("activistId"), request.getParameter("major"))) {
				request.setAttribute("activist", probonoService.getActivist(request.getParameter("activistId")));
				url = "activist/activistDetail.jsp";
			}else {
				request.setAttribute("errorMsg", "저장 실패");
			}
		}catch(Exception s){
			request.setAttribute("errorMsg", s.getMessage());
			s.printStackTrace();
		}
		request.getRequestDispatcher(url).forward(request, response);
	}
	
	//???
	//재능 기부자 삭제
	public void activistDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "showError.jsp";
		try {
			if(probonoService.deleteActivist(request.getParameter("activistId"))) {
				request.setAttribute("activistAll", probonoService.getAllActivists());
				url = "activist/activistList.jsp";
			}else {
				request.setAttribute("errorMsg", "저장 실패");
			}
			activistAll(request, response);
		}catch(Exception s){
			request.setAttribute("errorMsg", s.getMessage());
			s.printStackTrace();
		}
		request.getRequestDispatcher(url).forward(request, response);
	}

	//모든 재능 수혜자 검색 - 검색된 데이터 출력 화면[activistList.jsp]
		public void recipientAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String url = "showError.jsp";
			try {
				request.setAttribute("recipientAll", probonoService.getAllRecipients());
				url = "recipient/recipientList.jsp";
			}catch(Exception s){
				request.setAttribute("errorMsg", s.getMessage());
				s.printStackTrace();
			}
			request.getRequestDispatcher(url).forward(request, response);
		}
		
		//재능 수혜자 검색 
		public void recipient(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String url = "showError.jsp";
			try {
				RecipientDTO r = probonoService.getRecipient(request.getParameter("recipientId"));
				if(r != null) {
					request.setAttribute("recipient", r);
					url = "recipient/recipientDetail.jsp";
				}else {
					request.setAttribute("errorMsg", "존재하지 않는 수혜자입니다.");
				}
			}catch(Exception s){
				request.setAttribute("errorMsg", s.getMessage());
				s.printStackTrace();
			}
			request.getRequestDispatcher(url).forward(request, response);
		}
		

		//재능 수혜자 가입 메소드
		protected void recipientInsert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String url = "showError.jsp";
			
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String pw = request.getParameter("pw");
			String receiveHopeContent = request.getParameter("receiveHopeContent");
			
			//해킹등으로 불합리하게 요청도 될수 있다는 가정하에 모든 데이터가 제대로 전송이 되었는지를 검증하는 로직
			if(id != null && id.length() !=0 && name != null) {
			
			RecipientDTO recipient = new RecipientDTO(id, name, pw, receiveHopeContent);
				try{
					boolean result = probonoService.addRecipient(recipient);
					if(result){
						request.setAttribute("recipient", recipient);
						request.setAttribute("successMsg", "가입 완료");
						url = "recipient/recipientDetail.jsp";
					}else{
						request.setAttribute("errorMsg", "다시 시도하세요");
					}
				}catch(Exception s){
					request.setAttribute("errorMsg", s.getMessage());
				}
				request.getRequestDispatcher(url).forward(request, response);
			}
		}
		
		//재능 수혜자 수정 요구
		public void recipientUpdateReq(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String url = "showError.jsp";
			try {
				request.setAttribute("recipient", probonoService.getRecipient(request.getParameter("recipientId")));
				url = "recipient/recipientUpdate.jsp";
			}catch(Exception s){
				request.setAttribute("errorMsg", s.getMessage());
				s.printStackTrace();
			}
			request.getRequestDispatcher(url).forward(request, response);
		}

		//???
		//재능 수혜자 수정 - 상세정보 확인 jsp[activistDetail.jsp]
		public void recipientUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String url = "showError.jsp";
			try {
				boolean result = probonoService.updateRecipient(request.getParameter("recipientId"), request.getParameter("receiveHopeContent"));
				if(result) {
					request.setAttribute("recipient", probonoService.getRecipient(request.getParameter("recipientId")));
					url = "recipient/recipientDetail.jsp";
				}else {
					request.setAttribute("errorMsg", "수정 실패");
				}
			}catch(Exception s){
				request.setAttribute("errorMsg", s.getMessage());
				s.printStackTrace();
			}
			request.getRequestDispatcher(url).forward(request, response);
		}
		
		//???
		//재능 수혜자 삭제
		public void recipientDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String url = "showError.jsp";
			try {
				boolean result = probonoService.deleteRecipient(request.getParameter("recipientId"));
				if(result) {
					request.setAttribute("recipientAll", probonoService.getAllRecipients());
					url = "recipient/recipientList.jsp";
				}else {
					request.setAttribute("errorMsg", "삭제 실패");
				}
			}catch(Exception s){
				request.setAttribute("errorMsg", s.getMessage());
				s.printStackTrace();
			}
			request.getRequestDispatcher(url).forward(request, response);
		}
}
