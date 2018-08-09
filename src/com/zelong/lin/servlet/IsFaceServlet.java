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
import javax.servlet.http.HttpSession;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import com.zelong.lin.dao.User;
import com.zelong.lin.utils.Base64ToImg;
import com.zelong.lin.utils.BufImgToMat;
import com.zelong.lin.utils.DlibJNIJAVA;
import com.zelong.lin.utils.FaceDetection;
import com.zelong.lin.utils.ImageDeal;
import com.zelong.lin.utils.ImgPrint;
import com.zelong.lin.utils.MatToBufImg;
import com.zelong.lin.utils.MySql;

@WebServlet(name="IsFaceServlet",urlPatterns={"/IsFaceServlet"})
public class IsFaceServlet extends HttpServlet {
	static{
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );//����opencv�Ŀ�
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		String msg="";
		String imgbase64=request.getParameterValues("faceBase")[0];
		if(imgbase64==null){
			msg+="�������ͷ����";
		}else{
			//��ԭͼƬ������BufferedImage
			BufferedImage img=Base64ToImg.Base64ToImage(imgbase64);
			
			//���ԣ�
			//���ԣ���
			//���ԣ�����
			//img=ImageIO.read(new File("D:/pictest/t.jpg"));
			
			if(img==null){
				msg+="ϵͳ�쳣��base64�ַ�����������";
			}else{
				//BufferedImageתMat
				Mat mat_img=BufImgToMat.getMat(img);
				FaceDetection dt=new FaceDetection();
				ArrayList<Mat> detected_img=dt.detectFace(mat_img);
				if(detected_img==null){
					msg+="��ⲻ�������������³��ԣ�����ѡ�������ĳ�����";
				}else{
					//�ؼ��㶨λ,����У���Ƿ����������
					String dat="E:/face68/shape_predictor_68_face_landmarks.dat";
					DlibJNIJAVA jni=new DlibJNIJAVA();
					//��������Ϊregister.png
					String path="E:/TempOfFaceRegister/login.png";
					File file=new File(path);
					if(!file.exists()){
						file.mkdirs();
					}
					BufferedImage mark_buf=MatToBufImg.getImage(detected_img.get(1));
					//��ʱд�빩C++����
					ImageIO.write(mark_buf, "png", file);
					double[] b=jni.getLandMarks(dat, path);
					//���ܶ�λ����ʱ
					if(b[24]!=0 && b[44]!=0 && b[46]!=0 && b[57]!=0 && b[51]!=0 && b[8]!=0){
						double sumX=b[46]-b[40];
				        double sumY=b[89]-b[95];
						BufferedImage buf_img=null;
				        if(sumY==0 || sumX==0){
				        	buf_img=MatToBufImg.getImage(detected_img.get(0));
				        }else{
				        	try {
								BufferedImage after_mark_buf=new ImageDeal().spin(mark_buf, sumX, sumY);
								
								
								//���ԣ�
								//���ԣ���
								//���ԣ�����
								//ImageIO.write(after_mark_buf, "png", new File("D:/1.png"));
								
								
								ArrayList<Mat> mark_detected_img=dt.detectFace(BufImgToMat.getMat(after_mark_buf));
								if(mark_detected_img==null){
									msg+="��ⲻ�������������³��ԣ�";
								}else{
									//���¼�Ⲣ��ȡ��������
									buf_img=MatToBufImg.getImage(mark_detected_img.get(0));
									ImgPrint ip=new ImgPrint();							
									int num=0;
									int result=0;
									String username=null;
									String password=null;
									byte[] fromSql=null;
									String sql="select * from user";
									//�������ݿ�
									Connection con1=null;
									PreparedStatement ps1=null;
									ResultSet rs1=null;
									try {
										con1=MySql.getConn();
										ps1=con1.prepareStatement(sql);
										rs1=ps1.executeQuery();
										while(rs1.next()){
											fromSql=rs1.getBytes("imgprint");
											if(fromSql==null){
												continue;
											}
											result=ip.jugeImg(buf_img, fromSql);
											if(num<result){
												num=result;
												username=rs1.getString("username");
												password=rs1.getString("password");
											}
										}
										//����256�������ƶ�Ϊ200/256=0.78125ʱ������������ӦΪ218/256=0.85
										if(num>200){
											User person=new User();
											person.setUsername(username);
											person.setPassword(password);
											
											HttpSession session=request.getSession();
											session.setAttribute("username", person.getUsername());
											session.setAttribute("password", person.getPassword());
											msg+="Welcome.jsp?username='+person.getUsername()+'";
										}else{
											msg+=""+num;
										}		
									} catch (SQLException e) {
										e.printStackTrace();
									}finally{
										MySql.closeConn(rs1, ps1, con1);
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
		response.getWriter().print(msg);
	}
}











