package my.bank.cjd.dms.documents.storage;

import lombok.AllArgsConstructor;
import my.bank.cjd.dms.documents.service.DocumentNotFoundException;
import my.bank.cjd.dms.documents.storage.db.DbFile;
import my.bank.cjd.dms.documents.storage.db.DbFileRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
class InMemoryDocumentStore implements DocumentStorageService {
    @Override
    public List<UploadedDocument> listAll() {
        return documentsRepo.findAll() //TODO: set Hibernate projection to fetch without file content
                .stream()
                .map(DbFileMapper.INSTANCE::dbFileToUploadedDocument)
                .collect(toList());
    }

    @Override
    public DbFile storeFile(UploadDocumentCommand command) {
        return documentsRepo.save(DbFileMapper.INSTANCE.uploadCommandToDbFile(command));
    }

    @Override
    public DbFile getById(String documentId) {
        return documentsRepo.findById(documentId)
                            .orElseThrow(DocumentNotFoundException::new);
    }

    @Override
    public void delete(String documentId) {
        if (!documentsRepo.existsById(documentId)) {
            throw new DocumentNotFoundException("Document not found by id = " + documentId);
        }
        documentsRepo.deleteById(documentId);
    }

    private final DbFileRepository documentsRepo;

    @Mapper
    interface DbFileMapper {
        DbFileMapper INSTANCE = Mappers.getMapper(DbFileMapper.class);

        @Mapping(target = "id", ignore = true)
        DbFile uploadCommandToDbFile(UploadDocumentCommand command);
        UploadedDocument dbFileToUploadedDocument(DbFile dbFile);
    }
}
