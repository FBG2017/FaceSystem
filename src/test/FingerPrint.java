package test;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.Arrays;

/**
 * ��ֵ��ϣʵ��ͼ��ָ�ƱȽ�
 * @author guyadong
 *
 */
public final class FingerPrint {
    /**
     * ͼ��ָ�Ƶĳߴ�,��ͼ��resize��ָ���ĳߴ磬�������ϣ���� 
     */
    private static final int HASH_SIZE=16;
    /**
     * ����ͼ��ָ�ƵĶ�ֵ������
     */
    private final byte[] binaryzationMatrix;
    public FingerPrint (BufferedImage src){
        this(hashValue(src));
    }
    /**
     * ����ͼ��ָ�ƵĶ�ֵ������
     */

    public FingerPrint(byte[] hashValue) {
        if(hashValue.length!=HASH_SIZE*HASH_SIZE)
            throw new IllegalArgumentException(String.format("length of hashValue must be %d",HASH_SIZE*HASH_SIZE ));
        this.binaryzationMatrix=hashValue;
    }
private static byte[] hashValue(BufferedImage src){
        BufferedImage hashImage = resize(src,HASH_SIZE,HASH_SIZE);
        byte[] matrixGray = (byte[]) toGray(hashImage).getData().getDataElements(0, 0, HASH_SIZE, HASH_SIZE, null); 
        return  binaryzation(matrixGray);
    }
    private static BufferedImage resize(Image src,int width,int height){
        BufferedImage result = new BufferedImage(width, height,  
                BufferedImage.TYPE_3BYTE_BGR); 
         Graphics g = result.getGraphics();
         try{
             g.drawImage(src.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
         }finally{
             g.dispose();
         }
        return result;      
    }
    private static BufferedImage toGray(BufferedImage src){
        if(src.getType()==BufferedImage.TYPE_BYTE_GRAY){
            return src;
        }else{
            // ͼ��ת��
            BufferedImage grayImage = new BufferedImage(src.getWidth(), src.getHeight(),  
                    BufferedImage.TYPE_BYTE_GRAY);
            new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null).filter(src, grayImage);
            return grayImage;       
        }
    }
    	//��ֵ��
	    private static byte[] binaryzation(byte[]src){
        byte[] dst = src.clone();
        int mean=mean(src);
        for(int i=0;i<dst.length;++i){
            // ������Ԫ��תΪ�޷��������ٱȽ�
            dst[i]=(byte) (((int)dst[i]&0xff)>=mean?1:0);
        }
        return dst;

    }
	  private static  int mean(byte[] src){
        long sum=0;
        // ������Ԫ��תΪ�޷�������
        for(byte b:src)sum+=(long)b&0xff;
        return (int) (Math.round((float)sum/src.length));
    }
	/**
     * �Ƚ�ָ�����ƶ�
     * @param src
     * @return 
     * @see #compare(byte[], byte[])
     */
    public float compare(FingerPrint src){
        if(src.binaryzationMatrix.length!=this.binaryzationMatrix.length)
            throw new IllegalArgumentException("length of hashValue is mismatch");
        return compare(binaryzationMatrix,src.binaryzationMatrix);
    }
	/**
     * �ж������������ƶȣ����鳤�ȱ���һ�·����׳��쳣
     * @param f1
     * @param f2
     * @return �������ƶ�(0.0~1.0)
     */
    private static float compare(byte[] f1,byte[] f2){
        if(f1.length!=f2.length)
            throw new IllegalArgumentException("mismatch FingerPrint length");
        int sameCount=0;
        for(int i=0;i<f1.length;++i){
            if(f1[i]==f2[i])++sameCount;
        }
        return (float)sameCount/f1.length;
    }
	
	
}