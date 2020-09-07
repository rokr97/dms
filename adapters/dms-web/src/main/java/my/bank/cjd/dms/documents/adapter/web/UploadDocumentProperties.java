package my.bank.cjd.dms.documents.adapter.web;

import org.springframework.http.MediaType;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;

public class UploadDocumentProperties {
    static final FileNameMap fileNameMap = URLConnection.getFileNameMap();
    static final List<MediaType> supportedFileTypes = Collections.singletonList(MediaType.APPLICATION_PDF);
}
