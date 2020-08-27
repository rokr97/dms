package my.bank.cjd.dms.documents.integration.rest;

import lombok.AllArgsConstructor;
import my.bank.cjd.dms.documents.storage.DocumentStorageService;
import my.bank.cjd.dms.documents.storage.UploadDocumentCommand;
import my.bank.cjd.dms.documents.storage.UploadedDocument;
import my.bank.cjd.dms.documents.storage.db.DbFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URI;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@RestController()
@RequestMapping("/dms/documents")
class DocumentsController {
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    List<UploadedDocument> listAll() {
        return storage.listAll();
    }

    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     ResponseEntity<Void> uploadPdf(
            @RequestParam("ownerId") String ownerId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "documentName", required = false) String providedDocumentName) throws HttpMediaTypeNotSupportedException {
        PdfFile pdfFile = PdfFile.fromMultipartFile(file);
        String documentName = StringUtils.isNotBlank(providedDocumentName) ? providedDocumentName : pdfFile.name;
        DbFile dbFile = storage.storeFile(
                UploadDocumentCommand.builder()
                    .fileName(pdfFile.name)
                    .documentName(documentName)
                    .ownerId(ownerId)
                    .fileContent(pdfFile.content)
                    .build()
        );

        URI fileDownloadUri = ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(dbFile.getId())
                                .toUri();

        return ResponseEntity.created(fileDownloadUri).build();
    }

    @GetMapping("/{documentId}")
    ResponseEntity<Resource> downloadFile(@PathVariable String documentId) {
        return storage.getById(documentId)
                .map(DocumentsController::toFileDownloadResponse)
                .orElseGet(() -> ResponseEntity.notFound().build() );
    }

    @DeleteMapping("/{documentId}")
    void removeFile(@PathVariable String documentId) {
        storage.delete(documentId);
    }

    private static String normalizeFileName(String originalFileName) {
        return org.springframework.util.StringUtils.cleanPath(originalFileName);
    }

    private static ResponseEntity<Resource> toFileDownloadResponse(DbFile dbFile) {
        String contentTypeFromFilename = fileNameMap.getContentTypeFor(dbFile.getFileName());
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentTypeFromFilename))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getFileContent()));
    }

    @AllArgsConstructor
    static class PdfFile {
        String name;
        byte[] content;

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

        private static void validate(String fileName, String contentTypeValue) throws HttpMediaTypeNotSupportedException {
            if (!MediaType.APPLICATION_PDF_VALUE.equals(contentTypeValue)) {
                MediaType contentType = contentTypeValue != null ? MediaType.valueOf(contentTypeValue) : null;
                throw contentType == null ?
                        new HttpMediaTypeNotSupportedException(null, supportedFileTypes, "Unable to parse content type from filename: " + fileName) :
                        new HttpMediaTypeNotSupportedException(contentType, supportedFileTypes);
            }
        }
    }

    private final DocumentStorageService storage;
    private static final FileNameMap fileNameMap = URLConnection.getFileNameMap();
    private static final List<MediaType> supportedFileTypes = Collections.singletonList(MediaType.APPLICATION_PDF);
}
