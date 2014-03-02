package net.engio.blox.tools;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.engio.blox.blockdef.CsvBlockDescriptor;
import net.engio.blox.data.CsvEntry;
import net.engio.blox.data.CsvBlock;
import net.engio.blox.reader.BlockXreader;

/**
 * 
 * The csv comparator can be used to compare two different csv documents. It will
 * create a list of differences (see {@link Difference}) representing the
 * differences found between the two documents.
 * 
 * The comparator can be customized to control how different types of
 * differences are treated. The order of the csv entries, for example, can be of
 * importance or not matter at all, such that a target document that consists of all
 * entries of the source document but in different order will be considered equal.
 *
 * 
 * @author Benjamin Diedrichsen
 * 
 */
public class CsvComparator {

	private boolean blockOrderImportant;

	private boolean lineOrderImportant;

	private boolean detailedAnalysis;

	private BlockXreader.Factory blockXreader;

	public CsvComparator(boolean blockOrderImportant,
			boolean lineOrderImportant,
			boolean detailedAnalysis, String encoding) {
		super();
		this.blockOrderImportant = blockOrderImportant;
		this.lineOrderImportant = lineOrderImportant;
		this.detailedAnalysis = detailedAnalysis;
		this.blockXreader = new BlockXreader.Factory(encoding);
	}

	public CsvComparator() {
		this(false, false, true, "UTF-8");
	}

	/**
	 * Control whether the block order within the documents matters or not
	 * 
	 * @param value
	 *            Whether the block ordering matters or not. If the blocks in
	 *            the documents have different ordering and the value is set to
	 *            true, then the comparison will yield a difference for each
	 *            block that is not in order. If set to false, different
	 *            ordering will be ignored and no difference will be reported.
	 * @return The comparator
	 */
	public CsvComparator setBlockOrderImportant(boolean value) {
		blockOrderImportant = value;
		return this;
	}

	/**
	 * Control whether the line order within a block matters or not
	 * 
	 * @param value
	 *            Whether the line ordering matters or not. If the lines in the
	 *            blocks have different ordering and the value is set to true,
	 *            then the comparison will yield a difference for each line that
	 *            is not in order. If set to false, different ordering will be
	 *            ignored and no difference will be reported.
	 * @return The comparator
	 */
	public CsvComparator setLineOrderImportant(boolean value) {
		lineOrderImportant = value;
		return this;
	}

	/**
	 * A detailed analysis comprises an analysis of each block and line. Since
	 * this might be expensive it can be skipped in case a difference in the
	 * number of blocks or lines is found. Instead of detailed differences on
	 * line or value level, the result will only contain a difference that
	 * indicates that there are differences between the two documents
	 * 
	 * @return The comparator
	 */
	public CsvComparator setDetailedAnalysis(boolean value) {
		detailedAnalysis = value;
		return this;
	}



    public List<Difference> compare(InputStream csv1, InputStream csv2, CsvBlockDescriptor...definitions) throws Exception {
        CsvBlockBuilder[] doc1 = CsvBlockBuilder.fromBlockDefinitions(definitions);
        CsvBlockBuilder[] doc2 = CsvBlockBuilder.fromBlockDefinitions(definitions);
        blockXreader.createReaderFor(doc1).read(csv1);
        blockXreader.createReaderFor(doc2).read(csv2);

        List<CsvBlock> result1 = CsvBlockBuilder.getBlocks(doc1);
        List<CsvBlock> result2 = CsvBlockBuilder.getBlocks(doc2);

        return compare(result1, result2);

    }

	public List<Difference> compare(List<CsvBlock> csv1, List<CsvBlock> csv2) {
		List<Difference> differences = new LinkedList<Difference>();
		if (csv1.size() != csv2.size()) {
			differences.add(new Difference("Not the same number of blocks")
					.addDetail("Number of blocks in source", csv1.size())
					.addDetail("Number of blocks in target", csv2.size()));
			if (!detailedAnalysis)
				return differences;
		}

		// compare each block
		for (int i = 0; i < csv1.size(); i++) {
			List<Difference> blockDifferences = compare(csv1.get(i),
					csv2.get(i));
			if (blockDifferences.size() > 0) {

				if (blockOrderImportant) {
					// if the block order is important than the differences are
					// valid
					differences.addAll(blockDifferences);
					differences.add(new Difference("Blocks do not match")
							.addDetail("Block number", i).addReasons(
									blockDifferences));
				}

				// else try to find the block in a different position
				else {
					boolean valueMatches = false;
					List<List<Difference>> blockDeltas = new ArrayList<List<Difference>>(
							csv2.size());
					for (int j = 0; j < csv2.size(); j++) {
						blockDifferences = compare(csv1.get(i), csv2.get(j));
						blockDeltas.add(blockDifferences);
						valueMatches = blockDifferences.size() == 0 ? true
								: valueMatches;
						if (valueMatches)
							break; // if matching block is found exit the loop
					}
					if (!valueMatches) { // if no matching block is found
						differences.add(new Difference(
								"No matching block found")
								.addReasons(findMostSimilarItem(blockDeltas)));
						// differences.addAll(findMostSimilarItem(blockDeltas));
					}
				}
			}
		}

		return differences;
	}

	private List<Difference> findMostSimilarItem(List<List<Difference>> deltas) {
		List<Difference> nearest = deltas.get(0);
		for (List<Difference> current : deltas) {
			if (current.size() < nearest.size())
				nearest = current;
		}
		return nearest;
	}

