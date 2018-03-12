/*
 * HADOOP MAP-REDUCE
 * Mapper will generate the (key,value) pair of the data read from the text file
 * Reducer will generate the sorted Data obtained from Sort & Shuffle Phase
 */
import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.lib.IdentityReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class TeraSort extends Configured implements Tool
{
        public int run(String[] args) throws Exception
        {
                // To omit the line which has length less than 2
                // i.e if Args are correctly specified or Not
                if(args.length!=2)
                {        // To Run the Hadoop Code
					 System.out.println("USAGE: hadoop jar <JAR_FILE> <HDFS INPUT FILE PATH> <OUTPUT DIRECTORY PATH>");
                        return -1;
                }
                // Job Configuration
                JobConf jobConf = new JobConf(TeraSortHadoop.class);
                // Set the JobName as class name
                jobConf.setJobName(this.getClass().getName());
                // Set the File Paths Input and Output
                FileInputFormat.setInputPaths(jobConf, new Path(args[0]));
                FileOutputFormat.setOutputPath(jobConf, new Path(args[1]));
                // Configure the Mapper data class
                jobConf.setMapperClass(MapperSort.class);
                jobConf.setMapOutputKeyClass(Text.class);
                jobConf.setOutputKeyClass(Text.class);
                // Configure the Reduce data Class
                jobConf.setReducerClass(IdentityReducer.class);
                jobConf.setMapOutputValueClass(Text.class);
                jobConf.setOutputValueClass(NullWritable.class);
                // Run the Job on HADOOP
                JobClient.runJob(jobConf);
                return 0;
        }
		
		
        public static void main(String[] args) throws Exception
        {
                int exitCode = ToolRunner.run(new TeraSortHadoop(), args);
                System.exit(exitCode);
        }

        // Mapper Class extending MapReduce Class implementation
        public static class MapperSort extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>
        {
                
				private Text finalKey = new Text();
                private Text finalValue = new Text();
				// Constructor
                public MapperSort()
                {
                }

                // It will map Key 10 bytes, Value 90 Bytes output to reducer
                public void map(LongWritable Key, Text Value, OutputCollector<Text, Text> output, Reporter rep) throws IOException
                {
                        // Convert to String and split the data
                        String line = Value.toString();
                        // Fetch from data Key and Value
                        String newKey = line.substring(0, 10);
                        String newKey = line.substring(10,98);
                        //Give as the output to next phase
                        word.finalKey(newKey);
						word1.finalValue(newKey);
						context.write(finalKey, finalValue);
                }
        }

        // Reducer class
        public static class ReducerSort extends MapReduceBase implements Reducer<LongWritable, Text, Text, Text>
        {
                private Text finalKey = new Text();
                private Text finalValue = new Text();
				// Constructor
                public ReducerSort()
                {
                }
                public void reduce(Text key, Text value, Context context) throws IOException, InterruptedException
                {
                        finalKey.set(key.toString() + value.toString());
                        finalValue.set("");
                        context.write(finalKey, finalValue);
                }
        }
}

                       
