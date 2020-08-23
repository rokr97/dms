package my.bank.cjd.dms.documents;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "documents", path = "documents")
public interface UploadedDocumentRepository extends PagingAndSortingRepository<UploadedDocument, String> {

}
