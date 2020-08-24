package my.bank.cjd.dms.documents;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InMemoryDocumentStore implements DocumentStorageService {
    @Override
    public List<UploadedDocument> listAll() {
        return documentsRepo.findAll();
    }

    @Override
    public String upload() {
        return null;
//        return documentsRepo.save();
    }

    @Override
    public Optional<UploadedDocument> getById(String documentId) {
        return documentsRepo.findById(documentId);
    }

    @Override
    public void delete(String documentId) {
        documentsRepo.deleteById(documentId);
    }

    private final UploadedDocumentRepository documentsRepo;
}
