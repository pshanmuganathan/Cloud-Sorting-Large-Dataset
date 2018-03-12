import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SharedMem 
{
	public static int no_threads;
	private static List <String> chunk_list = new ArrayList<String>();
	public static int chunk_count = 80;
	public static long fileLinescount;
	public static String filepath= "temp";
	public static List <String> sortedlist = new ArrayList <String> ();

	public static void main(String[] args) throws Exception 
	{

no_threads = Integer.parseInt(args[0]);	
List <String> dataToSort = new ArrayList <String> ();



//Get the InputFiles ize
File file = new File("InputFile.txt");
fileLinescount = file.length();
file.delete();
//Creat the chunk list by giving them the same names as they used to generate through the GNU split command
	for(int i = 0; i<chunk_count; i++)
	{
			chunk_list.add("chunk-"+String.format("%02d", i)+".txt");	
	}
double startTime=System.currentTimeMillis();
//Creation of threads based on the user threadcount	
Thread [] thread = new Thread[no_threads];
		
		for (int i= 0 ;i <no_threads; i++)
		{
			
			thread[i] = new Thread(new Runnable()
			{	
				@Override
				public void run()
				{
					try
					{
						for(int i=0;i<chunk_count/no_threads;i++)
						{
							//Read the chunk File
							int x = ((Integer.parseInt(Thread.currentThread().getName())*chunk_count)/(no_threads))+i;
							  filepath = chunk_list.get(x);
							
							FileReader file = new FileReader(new File(filepath));
							BufferedReader bufRead = new BufferedReader(file);
							String line = null;
		
						while ((line = bufRead.readLine()) != null)
						{
							dataToSort.add(line);
			
						}
								//After Reading Quicksort the contents of each chunk file
							sortedlist=QuickSort(dataToSort,0,dataToSort.size()-1);
							//Once Quicksort is performed write the proper sorted output to the file again
							FileWriter filewrite = new FileWriter(new File(chunk_list.get(x)));
							BufferedWriter bufWrite = new BufferedWriter(filewrite);
							for(String str : sortedlist)
							{
								bufWrite.write(str+" "+"\n");
							}
							bufWrite.close();
							sortedlist.clear();
						}
					}
					catch(Exception e)	
					{
					}
				}
			});
			
			thread[i].setName(Integer.toString(i));
			thread[i].start();
		}
	
		//Wait for other threads to complete and join
		for(int j =0;j<no_threads;j++)
		{
			thread[j].join();
		}
		MergeSort();
			double endtime=System.currentTimeMillis();
		System.out.println("Time Taken in millis: "+(endtime-startTime));
	}

static List<String> QuickSort(List<String> dataToSort, int low, int high) throws IOException
	{
		int i = low;
		int j = high;
	//Center element as pivot
		String pivot = dataToSort.get(low + (high-low)/2).substring(0, 10);

		
		while(i <= j)
		{
			String temp = null;
			
			while(dataToSort.get(i).substring(0, 10).compareTo(pivot) < 0)
			{
				i++;
			}

			
			while(dataToSort.get(j).substring(0, 10).compareTo(pivot) > 0)
			{
				j--;
			}
		
			
			if(i<=j)
			{
				//Swap
				temp = dataToSort.get(i);
				dataToSort.set(i, dataToSort.get(j));
				dataToSort.set(j,temp);

				
				i++;
				j--;
			}
		}

		//Recursive Quicksort functions
		if (low < j)
			QuickSort(dataToSort,low, j);

	
		if (i < high)
			QuickSort(dataToSort,i, high);

		return dataToSort;
	}
private static void MergeSort() throws IOException 
	{
BufferedReader[] bufReaderArray = new BufferedReader[chunk_count];
		List <String> mergeList = new ArrayList<String>();
		List <String> keyValueList = new ArrayList<String>();
		
		
		//Copy first line from all the chunks and find minimum value among these chunks
		for (int i=0; i<chunk_count; i++)
		{
			bufReaderArray[i] = new BufferedReader(new FileReader(chunk_list.get(i)));

			String fileLine = bufReaderArray[i].readLine();

			if(fileLine != null)
			{
				mergeList.add(fileLine.substring(0, 10));
				keyValueList.add(fileLine);
			}
		}

		//Final Output File which will be sorted
		BufferedWriter bufw = new BufferedWriter(new FileWriter("MergedOutput.txt"));

		
//Merge and Sort Algorithm
		for(long j=0; j<fileLinescount;j++)
		{
			String minString = mergeList.get(0);
			int minFile = 0;

			for (int k = 0; k< chunk_count; k++)
			{
				if (minString.compareTo(mergeList.get(k)) > 0)
				{
					minString = mergeList.get(k);
					minFile = k;
				}
			}

			bufw.write(keyValueList.get(minFile)+"\n");
			mergeList.set(minFile,"-1");
			keyValueList.set(minFile,"-1");

			String temp = bufReaderArray[minFile].readLine();

			if (temp != null)
			{
				mergeList.set(minFile,temp.substring(0, 10));
				keyValueList.set(minFile, temp);
			}
			//if one of the files get finished earlier than others, copy other values and Quick Sort over them
				//write the output to the final sorted file
			else
			{
				
				j = fileLinescount;

				List <String> tempList = new ArrayList<String>();

				for(int i = 0;i< mergeList.size();i++)
				{
					if(keyValueList.get(i)!="-1")
						tempList.add(keyValueList.get(i));

					while((minString = bufReaderArray[i].readLine())!=null)
					{
						tempList.add(minString);
					}
				}

				tempList = QuickSort(tempList, 0, tempList.size()-1);
				int i =0;
				while(i < tempList.size())
				{
					bufw.write(tempList.get(i)+"\n");
					i++;
				}
				
				
			}
		}
		bufw.close();
	
		for(int i=0; i<chunk_count;i++)
			bufReaderArray[i].close();
	}

}

