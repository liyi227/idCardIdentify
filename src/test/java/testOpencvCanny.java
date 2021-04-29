import org.example.utils.ImageConvert;
import org.example.utils.ImageFilterUtil;
import org.example.utils.ImageOpencvUtil;
import org.opencv.core.Mat;
import org.opencv.core.RotatedRect;

import java.net.URL;
import java.util.List;

import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;

/**
 * @author ly
 * @since 2021/4/26
 */
//����Opencv��canny()��Ե��ⷽ��
public class testOpencvCanny {
    public static void main(String[] args) throws Exception {
        // ���ض�̬��
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java452.dll");
        System.load(url.getPath());

        //ԭͼ·��
        String sourceImage = "E:\\Desktop\\OCRTest\\image\\01a.png";
        //������ͼƬ����·��
        String processedImage = sourceImage.substring(0, sourceImage.lastIndexOf(".")) + "after.png";

        // ��ȡͼ��
        Mat image = imread(sourceImage);
        if (image.empty()) {
            throw new Exception("image is empty");
        }
        imshow("Original Image", image);

        //opencv�ҶȻ�
        Mat grayImage = ImageOpencvUtil.gray(image);

        //��ֵ��
        Mat binaryImage = ImageOpencvUtil.binaryzation(grayImage);

        //�����븯ʴ
        Mat corrodedImage = ImageOpencvUtil.corrosion(binaryImage);

        //��������
        List<RotatedRect> rects = ImageOpencvUtil.findTextRegion(corrodedImage);

        //��б����
        Mat correctedImg = ImageOpencvUtil.correction(rects, image);

        //����
        Mat cannyImg = ImageOpencvUtil.canny(correctedImg);

        imshow("Corrected Image", cannyImg);
        waitKey();
    }
}
