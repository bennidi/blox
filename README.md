BloX
=========

BloX is an event based csv parsing library that supports processing of simple csv documents as well as
csv documents that contain multiple blocks of data where each block adheres to a different format, e.g.
different number of columns, different value separators etc.

It features a declarative dsl for creating block format definitions and sets of event handlers that will build
an in-memory model of the parsed csv according to the given block definition.

The event based approach to parsing the csv files offers high parsing performance and a a good memory foot print.
With custom event handlers it is possible to implement any processing logic like value conversions, aggregations or
data histograms.


 <h2>Usage</h2>

Creating a block definitions is very simple. The only mandatory configuration data for a block
are its boundaries (i.e. beginning and end). A static block defines its start and end point in terms
of line numbers.

    // this block will contain data from line 19 until line 29
    new CsvBlockDefinition()
       .starts().after().line(18)
       .ends().with().line(30);

In many scenarios the length of the csv data (i.e. the number of lines of the data part) are not known in advance.
For this scenario a dynamic block can be used. A dynamic block can use regular expressions to detect the start and end
of a block.

    // this block will contain data from the first line after the line that contains the specified pattern
    // until a blank line is reached
    new CsvBlockDefinition()
        .starts().after().pattern("Transactions.*")
        .ends().with().emptyLine();

If the real data starts more than one line after the pattern (many csv exports contain header information and comments)
then a header size may be specified. This header information will not be processed as part of the block data. Instead
it will be copied as is.

    // this block will contain data from the first line after the line that contains the specified pattern
    // until a blank line is reached
    // the first three lines after the blocks start contain header information and will not be processed
    new CsvBlockDefinition()
        .starts().after().pattern("Transactions.*")
        .ends().with().emptyLine()
        .headerSize(3);



Creating a reader for a set of block definitions and an input stream is straight forward.

    // create the event handlers and pass the block configuration
    CsvBlockBuilder block1 = new CsvBlockBuilder(new CsvBlockDefinition()
            .starts().after().pattern("Parameter.*")
            .ends().with().emptyLine()
            .hasColumnNames(true));
    CsvBlockBuilder block2 = new CsvBlockBuilder(new CsvBlockDefinition()
            .starts().after().pattern("Daten.*")
            .ends().with().emptyLine()
            .hasColumnNames(true));

    // create a reader for the given block builders (a block builder is mainly a set of handlers
    // that will produce an in-memory model of the parsed csv data)
    BlockXreader utf8Reader = Utf8Reader.createReaderFor(block1, block2);
    // start reading
    utf8Reader.read(new FileInputStream(new File("/path/to/file.csv"));

    // access the parsed blocks and to whatever needs to be done
    block1.getBlock().getEntries() ....

BloX also provides a class for writing csv blocks to an output stream. Simply provide it with the stream and a set of
blocks.

    // get some block definitions and read the input
    CsvBlockBuilder[] blockBuilders = CsvBlockBuilder.fromBlockDefinitions(definitions);
    BlockXreader reader = new BlockXreader(ICsvReaderFactory.Default, blockBuilders, encoding);
    reader.read(input);

    Writer output = new FileWriter("/path/to/output.csv");
    MultiBlockWriter blockWriter = MultiBlockWriter.createForDestination(output);
    blockWriter.writeBlocks(CsvBlockBuilder.getBlocks(blockBuilders));
    blockWriter.close();


BloX also provides means to compare two csv documents. A Comparator can be configured to different levels of equality,
e.g. it might enforce line and column order or be less restrictive if for example different line order does not matter
as long as every line is found.






<h2>Planned features</h2>

+ Maven dependency: Add Mbassador to your project using maven. Coming soon!

<h2>License</h2>




