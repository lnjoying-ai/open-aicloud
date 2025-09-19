package com.lnjoying.justice.aos.util;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/22 19:06
 */

public class TarGzipUtils
{
    private static Logger LOGGER = LogManager.getLogger();

    /**
     * Extract the compressed package bytes to a directory
     * @param tarBytes
     * @param targetDir
     */
    public static void deCompressTarGzip( byte[] tarBytes, Path targetDir)
    {

        try (InputStream fi = new ByteArrayInputStream(tarBytes);
             BufferedInputStream bi = new BufferedInputStream(fi);
             GzipCompressorInputStream gzi = new GzipCompressorInputStream(bi);
             TarArchiveInputStream ti = new TarArchiveInputStream(gzi)) {

            ArchiveEntry entry;
            while ((entry = ti.getNextEntry()) != null) {

               // Get the decompressed file directory and determine whether the file is damaged
                Path newPath = zipSlipProtect(entry, targetDir);

                if (entry.isDirectory()) {
                    //create the unzip file directory
                    Files.createDirectories(newPath);
                } else {
                    //Check again whether the unzipped file directory exists
                    Path parent = newPath.getParent();
                    if (parent != null) {
                        if (Files.notExists(parent)) {
                            Files.createDirectories(parent);
                        }
                    }
                    //Input the decompressed file to Tar Archive Input Stream and output to the new Path directory on disk
                    Files.copy(ti, newPath, StandardCopyOption.REPLACE_EXISTING);

                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("extract the compressed package bytes to a directory fail:{}", e);
        }
    }


    /**
     * Determine whether the compressed file is damaged, and return the decompression directory of the file
     * @param entry
     * @param targetDir
     * @return
     * @throws IOException
     */
    private static Path zipSlipProtect(ArchiveEntry entry,Path targetDir)
            throws IOException
    {

        Path targetDirResolved = targetDir.resolve(entry.getName());
        Path normalizePath = targetDirResolved.normalize();

        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("the compressed file is corrupted: " + entry.getName());
        }

        return normalizePath;
    }
}
