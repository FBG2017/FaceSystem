package test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

//BufferedImageת��Mat
public class BufImgToMat {


    /**
     *
     * @param image
     * @param imgType bufferedImage������ �� BufferedImage.TYPE_3BYTE_BGR
     * @param matType ת����mat��type �� CvType.CV_8UC3
     * @throws IOException 
     */


    public static Mat getMat(BufferedImage in) throws IOException {
       if (in == null) {
              throw new IllegalArgumentException("image == null");
       }
       
       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
       ImageIO.write(in, "png", byteArrayOutputStream);
       byteArrayOutputStream.flush();
       return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
       
    }
}
