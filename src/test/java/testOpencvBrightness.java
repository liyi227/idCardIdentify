import org.example.utils.ImageOpencvUtil;
import org.opencv.core.Mat;

import java.net.URL;

import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

/**
 * @author ly
 * @since 2021/5/3
 */
public class testOpencvBrightness {
    public static void main(String[] args) throws Exception {
        //���ض�̬��
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java452.dll");
        System.load(url.getPath());

        //ԭͼ·��
        String sourceImage = "E:\\Desktop\\OCRTest\\image\\03.png";
        //������ͼƬ����·��--��ԭ����ͼƬ���������afterBrightnessProcessed
        String processedImage = sourceImage.substring(0, sourceImage.lastIndexOf(".")) + "afterBrightnessProcessed.png";

        //��ȡͼ��
        Mat image = imread(sourceImage);
        if (image.empty()) {
            throw new Exception("image is empty");
        }
        //չʾԭͼ
        imshow("Original Image", image);

        //����ImageOpencvUtil�����ȵ���imageBrightness����
        Mat brightnessImg = ImageOpencvUtil.imageBrightness(image);

        //չʾ�ҶȻ������ͼ��
        imshow("Processed Image", brightnessImg);

        //���浽�ַ���processedImage��Ӧλ��
//        imwrite(processedImage, grayImage);
        waitKey();
    }
}
