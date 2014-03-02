package net.engio.blox.data;

import net.engio.converse.IValueConversion;


public abstract class Column<V> {

	private IValueConversion<String, V> converter;
	
	public V valueOf(CsvEntry entry){
		if(converter == null) return (V)doValueOf(entry);
		return converter.convert(doValueOf(entry));
	}
	
	public abstract String doValueOf(CsvEntry entry);
	
	public Column<V> as(IValueConversion<String, V> conversion){
		this.converter = conversion;
		return this;
	}
	
	public static Column Named(String name) {
		return new NamedColumn(name);
	}

	public static Column Indexed(int index) {
		return new IndexColumn(index);
	}
	
	public static final Column Any = new AnyColumn();
	
	public static class AnyColumn extends Column {

		private AnyColumn() {
			super();
		}

		@Override
		public String doValueOf(CsvEntry entry) {
			return "*";
		}
		
		@Override
		public String toString() {
			return "*";
		}
	}

	public static class IndexColumn extends Column {

		private int index;

		private IndexColumn(int index) {
			super();
			this.index = index;
		}

		@Override
		public String doValueOf(CsvEntry entry) {
			return entry.getData()[index];
		}
		
		@Override
		public String toString() {
			return "Column" + index;
		}
	}

	public static class NamedColumn extends Column {

		private String name;

		private NamedColumn(String name) {
			super();
			this.name = name;
		}

		@Override
		public String doValueOf(CsvEntry entry) {
			return entry.getString(name);
		}
		
		@Override
		public String toString() {
			return name;
		}
	}

}
