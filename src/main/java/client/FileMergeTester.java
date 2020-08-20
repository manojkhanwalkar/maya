package client;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FileMergeTester {


    static class FileColumnTuple
    {
        String name;
        int column ;


        public FileColumnTuple(String name, int column) {
            this.name = name;
            this.column = column;
        }
    }
    public static void main(String[] args) throws Exception {


        FileMergeTester tester = new FileMergeTester();

      //  tester.sort(inputFile,outputFile,1);

        tester.sortSrcInput();
        tester.sortTgtInput();



        File srcInDir = new File("/home/manoj/data/filemerge/srctemp/");

       var srcFileColumnTuple =  Arrays.stream(srcInDir.listFiles()).map(file-> {
            return new FileColumnTuple(file.getAbsolutePath(),0);
        }).collect(Collectors.toList());


        File tgtInDir = new File("/home/manoj/data/filemerge/tgttemp/");

        var tgtFileColumnTuple =  Arrays.stream(tgtInDir.listFiles()).map(file-> {
            return new FileColumnTuple(file.getAbsolutePath(),0);
        }).collect(Collectors.toList());


        FileColumnTuple[] srcFileColumnTupleArray  = new FileColumnTuple[srcFileColumnTuple.size()];

        srcFileColumnTuple.toArray(srcFileColumnTupleArray);

        FileColumnTuple[] tgtFileColumnTupleArray  = new FileColumnTuple[tgtFileColumnTuple.size()];

        tgtFileColumnTuple.toArray(tgtFileColumnTupleArray);


        tester.merge(srcFileColumnTupleArray,tgtFileColumnTupleArray,"",SortType.N);

    }

    private void sortSrcInput()
    {
        File indir = new File("/home/manoj/data/filemerge/srcinput/");

        String outdir = "/home/manoj/data/filemerge/srctemp/";

        Arrays.stream(indir.listFiles()).forEach(file -> {

            sort(file.getAbsolutePath(),outdir+file.getName(),0, SortType.N);

        });
    }

    private void sortTgtInput()
    {
        File indir = new File("/home/manoj/data/filemerge/tgtinput/");

        String outdir = "/home/manoj/data/filemerge/tgttemp/";

        Arrays.stream(indir.listFiles()).forEach(file -> {

            sort(file.getAbsolutePath(),outdir+file.getName(),0, SortType.N);

        });


    }

    private void merge(FileColumnTuple[] srcFiles , FileColumnTuple[] tgtFiles , String outputFile, SortType type) throws Exception
    {
        BufferedReader[] srcFileReader = new BufferedReader[srcFiles.length];
        BufferedReader[] tgtFileReader = new BufferedReader[tgtFiles.length];


        String[] srcFilesCurrentValue = new String[srcFiles.length];

        String[] tgtFilesCurrentValue = new String[tgtFiles.length];

        String[] srcFilesCurrentSortColumnValue = new String[srcFiles.length];

        String[] tgtFilesCurrentSortColumnValue = new String[tgtFiles.length];



        int srcFileCount = srcFiles.length;
        int tgtFileCount = tgtFiles.length;

        initReaders(srcFilesCurrentValue,srcFiles,srcFileReader,srcFilesCurrentSortColumnValue);
        initReaders(tgtFilesCurrentValue,tgtFiles,tgtFileReader,tgtFilesCurrentSortColumnValue);

        while (srcFileCount>0 && tgtFileCount >0) {
            int srcSmallest = getSmallest(srcFilesCurrentSortColumnValue, type);
            int tgtSmallest = getSmallest(tgtFilesCurrentSortColumnValue, type);

            if (srcFilesCurrentSortColumnValue[srcSmallest].equals(tgtFilesCurrentSortColumnValue[tgtSmallest])) {
                System.out.println(srcFilesCurrentSortColumnValue[srcSmallest] + "  " + tgtFilesCurrentValue[tgtSmallest]);
                // move both readers forward for the smallest index .
                // decrease respective file count if EOF is reached for a reader .
                moveReader(srcSmallest,srcFileReader[srcSmallest],srcFilesCurrentValue,srcFilesCurrentSortColumnValue,srcFiles[srcSmallest].column);
                if (srcFilesCurrentValue[srcSmallest]==null) srcFileCount--;
                moveReader(tgtSmallest,tgtFileReader[tgtSmallest],tgtFilesCurrentValue,tgtFilesCurrentSortColumnValue,tgtFiles[tgtSmallest].column);
                if (tgtFilesCurrentValue[tgtSmallest]==null) tgtFileCount--;


            } else {
                // drop the smallest one as not going to be found
                if (isSmaller(srcFilesCurrentSortColumnValue[srcSmallest] ,tgtFilesCurrentSortColumnValue[tgtSmallest], type))
                {
                    moveReader(srcSmallest,srcFileReader[srcSmallest],srcFilesCurrentValue,srcFilesCurrentSortColumnValue,srcFiles[srcSmallest].column);
                    if (srcFilesCurrentValue[srcSmallest]==null) srcFileCount--;
                }
                else
                {
                    moveReader(tgtSmallest,tgtFileReader[tgtSmallest],tgtFilesCurrentValue,tgtFilesCurrentSortColumnValue,tgtFiles[tgtSmallest].column);
                    if (tgtFilesCurrentValue[tgtSmallest]==null) tgtFileCount--;

                }
            }

        }


    }

    private void moveReader(int smallest, BufferedReader reader, String[] filesCurrentValue, String[] filesCurrentSortColumnValue, int sortColumn) throws Exception
    {
        String line = reader.readLine();
        if (line ==null)
        {

            filesCurrentValue[smallest]=null;
            filesCurrentSortColumnValue[smallest]= null;
        }
        else
        {
            filesCurrentValue[smallest]=line;
            filesCurrentSortColumnValue[smallest]= extractSortColumnValue(line,sortColumn);
        }

    }

    private boolean isSmaller(String src , String tgt, SortType type)
    {
        if (type==SortType.S)
        {
            return src.compareTo(tgt) < 0 ;
        }
        else
        {
            Integer n1 = Integer.valueOf(src);
            Integer n2 = Integer.valueOf(tgt);

            return n1.compareTo(n2)<0;
        }
    }

    private int getSmallest(String[] values , SortType type)
    {
        if (type==SortType.N)
        {
            Integer[] intvalues = new Integer[values.length];
            for (int i=0;i<intvalues.length;i++)
            {
                if (values[i]!=null)
                 intvalues[i] = Integer.valueOf(values[i]);
                else
                    intvalues[i]=Integer.MAX_VALUE;
            }

            int index = -1; int min = Integer.MAX_VALUE;
            for (int i=0;i<intvalues.length;i++)
            {
                if (intvalues[i] < min)
                {
                    index = i;
                    min = intvalues[i];
                }
            }

            return index;
        }
        else
        {
            if (values[0]==null) values[0] = "zzzzzzzzzzzzzzz";
            int index =0;  String min = values[0];
            for (int i=1;i<values.length;i++)
            {
                if (values[i]==null) values[i] = "zzzzzzzzzzzzzzz";
                if (min.compareTo(values[i])>0)
                {
                    min = values[i];
                    index = i;
                }
            }

            return index;
        }
    }


    private void initReaders(String[] currentValue, FileColumnTuple[] fileColumnTuples, BufferedReader[] readers ,String[] sortColumnValue)

    {
        try {
            for (int i=0;i<fileColumnTuples.length;i++)
            {
                BufferedReader reader = new BufferedReader(new FileReader(fileColumnTuples[i].name));
                currentValue[i] = reader.readLine();
                readers[i] = reader;
                sortColumnValue[i] = extractSortColumnValue(currentValue[i], fileColumnTuples[i].column);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String extractSortColumnValue(String str, final int sortColumn)
    {
     return      str.split(";")[sortColumn];

    }

    public enum SortType { N,S};

    private void sort(String inputFile , String outputFile, final int sortColumn, SortType type)
    {
        Comparator<String> fileSorter = (String s1,String s2)->{

           String s11 = s1.split(";")[sortColumn];
           String s12 = s2.split(";")[sortColumn];

           if (type==SortType.N)
           {
               Integer n1 = Integer.valueOf(s11);
               Integer n2 = Integer.valueOf(s12);

               return Integer.compare(n1,n2);

           }
           else {
               return s11.compareTo(s12);
           }


        };



        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile)) ;

            BufferedWriter writer= new BufferedWriter(new FileWriter(outputFile))
        )
        {
            List<String> lines = new ArrayList<>();
            String line;

            while(( line= reader.readLine())!=null)
            {
                lines.add(line);
            }

            lines.sort(fileSorter);

            lines.stream().forEach(l-> {
                try {
                    writer.write(l);
                    writer.newLine();
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
