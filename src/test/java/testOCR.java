import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.example.utils.ImageConvert;
import org.example.utils.ImageFilterUtil;
import org.example.utils.ImageOpencvUtil;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;

/**
 * @author ly
 * @since 2021/4/29
 */
public class testOCR {
    private final static int targetDifferenceValue = 15;

    public static void main(String[] args) throws Exception {
        // ���ض�̬��
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java452.dll");
        System.load(url.getPath());

        //ԭͼ·��
        String sourceImage = "E:\\Desktop\\OCRTest\\image\\03.png";
        //������ͼƬ����·��
        String processedImage = sourceImage.substring(0, sourceImage.lastIndexOf(".")) + "after.png";

        //��ȡͼ��
        Mat image = imread(sourceImage);
        if (image.empty()) {
            throw new Exception("image is empty");
        }
//        imshow("Original Image", image);

        //��бУ��
        Mat correctedImg = ImageOpencvUtil.imgCorrection(image);

        //��бУ����ü�
        Mat cuttedImg = ImageOpencvUtil.cutRect(correctedImg);
        //�ü������ű�׼��
        Mat zoomedImg = ImageOpencvUtil.zoom(cuttedImg);

        imshow("Zoomed Image", zoomedImg);

        Mat img = zoomedImg.clone();
        BufferedImage bufferedImage = ImageConvert.Mat2BufImg(img, ".png");

        //ImageFilterUtil��������
        int brightness = ImageFilterUtil.imageBrightness(bufferedImage);
        System.out.println("brightness = " + brightness);
        BufferedImage brightnessImg = bufferedImage;
        if (brightness > 180) {
            brightnessImg = ImageFilterUtil.imageBrightness(bufferedImage, -60);
        }

        //Ϳ��
//        BufferedImage paintWhiteImg = ImageFilterUtil.imageRGBDifferenceFilter(bufferedImage, targetDifferenceValue);
        //�ڰ׻�
//        BufferedImage blackWhiteImage = ImageFilterUtil.replaceWithWhiteColor(paintWhiteImg);

        //ImageFilterUtil�ҶȻ�
        BufferedImage grayImage = ImageFilterUtil.gray(brightnessImg);
        //��ImageFilterUtil�ҶȻ����ͼƬת��ΪMat����ͼ��
        Mat matImg = ImageConvert.BufImg2Mat(grayImage);

        //opencv�Ǿֲ���ֵȥ�루��Ҫ��ͨ����Matͼ��
        Mat denoiseImg = ImageOpencvUtil.pyrMeanShiftFiltering(matImg);
//        grayImg = ImageOpencvUtil.pyrMeanShiftFiltering(grayImg);

        //opencv�ҶȻ�--תΪ��ͨ��
        Mat grayImg = ImageOpencvUtil.gray(denoiseImg);

        imshow("grayImg", grayImg);

        //�����븯ʴ���Matͼ��
        Mat dilationImg = ImageOpencvUtil.preprocess(grayImg);
        imshow("dilation", dilationImg);

        //���Һ�ɸѡ��������
        List<RotatedRect> rects = ImageOpencvUtil.findTextRegionRect(dilationImg);

        //�ú��߻����ҵ�������
        for (RotatedRect rotatedRect : rects) {
            Point[] rectPoint = new Point[4];
            rotatedRect.points(rectPoint);
            for (int j = 0; j <= 3; j++) {
                Imgproc.line(img, rectPoint[j], rectPoint[(j + 1) % 4], new Scalar(0, 0, 255), 2);
            }
        }
        //��ʾ��������ͼ��
        imshow("Contour Image", img);

        //��ȡ����ʾ����ͼƬ
        Mat dst;
        System.out.println("rects.size:" + rects.size());
        List<Mat> lstMat = new ArrayList<>();

        for (int i = 0; i < rects.size(); i++) {
//            dst = new Mat(img, rects.get(i).boundingRect());
            dst = ImageOpencvUtil.cropImage(matImg, rects.get(i).boundingRect());
            lstMat.add(dst);
            imshow("croppedImg" + i, dst);
        }

        List<BufferedImage> lstBufferedImg = new ArrayList<>();
        for (int i = 0; i < lstMat.size(); i++) {
            BufferedImage tempBufferedImg = ImageConvert.Mat2BufImg(lstMat.get(i), ".png");
            lstBufferedImg.add(tempBufferedImg);
        }

        ITesseract instance = new Tesseract();    //����ITesseract�ӿڵ�ʵ��ʵ������

        //java.lang.ClassLoader.getSystemResource()��������һ��URL�����ȡ��Դ�������Դ���ܱ��ҵ��򷵻�null��
        URL tessDataUrl = ClassLoader.getSystemResource("tessdata");    //file:/E:/Desktop/OCRTest/Tess4jOcr/Tess4jOcr/Tess4jOcr/target/classes/tessdata
        String path = tessDataUrl.getPath().substring(1);    //url.getPath()-/E:/Desktop/OCRTest/Tess4jOcr/Tess4jOcr/Tess4jOcr/target/classes/tessdata
        instance.setDatapath(path); //pathΪtessdata�ļ���Ŀ¼λ��
        instance.setLanguage("chi_sim");    //��Ӣ�Ļ��ʶ������ + �ָ���chi_sim���������ģ�eng��Ӣ��
        instance.setTessVariable("user_defined_dpi", "300");    //Warning: Invalid resolution 0 dpi. Using 70 instead.
        String result = null;

        System.out.println("ʶ�������£�");
        try {
            long startTime = System.currentTimeMillis();    //ʶ��֮ǰ��ʱ��

            for (int i = lstBufferedImg.size()-1; i >= 0; i--) {
                result = instance.doOCR(lstBufferedImg.get(i));    //��ʼʶ��
                System.out.println(result);    //��ӡʶ����
            }

            long endTime = System.currentTimeMillis();    //ʶ�������ʱ��
            System.out.println("ʶ����ʱ��" + (endTime - startTime) + "ms");    //ʶ��ͼƬ��ʱ
        } catch (TesseractException e) {
            System.out.println(e.getMessage());
        }

        waitKey();
    }
}
