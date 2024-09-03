package benchmark;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class CompressUtilsTest {

    public static void main(String[] args) {
        // 设置输入输出文件路径
        String inputFilePath = "init.txt";
        String gzipOutputFilePath = "output.txt.gz";
        String bz2OutputFilePath = "output.txt.bz2";
        String snappyOutputFilePath = "output.txt.snappy";

        try {
            // 读取输入文件内容
            byte[] inputData = readFileToByteArray(new File(inputFilePath));

            // 使用 GZIP 压缩
            byte[] gzipCompressedData = CompressUtils.compressWithGzip(inputData);
            writeByteArrayToFile(gzipOutputFilePath, gzipCompressedData);
            System.out.println("GZIP 压缩完成：" + gzipOutputFilePath);

            // 使用 Bzip2 压缩
            byte[] bz2CompressedData = CompressUtils.compressWithBzip2(inputData);
            writeByteArrayToFile(bz2OutputFilePath, bz2CompressedData);
            System.out.println("Bzip2 压缩完成：" + bz2OutputFilePath);

            // 使用 Snappy 压缩
            byte[] snappyCompressedData = CompressUtils.compressWithSnappy(inputData);
            writeByteArrayToFile(snappyOutputFilePath, snappyCompressedData);
            System.out.println("Snappy 压缩完成：" + snappyOutputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 将文件读入字节数组
    private static byte[] readFileToByteArray(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    // 将字节数组写入文件
    private static void writeByteArrayToFile(String outputFilePath, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            fos.write(data);
        }
    }
}
