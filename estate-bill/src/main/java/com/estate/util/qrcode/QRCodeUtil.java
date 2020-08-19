package com.estate.util.qrcode;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mq
 * @date 2020/8/18 14:54
 * @description 二维码工具类
 */
public class QRCodeUtil {

   private static final String charset="utf-8";
   private static final String format="jpg";
   private static final Integer size=300;
   private static final Integer width=60;
   private static final Integer height=60;

    public static byte[] createQRCode(String content) throws WriterException, IOException {
        Map<EncodeHintType, Object> map = new HashMap<EncodeHintType, Object>();
        // 设置编码字符集
        map.put(EncodeHintType.CHARACTER_SET, charset);
        // 设置纠错等级L/M/Q/H,纠错等级越高越不易识别，当前设置等级为最高等级H
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        // 可设置范围为0-10，但仅四个变化0 / 1(2) / 3(4 5 6) / 7(8 9 10)
        map.put(EncodeHintType.MARGIN, 0);
        // 创建位矩阵对象
        BarcodeFormat barcodeFormat = BarcodeFormat.QR_CODE;
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, barcodeFormat, width, height,map);
        // 位矩阵对象转流对象
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, format, os);

        return os.toByteArray();
    }


}
