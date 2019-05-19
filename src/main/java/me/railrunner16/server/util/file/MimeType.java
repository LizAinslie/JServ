package me.railrunner16.server.util.file;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public enum MimeType {
    // Web resources
    HTML("text/html", "html", "htm"),
    CSS("text/css", "css"),
    JAVASCRIPT("text/javascript", "js", "mjs"),

    // Images
    GIF("image/gif", "gif"),
    JPEG("image/jpeg", "jpg", "jpeg"),
    PNG("image/png", "png"),
    WEBP("image/webp", "webp"),
    SVG("image/svg+xml", "svg"),
    BMP("image/bmp", "bmp"),

    // Data
    JSON("application/json", "json"),
    XML("text/xml", "xml"),
    CSV("text/csv", "csv"),

    // Fonts
    TTF("font/ttf", "ttf"),
    WOFF("font/woff", "woff"),
    WOFF2("font/woff2", "woff2"),
    OTF("font/otf", "otf"),
    EOT("application/vnd.ms-fontobject", "eot"),

    // Archives
    ZIP("application/zip", "zip"),
    TAR("application/x-tar", "tar"),
    RAR("application/x-rar-compressed", "rar"),
    ZIP7("application/x-7z-compressed", "7z"),
    JAR("application/java-archive", "jar"),
    BZIP("application/x-bzip", "bz"),
    BZIP2("application/x-bzip2", "bz2"),

    // Text
    PLAINTEXT("text/plain", "txt"),
    MARKDOWN("text/markdown", "md", "markdown"),
    ;

    private String mimeString;
    private List<String> fileExtensions;

    MimeType(String mimeString, String... fileExtension) {
        this.mimeString = mimeString;
        this.fileExtensions = Arrays.asList(fileExtension);
    }
}
