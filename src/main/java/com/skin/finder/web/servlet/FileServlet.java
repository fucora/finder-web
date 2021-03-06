/*
 * $RCSfile: FileServlet.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.FileType;
import com.skin.finder.Range;
import com.skin.finder.util.GMTUtil;
import com.skin.finder.util.IO;
import com.skin.finder.util.MimeType;

/**
 * <p>Title: FileServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FileServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @param request
     * @param response
     * @param file
     * @param download
     * @throws IOException
     */
    public void service(HttpServletRequest request, HttpServletResponse response, File file, boolean download) throws IOException {
        long length = file.length();
        long lastModified = file.lastModified();
        String eTag = this.getETag(length, lastModified);

        if(!checkIfHeaders(request, response, eTag, lastModified)) {
            return;
        }

        Range range = Range.parse(request, length);
        String httpDate = GMTUtil.format(lastModified);
        String contentType = MimeType.getMimeType(file.getName());

        // cache
        // response.setHeader("Cache-Control", "private");
        // response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("ETag", eTag);
        response.setHeader("Last-Modified", GMTUtil.format(lastModified));
        response.setHeader("Date", httpDate);
        response.setDateHeader("Expires", System.currentTimeMillis() + 60L * 60L * 1000L);
        response.setContentType(contentType);

        if(range == null) {
            response.setHeader("Content-Length", String.valueOf(length));

            if(download || contentType.equals("application/octet-stream")) {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + this.urlEncode(FileType.getName(file.getName()), "UTF-8") + "\"");
            }

            InputStream inputStream = null;

            try {
                inputStream = new FileInputStream(file);
                IO.copy(inputStream, response.getOutputStream());
            }
            catch(IOException e) {
            }
            finally {
                IO.close(inputStream);
            }
        }
        else {
            RandomAccessFile raf = null;
            long maxBodySize = 5L * 1024L * 1024L;

            if(range.getSize() >= maxBodySize) {
                range.end = range.start + maxBodySize - 1;
            }

            long size = range.getSize();
            String contentRange = range.getContentRange();
            response.setStatus(206);
            response.setHeader("Content-Range", contentRange);
            response.setHeader("Content-Length", String.valueOf(size));
            response.setHeader("Part-Size", String.valueOf(maxBodySize));
            response.setHeader("Content-Type", contentType);

            try {
                raf = new RandomAccessFile(file, "r");

                if(range.start > 0) {
                    raf.seek(range.start);
                }
                this.copy(raf, response.getOutputStream(), 4096, size);
            }
            catch(Exception e) {
                // do nothing
            }
            finally {
                IO.close(raf);
            }
        }
    }

    /**
     * @param length
     * @param lastModified
     * @return String
     */
    protected String getETag(long length, long lastModified) {
        return ("W/\"" + length + "-" + lastModified + "\"");
    }

    /**
     * @param request
     * @param response
     * @param eTag
     * @param lastModified
     * @return boolean
     * @throws IOException
     */
    public static boolean checkIfHeaders(HttpServletRequest request, HttpServletResponse response, String eTag, long lastModified) throws IOException {
        if(!checkIfMatch(request, response, eTag, lastModified)) {
            return false;
        }

        if(!checkIfModifiedSince(request, response, eTag, lastModified)) {
            return false;
        }

        if(!checkIfNoneMatch(request, response, eTag, lastModified)) {
            return false;
        }

        if(!checkIfUnmodifiedSince(request, response, eTag, lastModified)) {
            return false;
        }
        return true;
    }

    /**
     * @param request
     * @param response
     * @param eTag
     * @param lastModified
     * @return boolean
     * @throws IOException
     */
    protected static boolean checkIfMatch(HttpServletRequest request, HttpServletResponse response, String eTag, long lastModified) throws IOException {
        String ifMatch = request.getHeader("If-Match");

        if(ifMatch != null && ifMatch.indexOf('*') < 0) {
            if(contains(ifMatch, eTag)) {
                return true;
            }
            else {
                response.sendError(412);
                return false;
            }
        }
        return true;
    }

    /**
     * @param request
     * @param response
     * @param eTag
     * @param lastModified
     * @return boolean
     * @throws IOException
     */
    protected static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response, String eTag, long lastModified) throws IOException {
        try {
            long ifModifiedSince = request.getDateHeader("If-Modified-Since");

            if(ifModifiedSince != -1L) {
                if((request.getHeader("If-None-Match") == null) && (lastModified < ifModifiedSince + 1000L)) {
                    response.setStatus(304);
                    response.setHeader("ETag", eTag);
                    return false;
                }
            }
        }
        catch(IllegalArgumentException e) {
            return true;
        }
        return true;
    }

    /**
     * @param request
     * @param response
     * @param eTag
     * @param lastModified
     * @return boolean
     * @throws IOException
     */
    protected static boolean checkIfNoneMatch(HttpServletRequest request, HttpServletResponse response, String eTag, long lastModified) throws IOException {
        String ifNoneMatch = request.getHeader("If-None-Match");

        if(ifNoneMatch != null) {
            boolean flag = false;

            if(ifNoneMatch.equals("*")) {
                flag = true;
            }
            else {
                flag = contains(ifNoneMatch, eTag);
            }

            if(flag) {
                String method = request.getMethod();

                if(("GET".equalsIgnoreCase(method)) || ("HEAD".equalsIgnoreCase(method))) {
                    response.setStatus(304);
                    response.setHeader("ETag", eTag);
                    return false;
                }
                else {
                    response.sendError(412);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param request
     * @param response
     * @param eTag
     * @param lastModified
     * @return boolean
     * @throws IOException
     */
    protected static boolean checkIfUnmodifiedSince(HttpServletRequest request, HttpServletResponse response, String eTag, long lastModified) throws IOException {
        try {
            long ifUnmodifiedSince = request.getDateHeader("If-Unmodified-Since");

            if((ifUnmodifiedSince != -1L) && (lastModified >= ifUnmodifiedSince + 1000L)) {
                response.sendError(412);
                return false;
            }
        }
        catch(IllegalArgumentException e) {
            return true;
        }
        return true;
    }

    /**
     * @param text
     * @param encoding
     * @return String
     */
    private String urlEncode(String text, String encoding) {
        try {
            return URLEncoder.encode(text, encoding);
        }
        catch (IOException e) {
        }
        return "";
    }

    /**
     * @param content
     * @param value
     * @return boolean
     */
    private static boolean contains(String content, String value) {
        if(content != null) {
            StringTokenizer tokenizer = new StringTokenizer(content, ",");

            while(tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();

                if(token.trim().equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param raf
     * @param outputStream
     * @param bufferSize
     * @param size
     * @throws IOException
     */
    protected void copy(RandomAccessFile raf, OutputStream outputStream, int bufferSize, long size) throws IOException {
        int readBytes = 0;
        long count = size;
        int length = Math.min(bufferSize, (int)(size));
        byte[] buffer = new byte[length];

        while(count > 0) {
            if(count > length) {
                readBytes = raf.read(buffer, 0, length);
            }
            else {
                readBytes = raf.read(buffer, 0, (int)count);
            }

            if(readBytes > 0) {
                outputStream.write(buffer, 0, readBytes);
                count -= readBytes;
            }
            else {
                break; 
            }
        }
        outputStream.flush();
    }
}
