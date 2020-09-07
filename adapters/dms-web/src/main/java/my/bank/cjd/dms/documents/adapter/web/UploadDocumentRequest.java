package my.bank.cjd.dms.documents.adapter.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import my.bank.cjd.dms.documents.service.PdfFile;
import my.bank.cjd.dms.documents.storage.UploadDocumentCommand;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static my.bank.cjd.dms.documents.adapter.web.UploadDocumentProperties.fileNameMap;
import static my.bank.cjd.dms.documents.adapter.web.UploadDocumentProperties.supportedFileTypes;


@AllArgsConstructor
@Value
@Builder
public class UploadDocumentRequest {
    String ownerId;
    MultipartFile file;
    String providedDocumentName;

    public UploadDocumentCommand toUploadDocumentCommand() throws HttpMediaTypeNotSupportedException {
        PdfFile pdfFile = fromMultipartFile(file);
        String documentName = isNotBlank(providedDocumentName) ? providedDocumentName : pdfFile.getName();
        return UploadDocumentCommand.builder()
                .fileName(pdfFile.getName())
                .documentName(documentName)
                .ownerId(ownerId)
                .fileContent(pdfFile.getContent())
                .build();
    }

    static PdfFile fromMultipartFile(MultipartFile file) throws HttpMediaTypeNotSupportedException {
        byte[] fileContent;
        try {
            fileContent = file.getBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to read document content", e); //todo: improve error handling
        }
        String fileName = normalizeFileName(file.getOriginalFilename());
        String contentType = fileNameMap.getContentTypeFor(fileName);

        validate(fileName, contentType);

        return new PdfFile(fileName, fileContent);
    }

    private static String normalizeFileName(String originalFileName) {
        return org.springframework.util.StringUtils.cleanPath(originalFileName);
    }

    private static void validate(String fileName, String contentTypeValue) throws HttpMediaTypeNotSupportedException {
        if (!MediaType.APPLICATION_PDF_VALUE.equals(contentTypeValue)) {
            MediaType contentType = contentTypeValue != null ? MediaType.valueOf(contentTypeValue) : null;
            throw contentType == null ?
                    new HttpMediaTypeNotSupportedException(null, supportedFileTypes, "Unable to parse content type from filename: " + fileName) :
                    new HttpMediaTypeNotSupportedException(contentType, supportedFileTypes);
        }
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isBlank(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }
}
