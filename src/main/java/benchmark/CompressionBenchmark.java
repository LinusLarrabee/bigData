package benchmark;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.xerial.snappy.Snappy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class CompressionBenchmark {

    private static byte[] generateSampleData(int sizeKB) {
        byte[] data = new byte[sizeKB * 1024];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i % 256);
        }
        return data;
    }

    private static long measureGzip(byte[] input) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        long startTime = System.nanoTime();
        gzipOutputStream.write(input);
        gzipOutputStream.close();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static long measureBzip2(byte[] input) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BZip2CompressorOutputStream bzip2OutputStream = new BZip2CompressorOutputStream(byteArrayOutputStream);
        long startTime = System.nanoTime();
        bzip2OutputStream.write(input);
        bzip2OutputStream.close();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static long measureSnappy(byte[] input) throws IOException {
        long startTime = System.nanoTime();
        byte[] compressed = Snappy.compress(input);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public static void main(String[] args) throws IOException {
        byte[] sampleData = generateSampleData(3); // 2.64KB = ~3KB, slightly rounded up

        long gzipTime = measureGzip(sampleData);
//        long bzip2Time = measureBzip2(sampleData);
        long snappyTime = measureSnappy(sampleData);

        System.out.println("Gzip compression time: " + (gzipTime / 1_000_000.0) + " ms");
//        System.out.println("Bzip2 compression time: " + (bzip2Time / 1_000_000.0) + " ms");
        System.out.println("Snappy compression time: " + (snappyTime / 1_000_000.0) + " ms");
    }
}
