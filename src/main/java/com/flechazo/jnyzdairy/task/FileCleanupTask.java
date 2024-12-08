package com.flechazo.jnyzdairy.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 文件清理任务
 * 定期清理超过30天的临时文件
 */
@Component
public class FileCleanupTask {
    
    private static final Logger logger = LoggerFactory.getLogger(FileCleanupTask.class);
    
    private final String rootPath;

    public FileCleanupTask(@Value("${app.storage.root-path}") String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * 每天凌晨2点执行清理任务
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupOldFiles() {
        logger.info("Starting file cleanup task");
        
        try {
            Path root = Paths.get(rootPath);
            if (!Files.exists(root)) {
                return;
            }

            // 遍历所有文件
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                @NonNull
                public FileVisitResult visitFile(Path file, @NonNull BasicFileAttributes attrs) throws IOException {
                    // 获取文件最后修改时间
                    LocalDateTime lastModified = LocalDateTime.ofInstant(
                            attrs.lastModifiedTime().toInstant(),
                            ZoneId.systemDefault());
                    
                    // 如果文件超过30天未修改，则删除
                    if (lastModified.plusDays(30).isBefore(LocalDateTime.now())) {
                        Files.delete(file);
                        logger.info("Deleted old file: {}", file);
                    }
                    
                    return FileVisitResult.CONTINUE;
                }

                @Override
                @NonNull
                public FileVisitResult visitFileFailed(Path file, @NonNull IOException exc) {
                    logger.error("Failed to access file: {}", file, exc);
                    return FileVisitResult.CONTINUE;
                }
            });
            
            logger.info("File cleanup task completed");
        } catch (IOException e) {
            logger.error("Error during file cleanup", e);
        }
    }
} 