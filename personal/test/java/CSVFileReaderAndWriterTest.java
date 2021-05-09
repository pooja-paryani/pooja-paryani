import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*Test Class*/
class CSVFileReaderAndWriterTest {

    List<ProductA> productAList = new ArrayList<>();
    List<ProductA> productBList = new ArrayList<>();
    Set<ProductA> resultList = new HashSet<>();
    static List<ProductCompanyDetails> productCompanyDetailsListA = new ArrayList<>();

    File csvFile = null;

    /* Setup*/
    @BeforeEach
    public void setUp() throws Exception {
        ProductA product1 = new ProductA("0001","11","123","A","tea cup");
        ProductA product2 = new ProductA("0001","12","123","B","tea cup");
        ProductA product3 = new ProductA("0001","13","125","A","tea cup");
        productAList.add(product1);
        productAList.add(product3);
        productBList.add(product2);
        resultList.add(product1);
        resultList.add(product3);

        ProductCompanyDetails productCompanyDetails = new ProductCompanyDetails("647-vyk-317","Walkers Special Old Whiskey test","A");
        productCompanyDetailsListA.add(productCompanyDetails);

    }

    @Test
    /*Test Read Catalog Method */
    void testReadCatalogFile() throws IOException {
        //provide testcatalogA file path
        String path = "/Users/poojarupani/Downloads/personal/input/testcatalogA.csv";


        assertEquals(productCompanyDetailsListA.size(), CSVFileReaderAndWriter.readCatalogFile("testcatalogA.csv",path).size());

    }


    @Test
    /* test Merging catalogs*/
    void testMergeCatalog(){

        assertEquals(resultList.size(), CSVFileReaderAndWriter.mergeCatalog(productAList,productBList).size());
        assertEquals(resultList, CSVFileReaderAndWriter.mergeCatalog(productAList,productBList));



    }

}