package my.bank.cjd.dms.documents.storage;

import my.bank.cjd.dms.documents.service.PdfFile;

import java.util.List;

public interface DocumentStorageService {
    List<UploadedDocument> listAll();
    UploadedDocument storeFile(UploadDocumentCommand document);
    PdfFile getById(String documentId);
    void delete(String documentId);
}
