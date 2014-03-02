package net.engio.blox.tests;

import net.engio.blox.blockdef.CsvBlockDescriptor;
import net.engio.blox.blockdef.CsvFileFormat;
import net.engio.blox.tools.CsvComparator;
import net.engio.blox.tools.CsvComparator.Difference;
import org.junit.Test;

import java.util.List;

public class CsvComparatorTest extends AbstractCsvUnitTest {

    @Test
    public void testLineOrder() {
        try {
            CsvBlockDescriptor blockDefinition = new CsvBlockDescriptor()
                    .starts().with().pattern("Daten.*")
                    .ends().with().pattern(CsvFileFormat.EmptyLine)
                    .headerSize(1)
                    .hasColumnNames(true);

            //comparison will ignore different block,line and column order by default
            CsvComparator comparator = new CsvComparator();
            List<Difference> differences = comparator.compare(
                    getTestResource(Testfiles.Comparison.SingleBlockControl),
                    getTestResource(Testfiles.Comparison.SingleBlock),
                    blockDefinition);

            // although the two files have a structural difference (two lines are swapped)
            // no differences are reported, since the comparator is configured to ignore
            // these kind of differences
            assertTrue(differences.size() == 0);

            //this comparator will recognize different line order
            comparator = new CsvComparator().setLineOrderImportant(true);
            differences = comparator.compare(
                    getTestResource(Testfiles.Comparison.SingleBlockControl),
                    getTestResource(Testfiles.Comparison.SingleBlock), blockDefinition);

            // now a difference will be found
            for (Difference diff : differences)
                System.out.println(diff);
            assertTrue(differences.size() == 1);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

}
