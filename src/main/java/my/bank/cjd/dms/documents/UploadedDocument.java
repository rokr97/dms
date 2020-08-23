package my.bank.cjd.dms.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Table(name = "uploaded_documents")
public class UploadedDocument {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String fileName;
    private String fileType;
    private String ownerId; // login, domain name or email of a user who uploaded the document
    @Lob private byte[] data;

    public UploadedDocument() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UploadedDocument that = (UploadedDocument) o;
        return Objects.equals(this.id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
