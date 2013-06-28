/*
        The following code is automatically generated by YSmart 12.01.
        Author: Rubao Lee, Yuan Yuan    
        Email: yuanyu@cse.ohio-state.edu
*/

package edu.osu.cse.ysmart.q3_1;
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


public class q3_1003 extends Configured implements Tool{

	public static class Map extends  Mapper<Object, Text,Text,Text>{

		private int left = 0;
		public void setup(Context context) throws IOException, InterruptedException {

			int last_index = -1, start_index = -1;
			String path = ((FileSplit)context.getInputSplit()).getPath().toString();
			last_index = path.lastIndexOf('/');
			last_index = last_index - 1;
			start_index = path.lastIndexOf('/',last_index);
			String f_name = path.substring(start_index+1,last_index+1);
			if(f_name.compareTo("q3_1004") == 0 )
				left = 1;
		}
		public void map(Object key, Text value,Context context) throws IOException,InterruptedException{

			String line = value.toString();
			int prev=0,i=0,n=0;
			if(this.left == 1){

				String[] line_buf = new String[4];
				for(i=0,n=0,prev=0;i<line.length();i++){

					if (line.charAt(i) == '|'){
						line_buf[n] = line.substring(prev,i);
						n = n+1;
						prev = i+1;
					}
					if(n == 4)
						break;
				}

			if(n<4)
				line_buf[n] = line.substring(prev,i);
				context.write(new Text(line_buf[3]+ "|" ), new Text("L"+"|" +line_buf[0]+ "|"+line_buf[1]+ "|"+line_buf[2]+ "|"+line_buf[3]+ "|"));
			}else{

				String[] line_buf = new String[5];
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
				if(Integer.parseInt(line_buf[4]) >= 1992 && Integer.parseInt(line_buf[4]) <= 1997){

					context.write(new Text(line_buf[0]+ "|" ), new Text("R"+"|" +Integer.parseInt(line_buf[4])+ "|" +line_buf[0]+ "|" ));
				}
			}

		}

	}

	public static class Reduce extends  Reducer<Text,Text,NullWritable,Text>{

		public void reduce(Text key, Iterable<Text> v, Context context) throws IOException,InterruptedException{

			Iterator values = v.iterator();
			ArrayList al_left = new ArrayList();
			ArrayList al_right = new ArrayList();
			while(values.hasNext()){

				String tmp = values.next().toString();
				if(tmp.charAt(0) == 'L'){

					al_left.add(tmp.substring(2));
				}else{

					al_right.add(tmp.substring(2));
				}

			}

			NullWritable key_op = NullWritable.get();
			for(int i=0;i<al_left.size();i++){

				String[] left_buf = ((String)al_left.get(i)).split("\\|");
				for(int j=0;j<al_right.size();j++){

					String[] right_buf = ((String)al_right.get(j)).split("\\|");
					context.write(key_op, new Text(left_buf[0]+ "|" +left_buf[1]+ "|" +Integer.parseInt(right_buf[0])+ "|" +Double.parseDouble(left_buf[2])+ "|" ));
				}

			}

		}

	}

	public int run(String[] args) throws Exception{

		Configuration conf = new Configuration();
		Job job = new Job(conf,"q3_1003");
		job.setJarByClass(q3_1003.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		FileInputFormat.addInputPath(job,new Path(args[0]));
		FileInputFormat.addInputPath(job,new Path(args[1]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));
		return (job.waitForCompletion(true) ? 0 : 1);
	}

	public static void main(String[] args) throws Exception {

			int res = ToolRunner.run(new Configuration(), new q3_1003(), args);
			System.exit(res);
	}

}

