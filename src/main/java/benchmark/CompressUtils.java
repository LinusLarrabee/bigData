package benchmark;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.xerial.snappy.Snappy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class CompressUtils {

    /**
     * 使用 GZIP 压缩数据
     *
     * @param data 要压缩的数据
     * @return 压缩后的字节数组
     * @throws IOException 如果发生 I/O 错误
     */
    public static byte[] compressWithGzip(byte[] data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(data);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 使用 Bzip2 压缩数据
     *
     * @param data 要压缩的数据
     * @return 压缩后的字节数组
     * @throws IOException 如果发生 I/O 错误
     */
    public static byte[] compressWithBzip2(byte[] data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (BZip2CompressorOutputStream bzip2OutputStream = new BZip2CompressorOutputStream(byteArrayOutputStream)) {
            bzip2OutputStream.write(data);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 使用 Snappy 压缩数据
     *
     * @param data 要压缩的数据
     * @return 压缩后的字节数组
     * @throws IOException 如果发生 I/O 错误
     */
    public static byte[] compressWithSnappy(byte[] data) throws IOException {
        return Snappy.compress(data);
    }
}
