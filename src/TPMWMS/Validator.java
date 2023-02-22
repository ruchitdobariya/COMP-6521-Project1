package TPMWMS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Validator {
	public static int recordCount = 0;
	public static int recordFrequencySum = 0;
	
	public static void CountNumberOfRecords(String outputFile) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(outputFile));
		String record = null;

		while ((record = br.readLine()) != null) {
			if (record.trim().length() > 0) {
				recordFrequencySum += 
						Integer.parseInt(record.substring(record.lastIndexOf(":") + 1));
				recordCount++;
			}
		}
		br.close();
	}
}
