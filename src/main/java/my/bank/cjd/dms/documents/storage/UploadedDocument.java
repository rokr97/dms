package my.bank.cjd.dms.documents.storage;

import lombok.Value;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class UploadedDocument {
    String id;
    String ownerId;
    String fileName;
    String documentName;
}
