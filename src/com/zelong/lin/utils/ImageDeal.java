package com.zelong.lin.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageDeal {
	public BufferedImage spin(BufferedImage bi,double sumX,double sumY) throws Exception {
        int swidth = 0; // ��ת��Ŀ��
        int sheight = 0; // ��ת��ĸ߶�
        int x; // ԭ�������
        int y; // ԭ��������
        
        
        double k=(sumY)*(-1.0)/(sumX);
        double degree=(Math.atan(k)/Math.PI*180);
        
        // ����Ƕ�--ȷ����ת����
        degree = degree % 360;
        if (degree < 0)
            degree = 360 + degree;// ���Ƕ�ת����0-360��֮��
        double theta = Math.toRadians(degree);// ���Ƕ�תΪ����

        // ȷ����ת��Ŀ�͸�
        if (degree == 180 || degree == 0 || degree == 360) {
            swidth = bi.getWidth();
            sheight = bi.getHeight();
        } else if (degree == 90 || degree == 270) {
            sheight = bi.getWidth();
            swidth = bi.getHeight();
        } else {
            swidth = (int) (Math.sqrt(bi.getWidth() * bi.getWidth()
                    + bi.getHeight() * bi.getHeight()));
            sheight = (int) (Math.sqrt(bi.getWidth() * bi.getWidth()
                    + bi.getHeight() * bi.getHeight()));
        }

        x = (swidth / 2) - (bi.getWidth() / 2);// ȷ��ԭ������
        y = (sheight / 2) - (bi.getHeight() / 2);

        BufferedImage spinImage = new BufferedImage(swidth, sheight,
                bi.getType());
        // ����ͼƬ������ɫ
        Graphics2D gs = (Graphics2D) spinImage.getGraphics();
        gs.setColor(Color.WHITE);
        gs.fillRect(0, 0, swidth, sheight);// �Ը�����ɫ������ת��ͼƬ�ı���

        AffineTransform at = new AffineTransform();
        at.rotate(theta, swidth / 2, sheight / 2);// ��תͼ��
        at.translate(x, y);
        AffineTransformOp op = new AffineTransformOp(at,
                AffineTransformOp.TYPE_BICUBIC);
        spinImage = op.filter(bi, spinImage);

        return spinImage;

    }
}
