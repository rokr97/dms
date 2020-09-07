package my.bank.cjd.dms.documents.storage;

import lombok.Value;
import lombok.Builder;

@Value
@Builder
public class UploadedDocument {
    String id;
    String ownerId;
    String fileName;
    String documentName;
}
