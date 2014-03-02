package net.engio.blox.reader;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;


public class UnicodeInputStream extends InputStream {

    private PushbackInputStream pushBackStream;
    private final String defaultEnc;
    private String encoding;

    private static final int BOM_SIZE = 4;

    public UnicodeInputStream(InputStream in, String defaultEnc) throws IOException{
        pushBackStream = new PushbackInputStream(in, BOM_SIZE);
        this.defaultEnc = defaultEnc;
        inspectInput();
    }


    /**
     * Read-ahead four bytes and check for BOM marks. Extra bytes are
     * unread back to the stream, only BOM bytes are skipped.
     *
     * @throws IOException
     */
    private void inspectInput() throws IOException {

        byte bom[] = new byte[BOM_SIZE];
        int n, unread;
        n = pushBackStream.read(bom, 0, bom.length);

        if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00) &&
                (bom[2] == (byte) 0xFE) && (bom[3] == (byte) 0xFF)) {
            encoding = "UTF-32BE";
            unread = n - 4;
        } else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE) &&
                (bom[2] == (byte) 0x00) && (bom[3] == (byte) 0x00)) {
            encoding = "UTF-32LE";
            unread = n - 4;
        } else if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB) &&
                (bom[2] == (byte) 0xBF)) {
            encoding = "UTF-8";
            unread = n - 3;
        } else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF)) {
            encoding = "UTF-16BE";
            unread = n - 2;
        } else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)) {
            encoding = "UTF-16LE";
            unread = n - 2;
        } else {
            // Unicode BOM mark not found, unread all bytes
            encoding = defaultEnc;
            unread = n;
        }

        if (unread > 0) pushBackStream.unread(bom, (n - unread), unread);
    }

    public void close() throws IOException {
        pushBackStream.close();
    }

    public int read() throws IOException {
        return pushBackStream.read();
    }
}

