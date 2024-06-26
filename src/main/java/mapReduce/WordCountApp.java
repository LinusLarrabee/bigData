package mapReduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCountApp {
    public static void main(String[] args) throws Exception {

        // 确保能打印出接收到的参数，检查是否正确传递
        System.out.println("Input Path: " + args[0]);
        System.out.println("Output Path: " + args[1]);

        if (args.length != 2) {
            System.err.println("Usage: WordCountApp <input path> <output path>");
            System.exit(-1);
        }

        Job job = Job.getInstance(new Configuration(), "Word Count");
        job.setJarByClass(WordCountApp.class);
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