	private Set<String> toSet(String[] array) {
		Set<String> headers = new HashSet<String>();
		for (String header : array) {
			headers.add(header);
		}
		return headers;
	}

	public List<Difference> compare(CsvBlock block1, CsvBlock block2) {
		List<Difference> differences = new LinkedList<Difference>();

		// check dimensions
		if (block1.getNumberOfRows() != block2.getNumberOfRows()) {
			differences.add(new Difference(
					"Not the same number of rows in blocks").addDetail(
					"Row count in source block", block1.getNumberOfRows())
					.addDetail("Row count in target block",
							block2.getNumberOfRows()));
			return differences;
		}
		// if (block1.getBody().getNumberOfColumns() !=
		// block2.getBody().getNumberOfColumns()) {
		// differences.add(new
		// Difference("Not the same number of columns in blocks")
		// .addDetail("Column count in source block",block1.getBody().getNumberOfColumns())
		// .addDetail("Column count in target block",block2.getBody().getNumberOfColumns()));
		// return differences;
		// }

		// check headers, since the number of columns is the same
		// and due to the semantics of java.util.Set a test in one direction
		// suffices
		if (!toSet(block1.getBody().getColumnNames()).containsAll(
				toSet(block2.getBody().getColumnNames()))) {
			differences.add(new Difference("Headers are not the same"));

		}

		// compare the lines contained in the blocks
		List<CsvEntry> lines1 = block1.getEntries();
		List<CsvEntry> lines2 = block2.getEntries();

		for (int i = 0; i < lines1.size(); i++) {
			List<Difference> lineDifferences = compare(lines1.get(i),
					lines2.get(i));
			if (lineDifferences.size() > 0) {

				if (lineOrderImportant) {
					differences.add(new Difference("Difference in line [" + i  + "]")
							.addDetail("Line number", i).addReasons(
									lineDifferences));
				}
				// else try to find the value in a different column
				else {
					boolean valueMatches = false;
					List<List<Difference>> lineDeltas = new ArrayList<List<Difference>>(
							lines2.size());
					for (int j = 0; j < lines2.size(); j++) {
						lineDifferences = compare(lines1.get(i), lines2.get(j));
						lineDeltas.add(lineDifferences);
						valueMatches = lineDifferences.size() == 0 ? true
								: valueMatches;
						if (valueMatches)
							break;
					}
					if (!valueMatches) {
						differences
								.add(new Difference("No matching line found"));
						differences.addAll(findMostSimilarItem(lineDeltas));
					}
				}
			}
		}

		return differences;
	}

	public List<Difference> compare(CsvEntry line1, CsvEntry line2) {
		List<Difference> differences = new LinkedList<Difference>();
		if (line1.getNumberOfColumns() != line2.getNumberOfColumns()) {
			differences.add(new Difference(
					"Not the same number of columns in line")
					.addDetail("Line index of source line", line1.getLineIdx())
					.addDetail("Line index of target line", line2.getLineIdx())
					.addDetail("Column number in source line",
							line1.getNumberOfColumns())
					.addDetail("Column number in target line",
							line2.getNumberOfColumns()));
			return differences;
		}

		// check single values
		for (int i = 0; i < line1.getData().length; i++) {
			String value1 = line1.getData()[i];
			String value2 = line2.getData()[i];
			if(value1 == null){
				if(value2 != null){
					differences.add(new Difference("Difference in column ["
						+ i + "] -> " + value1
						+ " != " + value2));
				}else{
					continue;
				}	
			}
			if (!value1.equals(line2.getData()[i])) {
				// if column order matters then this makes a difference
				
					differences.add(new Difference("Difference in column ["
							+ i + "] -> " + value1
							+ " != " + value2));
				// else try to find the value in a different column
			}

		}// end loop
		return differences;

	}

	/**
	 * A difference is used to represent a difference between two csv documents.
	 * See CsvComparator for reference.
	 * 
	 * @author Benjamin Diedrichsen
	 * 
	 */

	public class Difference {

		private String message;

		private Map<String, Object> details;

		private List<Difference> reasons;

		public Difference(String message) {
			super();
			this.message = message;
			this.details = new LinkedHashMap<String, Object>();
			this.reasons = new LinkedList<Difference>();
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Difference addDetail(String message, Object value) {
			details.put(message, value);
			return this;
		}

		public Difference addReason(Difference reason) {
			reasons.add(reason);
			return this;
		}

		public Difference addReasons(List<Difference> reasons) {
			this.reasons.addAll(reasons);
			return this;
		}

		@Override
		public String toString() {
			return printRecursive(0);
		}

		public String printRecursive(int indentationLvl) {
			StringBuilder b = new StringBuilder();
			b.append(getIndentation(indentationLvl));
			b.append("[");
			b.append(message);
			b.append("\n");
			/*
			for (Entry<String, Object> detail : details.entrySet()) {
				b.append(getIndentation(indentationLvl + 1));
				b.append(detail.getKey());
				b.append(" :: ");
				b.append(detail.getValue());
				b.append("\n");
			}
			if (reasons.size() > 0) {
				b.append(getIndentation(indentationLvl));
				b.append("Reasons for Difference...\n");
			}*/
			for (Difference reason : reasons) {
				b.append(reason.printRecursive(indentationLvl + 1));
			}
			//b.append(getIndentation(indentationLvl));
			//b.append("\n");
			return b.toString();
		}

		private String getIndentation(int lvl) {
			if (lvl <= 0)
				return "";
			else
				return "\t" + getIndentation(lvl - 1);
		}

	}

}
