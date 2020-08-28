package my.bank.cjd.dms.documents.service;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException() {
        super();
    }
    public DocumentNotFoundException(String message) {
        super(message);
    }
}
