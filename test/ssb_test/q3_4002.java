/*
        The following code is automatically generated by YSmart 12.01.
        Author: Rubao Lee, Yuan Yuan    
        Email: yuanyu@cse.ohio-state.edu
*/

package edu.osu.cse.ysmart.q3_4;
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


public class q3_4002 extends Configured implements Tool{

	public static class Map extends Mapper<Object, Text,Text,Text>{

		Hashtable<String,Double>[] adv_gb_output=new Hashtable[1];
		Hashtable<String,Integer> adv_count_output=new Hashtable<String,Integer>();
		public void setup(Context context) throws IOException, InterruptedException {

			for(int i =0;i<1;i++){
				adv_gb_output[i] = new Hashtable<String,Double>();
			}
		}

		public void cleanup(Context context) throws IOException, InterruptedException {

			for(String tmp_key:adv_count_output.keySet()){
				Double count = (double) adv_count_output.get(tmp_key);
				Double tmp_0 = adv_gb_output[0].get(tmp_key);
				context.write(new Text(tmp_key.toString()),new Text(tmp_0 + "&"+"|"));
			}
		}
		public void map(Object key, Text value, Context context) throws IOException,InterruptedException{

			String line = value.toString();
			String[] line_buf = new String[4];
			int prev=0,i=0,n=0;
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
			String hash_key = line_buf[0]+"|"+line_buf[1]+"|"+line_buf[2]+"|";
			if(adv_count_output.containsKey(hash_key)){
				Integer count = adv_count_output.get(hash_key)+1;
				adv_count_output.put(hash_key,count);
			}else{
				adv_count_output.put(hash_key,1);
			}
			if(adv_gb_output[0].containsKey(hash_key)){
				Double sum_tmp = (double)Double.parseDouble(line_buf[3]);
				sum_tmp += adv_gb_output[0].get(hash_key);
				adv_gb_output[0].put(hash_key, sum_tmp);
			}else{
				adv_gb_output[0].put(hash_key,(double)Double.parseDouble(line_buf[3]));
			}
		}

	}

	public static class Reduce extends Reducer<Text,Text,NullWritable,Text>{

		public void reduce(Text key, Iterable<Text> v, Context context) throws IOException,InterruptedException{

			Iterator values = v.iterator();
			Double[] result = new Double[1];
			ArrayList[] d_count_buf = new ArrayList[1];
			String tmp = "";
			for(int i=0;i<1;i++){

				result[i] = 0.0;
				d_count_buf[i] = new ArrayList();
			}

			int[] al_line = new int[1];
			for(int i=0;i<1;i++){
				al_line[0] = 0;
			}
			int tmp_count = 0;
			while(values.hasNext()){

				String[] tmp_buf = values.next().toString().split("\\|");
				tmp = key.toString();
				String[] agg_tmp;
				agg_tmp = tmp_buf[0].split("&");
				result[0] += Double.parseDouble(agg_tmp[0]);
				tmp_count++;
			}
			String[] line_buf = tmp.split("\\|");
			NullWritable key_op = NullWritable.get();
			context.write(key_op,new Text(Integer.parseInt(line_buf[2]) + "|"+(result[0]) + "|"+line_buf[0] + "|"+line_buf[1] + "|"+Integer.parseInt(line_buf[2]) + "|"+(result[0]) + "|"));
		}

	}

	public int run(String[] args) throws Exception{

		Configuration conf = new Configuration();
		Job job = new Job(conf,"q3_4002");
		job.setJarByClass(q3_4002.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		return (job.waitForCompletion(true) ? 0 : 1);
	}

	public static void main(String[] args) throws Exception {

			int res = ToolRunner.run(new Configuration(), new q3_4002(), args);
			System.exit(res);
	}

}
