public class ProductCompanyDetails {

    private String SKU;
    private String source;
    private String description;

    public ProductCompanyDetails(String SKU, String description, String source) {
        this.SKU=SKU;
        this.description=description;
        this.source = source;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
