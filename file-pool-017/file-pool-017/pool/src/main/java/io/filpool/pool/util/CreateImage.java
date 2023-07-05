package io.filpool.pool.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CreateImage {

    /**
     * 图片放大缩小
     */
    private static BufferedImage zoomInImage(BufferedImage originalImage, int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;

    }

    private static BitMatrix updateBit(BitMatrix matrix, int margin) {
        int tempM = margin * 2;
        int[] rec = matrix.getEnclosingRectangle();   //获取二维码图案的属性
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
        resMatrix.clear();
        for (int i = margin; i < resWidth - margin; i++) {   //循环，将二维码图案绘制到新的bitMatrix中
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }
    //生成二维码
    public static BufferedImage getQRCode(String content) throws WriterException {
        int width = 400; // 图像宽度
        int height = 400; // 图像高度
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
        bitMatrix = updateBit(bitMatrix, 0);
        MatrixToImageConfig config = new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF); //左边是字的颜色，右边是底色
        BufferedImage bi = MatrixToImageWriter.toBufferedImage(bitMatrix, config);
        return zoomInImage(bi, width, height);//根据size放大、缩小生成的二维码
    }

    //图片转圆角
    public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0,0));
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return output;
    }


    public static ByteArrayOutputStream saveImage(String urlOrBase64String, String invatedCode, String active) throws Exception {
        int imageWidth = 750;  //图片的宽度
        int imageHeight = 1334; //图片的高度
        String imagePath = "/static/" + active + ".png";
        InputStream inputStream = CreateImage.class.getResourceAsStream(imagePath);
//        InputStream inputStream = CreateImage.class.getResourceAsStream("/static/bb.png");
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        //设置图片的背景色
        Graphics2D main = image.createGraphics();
        main.setColor(Color.white);
        main.fillRect(0, 0, imageWidth, imageHeight);

        //设置区域颜色
        main.setColor(Color.white);
        //填充区域并确定区域大小位置
        main.fillRect(0, 0, imageWidth, imageHeight);

        BufferedImage base = ImageIO.read(inputStream);
        if (null != base) {
            main.drawImage(base, 0, 0, imageWidth, imageHeight, null);
        }

        main.setColor(Color.black);
        main.setFont(new Font("SourceHanSansCN-Medium", Font.BOLD, 60));
        main.drawString(invatedCode, 195, 1175);

        //插入二维码
        BufferedImage QRCode = null;
        if (urlOrBase64String.startsWith("http"))
            QRCode = getQRCode(urlOrBase64String);
        else
            QRCode = base64ToBufferedImage(urlOrBase64String);
        main.drawImage(QRCode, 315, 520, 140, 140, null);
        main.dispose();
        BufferedImage rounded = makeRoundedCorner(image, 0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(rounded, "png", out);
        return out;
    }


    public static ByteArrayOutputStream saveImage(String url) throws Exception {
        int imageWidth = 1240;  //图片的宽度
        int imageHeight = 2024; //图片的高度

//        url = url.substring(url.lastIndexOf("/")+1);

        String name = "/static/" + "filpool_013_poster" + ".png";
        InputStream inputStream = CreateImage.class.getResourceAsStream(name);
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

        //设置图片的背景色
        Graphics2D main = image.createGraphics();
        main.setColor(new Color(0, 0, 0,0));
        main.fillRect(0, 0, imageWidth, imageHeight);

        //设置区域颜色
//        main.setColor(Color.WHITE);
        main.setColor(new Color(0, 0, 0,0));
        //填充区域并确定区域大小位置
        main.fillRect(0, 0, imageWidth, imageHeight);

        BufferedImage base = ImageIO.read(inputStream);
        if (null != base) {
            main.drawImage(base, 0, 0, imageWidth, imageHeight, null);
        }

        //插入二维码
        BufferedImage QRCode = getQRCode(url);
        main.drawImage(QRCode, 488, 1000, 268, 268, null);
        main.dispose();
//        BufferedImage rounded = makeRoundedCorner(image, 0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
//        ImageIO.write(rounded, "png", new File("/Users/kiip_out/IdeaProjects/fip-pool/src/main/resources/static/a.png"));
        return out;
    }

    public static BufferedImage base64ToBufferedImage(String QRCodeString) throws Exception {
        byte[] bytes1 = Base64.decodeBase64(QRCodeString);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
        return ImageIO.read(bais);
    }

    public static ByteArrayOutputStream saveQrCode(String content) throws WriterException, IOException {
        BufferedImage QRCode = getQRCode(content);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(QRCode, "png", out);
        return out;
    }

    /**
     * 通过网络获取图片
     */
    private static BufferedImage getUrlByBufferedImage(String url) throws Exception {
        URL urlObj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        // 连接超时
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(25000);
        // 读取超时 --服务器响应比较慢,增大时间
        conn.setReadTimeout(25000);
        conn.setRequestMethod("GET");
        conn.addRequestProperty("Accept-Language", "zh-cn");
        conn.addRequestProperty("Content-type", "image/jpeg");
        conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727)");
        conn.connect();
        BufferedImage bufImg = ImageIO.read(conn.getInputStream());
        conn.disconnect();
        return bufImg;
    }

    /**
     * 传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的
     * 用户头像地址
     */
    private static BufferedImage convertCircular(BufferedImage bi1) throws IOException {
        // 透明底的图片
        BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(), bi1.getHeight());
        Graphics2D g2 = bi2.createGraphics();
        g2.setClip(shape);
        // 使用 setRenderingHint 设置抗锯齿
        g2.drawImage(bi1, 0, 0, null);
        // 设置颜色
        g2.setBackground(Color.green);
        g2.dispose();
        return bi2;
    }

    private static BufferedImage getProfileImageToCircular(String url) throws Exception {
        BufferedImage bufferedImage =
                getUrlByBufferedImage(url);

        // 处理图片将其压缩成正方形的小图
        return convertCircular(bufferedImage);
    }


    public static void main(String[] args) throws Exception {
//        BufferedImage bufferedImage = getQRCode("http://www.baidu.com");

//        saveImage("asd", "en", 1);
//        BufferedImage rounded = makeRoundedCorner(graphicsGeneration( "李洋傻逼"), 100);
//        ImageIO.write(rounded, "png", new File("/Users/kiip_out/IdeaProjects/hubbleRating-master/src/main/resources/aa.jpg"));
        // ByteArrayOutputStream byteArrayOutputStream = saveImage("http://192.168.0.165:3000/#/index/4W7G30015");
//        String fileKey = "/invitation/" + "test" + "/miner/" + "b62954b830a740808c2bd3dd55a2fe90" + ".jpg";
//        StorageHandler.FileStreamUpload(fileKey, is);

    }
}
