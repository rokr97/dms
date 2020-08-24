package my.bank.cjd.dms.documents;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface UploadedDocumentRepository extends JpaRepository<UploadedDocument, String> {
}
