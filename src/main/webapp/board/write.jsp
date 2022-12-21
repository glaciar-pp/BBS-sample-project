<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="../common/heading.jsp" %>
    <style>
        th, td { text-align: center; }
    </style>
</head>

<body>
    <%@ include file="../common/top.jsp" %>

    <div class="container" style="margin-top: 80px;">
        <div class="row">
            <%@ include file="../common/aside.jsp" %>
            
            <!-- =================== main =================== -->
            <div class="col-sm-9">
            	<h3><strong>게시글 쓰기</strong></h3>
            	<hr>
                <div class="row">
			        <div class="col-1"></div>
			        <div class="col-10">
			            <form action="/bbs/board/write" method="post" enctype="multipart/form-data">
			                <table class="table table-borderless">
			                    <tr>
			                        <td><label for="title">제목</label></td>
			                        <td colspan="2"><input class="form-control" type="text" name="title" id="title"></td>
			                    </tr>
			                    <tr>
			                        <td><label for="content">내용</label></td>
			                        <td colspan="2">
			                        	<textarea class="form-control" name ="content" rows="10" id="content"></textarea>
			                        </td>
			                    </tr>
			                    <tr>
			                        <td><label for="file1">첨부파일</label></td>
			                        <td><input class="form-control" type="file" name="file1" id="file1"></td>
			                        <td><input class="form-control" type="file" name="file2" id="file2"></td>
			                    </tr>
			                    <tr>
			                        <td colspan="3" style="text-align: center;">
			                            <input class="btn btn-primary" type="submit" value="제출">
			                            <input class="btn btn-secondary" type="reset" value="취소">
			                        </td>
			                    </tr>
			                </table>
			            </form>
			        </div>
			        <div class="col-1"></div>
			    </div>
            </div>
            <!-- =================== main =================== -->
            
        </div>
    </div>

    <%@ include file="../common/bottom.jsp" %>
</body>
</html>