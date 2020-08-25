package util.GVAPCHFUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CHFMetaData {



    //Field Name	Type	Len	Pos	To
    static class Field
    {
        String name;
        int start;
        int end;
        int len;
        String type;

        @Override
        public String toString() {
            return "Field{" +
                    "name='" + name + '\'' +
                    ", start=" + start +
                    ", end=" + end +
                    ", len=" + len +
                    ", type='" + type + '\'' +
                    '}';
        }
    }


    List<Field> chfFields = new ArrayList<>();

    public static CHFMetaData parse(String metaFileName) {

        CHFMetaData chfMetaData = new CHFMetaData();
        try (BufferedReader reader = new BufferedReader(new FileReader(metaFileName))) {
            String line = reader.readLine();  // ignore first line
             line = reader.readLine();


            while (line != null) {
                String[] fields = line.split("\t");
                Field field = new Field();
                field.name = fields[0];
                field.type = fields[1];
                field.len = Integer.valueOf(fields[2]);
                field.start = Integer.valueOf(fields[3]);
                field.end = Integer.valueOf(fields[4]);

                chfMetaData.chfFields.add(field);
                line = reader.readLine();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return chfMetaData;

    }

    @Override
    public String toString() {
        return "CHFMetaData{" +
                "chfFields=" + chfFields +
                '}';
    }

    public static void main(String[] args) {

        CHFMetaData chfMetaData = CHFMetaData.parse("/home/manoj/data/CHF.txt");

        System.out.println(chfMetaData);


        System.out.println(chfMetaData.chfFields.get(chfMetaData.chfFields.size()-1).end);



    }


}
