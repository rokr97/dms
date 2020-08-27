package my.bank.cjd.dms.documents.storage;

import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Value
@Builder
public class UploadDocumentCommand {
    String fileName;
    String documentName;
    String ownerId;
    byte[] fileContent;
}
