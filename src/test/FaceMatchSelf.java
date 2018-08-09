package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FaceMatchSelf{
    public static void main(String[] args) throws IOException{
		System.out.println("���������У���ȴ�������");
    	String path="D:/pictest/bmp/s";
    	ImgPrint ip=new ImgPrint();
    	double yuzhi=0.81;
    	int MatchNum=0;
		int total=0;
		int noMatchNum=0;
		for(int m=1;m<57;m++){
			String str=path+m+"/";
			File file=new File(str);
			File[] fs=file.listFiles();


			for(int i=0;i<fs.length;i++){

				for(int j=i+1;j<fs.length;j++){

					total+=1;
					String imgPathRow=str+fs[i].getName();
					String imgPathColn=str+fs[j].getName();
					BufferedImage bufRow=ImageIO.read(new File(imgPathRow));
					BufferedImage bufColn=ImageIO.read(new File(imgPathColn));
					double d=ip.jugeImg(bufRow, bufColn);
					if(d<yuzhi){
						noMatchNum+=1;
					}else{
						MatchNum+=1;
					}
				}
				
			}
		}
		
		
		
		double noMatchRate=noMatchNum*1.0/total;
		double MatchRate=MatchNum*1.0/total;
		System.out.println("������"+total);
		System.out.println("ƥ����"+MatchNum);
		System.out.println("��ƥ����"+noMatchNum);
		System.out.println("ƥ����"+MatchRate);
		System.out.println("��ƥ����"+noMatchRate);
    	

    }
    
}
