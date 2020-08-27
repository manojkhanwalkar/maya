package batch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class FileContentsIterator implements Iterator<String> {

    BufferedReader reader;

    public FileContentsIterator(String dataFileName) {
        try {
            reader = new BufferedReader(new FileReader(dataFileName));
            line = reader.readLine();
            if (line == null) more = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean more = true;
    String line;

    @Override
    public boolean hasNext() {
        return more;
    }

    @Override
    public String next() {

        String str = line;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (line == null) more = false;
        return str;
    }

}



