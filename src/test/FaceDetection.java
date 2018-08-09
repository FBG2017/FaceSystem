package test;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;


public class FaceDetection {
	static{
        // ����opencv�Ŀ�
    	System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
    }

    public Mat detectFace(Mat image) {
        //String xmlfilePath=getClass().getResource("lbpcascade_frontalface_improved.xml").getPath().substring(1);
    	//String xmlfilePath="E:/face68/haarcascade_frontalface_alt_tree.xml";
    	String xmlfilePath="E:/face68/haarcascade_frontalface_alt.xml";
    	//String xmlfilePath="E:/face68/cascade.xml";
        CascadeClassifier faceDetector = new CascadeClassifier(xmlfilePath);      
        //ͼ�����ţ�����
        Mat resizeImage = new Mat();
        if(image.width()>1000 && image.height()>1000){
        	Imgproc.resize(image, resizeImage, new Size(image.cols()/2,image.rows()/2), 0, 0, Imgproc.INTER_LINEAR);
        }else{
        	resizeImage=image;
        }
        
        Mat grayImage = new Mat();
	    Imgproc.cvtColor(resizeImage, grayImage, Imgproc.COLOR_RGB2GRAY);
//	    Mat afterImage = new Mat();
//	    List<Mat> mv = new ArrayList<Mat>();
//	    Core.split(grayImage, mv);
//	    for (int i = 0; i < grayImage.channels(); i++){
//	      Imgproc.equalizeHist(mv.get(i), mv.get(i));
//	    }
//	    Core.merge(mv, afterImage); 
	    // ��ͼƬ�м������
        MatOfRect faceDetections = new MatOfRect();
        
        faceDetector.detectMultiScale(grayImage, faceDetections);
        //faceDetector.detectMultiScale(grayImage, faceDetections,1.1,3,8,new Size(0,0),grayImage.size());
        Rect[] rects = faceDetections.toArray();
        //System.out.println(rects.length);
	        for (Rect rect : rects){    
	        	Imgproc.rectangle(grayImage, new Point(rect.x, rect.y),
	                    new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 255, 255));   
	        } 

	    	//roi_img.copyTo(tmp_img);
	        return grayImage;
      
 
    }  
}
