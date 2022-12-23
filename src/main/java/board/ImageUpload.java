package board;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


@WebServlet("/board/imageupload")
public class ImageUpload extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String tmpPath = "c:/Temp/upload";
		String callback = request.getParameter("CKEditorFuncNum");
		//System.out.println(callback);
		String error = "";
		String url = null;		//이미지가 받을 주소, 즉 다운로드 주소
		//(다른 언어는 path까지만 주면 되는데 자바는 url을 줘야하는 점이 있음)

		/** 업로드된 파일을 저장할 저장소 */ // 임시파일로 저장되는 곳!
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File(tmpPath)); // 저장할 위치를 File객체로 생성
		factory.setSizeThreshold(1024 * 1024 * 10); // MaxMemorySize 10MB 제한 잡아둠. 필요에따라 조정 가능!

		/** 파일변환 -> 리스트에 담기 */
		ServletFileUpload fu = new ServletFileUpload(factory);
		fu.setSizeMax(1024 * 1024 * 100); // maxRequestSize 전체 파일 용량
		fu.setFileSizeMax(1024 * 1024 * 10); // maxFileSize 파일 한개당 용량

		try {
			List<FileItem> items = fu.parseRequest(request);
			/** 파일 저장 */
			for (FileItem i : items) {
				// 이미지 파일일 때
				if (!i.isFormField() && i.getSize() > 0) {
					String fileName = i.getName();
					String now = LocalDateTime.now().toString().substring(0, 22).replaceAll("[-T:.]", "");
					int idx = fileName.lastIndexOf('.');
					fileName = now + fileName.substring(idx);	//유니크한 파일 이름으로 변경
					String fullPath = tmpPath + "/" + fileName;
					File uploadFile = new File (fullPath);
					i.write(uploadFile); // 임시 파일을 파일로 씀
					
					url = "/bbs/board/download?file=" + fileName;
                    // System.out.println(fileName);
				}
				// 다른 타입 request일 때
				else if (i.isFormField()) {
					//request.setAttribute(i.getFieldName(), i.getString("UTF-8"));
					//System.out.println(i.getFieldName() + ": " + i.getString("UTF-8"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String data = "<script> "
                + "     window.parent.CKEDITOR.tools.callFunction(" 
                +           callback + ", '" + url + "', '" + error + "'); "
                + "</script>";
		System.out.println(data);
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(data);
	}
}