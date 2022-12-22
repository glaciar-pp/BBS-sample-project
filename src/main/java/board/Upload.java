package board;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class Upload
 */
@WebServlet("/board/upload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 100 // 100 MB
)
public class Upload extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 업로드 받을 파일경로 정리
		String tmpPath = "c:/Temp/upload"; // configure 파일에서 지정해주는게 제일 정확하긴 함
		//System.out.println("tmpPath: " + tmpPath);
		File file = new File(tmpPath);
		if (!file.exists())
			file.mkdirs();

		/* Receive file uploaded to the Servlet from the HTML5 form */
		request.setCharacterEncoding("utf-8");
		String param = request.getParameter("param");
		System.out.println("param: " + param);

		String fileName = null;
//      Part filePart = request.getPart("file");
//      if (filePart == null) {
//      	System.out.println("No files uploaded.");
//      } else {
//      	fileName = filePart.getSubmittedFileName();
//      	System.out.println("fileName: " + fileName);
//      	
//      	for (Part part : request.getParts()) {
//              part.write(tmpPath + File.separator + fileName);
//          }
//      }

		// 여러개 파일을 보낼 경우
		Part filePart = null;
		List<String> fileList = new ArrayList<>();
		for (int i = 1; i <= 2; i++) {
			filePart = request.getPart("file" + i); // name이 file1, file2
			if (filePart == null)
				continue;
			fileName = filePart.getSubmittedFileName();
			System.out.println("file" + i + ": " + fileName);
			if (fileName == null || fileName.equals(""))
				continue;
			fileList.add(fileName);

//            ByteArrayInputStream bis = (ByteArrayInputStream) filePart.getInputStream();
//            byte[] buffer = bis.readAllBytes();
//            OutputStream fos = new FileOutputStream(tmpPath + File.separator + fileName);
//            fos.write(buffer);
			for (Part part : request.getParts()) {
				part.write(tmpPath + File.separator + fileName);
			}
			response.getWriter().print("The file is uploaded sucessfully.");
			// response.getWriter().print("The file " + fileName + "is uploaded
			// sucessfully."); 한글이 계속 깨져서 뺌
		}
	}
}
/*
 * 초기 설정으로 파일 한개 업로드 진행해본 결과
 * 
 * 웹 페이지는 한글 깨져서 나옴, The file ????.jpgis uploaded sucessfully. 출력 tmpPath:
 * c:/Temp/upload param: 파라메터 fileName: 길고양이.jpg
 * 
 * 잘 나옴!
 */
