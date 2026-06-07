package com.github.polaris.util;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * @author zhongkunming
 */
@Slf4j
public class ExportFileUtil {

    public static ResponseEntity<StreamingResponseBody> asStream(String fileName, Consumer<OutputStream> consumer) {
        StreamingResponseBody body = consumer::accept;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, concatContentDisposition(fileName))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(body);
    }

    public static void asResponse(String fileName, Consumer<OutputStream> consumer) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null || attributes.getResponse() == null) {
            log.error("文件导出失败，获取 response 为空");
            throw new RuntimeException("文件导出失败，获取 response 为空");
        }
        HttpServletResponse response = attributes.getResponse();
        asResponse(response, fileName, consumer);
    }

    public static void asResponse(HttpServletResponse response, String fileName, Consumer<OutputStream> consumer) {
        try {
            setResponseHeaders(response, fileName);
            try (ServletOutputStream sos = response.getOutputStream()) {
                consumer.accept(sos);
                sos.flush();
            }
        } catch (IOException e) {
            log.error("文件导出失败, {}", e.getMessage(), e);
            throw new RuntimeException("文件导出失败", e);
        }
    }

    public static void setResponseHeaders(HttpServletResponse response, String fileName) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, concatContentDisposition(fileName));
    }

    private static String concatContentDisposition(String fileName) {
        return "attachment; filename=" + encodeFileName(fileName);
    }

    private static String encodeFileName(String fileName) {
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }
}
