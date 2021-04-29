import org.example.utils.ImageOpencvUtil;
import org.opencv.core.Mat;
import org.opencv.core.RotatedRect;

import java.net.URL;
import java.util.List;

import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

/**
 * @author ly
 * @since 2021/4/28
 */
//��������ImageOpencvUtil��CutRect()����
public class batchTestOpencvCutRect {
    public static void main(String[] args) throws Exception {
        // ���ض�̬��
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java452.dll");
        System.load(url.getPath());

        //ԭͼ·��
        String[] sourceImage = new String[]{"E:\\Desktop\\OCRTest\\image\\01.png",
                "E:\\Desktop\\OCRTest\\image\\02.png",
                "E:\\Desktop\\OCRTest\\image\\03.png",
                "E:\\Desktop\\OCRTest\\image\\04.png",
                "E:\\Desktop\\OCRTest\\image\\05.png",
                "E:\\Desktop\\OCRTest\\image\\06.png",
                "E:\\Desktop\\OCRTest\\image\\07.png",
                "E:\\Desktop\\OCRTest\\image\\08.png",
                "E:\\Desktop\\OCRTest\\image\\09.png",
                "E:\\Desktop\\OCRTest\\image\\10.png",
                "E:\\Desktop\\OCRTest\\image\\11.png"
        };
        String[] processedImage = new String[sourceImage.length];
        for (int i = 0; i < sourceImage.length; i++) {
            //������ͼƬ����·��
            processedImage[i] = sourceImage[i].substring(0, sourceImage[i].lastIndexOf(".")) + "afterCutted.png";

            //��ȡͼ��
            Mat image = imread(sourceImage[i]);
            if (image.empty()) {
                throw new Exception("image is empty");
            }

            //opencv�ҶȻ�
            Mat grayImg = ImageOpencvUtil.gray(image);

            //��ֵ��
            Mat binaryImg = ImageOpencvUtil.binaryzation(grayImg);

            //�����븯ʴ
            Mat corrodedImg = ImageOpencvUtil.corrosion(binaryImg);

            //��������
            List<RotatedRect> rects = ImageOpencvUtil.findTextRegion(corrodedImg);

            //��б����
            Mat correctedImg = ImageOpencvUtil.correction(rects, image);

            //��бУ����ü�
            Mat cuttedImg = ImageOpencvUtil.cutRect(correctedImg);

            //������бУ�����ü����ͼƬ
            imwrite(processedImage[i], cuttedImg);
        }
    }
}
