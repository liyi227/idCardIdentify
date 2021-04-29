import org.example.utils.ImageFilterUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author ly
 * @since 2021/4/27
 */
public class testFilterZoom {
    public static void main(String[] args) throws Exception {
        //ԭͼ·��
        String sourceImage = "E:\\Desktop\\OCRTest\\image\\04.png";
        //������ͼƬ����·��
        String processedImage = sourceImage.substring(0, sourceImage.lastIndexOf(".")) + "afterFilterZoom.png";
        String _processedImage = sourceImage.substring(0, sourceImage.lastIndexOf(".")) + "afterFilter_Zoom.png";

        File image = new File(sourceImage);
        BufferedImage bufferedImage = ImageIO.read(image);

        //Filter������testZoom()����
        BufferedImage zoomedImg = ImageFilterUtil.testZoom(bufferedImage, 673, 425);
        File outFile = new File(processedImage);
        //�����ź��ͼƬ���浽outFile��Ӧ�ļ�λ�ô�
        ImageIO.write(zoomedImg, "png", outFile);

        //Filter������testZoom()����
        BufferedImage _zoomedImg = ImageFilterUtil.testZoom(zoomedImg, 453, 282);
        File _outFile = new File(_processedImage);
        //�����ź��ͼƬ���浽_outFile��Ӧ�ļ�λ�ô�
        ImageIO.write(_zoomedImg, "png", _outFile);
    }
}
