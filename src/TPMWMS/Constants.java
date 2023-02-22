package TPMWMS;

public class Constants {

	public static final int TUPLE_SIZE = 100;
	public static final int BLOCK_SIZE = 4096;

	public static final int MAX_RECORD = 40;
	public static final int M = 101;
	public static final int MAX_TUPLE_IN_MEMORY = Constants.MAX_RECORD * Constants.M;
	
	public static final String INPUT_PATH = "./inputfiles/";
	public static final String BLOCK_PATH = "./sublists/";
	public static final String OUTPUT_PATH = "./outputfiles/";
	
	public static final String INPUT_FILE1 = "r1.txt";
	public static final String INPUT_FILE2 = "r2.txt";
	public static final String T1 = "T1";
	public static final String T2 = "T2";
}
