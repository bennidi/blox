package net.engio.csvblockx.histogram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.engio.csvblockx.data.Column;
import net.engio.csvblockx.data.CsvEntry;

public class Histogram {
	
	private List<Item> historyItems = new ArrayList<Histogram.Item>();
	 
	public Item For(Column<?>... columns) {
		Item i = new Item(columns);
		historyItems.add(i);
		return i;
	}
	
	public Histogram Compute(IAggregator... aggregates) {
		Item i = new Item(AllEntries());
		i.aggregators = aggregates;
		return this;
	}
	
	public Histogram store(CsvEntry entry){
		for(Item item: historyItems)item.store(entry);
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for(Item item : historyItems)b.append(item);
		return b.toString();
	}
	
	public static Column Column(String name){
		return Column.Named(name);
	}
	
	public static Column Column(int index){
		return Column.Indexed(index);
	}
	
	public static Column AllEntries(){
		return Column.Any;
	}
	
	public class Item {

		private IAggregator[] aggregators;

		private Column[] columns;

		private Map<Node, List<Akkumulator>> histographics = new HashMap<Histogram.Node, List<Akkumulator>>();

		private Map<Object, Node> roots = new HashMap<Object, Histogram.Node>();
		
		public Item(Column ...columns){
			this.columns = columns;
		}

		public Histogram Compute(IAggregator... aggregates) {
			this.aggregators = aggregates;
			return Histogram.this;
		}

		

		public String toString() {
			StringBuilder builder = new StringBuilder();
			for (Node node : histographics.keySet()) {
				builder.append(print(node));
				builder.append("{");
				for (Akkumulator akku : histographics.get(node)) {
					builder.append(akku);
					builder.append("|");
				}
				builder.append("}");
				builder.append("\n");
			}
			return builder.toString();
		}

		public void store(CsvEntry values) {
			Object value = columns[0].valueOf(values);
			Node current = roots.get(value);
			if (current == null) {
				current = new Node(value, null);
				roots.put(value, current);
			}
			for (int i = 1; i < columns.length; i++) {
				current = current.insert(columns[i].valueOf(values));
			}
			historize(current, values);
		}

		private void historize(Node node, CsvEntry entry) {
			List<Akkumulator> akkumulators = histographics.get(node);
			if (akkumulators == null) {
				akkumulators = createAkkumulators();
				histographics.put(node, akkumulators);
			}
			for (Akkumulator akku : akkumulators) {
				akku.add(entry);
			}
		}

		private List<Akkumulator> createAkkumulators() {
			List<Akkumulator> akkumulators = new ArrayList<Histogram.Akkumulator>();
			for (IAggregator aggregator : aggregators) {
				akkumulators.add(new Akkumulator(aggregator));
			}
			return akkumulators;
		}

		private Object[] getValues(Node node) {
			ArrayList<Object> values = new ArrayList();
			Node current = node;
			do {
				values.add(current.value);
				current = current.parent;
			} while (current != null);
			return values.toArray();
		}

		private String print(Node node) {
			Object[] values = getValues(node);
			StringBuilder b = new StringBuilder();
			b.append("[");
			for (int i = 0; i < values.length; i++) {
				b.append(" ");
				b.append(columns[i]);
				b.append("=");
				b.append(values[i]);
				b.append(" ");
				if(i < values.length -1)b.append("|");
			}
			b.append("]");
			return b.toString();
		}

	}

	private class Node {

		private Object value;

		private Node parent;

		private Map<Object, Node> children = new HashMap<Object, Node>();

		public Node(Object value, Node parent) {
			super();
			this.value = value;
			this.parent = parent;
		}

		public Node insert(Object value) {
			Node holder = children.get(value);
			if (holder == null) {
				holder = new Node(value, this);
				children.put(value, holder);
			}
			return holder;
		}
	}

	private class Akkumulator<T> {

		private IAggregator<T, Object> aggregator;

		private T accumulatedValue;

		public Akkumulator(IAggregator<T, Object> aggregator) {
			super();
			this.aggregator = aggregator;
		}

		public void add(CsvEntry entry) {
			Object value = aggregator.getColumn().valueOf(entry);
			accumulatedValue = aggregator.apply(accumulatedValue, value);
		}

		@Override
		public String toString() {
			return aggregator.getClass().getSimpleName() + "(" + aggregator.getColumn() + "): "
					+ accumulatedValue;
		}

	}

}
