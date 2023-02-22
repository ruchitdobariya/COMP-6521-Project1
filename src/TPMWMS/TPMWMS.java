package TPMWMS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TPMWMS {
	static String fileName1 = Constants.INPUT_PATH + Constants.INPUT_FILE1;
	static String fileName2 = Constants.INPUT_PATH + Constants.INPUT_FILE2;
	
	private static void buildOutputDirectory(String outputPath) {
		try {
			File outputDir = new File(Constants.OUTPUT_PATH);
			if (!outputDir.exists() && outputDir.mkdir()) {
				System.out.println("Successfully created Output Directory.");
			} else if(!outputDir.exists()) {
				System.out.println("Failed to create Output Directory.");
			}
			
			BufferedReader br = new BufferedReader(new FileReader(outputPath));
			FileWriter fw = new FileWriter(Constants.OUTPUT_PATH + "output.txt", true);
			String temp;

			while ((temp = br.readLine()) != null) {
				fw.write(temp + "\n");
				fw.flush();
			}
			br.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void buildOutputDirectory() {
		File outputDir = new File(Constants.OUTPUT_PATH);
		if (!outputDir.exists() && outputDir.mkdir()) {
			System.out.println("Successfully created Directory For Output.");
		} else if (outputDir.isFile()) {
		} else {
			String fileList[] = outputDir.list();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].trim().length() >= 1) {
					File currentBlockFiles = new File(outputDir.getPath(), fileList[i]);
					currentBlockFiles.delete();
				}
			}
			if(outputDir.delete() && outputDir.mkdir()) {
				System.out.println("Successfully deleted and created Directory For Output.");
			} else if (!outputDir.exists()) {
				System.out.println("Failed to delete and create Directory For Output.");
			}
		}
	}

	public static void buildBlockDirectory() {
		File deleteBlocks = new File(Constants.BLOCK_PATH);
		if (!deleteBlocks.exists() && deleteBlocks.mkdir()) {
			System.out.println("Successfully created Directory For storing blocks.");
		} else if (deleteBlocks.isFile()) {
		} else {
			String fileList[] = deleteBlocks.list();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].trim().length() >= 1) {
					File currentBlockFiles = new File(deleteBlocks.getPath(), fileList[i]);
					currentBlockFiles.delete();
				}
			} 
			if(deleteBlocks.delete() && deleteBlocks.mkdir()) {
				System.out.println("Successfully deleted and created Directory For storing blocks.");
			} else if(!deleteBlocks.exists()) {
				System.out.println("Failed to delete and create Directory For storing blocks.");
			}
		}
	}

	private static double getMemorySize() {
		return (double) Runtime.getRuntime().maxMemory() / (1024 * 1024);
	}
	
	public static int getTotalBlocks(final int fileSize, final int blockSize) {
		return (int) Math.ceil((double) fileSize / blockSize);
	}

	public static void main(String[] args) throws InterruptedException, NumberFormatException, IOException {		
		System.out.println("=====================Deleting Old Files From Directory====================");
		buildBlockDirectory();
		buildOutputDirectory();
		System.out.println("--------------------------------------------------------------------------");
		
		
		System.out.println("===========================TPMMS-Mini-Project-1===========================");
		System.gc();
		System.out.println("Max Memory Size(Including memory consumed by JVM Processes) = " + getMemorySize()+" Megabyte");
		System.out.println("Tuple Size (In Bytes) = " + Constants.TUPLE_SIZE);
		System.out.println("--------------------------------------------------------------------------");
		
		PhaseOne phaseOne = new PhaseOne();
		System.out.println("===============================T1 - Phase-1===============================");
		List<String> T1 = phaseOne.sortTuple("T1", fileName1);
		int blockCount1 = 0;
		int recordCount1 = phaseOne.getRecordCount();
		if( recordCount1 % Constants.MAX_RECORD  == 0) {
			blockCount1 = recordCount1 / Constants.MAX_RECORD;
		} else {
			blockCount1 = (recordCount1 / Constants.MAX_RECORD) +1 ;

		}
		System.out.println("Number of records in T1 = " + recordCount1);
		System.out.println("Number of blocks for T1 = " + blockCount1);
		
		System.out.println("===============================T2 - Phase-1===============================");
		List<String> T2 = phaseOne.sortTuple("T2", fileName2);
		int blockCount2 = 0;
		int recordCount2 = phaseOne.getRecordCount() - recordCount1;
		if( recordCount2 % Constants.MAX_RECORD  == 0) {
			blockCount2 = recordCount2 / Constants.MAX_RECORD;
		} else {
			blockCount2 = (recordCount2 / Constants.MAX_RECORD) +1 ;

		}		
		System.out.println("Number of records in T2 = " + recordCount2);
		System.out.println("Number of blocks for T2 = " + blockCount2);
		
		System.out.println("==============================Output-Phase-1==============================");
		System.out.println("Total time taken by Phase 1 (T1+T2) = " + phaseOne.getSortingTime() + 
				"ms ("  + phaseOne.getSortingTime() / 1000.0 + "sec)");
		System.out.println("Total number of records = " + phaseOne.getRecordCount());
		System.out.println("Total number of Blocks = " + (blockCount1 + blockCount2));
		int phaseOneDiskIO = 2 * (blockCount1 + blockCount2);
		System.out.println("Sorted Disk IO = " + phaseOneDiskIO);
		System.out.println("--------------------------------------------------------------------------");
		System.gc();
		
		System.out.println("=================================Phase-2==================================");
		PhaseTwo phaseTwo = new PhaseTwo(T1, T2);
		phaseTwo.performMergeSort();
		System.out.println("\nPhase 2 Total time = " + phaseTwo.getMergeTtime() + "ms" + " (" + phaseTwo.getMergeTtime() / 1000.0 + " sec)");
		System.out.println("Phase 2 Prepare Output Time = " + (phaseTwo.getMergeTtime() - phaseTwo.getOutputTtime())
				+ "ms" + " (" + (phaseTwo.getMergeTtime() - phaseTwo.getOutputTtime()) / 1000.0 + " sec)");
		System.out.println("Phase 2 Write Output Time = " + phaseTwo.getOutputTtime() + "ms" + " (" + phaseTwo.getOutputTtime() / 1000.0 + " sec)");
		
		System.out.println("Merge Phase - Number of I/O = " + (phaseTwo.getReadCount() + phaseTwo.getWriteCount()));		
		System.out.println("Phase 2 Number of blocks written  = " + phaseTwo.getWriteCount());
		System.out.println("Phase 2 number of output records (including all iteration) = " + (phaseTwo.getRecordCount()));
		
		System.out.println("==============================Output-Phase-2==============================");
		System.out.println("Total time Phase 1 & Phase 2 = " + (phaseTwo.getMergeTtime() + phaseOne.getSortingTime()) + "ms");
		System.out.println("Total time Phase 1 & Phase 2 = " + ((phaseTwo.getMergeTtime() + phaseOne.getSortingTime()) / 1000.0) + " sec");
		System.out.println("Total Number of I/O = " + (phaseOneDiskIO + phaseTwo.getReadCount() + phaseTwo.getWriteCount()));
		buildOutputDirectory(phaseTwo.getOutputPath());
		System.gc();
		
		Validator.CountNumberOfRecords(Constants.OUTPUT_PATH + "output.txt");
		System.out.println("Total Number of records in final output = " + Validator.recordCount);
		System.out.println("Sum of frequency of all records in final output = " + Validator.recordFrequencySum);
		System.out.println("Which should be same as Number of given input = " + phaseOne.getRecordCount());

		System.out.println("--------------------------------------------------------------------------");
	}
}
