public class Main {

    public static void main(String[] args) {

        // Arquivo isolado, consultado diretamente
        DocumentFile report = new DocumentFile("report.pdf", 500);
        System.out.println("Arquivo isolado:");
        report.display("");

        // Pasta vazia
        Folder empty = new Folder("empty");
        System.out.println("\nPasta vazia:");
        empty.display("");

        // Estrutura com arquivos e pelo menos dois niveis de subpastas
        DocumentFile photo = new DocumentFile("photo.png", 1_500);
        DocumentFile spreadsheet = new DocumentFile("budget.xlsx", 800);
        DocumentFile invoice = new DocumentFile("invoice.docx", 300);
        DocumentFile icon = new DocumentFile("icon.svg", 50);

        Folder invoices = new Folder("invoices");
        invoices.add(invoice);

        Folder finance = new Folder("finance");
        finance.add(spreadsheet);
        finance.add(invoices); // nivel 2: pasta dentro de pasta

        Folder assets = new Folder("assets");
        assets.add(icon);

        Folder documents = new Folder("documents");
        documents.add(report);
        documents.add(photo);
        documents.add(finance); // nivel 1: subpasta de documents
        documents.add(assets);

        System.out.println("\nHierarquia completa:");
        documents.display("");

        // Consulta por meio da abstracao comum, sem verificar o tipo concreto
        FileSystemComponent component = finance;
        System.out.println("\nConsulta via FileSystemComponent -> " + component.getName()
                + ": " + component.getSize() + " KB");

        System.out.println("Tamanho total de 'documents': " + documents.getSize() + " KB");

        // Removendo um elemento e recalculando o tamanho
        finance.remove(invoices);
        System.out.println("Tamanho de 'finance' apos remover 'invoices': " + finance.getSize() + " KB");
    }
}
