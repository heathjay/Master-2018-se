package solutions.assignment2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import examples.MapRedFileUtils;

public class MapRedSolution2
{
    /* your code goes in here*/
    public static class MapRecords extends Mapper<LongWritable, Text, Text, IntWritable>{
        private final static IntWritable one = new IntWritable(1);

        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
        {
           try{
                String valueString = value.toString();
                String[] singleTaxiData = valueString.split(",");
                String Tempdata = singleTaxiData[1];
                String[] DateData = Tempdata.split(" ");
                String HourMin = DateData[1];
                int hour  = Integer.parseInt(HourMin.substring(0,2));

                switch(hour){
                    case 0 : context.write(new Text("12am"), one);break;
                    case 1 : context.write(new Text("1am"), one);break;
                    case 2 : context.write(new Text("2am"), one);break;
                    case 3 : context.write(new Text("3am"), one);break;
                    case 4 : context.write(new Text("4am"), one);break; 
                    case 5 : context.write(new Text("5am"), one);break;
                    case 6 : context.write(new Text("6am"), one);break; 
                    case 7 : context.write(new Text("7am"), one);break;
                    case 8 : context.write(new Text("8am"), one);break; 
                    case 9 : context.write(new Text("9am"), one);break;
                    case 10 : context.write(new Text("10am"), one);break; 
                    case 11 : context.write(new Text("11am"), one);break;
                    case 12 : context.write(new Text("12pm"), one);break; 
                    case 13 : context.write(new Text("1pm"), one);break;
                    case 14 : context.write(new Text("2pm"), one);break; 
                    case 15 : context.write(new Text("3pm"), one);break;
                    case 16 : context.write(new Text("4pm"), one);break; 
                    case 17 : context.write(new Text("5pm"), one);break;
                    case 18 : context.write(new Text("6pm"), one);break; 
                    case 19 : context.write(new Text("7pm"), one);break;
                    case 20 : context.write(new Text("8pm"), one);break; 
                    case 21 : context.write(new Text("9pm"), one);break;
                    case 22 : context.write(new Text("10pm"), one);break; 
                    case 23 : context.write(new Text("11pm"), one);break; 
                    default: context.write(new Text("Error"), one);  
                }
              }catch(Exception e){
                System.out.println("Exception happens");
              }  
           
            
        }

     }
    
    //Reduce
       public static class ReduceRecords extends Reducer<Text, IntWritable, Text, IntWritable>
        {
            private IntWritable result = new IntWritable();

        
            protected void reduce(Text key, Iterable<IntWritable> values,
                Context context) throws IOException, InterruptedException
            {
                int sum = 0;
        
                for (IntWritable val : values)
                sum += val.get();
            
                result.set(sum);
                context.write(key, result);
            }
        }

    public static void main(String[] args) throws Exception
    {
        Configuration conf = new Configuration();

        String[] otherArgs =
            new GenericOptionsParser(conf, args).getRemainingArgs();
        
        if (otherArgs.length != 2)
        {
            System.err.println("Usage: MapRedSolution2 <in> <out>");
            System.exit(2);
        }
        
        Job job = Job.getInstance(conf, "MapRed Solution #2");
        
        /* your code goes in here*/

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        job.setMapperClass(MapRecords.class);
        job.setReducerClass(ReduceRecords.class);

        
        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
        
        MapRedFileUtils.deleteDir(otherArgs[1]);
        int exitCode = job.waitForCompletion(true) ? 0 : 1; 

        FileInputStream fileInputStream = new FileInputStream(new File(otherArgs[1]+"/part-r-00000"));
        String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fileInputStream);
        fileInputStream.close();
        
        String[] validMd5Sums = {"03357cb042c12da46dd5f0217509adc8", "ad6697014eba5670f6fc79fbac73cf83", "07f6514a2f48cff8e12fdbc533bc0fe5", 
            "e3c247d186e3f7d7ba5bab626a8474d7", "fce860313d4924130b626806fa9a3826", "cc56d08d719a1401ad2731898c6b82dd", 
            "6cd1ad65c5fd8e54ed83ea59320731e9", "59737bd718c9f38be5354304f5a36466", "7d35ce45afd621e46840627a79f87dac"};
        
        for (String validMd5 : validMd5Sums) 
        {
            if (validMd5.contentEquals(md5))
            {
                System.out.println("The result looks good :-)");
                System.exit(exitCode);
            }
        }
        System.out.println("The result does not look like what we expected :-(");
        System.exit(exitCode);
    }
}
