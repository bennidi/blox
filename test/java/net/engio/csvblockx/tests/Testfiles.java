package net.engio.csvblockx.tests;

/**
 * Organize collection of test files. Test files are grouped by functional aspects
 */
public class Testfiles {
	

	public static class Common{
		
		private static String BaseDir = "common/";

		public static String PaymentMultiblock = BaseDir + "payment.csv";

		public static String PaymentSingleBlock = BaseDir + "singleblock.csv";
		
		public static String CarRental = BaseDir + "car-rental.csv";

	}
	
	public static class Comparison{
		
		private static String BaseDir = "comparison/";

		public static String SingleBlockControl = BaseDir + "singleblock.csv.control";
		
		public static String SingleBlock = BaseDir + "singleblock.csv";
		
		
	}
	
	public static class Formats{
		
		private static String BaseDir = "formats/";

		public static String Format1 = BaseDir + "format1.csv";
		
		public static String Format1Control = BaseDir + "format1.csv.control";
		
	}
	
	public static class Encodings{
		
		private static String BaseDir = "encoding/";

		public static String Iso8859_15 = BaseDir + "iso-8859-15.csv";
		
		public static String Utf_8 = BaseDir + "utf-8.csv";
		
	}

}
