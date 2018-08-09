package com.zelong.lin.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySql {
	public static final String driver = "com.mysql.jdbc.Driver";// ����
	public static final String url = "jdbc:mysql://localhost:3306/facesystem?characterEncoding=utf8&useSSL=true";// mysql�̶���URL:jdbc:mysql://localhost:3306/���ݿ���
	public static final String user = "root";// ���ݿ���û���
	public static final String pwd = "1234";// ���ݿ�����

	public static Connection getConn() {
		Connection con = null;
		try {
			// ����mysql������
			Class.forName(driver);
			// �������ݿ�����
			con = DriverManager.getConnection(url, user, pwd);
		} catch (ClassNotFoundException e) {
			System.out.println("����������ʧ��");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("ע��������ʧ��");
			e.printStackTrace();
		}
		return con;
	}
	// �ر�����
	     public static void closeConn(ResultSet rs,PreparedStatement ps,Connection con) {
	    	 if (rs!=null) {
	             try {
	                 rs.close();
	             } catch (SQLException e) {
	                 System.out.println("�ر�����ʧ�ܣ�");
	                 e.printStackTrace();
	             }
	         }
	    	 if (ps!=null) {
	             try {
	                 ps.close();
	             } catch (SQLException e) {
	                 System.out.println("�ر�����ʧ�ܣ�");
	                 e.printStackTrace();
	             }
	         }
	         if (con!=null) {
	             try {
	                 con.close();
	             } catch (SQLException e) {
	                 System.out.println("�ر�����ʧ�ܣ�");
	                 e.printStackTrace();
	             }
	         }
	     }
}