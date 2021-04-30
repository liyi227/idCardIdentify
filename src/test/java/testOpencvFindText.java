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
    private final static int targetDifferenceValue = 10;

    public static void main(String[] args) throws Exception {
        // ���ض�̬��
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java452.dll");
        System.load(url.getPath());

        //ԭͼ·��
        String sourceImage = "E:\\Desktop\\OCRTest\\image\\02.png";
        //������ͼƬ����·��
        String processedImage = sourceImage.substring(0, sourceImage.lastIndexOf(".")) + "after.png";

        //��ȡͼ��
        Mat image = imread(sourceImage);
        if (image.empty()) {
            throw new Exception("image is empty");
        }
//        imshow("Original Image", image);

        //opencv�ҶȻ�
        Mat grayImage = ImageOpencvUtil.gray(image);
        //��ֵ��
        Mat binaryImg = ImageOpencvUtil.binaryzation(grayImage);
        //�����븯ʴ
        Mat corrodedImg = ImageOpencvUtil.corrosion(binaryImg);
        //��������
        List<RotatedRect> rects = ImageOpencvUtil.findTextRegion(corrodedImg);
        //��б����
        Mat correctedImg = ImageOpencvUtil.correction(rects, image);
        //��бУ����ü�
        Mat cuttedImg = ImageOpencvUtil.cutRect(correctedImg);
        //�ü������ű�׼��
        Mat zoomedImg = ImageOpencvUtil.zoom(cuttedImg);
//        imshow("Zoomed Image", zoomedImg);


        Mat img = zoomedImg.clone();
        BufferedImage bufferedImage = ImageConvert.Mat2BufImg(img, ".png");
        //Ϳ��
        BufferedImage paintWhiteImg = ImageFilterUtil.imageRGBDifferenceFilter(bufferedImage, targetDifferenceValue);
        //�ڰ׻�
        BufferedImage blackWhiteImage = ImageFilterUtil.replaceWithWhiteColor(paintWhiteImg);
        //�ҶȻ�
        BufferedImage _grayImg = ImageFilterUtil.gray(blackWhiteImage);

        Mat temp = ImageConvert.BufImg2Mat(_grayImg);
        //opencv�ҶȻ�
        Mat __grayImg = new Mat();
        __grayImg = ImageOpencvUtil.pyrMeanShiftFiltering(temp);
        imshow("__grayImg", __grayImg);
        __grayImg = ImageOpencvUtil.gray((__grayImg));

        //2.��ֵ��
        Mat binaryImage = new Mat();
//        binaryImage = ImageOpencvUtil.binaryzation(__grayImg);
//        binaryImage = ImageOpencvUtil.ImgBinarization(__grayImg);//����

        //1.Sobel���ӣ�x�������ݶ�
        Mat sobel = new Mat();
        Imgproc.Sobel(__grayImg, sobel, 0, 1, 0, 3);
        Imgproc.threshold(sobel, binaryImage, 0, 255, Imgproc.THRESH_OTSU | Imgproc.THRESH_BINARY);


        imshow("binaryImage", binaryImage);

        //3.���ͺ͸�ʴ�������趨
        Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 9));
        //���Ƹ߶����ÿ��Կ��������е����ͳ̶ȣ�����3��4������������ǿ,��Ҳ�����©��
        Mat element2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(26, 9));

        //4.����һ�Σ�������ͻ��
        Mat dilate1 = new Mat();
//        Imgproc.dilate(binaryImage, dilate1, element2);
        Imgproc.dilate(binaryImage, dilate1, element2, new Point(-1, -1), 1, 1, new Scalar(1));

        //5.��ʴһ�Σ�ȥ��ϸ�ڣ�����ߵȡ�����ȥ��������ֱ����
        Mat erode1 = new Mat();
        Imgproc.erode(dilate1, erode1, element1);

        //6.�ٴ����ͣ�����������һЩ
        Mat dilate2 = new Mat();
        Imgproc.dilate(erode1, dilate2, element2);


        Mat dilation = dilate2;
        imshow("����", dilate2);
        //3.���Һ�ɸѡ��������
        List<RotatedRect> rects1 = ImageOpencvUtil.findTextRegion1(dilation);

        //4.���߻�����Щ�ҵ�������
        for (RotatedRect rotatedRect : rects1) {
            Point[] rectPoint = new Point[4];
            rotatedRect.points(rectPoint);
            for (int j = 0; j <= 3; j++) {
                Imgproc.line(img, rectPoint[j], rectPoint[(j + 1) % 4], new Scalar(0, 0, 255), 2);
            }
        }

        //5.��ʾ��������ͼ��
        imshow("img", img);

        waitKey();

    }
}
