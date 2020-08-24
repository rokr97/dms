package my.bank.cjd.dms.documents;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController()
@RequestMapping("/dms/documents")
public class DocumentsController {
    @GetMapping(path = "/", produces = "application/json")
    public List<UploadedDocument> listAll() {
        return storage.listAll();
    }

    private final DocumentStorageService storage;
}
