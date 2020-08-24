package my.bank.cjd.dms.documents;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface DocumentStorageService {
    List<UploadedDocument> listAll();
    String upload();
    Optional<UploadedDocument> getById(String documentId);
    void delete(String documentId);
}
