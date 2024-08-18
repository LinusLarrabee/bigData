package benchmark;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.xerial.snappy.Snappy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressionBenchmark {

    public static byte[] readFile(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    public static void writeFile(String path, byte[] data) throws IOException {
        Files.write(Paths.get(path), data);
    }

    public static long[] measureGzip(byte[] input) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);

        long startTime = System.nanoTime();
        gzipOutputStream.write(input);
        gzipOutputStream.close();
        long endTime = System.nanoTime();

        byte[] compressedData = byteArrayOutputStream.toByteArray();
        long compressTime = endTime - startTime;

        startTime = System.nanoTime();
        GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(compressedData));
        while (gzipInputStream.read() != -1) {}
        gzipInputStream.close();
        endTime = System.nanoTime();
        long decompressTime = endTime - startTime;

        return new long[]{compressTime, decompressTime, compressedData.length};
    }

    public static long[] measureBzip2(byte[] input) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BZip2CompressorOutputStream bzip2OutputStream = new BZip2CompressorOutputStream(byteArrayOutputStream);

        long startTime = System.nanoTime();
        bzip2OutputStream.write(input);
        bzip2OutputStream.close();
        long endTime = System.nanoTime();

        byte[] compressedData = byteArrayOutputStream.toByteArray();
        long compressTime = endTime - startTime;

        startTime = System.nanoTime();
        BZip2CompressorInputStream bzip2InputStream = new BZip2CompressorInputStream(new ByteArrayInputStream(compressedData));
        while (bzip2InputStream.read() != -1) {}
        bzip2InputStream.close();
        endTime = System.nanoTime();
        long decompressTime = endTime - startTime;

        return new long[]{compressTime, decompressTime, compressedData.length};
    }

    public static long[] measureSnappy(byte[] input) throws IOException {
        long startTime = System.nanoTime();
        byte[] compressedData = Snappy.compress(input);
        long endTime = System.nanoTime();
        long compressTime = endTime - startTime;

        startTime = System.nanoTime();
        byte[] decompressedData = Snappy.uncompress(compressedData);
        endTime = System.nanoTime();
        long decompressTime = endTime - startTime;

        return new long[]{compressTime, decompressTime, compressedData.length};
    }

    public static void main(String[] args) throws IOException {
        String filePath = "/Users/sunhao/message.txt"; // 替换为你的文件路径
        byte[] fileData = readFile(filePath);

        System.out.println("Original file size: " + fileData.length + " bytes");

        long[] gzipResult = measureGzip(fileData);
        System.out.println("Gzip compression time: " + (gzipResult[0] / 1_000_000.0) + " ms");
        System.out.println("Gzip decompression time: " + (gzipResult[1] / 1_000_000.0) + " ms");
        System.out.println("Gzip compressed size: " + gzipResult[2] + " bytes");
        System.out.println("Gzip compression ratio: " + (double)gzipResult[2] / fileData.length);

        long[] bzip2Result = measureBzip2(fileData);
        System.out.println("Bzip2 compression time: " + (bzip2Result[0] / 1_000_000.0) + " ms");
        System.out.println("Bzip2 decompression time: " + (bzip2Result[1] / 1_000_000.0) + " ms");
        System.out.println("Bzip2 compressed size: " + bzip2Result[2] + " bytes");
        System.out.println("Bzip2 compression ratio: " + (double)bzip2Result[2] / fileData.length);

        long[] snappyResult = measureSnappy(fileData);
        System.out.println("Snappy compression time: " + (snappyResult[0] / 1_000_000.0) + " ms");
        System.out.println("Snappy decompression time: " + (snappyResult[1] / 1_000_000.0) + " ms");
        System.out.println("Snappy compressed size: " + snappyResult[2] + " bytes");
        System.out.println("Snappy compression ratio: " + (double)snappyResult[2] / fileData.length);
    }
}
