import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/*This Class read and write CSV*/
public class CSVFileReaderAndWriter {

    private static Logger logger = Logger.getLogger(CSVFileReaderAndWriter.class.getName());
    static List<ProductA> productsA = new ArrayList<>();
    static List<ProductA> productsB = new ArrayList<>();
    private static final String FILE_HEADER = "SKU,Description,Source";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String PIPE_DELIMITER = ",";
    static List<ProductCompanyDetails> productCompanyDetailsListA = new ArrayList<>();
    static List<ProductCompanyDetails> productCompanyDetailsListB = new ArrayList<>();
    static Set<ProductA> mergedCatalogList = new HashSet<>();


    /*
    * read from Catalog and Barcodes File
    * write into output file
    * */
    public static void readCsvFileProcessAndWrite(File file, String outputFileName, String[] fileSequence) {

        String path = file.getPath();
        BufferedReader fileReader = null;
        String company = StringUtils.EMPTY;

        for (String fileName : fileSequence) {

            String filePath = path + "/" + fileName;

            readCatalogFile(fileName, filePath);

            readBarcodeFile(fileName, filePath);
        }

        mergedCatalogList = mergeCatalog(productsA,productsB);
        writeCsvFile(mergedCatalogList,outputFileName);



    }

    /*
    * Method to read Barcodes File and store into ProductA.
    * */
    public static void readBarcodeFile(String fileName, String filePath) {
        BufferedReader fileReader;
        String company;
        if (fileName.contains("barcode")) {

            //create Product object

            try {

                String line = "";
                fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
                fileReader.readLine();

                while ((line = fileReader.readLine()) != null && StringUtils.isNotEmpty(line.trim())) {
                    String[] tokens = line.split(",");
                    if (tokens.length > 0) {

                        if (fileName.endsWith("A.csv")) {

                            company = "A";
                            String sku = tokens[1];
                            ProductCompanyDetails productCompanyDetail = productCompanyDetailsListA.stream()
                                    .filter(p -> sku.equals(p.getSKU()))
                                    .findAny()
                                    .orElse(null);
                            String desc = StringUtils.EMPTY;
                            if(null != productCompanyDetail)
                                desc = productCompanyDetail.getDescription();
                            ProductA record = new ProductA(tokens[0], sku, tokens[2], company,desc);
                            productsA.add(record);

                        } else if (fileName.endsWith("B.csv")) {
                            company = "B";
                            String sku = tokens[1];
                            ProductCompanyDetails productCompanyDetail = productCompanyDetailsListB.stream()
                                    .filter(p -> sku.equals(p.getSKU()))
                                    .findAny()
                                    .orElse(null);
                            String desc = StringUtils.EMPTY;
                            if(null != productCompanyDetail)
                                desc = productCompanyDetail.getDescription();
                            ProductA record = new ProductA(tokens[0], tokens[1], tokens[2], company,desc);
                            productsB.add(record);
                        }

                    }
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /*
    * Read Catalog file and store into ProductCompanyDetails
    * */
    public static List<ProductCompanyDetails> readCatalogFile(String fileName, String filePath) {
        BufferedReader fileReader;
        String company;
        if (fileName.contains("catalog")) {
            //create company object

            try {

                String line = "";

                fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));

                fileReader.readLine();

                        if (fileName.endsWith("A.csv")) {
                            while ((line = fileReader.readLine()) != null && StringUtils.isNotEmpty(line.trim())) {
                                String[] tokens = line.split(",");
                                if (tokens.length > 0) {

                                    company = "A";
                                    ProductCompanyDetails record = new ProductCompanyDetails(tokens[0], tokens[1], company);
                                    productCompanyDetailsListA.add(record);
                                }
                            }
                            return productCompanyDetailsListA;

                        } else if (fileName.endsWith("B.csv")) {
                            while ((line = fileReader.readLine()) != null && StringUtils.isNotEmpty(line.trim())) {
                                String[] tokens = line.split(",");
                                if (tokens.length > 0) {
                                    company = "B";
                                    ProductCompanyDetails record = new ProductCompanyDetails(tokens[0], tokens[1], company);
                                    productCompanyDetailsListB.add(record);
                                }
                            }
                            return productCompanyDetailsListB;

                        }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return null;
    }

    /*
    * Perform union of A and B to merge them
    * */
    public static Set<ProductA> mergeCatalog(List<ProductA> productsA, List<ProductA> productsB) {

        Set<ProductA> data = new HashSet<>();
        data.addAll(productsA);
        data.addAll(productsB);
        Set<ProductA> finalList =  data.stream().filter(distinctByKey(ProductA::getSKU)).collect(Collectors.toSet());
        return finalList;

    }

    /*
    * Write merged catalog data into CSV file
    * */
    private static void writeCsvFile(Set<ProductA> data, String fileName) {
        FileWriter fileWriter = null;
        //Write the CSV file header
        try {
            fileWriter = new FileWriter(fileName);
            //Write the CSV file header
            fileWriter.append(FILE_HEADER.toString());

            //Add a new line separator after the header
           fileWriter.append(NEW_LINE_SEPARATOR);

            for (ProductA record : data) {
                fileWriter.append(record.getSKU());
                fileWriter.append(PIPE_DELIMITER);
                fileWriter.append(record.getDescription());
                fileWriter.append(PIPE_DELIMITER);
                fileWriter.append(record.getSource());
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
        } catch (Exception e) {
            logger.info("Error in CsvFileWriter !!! " + ExceptionUtils.getStackTrace(e));
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                logger.info("Error while flushing/closing fileWriter !!! " + ExceptionUtils.getStackTrace(e));
            }
        }
    }

    /*
    * distinctByKey
    * */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
