package net.engio.csvblockx.reader;

import net.engio.csvblockx.blockdef.CsvFormat;
import net.engio.csvblockx.blockdef.ICsvFormat;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

public class CsvReader implements ICsvReader{

	private ICsvFormat format;

    private ParseStrategy currentStrategy;

	public CsvReader(ICsvFormat format) {
		super();
		this.format = format;
	}

	public CsvReader() {
		this(CsvFormat.Default());
	}

    public String[] parse(String line) {
        currentStrategy = ParseNonEncapsulated;
        StringReader input = new StringReader(line);
        List<String> values = read(input);
        return values.toArray(new String[] {});
    }

	private boolean isEOF(int character, StringReader input){
		try {
			if (character == -1)
				return true;
			else return false;
		} catch (Exception e) {
			return true;
		}
	}

	private int getNextCharacter(StringReader input) {
		try {
			int currentCharacter = input.read();
			// if unicode processing is not enabled or 
			// character does not represent beginning of unicode sequence the read value is treated as character
			if (!format.isProcessUnicodeEscapesEnabled() || currentCharacter != '\\')
				return currentCharacter;
			if (input.read() == 'u') { // begin of unicode sequence
				currentCharacter = readUnicodeCharacter(input);
			}
			return currentCharacter;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private int readUnicodeCharacter(StringReader input) throws IOException {
		int next;
		StringBuilder unicodeSequence = new StringBuilder();
		int charCount = 0;
		while (charCount < 4 && (next = input.read()) != -1) {
			unicodeSequence.append((char) next);
			charCount++;
		}
		return Integer.parseInt(unicodeSequence.toString(), 16);
	}


    private List<String> read(StringReader input) {
        Token currentToken = new Token();
        Token LastToken = currentToken;
        List<String> tokes = new LinkedList<String>();
        int currentCharacter;
        while (!isEOF(currentCharacter = getNextCharacter(input), input)) {
               currentToken = currentStrategy.evaluateCharacter(currentToken, currentCharacter);
            if(currentToken != LastToken){
                tokes.add(LastToken.toString());
                LastToken = currentToken;
            }
        }
        if(!currentToken.isComment)tokes.add(currentToken.toString());
        return tokes;
    }




    private final class Token{

        private StringBuilder content = new StringBuilder();

        private boolean nonNull = false;

        private boolean isComment = false;

        public void markAsComment(){
            isComment = true;
        }

        public boolean isComment(){
            return isComment;
        }

        public void markNonNull(){
            nonNull = true;
        }

        public boolean isEmpty(){
            return content.length() == 0;
        }

        private boolean isNonNull(){
            return nonNull || content.length() > 0;
        }

        public StringBuilder append(int c) {
            return content.append((char)c);
        }



        @Override
        public String toString() {
            return isNonNull() ?  content.toString() : null;
        }
    }

    private interface ParseStrategy{

        public Token evaluateCharacter(Token current, int nextCharacter);

    }



    private final ParseStrategy ParseEncapsulated = new ParseStrategy(){


        @Override
        public Token evaluateCharacter(Token current, int nextCharacter) {
            if(format.getComment() == nextCharacter){
                currentStrategy = ParseComment;
                return current;
            }
            if(format.getEncapsulator() == nextCharacter){
                currentStrategy = ParseNonEncapsulated;
                return current;
            }
            current.append(nextCharacter);
            return current;
        }


    };

    private final ParseStrategy ParseComment = new ParseStrategy(){


        @Override
        public Token evaluateCharacter(Token current, int nextCharacter) {
            if(current.isEmpty()){
                current.markAsComment();
            }
            return current;
        }


    };

    private final ParseStrategy ParseNonEncapsulated = new ParseStrategy(){


        @Override
        public Token evaluateCharacter(Token current, int nextCharacter) {
            if(format.getComment() == nextCharacter){
                currentStrategy = ParseComment;
                return current;
            }
            if(format.getDelimiter() == nextCharacter){
                return new Token();
            }
            if(format.getEncapsulator() == nextCharacter){
                currentStrategy = ParseEncapsulated;
                current.markNonNull();
                return current;
            }
            else current.append(nextCharacter);
            return current;
        }
    };

}
