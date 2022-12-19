package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {
	public static Connection getConnection() {
		Connection conn;
		try {
			Context initContext = new InitialContext();
			DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/project");
			conn = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return conn;
	}
	
	public void deleteUser(String uid) {
		Connection conn = getConnection();
		String sql = "UPDATE users SET isDeleted=1 WHERE uid=?;";
		try {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, uid);
			
			pStmt.executeUpdate();
			pStmt.close(); conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateUser(User u) {
		Connection conn = getConnection();
		String sql = "UPDATE users SET uname=?, email=? WHERE uid=?;";
		try {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, u.getUname());
			pStmt.setString(2, u.getEmail());
			pStmt.setString(3, u.getUid());
			
			pStmt.executeUpdate();
			pStmt.close(); conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public User getUserInfo(String uid) {
		Connection conn = getConnection();
		String sql = "SELECT * FROM users WHERE uid=?;";
		User u = new User();
		try {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, uid);
			
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				u.setUid(rs.getString(1));
				u.setPwd(rs.getString(2));
				u.setUname(rs.getString(3));
				u.setEmail(rs.getString(4));
				u.setRegDate(LocalDate.parse(rs.getString(5)));
			}
			rs.close(); pStmt.close(); conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return u;
	}
	
	public List<User> listUsers() {
		Connection conn = getConnection();
		String sql = "SELECT * FROM users WHERE isDeleted=0 ORDER BY regDate DESC, uid LIMIT 10;";
		List<User> list = new ArrayList<>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				User u = new User();
				u.setUid(rs.getString(1));
				u.setPwd(rs.getString(2));
				u.setUname(rs.getString(3));
				u.setEmail(rs.getString(4));
				u.setRegDate(LocalDate.parse(rs.getString(5)));
				list.add(u);
			}
			rs.close(); stmt.close(); conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void registerUser(User u) {
		Connection conn = getConnection();
		String sql = "INSERT INTO users VALUES (?, ?, ?, ?, DEFAULT, DEFAULT);";
		try {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, u.getUid());
			String cryptedPwd = BCrypt.hashpw(u.getPwd(), BCrypt.gensalt());
			pStmt.setString(2, cryptedPwd);
			pStmt.setString(3, u.getUname());
			pStmt.setString(4, u.getEmail());
			
			pStmt.executeUpdate();
			pStmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}