package benchmark;

import org.xerial.snappy.Snappy;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
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

    private static long measureGzip(byte[] input, String outputPath) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        long startTime = System.nanoTime();
        gzipOutputStream.write(input);
        gzipOutputStream.close();
        long endTime = System.nanoTime();

        // Save compressed data to a file
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath)) {
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
        }

        return endTime - startTime;
    }

    private static long measureSnappy(byte[] input, String outputPath) throws IOException {
        long startTime = System.nanoTime();
        byte[] compressed = Snappy.compress(input);
        long endTime = System.nanoTime();

        // Save compressed data to a file
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath)) {
            fileOutputStream.write(compressed);
        }

        return endTime - startTime;
    }

    public static void main(String[] args) throws IOException {
        byte[] sampleData = generateSampleData(6000); // 2.64KB = ~3KB, slightly rounded up

        long gzipTime = measureGzip(sampleData, "gzip_output.gz");
        long snappyTime = measureSnappy(sampleData, "snappy_output.snappy");

        System.out.println("Gzip compression time: " + (gzipTime / 1_000_000.0) + " ms");
        System.out.println("Snappy compression time: " + (snappyTime / 1_000_000.0) + " ms");
    }
}
