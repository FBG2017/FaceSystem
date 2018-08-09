package com.zelong.lin.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import com.zelong.lin.utils.Base64ToImg;
import com.zelong.lin.utils.BufImgToMat;
import com.zelong.lin.utils.DlibJNIJAVA;
import com.zelong.lin.utils.FaceDetection;
import com.zelong.lin.utils.ImageDeal;
import com.zelong.lin.utils.ImgPrint;
import com.zelong.lin.utils.MatToBufImg;
import com.zelong.lin.utils.MySql;

@WebServlet(name="RegisterServlet",urlPatterns={"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {
	static{ 
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );// ����opencv�Ŀ�
    }
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String imgbase64=request.getParameter("base64");
		String msg="";	
		String userUnique="select * from user where username=?";
		//�������ݿ�
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			con=MySql.getConn();
			ps=con.prepareStatement(userUnique);
			ps.setString(1, username);
			rs=ps.executeQuery();
			if(rs.next()){
				msg+="�û��Ѵ��ڣ�";
				MySql.closeConn(rs, ps, con);
			}else{
				MySql.closeConn(rs, ps, con);
				if(imgbase64==null){
					msg+="�������ͷ���գ�";
				}else{
					//��ԭͼƬ������BufferedImage
					BufferedImage img=Base64ToImg.Base64ToImage(imgbase64);
					
					//���ԣ�
					//���ԣ���
					//���ԣ�����
					//img=ImageIO.read(new File("D:/pictest/t.jpg"));
					
					if(img==null){
						msg+="������Ƭʧ�ܣ�";
					}else{
						//BufferedImageת��Mat
						Mat mat_img=BufImgToMat.getMat(img);
						//��Ⲣ��ȡ��������
						FaceDetection dt=new FaceDetection();
						ArrayList<Mat> detected_img=dt.detectFace(mat_img);
						if(detected_img==null){
							msg+="��ⲻ�������������³��ԣ�";
						}else{
							//System.out.println("opencv is ok");
							//�ؼ��㶨λ,����У���Ƿ����������
							String dat="E:/face68/shape_predictor_68_face_landmarks.dat";
							DlibJNIJAVA jni=new DlibJNIJAVA();
							//��������Ϊregister.png
							String path="E:/TempOfFaceRegister/register.png";
							File file=new File(path);
							if(!file.exists()){
								file.mkdirs();
							}
							//������ʱͼƬ����c++�����ȡ
							BufferedImage mark_buf=MatToBufImg.getImage(detected_img.get(1));
							ImageIO.write(mark_buf, "png", file);
							double[] b=jni.getLandMarks(dat, path);						
							//���ܶ�λ����ʱ
							if(b[24]!=0 && b[44]!=0 && b[46]!=0 && b[57]!=0 && b[51]!=0 && b[8]!=0){
								double sumX=b[46]-b[40];
						        double sumY=b[89]-b[95];
						        //����ͼƬ������
								String imgRealPath="E:/FaceOfUser/"+username+".png";
								String imgsrc="/uploadfile/"+username+".png";
								BufferedImage buf_img=null;
						        if(sumY==0 || sumX==0){
						        	buf_img=MatToBufImg.getImage(detected_img.get(0));
						        }else{
						        	try {
										BufferedImage after_mark_buf=new ImageDeal().spin(mark_buf, sumX, sumY);
										
										
										//���ԣ�
										//���ԣ���
										//���ԣ�����
										//ImageIO.write(after_mark_buf, "png", new File("D:/aa.png"));
										
										
										ArrayList<Mat> mark_detected_img=dt.detectFace(BufImgToMat.getMat(after_mark_buf));
										if(mark_detected_img==null){
											msg+="��ⲻ�������������³��ԣ�";
										}else{
											//���¼�Ⲣ��ȡ��������
											buf_img=MatToBufImg.getImage(mark_detected_img.get(0));
											ImageIO.write(buf_img,"png",new File(imgRealPath));
											//����ͼƬ���������ݿ�
											byte[] imgprint=new ImgPrint().getImgPrint(buf_img);
											String sql="insert into user(username,password,imgsrc,imgprint) values(?,?,?,?)";
											//�������ݿ�
											Connection con1=null;
											PreparedStatement ps1=null;
											try {
												con1=MySql.getConn();
												ps1=con1.prepareStatement(sql);
												ps1.setString(1, username);
												ps1.setString(2, password);
												ps1.setString(3, imgsrc);
												ps1.setBytes(4, imgprint);
												int i=ps1.executeUpdate();
												if(i>0){
													msg+="ע��ɹ�";
												}
											} catch (SQLException e) {
												e.printStackTrace();
											}finally{
												MySql.closeConn(null, ps1, con1);
											}
										}	
									} catch (Exception e1) {
										e1.printStackTrace();
									}
						        }	
							}else{
								msg+="��ⲻ�������������³��ԣ�";
							}					
						}
					}
				}	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			response.getWriter().print(msg);
		}
	}
}
