package my.bank.cjd.dms.documents.storage.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbFileRepository extends JpaRepository<DbFile, String> {
}
