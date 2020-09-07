package my.bank.cjd.dms.documents.storage;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UploadDocumentCommand {
    String fileName;
    String documentName;
    String ownerId;
    byte[] fileContent;
}
