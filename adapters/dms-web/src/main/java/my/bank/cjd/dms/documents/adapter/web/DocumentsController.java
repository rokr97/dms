package my.bank.cjd.dms.documents.adapter.web;

import lombok.RequiredArgsConstructor;
import my.bank.cjd.dms.documents.service.PdfFile;
import my.bank.cjd.dms.documents.storage.DocumentStorageService;
import my.bank.cjd.dms.documents.storage.UploadedDocument;
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

import java.net.URI;
import java.util.List;

import static my.bank.cjd.dms.documents.adapter.web.UploadDocumentProperties.fileNameMap;

@RequiredArgsConstructor
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

        UploadedDocument newDocument = storage.storeFile(
                UploadDocumentRequest.builder()
                        .providedDocumentName(providedDocumentName)
                        .ownerId(ownerId)
                        .file(file)
                        .build()
                        .toUploadDocumentCommand()
        );

        URI fileDownloadUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDocument.getId())
                .toUri();

        return ResponseEntity.created(fileDownloadUri).build();
    }

    @GetMapping("/{documentId}")
    ResponseEntity<Resource> downloadFile(@PathVariable String documentId) {
        return toFileDownloadResponse(storage.getById(documentId));
    }

    @DeleteMapping("/{documentId}")
    void removeFile(@PathVariable String documentId) {
        storage.delete(documentId);
    }

    //todo: extract to a mapper/adapter class
    static ResponseEntity<Resource> toFileDownloadResponse(PdfFile file) {
        String contentTypeFromFilename = fileNameMap.getContentTypeFor(file.getName());
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentTypeFromFilename))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(new ByteArrayResource(file.getContent()));
    }

    private final DocumentStorageService storage;
}


