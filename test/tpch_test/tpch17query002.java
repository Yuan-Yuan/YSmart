/*
        The following code is automatically generated by YSmart 12.01.
        Author: Rubao Lee, Yuan Yuan    
        Email: yuanyu@cse.ohio-state.edu
*/

package edu.osu.cse.ysmart.tpch17query;
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


public class tpch17query002 extends Configured implements Tool{

	public static class Map extends  Mapper<Object, Text,IntWritable,Text>{

		private String filename;
		private int filetag = -1;
		public void setup(Context context) throws IOException, InterruptedException {

			int last_index = -1, start_index = -1;
			String path = ((FileSplit)context.getInputSplit()).getPath().toString();
			last_index = path.lastIndexOf('/');
			last_index = last_index - 1;
			start_index = path.lastIndexOf('/',last_index);
			filename = path.substring(start_index+1,last_index+1);
			if(filename.compareTo("LINEITEM")==0){
				filetag = 2;
			}
			if(filename.compareTo("PART")==0){
				filetag = 1;
			}
		}

		public void map(Object key, Text value,Context context) throws IOException,InterruptedException{

			String line = value.toString();
			String[] line_buf= line.split("\\|");
			BitSet dispatch = new BitSet(32);
			if(filetag ==1){

				if(line_buf[3].compareTo("Brand#34") == 0 && line_buf[6].compareTo("MED PACK") == 0){

					if(!(line_buf[3].compareTo("Brand#34") == 0 && line_buf[6].compareTo("MED PACK") == 0))
						dispatch.set( 0 );
					if(dispatch.isEmpty())
						context.write(new IntWritable(Integer.parseInt(line_buf[0])),new Text(1+"||"+Integer.parseInt(line_buf[0])+ "|" ));
					else
						context.write(new IntWritable(Integer.parseInt(line_buf[0])),new Text(1+"|"+dispatch.toString()+"|"+Integer.parseInt(line_buf[0])+ "|" ));
				}
			}

			if(filetag ==2){

				if(true || true){

					if(dispatch.isEmpty())
						context.write(new IntWritable(Integer.parseInt(line_buf[1])),new Text(2+"||"+Integer.parseInt(line_buf[1])+ "|" +Double.parseDouble(line_buf[4])+ "|" +Double.parseDouble(line_buf[5])+ "|" ));
					else
						context.write(new IntWritable(Integer.parseInt(line_buf[1])),new Text(2+"|"+dispatch.toString()+"|"+Integer.parseInt(line_buf[1])+ "|" +Double.parseDouble(line_buf[4])+ "|" +Double.parseDouble(line_buf[5])+ "|" ));
				}
			}

		}

	}

	public static class Reduce extends  Reducer<IntWritable,Text,NullWritable,Text>{

		public void reduce(IntWritable key, Iterable<Text> v, Context context) throws IOException,InterruptedException{

			Iterator values = v.iterator();
			ArrayList[] it_output = new ArrayList[2];
			for(int i=0;i<2;i++){
				it_output[i]=new ArrayList();
			}
			String tmp = "";
			ArrayList al_left_0= new ArrayList();
			ArrayList al_right_0= new ArrayList();
			Double[] result_1=new Double[1];
			ArrayList[] d_count_buf_1=new ArrayList[1];
			int al_line_1 = 0;
			for(int i=0;i<1;i++){
				result_1[i] = 0.0;
				d_count_buf_1[i] = new ArrayList();
			}

			while(values.hasNext()){
				String line = values.next().toString();
				String dispatch = line.split("\\|")[1];
				tmp = line.substring(2+dispatch.length()+1);
				String[] line_buf= tmp.split("\\|");
				if(line.charAt(0)=='2'&&(dispatch.length()==0 ||dispatch.indexOf("0")==-1))
					al_left_0.add(tmp);
				if(line.charAt(0)=='1'&&(dispatch.length()==0 ||dispatch.indexOf("0")==-1))
					al_right_0.add(tmp);
				if(line.charAt(0) =='2'&& (dispatch.length()==0 ||dispatch.indexOf('1')==-1)){
					result_1[0] +=Double.parseDouble(line_buf[1]);
					al_line_1++;
				}
			}
			String[] line_buf = tmp.split("\\|");
			for(int i=0;i<al_left_0.size();i++){
				String[] left_buf_0=((String)al_left_0.get(i)).split("\\|");
				for(int j=0;j<al_right_0.size();j++){
					String[] right_buf_0=((String)al_right_0.get(j)).split("\\|");
					it_output[0].add(Integer.parseInt(left_buf_0[0])+ "|" +Double.parseDouble(left_buf_0[1])+ "|" +Double.parseDouble(left_buf_0[2])+ "|" );
				}
			}
			result_1[0]=result_1[0]/al_line_1;
			it_output[1].add(Integer.parseInt(line_buf[0]) + "|"+(0.2 * (result_1[0])) + "|");
			ArrayList[] jfc_output = new ArrayList[1];
			for(int i=0;i<1;i++){
				jfc_output[i]=new ArrayList();
			}
			for(int i=0;i<it_output[0].size();i++){
				String[] jfc_left_buf_0 = ((String)it_output[0].get(i)).split("\\|");
				for(int j=0;j<it_output[1].size();j++){
					String[] jfc_right_buf_0 = ((String)it_output[1].get(j)).split("\\|");
					if(Double.parseDouble(jfc_left_buf_0[1]) < Double.parseDouble(jfc_right_buf_0[1])){
						jfc_output[0].add(1+ "|" +Double.parseDouble(jfc_left_buf_0[2])+ "|" );
					}
				}
			}
			NullWritable key_op=NullWritable.get();
			for(int i=0;i<jfc_output[0].size();i++){
				String jfc_result = (String)jfc_output[0].get(i);
				context.write(key_op, new Text(jfc_result));
			}
		}

	}

	public int run(String[] args) throws Exception{

		Configuration conf = new Configuration();
		Job job = new Job(conf,"tpch17query002");
		job.setJarByClass(tpch17query002.class);
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		FileInputFormat.addInputPath(job,new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));
		FileInputFormat.addInputPath(job,new Path(args[1]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));
		return (job.waitForCompletion(true) ? 0 : 1);
	}

	public static void main(String[] args) throws Exception {

			int res = ToolRunner.run(new Configuration(), new tpch17query002(), args);
			System.exit(res);
	}

}

