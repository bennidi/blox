package net.engio.blox.samples;

import net.engio.converse.value.ValueConversions;
import net.engio.blox.blockdef.CsvBlockDescriptor;
import net.engio.blox.data.Column;
import net.engio.blox.histogram.Histogram;
import net.engio.blox.histogram.Histogrammer;
import net.engio.blox.histogram.aggregates.Count;
import net.engio.blox.histogram.aggregates.Max;
import net.engio.blox.histogram.aggregates.Min;
import net.engio.blox.reader.BlockXreader;
import net.engio.blox.tests.Testfiles;

import java.io.InputStream;

import static net.engio.blox.histogram.Histogram.Column;

public class HistogramSample {
	
	private static BlockXreader.Factory ReaderFactory = new BlockXreader.Factory("UTF-8");


    public static void main(String[] args){
        try {
            analyzeCarRental();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    protected static InputStream getTestResource(String resourceUrl){
        return HistogramSample.class.getClassLoader().getResourceAsStream(resourceUrl);
    }
	

	public static void analyzeCarRental() throws Exception{
		InputStream input =getTestResource(Testfiles.Common.CarRental);
		Column origin = Column("Origin (Anmietort)");
		Column destination = Column("Destination (Abgabeort)");
		Column salesDate = Column("Sales Date").as(ValueConversions.StringToDate("dd.MM.yyyy"));
		Histogram histogram = new Histogram()
			.For(origin, destination).Compute(new Count())
			.For(salesDate).Compute(new Min(salesDate), new Max(salesDate), new Count());
		CsvBlockDescriptor blockdef = new CsvBlockDescriptor()
			.starts().with().line(2)
			.ends().with().emptyLine()
			.hasColumnNames(true);

		//run the multiblock reader
		BlockXreader parser = ReaderFactory.createReaderFor(new Histogrammer(histogram, blockdef));
		parser.read(input);
		System.out.println(histogram);		
	}
	

}


