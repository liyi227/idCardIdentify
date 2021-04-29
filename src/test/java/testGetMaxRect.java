import org.example.utils.ImageOpencvUtil;
import org.opencv.core.Mat;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.net.URL;
import java.util.List;

import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;

/**
 * @author ly
 * @since 2021/4/27
 */
public class testGetMaxRect {
    public static void main(String[] args) throws Exception {
        // ���ض�̬��
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java452.dll");
        System.load(url.getPath());

        //ԭͼ·��
        String sourceImage = "E:\\Desktop\\OCRTest\\image\\10.jpg";
        //������ͼƬ����·��
        String processedImage = sourceImage.substring(0, sourceImage.lastIndexOf(".")) + "after.png";

        // ��ȡͼ��
        Mat image = imread(sourceImage);
        if (image.empty()) {
            throw new Exception("image is empty");
        }
        imshow("Original Image", image);

        // ��ɫת�Ҷ�
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);

        // ��˹�˲�������
        Imgproc.GaussianBlur(image, image, new Size(3, 3), 2, 2);

        //����
        Mat cannyImg = ImageOpencvUtil.canny(image);


        imshow("Corrected Image", cannyImg);
        waitKey();
    }
}
