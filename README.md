BloX
=========

BloX is an event based csv parsing library with support for processing of **simple csv files** as well as multi-format files containing multiple blocks and each block adhering to a different format, e.g. different number of columns, different value separators etc.

It comes wit a declarative API for creating block descriptors. Event handlers can be used to create in-memory models of the incoming csv data or do any other kind of processing.

The event based approach to parsing the csv files offers high performance and a constant memory foot print.

With custom event handlers it is possible to implement any processing logic like value conversions, aggregations or data histograms.

Check out the [javadoc](http://bennidi.github.io/blox/)


 <h2>Usage</h2>

Creating a block definitions is very simple. The only mandatory configuration data for a block
are its boundaries (i.e. beginning and end). A static block defines its start and end point in terms
of line numbers.


```java
// this block will contain data from line 19 until line 29
new CsvBlockDescriptor()
   .starts().after().line(18)
   .ends().with().line(30);
```
    

In many scenarios the length of the csv data (i.e. the number of lines of the data part) are not known in advance.
For this scenario a dynamic block can be used. A dynamic block uses regular expressions to detect the start and end
of a block.

```java
// this block will contain data from the first line after the line that contains the specified pattern
// until a blank line is reached
new CsvBlockDescriptor()
    .starts().after().pattern("Transactions.*")
    .ends().with().emptyLine();
```

If the real data starts more than one line after the pattern (many csv exports contain header information and comments)
then a header size may be specified. This header information will not be processed as part of the block data. Instead
it will be copied as is.


```java
    // this block will contain data from the first line after the line that contains the specified pattern
    // until a blank line is reached
    // the first three lines after the blocks start contain header information and will not be processed
    new CsvBlockDescriptor()
        .starts().after().pattern("Transactions.*")
        .ends().with().emptyLine()
        .headerSize(3);
```

Creating a reader for a set of block definitions and an input stream is straight forward.

```java
// create the event handlers and pass the block configuration
CsvBlockBuilder block1 = new CsvBlockBuilder(new CsvBlockDescriptor()
        .starts().after().pattern("Parameter.*")
        .ends().with().emptyLine()
        .hasColumnNames(true));
CsvBlockBuilder block2 = new CsvBlockBuilder(new CsvBlockDescriptor()
        .starts().after().pattern("Daten.*")
        .ends().with().emptyLine()
        .hasColumnNames(true));

// create a reader for the given block builders (a block builder is mainly a set of handlers
// that will produce an in-memory model of the parsed csv data)
BloxReader utf8Reader = Utf8Reader.createReaderFor(block1, block2);
// start reading
utf8Reader.read(new FileInputStream(new File("/path/to/file.csv"));

// access the parsed blocks and to whatever needs to be done
block1.getBlock().getEntries() ....
```


BloX also provides a class for writing csv blocks to an output stream. Simply provide it with the stream and a set of
blocks.

```java

// get some block definitions and read the input
CsvBlockBuilder[] blockBuilders = CsvBlockBuilder.fromDescriptors(definitions);
BloxReader reader = new BloxReader(ICsvParserFactory.Default, blockBuilders, encoding);
reader.read(input);

Writer output = new FileWriter("/path/to/output.csv");
MultiBlockWriter blockWriter = new MultiBlockWriter(output);
blockWriter.writeBlocks(CsvBlockBuilder.getBlocks(blockBuilders));
blockWriter.close();
```

BloX also provides means to compare two csv documents. A Comparator can be configured to different levels of equality,
e.g. it might enforce line and column order or be less restrictive if for example different line order does not matter
as long as every line is found.


```java
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
```


<h2>Contribute</h2>

One area that needs more attention is the handling of different file formats and character encodings. Test coverage
is still too low

<h2>License</h2>

This project is distributed under the terms of the MIT License. See file "LICENSE" for further reference.




