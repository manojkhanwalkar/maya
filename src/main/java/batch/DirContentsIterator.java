package batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;
import java.util.stream.Collectors;

public class DirContentsIterator implements Iterator<String> {

    Stack<String> fileNames ;



    public DirContentsIterator(String dirName) {

        File file = new File(dirName);

 //       fileNames = Arrays.stream(file.listFiles()).filter(f->f.isFile()).map(f1-> f1.getAbsolutePath()).collect(Collectors.toCollection(()->{return new Stack<>(); }));
        fileNames = Arrays.stream(file.listFiles()).filter(File::isFile).map(File::getAbsolutePath).collect(Collectors.toCollection(Stack::new));

    }



    @Override
    public boolean hasNext() {
        return !fileNames.isEmpty();
    }

    @Override
    public String next() {

      return fileNames.pop();
    }

}



