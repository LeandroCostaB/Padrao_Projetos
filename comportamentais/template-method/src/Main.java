public class Main {

    public static void main(String[] args) {

        ReportGenerator csvReport = new CsvReport();
        ReportGenerator htmlReport = new HtmlReport();
        ReportGenerator textReport = new TextReport();

        csvReport.generate();

        System.out.println();
        htmlReport.generate();

        System.out.println();
        textReport.generate();
    }
}
