package my.bank.cjd.dms.documents.storage;

import my.bank.cjd.dms.documents.storage.db.DbFile;

import java.util.List;
import java.util.Optional;

public interface DocumentStorageService {
    List<UploadedDocument> listAll();
    DbFile storeFile(UploadDocumentCommand document);
    Optional<DbFile> getById(String documentId);
    void delete(String documentId);
}
