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
 * @since 2021/4/27
 */
//����opencv������zoom()����
public class testOpencvZoom {
    public static void main(String[] args) throws Exception {
        // ���ض�̬��
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java452.dll");
        System.load(url.getPath());

        //ԭͼ·��
        String sourceImage = "E:\\Desktop\\OCRTest\\image\\09.png";
        //������ͼƬ����·��
        String processedImage = sourceImage.substring(0, sourceImage.lastIndexOf(".")) + "afterZoom.png";

        //��ȡͼ��
        Mat image = imread(sourceImage);
        if (image.empty()) {
            throw new Exception("image is empty");
        }

        //ԭͼչʾ
        imshow("Original Image", image);

        //opencv�ҶȻ�
        Mat grayImage = ImageOpencvUtil.gray(image);

        //��ֵ��
        Mat binaryImage = ImageOpencvUtil.binaryzation(grayImage);

        //�����븯ʴ
        Mat corrodedImage = ImageOpencvUtil.corrosion(binaryImage);

        //��������
        List<RotatedRect> rects = ImageOpencvUtil.findTextRegion(corrodedImage);

        //ԭͼ-��б����
        Mat correctedImg = ImageOpencvUtil.correction(rects, image);


        //�ü�У�����ͼƬ
        Mat cuttedImg = ImageOpencvUtil.cutRect(correctedImg);

        //opencv����
        Mat zoomedImage = ImageOpencvUtil.zoom(cuttedImg);

        imshow("Zoomed Image", zoomedImage);
        waitKey();
    }
}
