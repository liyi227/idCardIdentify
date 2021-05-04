import org.example.utils.ImageConvert;
import org.example.utils.ImageFilterUtil;
import org.example.utils.ImageOpencvUtil;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;

/**
 * @author ly
 * @since 2021/4/30
 */
public class testOpencvFindText {
    private final static int targetDifferenceValue = 20;

    public static void main(String[] args) throws Exception {
        // ���ض�̬��
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java452.dll");
        System.load(url.getPath());

        //ԭͼ·��
        String sourceImage = "E:\\Desktop\\OCRTest\\image\\09.png";
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
        for (int i = 0; i < rects.size(); i++) {
//            dst = new Mat(img, rects.get(i).boundingRect());
            dst =ImageOpencvUtil.cropImage(img,rects.get(i).boundingRect());
            //��ʾ��ȡ�Ĺؼ���Ϣͼ��
            imshow("croppedImg" + i, dst);
        }

        waitKey();
    }
}
