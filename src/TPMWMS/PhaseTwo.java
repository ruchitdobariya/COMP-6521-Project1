package TPMWMS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhaseTwo {
	long mergeTtime = 0, outputTime = 0, outputTtime = 0;	
	static int itertion = 0;
	int WriteCount = 0, readCount = 0, tupleCount1 = 1, tupleCount2 = 1, write = 0;
	int bufferCnt = 0, recordCount = 0;

	static String currentMergeFile = "", outputPath = "";
	static List<String> listOfFiles;
	static ArrayList<String> buffer = new ArrayList<>();
	static BufferedWriter bw;

	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}
	public int getWriteCount() {
		return WriteCount;
	}

	public void setWriteCount(int writeCount) {
		WriteCount = writeCount;
	}
	
	public long getMergeTtime() {
		return mergeTtime;
	}
	
	public long getOutputTtime() {
		return outputTtime;
	}

	public int getRecordCount() {
		return recordCount;
	}
	
	public int getTupleCount1() {
		return tupleCount1;
	}

	public void setTupleCount1(int tupleCount1) {
		this.tupleCount1 = tupleCount1;
	}

	public int getTupleCount2() {
		return tupleCount2;
	}

	public void setTupleCount2(int tupleCount2) {
		this.tupleCount2 = tupleCount2;
	}
	
	public int getWrite() {
		return write;
	}

	public void setWrite(int write) {
		this.write = write;
	}
	
	public String getOutputPath() {
		return outputPath;
	}

	public static void setOutputPath(String outputPath) {
		PhaseTwo.outputPath = outputPath;
	}

	public PhaseTwo(List<String> T1, List<String> T2) {
		listOfFiles = new ArrayList<>();
		listOfFiles.addAll(T1);
		listOfFiles.addAll(T2);
	}
	
	public long BufferWriter() throws IOException {
		long itertionStart = System.currentTimeMillis();
		recordCount += buffer.size();
		
		for (int i = 0; i < buffer.size(); i++) {
			bw.write(buffer.get(i));
			bw.newLine();
		}
		buffer.clear();
		
		
		return System.currentTimeMillis() - itertionStart;
	}

	public void mergeSort(List<String> blockList) {
		
		long startTime = System.currentTimeMillis();
		
		ArrayList<String> mergedFiles = new ArrayList<>();
		
		for (int i = 0; i < blockList.size(); i = i + 2) {
			
			currentMergeFile = Constants.BLOCK_PATH + itertion + "-Sublist-" + i + "_" + (i + 1);
			
			try {
				BufferedReader br1 = new BufferedReader(new FileReader(blockList.get(i)));
				BufferedReader br2 = null;
				
				if (i + 1 < blockList.size())
					br2 = new BufferedReader(new FileReader(blockList.get(i + 1)));

				bw = new BufferedWriter(new FileWriter(currentMergeFile));
				
				String tuple1 = null, tuple2 = null, currentTuple = "";
				int t1Count = 0, t2Count = 0, currentTCount = 0;
				
				if (br2 != null) {
					while (true) {
						if (tuple1 == null) {
							tuple1 = br1.readLine();
							if (tuple1 != null && tuple1.isEmpty()) {
								tuple1 = null;
							}
							
							tupleCount1++;
							if(tupleCount1 == 40) {
								++readCount;
								tupleCount1 = 0;
							}
						}

						if (tuple2 == null) {
							tuple2 = br2.readLine();
							if (tuple2 != null && tuple2.isEmpty()) {
								tuple2 = null;
							}
							
							tupleCount2++;
							if(tupleCount2 == 40) {
								++readCount;
								tupleCount2 = 0;
							}
						}
						
						if (tuple1 == null && tuple2 == null) {
							break;
						}
						
						if (tuple1 != null && tuple2 != null) {
							String t1 = tuple1.substring(0, 100);
							String t2 = tuple2.substring(0, 100);
							t1Count = Integer.parseInt(tuple1.substring(tuple1.lastIndexOf(":") + 1));
							t2Count = Integer.parseInt(tuple2.substring(tuple2.lastIndexOf(":") + 1));
							
							if (currentTuple.trim().length() > 0) {
								if (t1.equals(t2)) {
									if (currentTuple.equals(t1)) {
										currentTCount += (t1Count + t2Count);
									} else {
										buffer.add((currentTuple)+":"+String.valueOf(currentTCount));
										bufferCnt++;
										write++;
										if(write == 40) {
											++WriteCount;
											write = 0;
										}
										
										currentTuple = t1;
										currentTCount = t1Count + t2Count;
									}
									tuple1 = null;
									tuple2 = null;
								} else {
									if (currentTuple.equals(t1)) {
										currentTCount += t1Count;
										tuple1 = null;
									} else if (currentTuple.equals(t2)) {
										currentTCount += t2Count;
										tuple2 = null;
									} else {
										buffer.add((currentTuple)+":"+String.valueOf(currentTCount));
										bufferCnt++;
										write++;
										if(write == 40) {
											++WriteCount;
											write = 0;
										}
										
										if (t1.compareTo(t2) > 0) {
											currentTuple = t2;
											currentTCount = t2Count;
											tuple2 = null;
										} else{ // t1.compareTo(t2) < 0, meaning t1 < t2
											currentTuple = t1;
											currentTCount = t1Count;
											tuple1 = null;
										}
									}
								}
							} else { // This Block is executed once Time When Merge take place 1st Time
								if (t1.equals(t2)) {
									currentTuple = t1;
									currentTCount = t1Count + t2Count;

									tuple1 = null;
									tuple2 = null;
								} else {
									if (t1.compareTo(t2) > 0) {
										currentTuple = t2;
										currentTCount = t2Count;
										tuple2 = null;
									} else{ // t1.compareTo(t2) < 0, meaning t1 < t2
										currentTuple = t1;
										currentTCount = t1Count;
										tuple1 = null;
									}
								}

							}
						} else {
							if (tuple1 != null) {
								String t1 = tuple1.substring(0, 100);
								t1Count = Integer.parseInt(tuple1.substring(tuple1.lastIndexOf(":") + 1));
								
								if (currentTuple.trim().length() > 0) {
									if (currentTuple.equals(t1)) {
										currentTCount += t1Count;
										tuple1 = null;
									} else {
										buffer.add((currentTuple)+":"+String.valueOf(currentTCount));
										bufferCnt++;
										++write;
										if(write == 40) {
											++WriteCount;
											write = 0;
										}
										currentTuple = t1;
										currentTCount = t1Count;
										tuple1 = null;
									}
								} else {
									currentTuple = t1;
									currentTCount = t1Count;
									tuple1 = null;
								}

							} else {
								String t2 = tuple2.substring(0, 100);
								t2Count = Integer.parseInt(tuple2.substring(tuple2.lastIndexOf(":") + 1));
								
								if (currentTuple.trim().length() > 0) {
									if (currentTuple.equals(t2)) {
										currentTCount += t2Count;
										tuple2 = null;
									} else {
										buffer.add((currentTuple)+":"+String.valueOf(currentTCount));
										bufferCnt++;
										++write;
										if(write == 40) {
											++WriteCount;
											write = 0;
										}
										currentTuple = t2;
										currentTCount = t2Count;
										tuple2 = null;
									}
								} else {
									currentTuple = t2;
									currentTCount = t2Count;
									tuple2 = null;
								}

							}
						}
						
						
						if (bufferCnt == Constants.MAX_TUPLE_IN_MEMORY) {
							outputTime += BufferWriter();
							bufferCnt = 0;
						}
					}
				} else {
					while ((tuple1 = br1.readLine()) != null) {
						if(tuple1 == null || tuple1.isEmpty()) {
							break;
						}
						
						if (currentTuple.trim().length() > 0) {
							if (currentTuple.equals(tuple1.substring(0, 100))) {
								currentTCount += Integer.parseInt(tuple1.substring(tuple1.lastIndexOf(":") + 1));
							} else {
								buffer.add((currentTuple)+":"+String.valueOf(currentTCount));
								bufferCnt++;
								++write;
								if(write == 40) {
									++WriteCount;
									write = 0;
								}
								
								currentTuple = tuple1.substring(0, 100);
								currentTCount = Integer.parseInt(tuple1.substring(tuple1.lastIndexOf(":") + 1));
							}
						} else {
							currentTuple = tuple1.substring(0, 100);
							currentTCount = Integer.parseInt(tuple1.substring(tuple1.lastIndexOf(":") + 1));
						}
						
						if (bufferCnt == Constants.MAX_TUPLE_IN_MEMORY) {
							outputTime += BufferWriter();
							bufferCnt = 0;
						}
					}
				}
				
				buffer.add((currentTuple)+":"+String.valueOf(currentTCount));
				outputTime += BufferWriter();
				bw.close();
				
				mergedFiles.add(currentMergeFile);
				br1.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		mergeTtime  += (System.currentTimeMillis() - startTime);
		outputTtime += outputTime;
		
		System.out.println(
				"Phase 2 merging time for iteration  " + itertion + " : " + (System.currentTimeMillis() - startTime)
				+ "ms" + "(" + "~approx " + (System.currentTimeMillis() - startTime) / 1000.0 + "sec)");
		
		if (mergedFiles.size() > 1) {
			itertion++;
			mergeSort(mergedFiles);
		} else {
			setOutputPath(currentMergeFile);
		}
	}

	public void performMergeSort() {
		mergeSort(listOfFiles);
	}
}
