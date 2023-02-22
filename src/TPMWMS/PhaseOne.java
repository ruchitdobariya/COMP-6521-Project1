package TPMWMS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PhaseOne {
	QuickSort quickSort = new QuickSort();
	
	static int inputCount = 0;
	static int outputCount = 0;
	static int recordCount;

	long sortingTime = 0;
	int currentBlock = 0;
	BufferedReader br;
	
	public int getInputCount() {
		return inputCount;
	}

	public void setInputCount(int inputCount) {
		PhaseOne.inputCount = inputCount;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public static void setRecordCount(int recordCount) {
		PhaseOne.recordCount = recordCount;
	}


	public int getOutputCount() {
		return outputCount;
	}

	public void setOutputCount(int outputCount) {
		PhaseOne.outputCount = outputCount;
	}

	public int getCurrentBlock() {
		return currentBlock;
	}

	public void setCurrentBlock(int currentBlock) {
		this.currentBlock = currentBlock;
	}
	
	public static int getTotalBlocks(int fileSize) {
		return (int) Math.ceil((double) fileSize / Constants.BLOCK_SIZE);
	}
	
	public long getSortingTime() {
		return sortingTime;
	}

	public void setSortingTime(long sortingTime) {
		this.sortingTime = sortingTime;
	}


	public ArrayList<String> sortTuple(String tupleFileId, String path) {
		if (tupleFileId.equals(Constants.T2))
			currentBlock++;
		
		long dataCount = 0;
		ArrayList<String> temp = new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader(path));
			long startTime = System.currentTimeMillis();
			
			while (true) {
				String tuple = null;
				ArrayList<String> inputBuffer = new ArrayList<>();

				while((tuple = br.readLine()) != null) {
					inputBuffer.add(tuple);
					recordCount++;
					++dataCount;
					if (dataCount == Constants.MAX_TUPLE_IN_MEMORY) {
						dataCount = 0;
						++inputCount;
						++outputCount;
						break;
					}	
				}
				
				inputBuffer = quickSort.executeQuickSort(inputBuffer);

				String outputFile = Constants.BLOCK_PATH + "/Sublist-" + currentBlock;
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
				
				for (int i = 0; i < inputBuffer.size(); i++) {
					// all the tuple will have frequency one, 
					// which we will use in merge phase
					bw.write(inputBuffer.get(i)+":1");
					bw.newLine();
				}
				bw.close();
				
				temp.add(outputFile);

				if (tuple == null)
					break;
				
				currentBlock++;
			}
			
			sortingTime += (System.currentTimeMillis() - startTime);
			
			System.out.println("Time taken by Phase 1 for " + tupleFileId + " : " + 
					(System.currentTimeMillis() - startTime) + 
					"ms ("  + (System.currentTimeMillis() - startTime) / 1000.0 + "sec)");
		
		} catch (FileNotFoundException e) {
			System.out.println("The File doesn't Exist : " + path);
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return temp;
	}
}
