/*
        The following code is automatically generated by YSmart 12.01.
        Author: Rubao Lee, Yuan Yuan    
        Email: yuanyu@cse.ohio-state.edu
*/

package edu.osu.cse.ysmart.q2_1;
import java.io.IOException;
import java.util.*;
import java.text.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.partition.*;


public class q2_1001 extends Configured implements Tool{

	public static class Map extends  Mapper<Object, Text,Text,Text>{

		public void map(Object key, Text value, Context context) throws IOException,InterruptedException{

			String line = value.toString();
			String[] line_buf = new String[5];
			int prev=0,i=0,n=0;
			for(i=0,n=0,prev=0;i<line.length();i++){

				if (line.charAt(i) == '|'){
					line_buf[n] = line.substring(prev,i);
					n = n+1;
					prev = i+1;
				}
				if(n == 5)
					break;
			}

			if(n<5)
				line_buf[n] = line.substring(prev,i);
			context.write(new Text(line_buf[0] +"|"+line_buf[1] +"|"),new Text(line_buf[2] +"|"+line_buf[3] +"|"+line_buf[4] +"|"));
		}

	}

	public static class Reduce extends  Reducer<Text,Text,NullWritable,Text>{

		public void reduce(Text key, Iterable<Text> v, Context context) throws IOException,InterruptedException{

			Iterator values = v.iterator();
			NullWritable key_op = NullWritable.get();
			while(values.hasNext()){

				String tmp = values.next().toString();
				context.write(key_op,new Text(tmp));
			}

		}

	}

	public int run(String[] args) throws Exception{

		Configuration conf = new Configuration();
		conf.set("mapreduce.partition.keycomparator.options","-k1,1n -k2,2 ");
		conf.set("mapreduce.map.output.key.field.separator", "|");
		Job job = new Job(conf, "q2_1001");
		job.setJarByClass(q2_1001.class);
		job.setSortComparatorClass(KeyFieldBasedComparator.class);
		job.setPartitionerClass(KeyFieldBasedPartitioner.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		job.setNumReduceTasks(1);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		return (job.waitForCompletion(true) ? 0 : 1);
	}

	public static void main(String[] args) throws Exception {
 
		int res = ToolRunner.run(new Configuration(), new q2_1001(),args);
		System.exit(res);
	}

}

