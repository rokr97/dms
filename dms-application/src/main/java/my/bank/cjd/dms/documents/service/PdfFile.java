package my.bank.cjd.dms.documents.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
@Builder
public class PdfFile {
    String name;
    byte[] content;
}
