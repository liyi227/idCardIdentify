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
        String sourceImage = "E:\\Desktop\\OCRTest\\image\\04.png";
        //������ͼƬ����·��
        String processedImage = sourceImage.substring(0, sourceImage.lastIndexOf(".")) + "after.png";

        //��ȡͼ��
        Mat image = imread(sourceImage);
        if (image.empty()) {
            throw new Exception("image is empty");
        }
        imshow("Original Image", image);

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


        BufferedImage bufferedImage = ImageConvert.Mat2BufImg(zoomedImg, ".png");

        BufferedImage paintWhiteImg = ImageFilterUtil.imageRGBDifferenceFilter(bufferedImage, targetDifferenceValue);
        BufferedImage _grayImg = ImageFilterUtil.gray(paintWhiteImg);

        Mat __grayImg = ImageOpencvUtil.gray(ImageConvert.BufImg2Mat(_grayImg));

        Mat _binaryImg = ImageOpencvUtil.ImgBinarization(__grayImg);
//        Mat _binaryImg = ImageOpencvUtil.whiteBlack(ImageConvert.BufImg2Mat(paintWhiteImg));

//        _binaryImg.removeTileObserver(_binaryImg);
//        Mat temp = ImageOpencvUtil.medianBlur(ImageConvert.BufImg2Mat(_binaryImg));
        imshow("Image", _binaryImg);
        BufferedImage img = ImageConvert.Mat2BufImg(_binaryImg, ".png");


        ITesseract instance = new Tesseract();    //����ITesseract�ӿڵ�ʵ��ʵ������

        //java.lang.ClassLoader.getSystemResource()��������һ��URL�����ȡ��Դ�������Դ���ܱ��ҵ��򷵻�null��
        URL tessDataUrl = ClassLoader.getSystemResource("tessdata");    //file:/E:/Desktop/OCRTest/Tess4jOcr/Tess4jOcr/Tess4jOcr/target/classes/tessdata
        String path = tessDataUrl.getPath().substring(1);    //url.getPath()-/E:/Desktop/OCRTest/Tess4jOcr/Tess4jOcr/Tess4jOcr/target/classes/tessdata
        instance.setDatapath(path); //pathΪtessdata�ļ���Ŀ¼λ��
        instance.setLanguage("chi_sim");    //��Ӣ�Ļ��ʶ������ + �ָ���chi_sim���������ģ�eng��Ӣ��
        instance.setTessVariable("user_defined_dpi", "300");    //Warning: Invalid resolution 0 dpi. Using 70 instead.
        String result = null;

        try {
            long startTime = System.currentTimeMillis();    //ʶ��֮ǰ��ʱ��
            result = instance.doOCR(_grayImg);    //��ʼʶ��
            long endTime = System.currentTimeMillis();    //ʶ�������ʱ��
            System.out.println("ʶ����ʱ��" + (endTime - startTime) + "ms");    //ʶ��ͼƬ��ʱ
        } catch (TesseractException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("ʶ�������£�");
        System.out.println(result);    //��ӡʶ����


        waitKey();
    }
}
