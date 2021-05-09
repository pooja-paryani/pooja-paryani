import java.io.File;

public class FileSource {

    public static void main(String[] args) {

        String[] fileSequence = {"catalogA.csv","catalogB.csv","barcodesA.csv","barcodesB.csv"};
        //provide path for input folder
        String path = "/Users/poojarupani/Downloads/personal/input";
        File file = new File(path);
        //provide path for output folder
        String outputFileName = "/Users/poojarupani/Downloads/personal/output/output.csv";
        CSVFileReaderAndWriter.readCsvFileProcessAndWrite(file,outputFileName,fileSequence);


    }
}
