package my.bank.cjd.dms.documents;

import org.springframework.data.repository.PagingAndSortingRepository;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@RepositoryRestResource(collectionResourceRel = "documents", path = "documents")
public interface UploadedDocumentRestRepository extends PagingAndSortingRepository<UploadedDocument, String> {

}
