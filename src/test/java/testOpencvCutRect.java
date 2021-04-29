import org.example.utils.ImageOpencvUtil;
import org.opencv.core.Mat;
import org.opencv.core.RotatedRect;

import java.net.URL;
import java.util.List;

import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

/**
 * @author ly
 * @since 2021/4/26
 */
//����Opencv��cutRect()�ü�����
public class testOpencvCutRect {
    public static void main(String[] args) throws Exception {
        // ���ض�̬��
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java452.dll");
        System.load(url.getPath());

        //ԭͼ·��
        String sourceImage = "E:\\Desktop\\OCRTest\\image\\01a.png";
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

        imshow("Cutted Image", cuttedImg);
        waitKey();
    }
}
