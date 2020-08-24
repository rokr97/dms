package my.bank.cjd.dms.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
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

    @NotNull(message = "File name cannot be null")
    private String fileName;
    @NotNull(message = "File type cannot be null")
    private String fileType;
    @Email
    private String ownerId; // email of a user who uploaded the document
    @Lob
    private byte[] data;

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
