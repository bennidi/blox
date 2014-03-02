package net.engio.csvblockx.samples;

import static net.engio.csvblockx.histogram.Histogram.Column;

import java.io.InputStream;

import net.engio.converse.value.ValueConversions;
import net.engio.csvblockx.blockdef.CsvBlockDefinition;
import net.engio.csvblockx.data.Column;
import net.engio.csvblockx.histogram.Histogrammer;
import net.engio.csvblockx.histogram.aggregates.Count;
import net.engio.csvblockx.histogram.aggregates.Max;
import net.engio.csvblockx.histogram.aggregates.Min;
import net.engio.csvblockx.reader.BlockXreader;
import net.engio.csvblockx.reader.BlockXreaderFactory;
import net.engio.csvblockx.tests.Testfiles;

public class Histogram {
	
	private static BlockXreaderFactory ReaderFactory = new BlockXreaderFactory("UTF-8");


    public static void main(String[] args){
        try {
            analyzeCarRental();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    protected static InputStream getTestResource(String resourceUrl){
        return Histogram.class.getClassLoader().getResourceAsStream(resourceUrl);
    }
	

	public static void analyzeCarRental() throws Exception{
		InputStream input =getTestResource(Testfiles.Common.CarRental);
		Column origin = Column("Origin (Anmietort)");
		Column destination = Column("Destination (Abgabeort)");
		Column salesDate = Column("Sales Date").as(ValueConversions.StringToDate("dd.MM.yyyy"));
		net.engio.csvblockx.histogram.Histogram histogram = new net.engio.csvblockx.histogram.Histogram()
			.For(origin, destination).Compute(new Count())
			.For(salesDate).Compute(new Min(salesDate), new Max(salesDate), new Count());
		CsvBlockDefinition blockdef = new CsvBlockDefinition()
			.starts().with().line(2)
			.ends().with().emptyLine()
			.hasColumnNames(true);

		//run the multiblock reader
		BlockXreader parser = ReaderFactory.createReaderFor(new Histogrammer(histogram, blockdef));
		parser.read(input);
		System.out.println(histogram);		
	}
	

}


