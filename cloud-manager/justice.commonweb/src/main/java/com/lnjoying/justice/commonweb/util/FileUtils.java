package com.lnjoying.justice.commonweb.util;

import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;


/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/31 15:46
 */
@Slf4j
public class FileUtils
{
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final String FILE_SEPARATOR = File.separator;

    public static String getContentFromClassPathResource(String path)
    {
        String content = "";
        try
        {
            Resource resource = new ClassPathResource(path);
            InputStream inputStream = resource.getInputStream();
            byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
            content = new String(bdata, StandardCharsets.UTF_8);
            return content;
        }
        catch (FileNotFoundException e)
        {
            log.error("file not found: {}", e);
        }
        catch (IOException e)
        {
            log.error("file read error: {}", e);
        }

        return content;
    }


    public static String getContentFromBaseConfigPathResource(String path)
    {
        String content = "";

        try
        {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            content = new String (bytes, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            log.error("file:{} read error: {}", path, e);
        }

        return content;
    }

    public static String getNacosContentFromNacosConfigPathResource(String path)
    {
        path = BaseConfigPathUtils.getNacosConfigPath() + path;
        return getContentFromBaseConfigPathResource(path);
    }

    public static String getRegoContentFromNacosConfigPathResource(String path)
    {
        path = BaseConfigPathUtils.getRegoConfigPath() + path;
        return getContentFromBaseConfigPathResource(path);
    }

    /**
     * read all files in a directory
     * @param dir
     * @param relativePath
     * @return
     * @throws IOException
     */
    public static Set<String> listFilesUsingFileWalkAndVisitor(String dir, boolean relativePath) {
        Set<String> fileList = new HashSet<>();
        try
        {
            Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    if (!Files.isDirectory(file)) {
                        if (relativePath)
                        {
                            fileList.add(file.toFile().getPath().substring(Paths.get(dir).toFile().getPath().length() + 1) );
                        }
                        else
                        {
                            fileList.add(file.toFile().getPath());
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException e)
        {
            return Collections.EMPTY_SET;
        }
        return fileList;
    }


    /**
     * read file path and file content
     * @param dir
     * @param relativePath
     * @return
     */
    public static Map<String, String> listFilesContentUsingFileWalkAndVisitor(String dir, boolean relativePath,boolean base64Encode) {
        Map<String, String> fileContentMap = new HashMap<>();
        try
        {
            Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    if (!Files.isDirectory(file)) {
                        String filePath = "";
                        if (relativePath)
                        {
                            filePath = (file.toFile().getCanonicalPath().substring(Paths.get(dir).toFile().getCanonicalPath().length() + 1) );
                        }
                        else
                        {
                            filePath = file.toFile().getPath();
                        }

                        String content = getContentFromBaseConfigPathResource(file.toFile().getPath());
                        String encodeContent = "";

                        if (base64Encode)
                        {
                            encodeContent =  Base64Utils.encode(content);
                        }
                        else
                        {
                            encodeContent = content;
                        }

                        fileContentMap.put(filePath, encodeContent);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException e)
        {
            return Collections.EMPTY_MAP;
        }
        return fileContentMap;
    }

    public static Map<String, String> listFilesContentWithBase64Encode(String dir, boolean relativePath) {
        return listFilesContentUsingFileWalkAndVisitor(dir, true, true);
    }

}
