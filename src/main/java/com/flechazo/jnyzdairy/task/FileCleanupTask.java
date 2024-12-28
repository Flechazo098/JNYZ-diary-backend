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
 * 文件清理任务，定期清理超过30天的临时文件。
 * <p>
 * 该任务每天凌晨2点自动执行，遍历指定的根目录下的所有文件，
 * 并删除最后修改时间超过30天的文件。此功能有助于保持存储空间的有效利用，
 * 避免临时文件占用过多磁盘空间。
 *
 * @author Flechazo
 */
@Component
public class FileCleanupTask {

    /**
     * 文件的最大保存天数，超过此期限的文件将被清理。
     */
    private static final int MAX_FILE_AGE_DAYS = 30;

    /**
     * 日志记录器，用于记录任务执行过程中的信息和错误。
     */
    private static final Logger logger = LoggerFactory.getLogger(FileCleanupTask.class);

    /**
     * 存储文件的根路径。
     */
    private final String rootPath;

    /**
     * 构造函数，通过Spring的依赖注入获取配置文件中定义的根路径。
     *
     * @param rootPath 存储文件的根路径
     */
    public FileCleanupTask(@Value("${app.storage.root-path}") String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * 定时任务方法，每天凌晨2点执行清理操作。
     * <p>
     * 该方法会遍历根目录下的所有文件，检查每个文件的最后修改时间，
     * 如果文件的最后修改时间超过了设定的最大保存期限，则将其删除。
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupOldFiles() {
        logger.info("Starting file cleanup task");

        try {
            Path root = Paths.get(rootPath);
            if (!Files.exists(root)) {
                logger.warn("Root path does not exist: {}", rootPath);
                return;
            }

            /* 遍历所有文件 */
            Files.walkFileTree(root, new SimpleFileVisitor<>() {
                @Override
                @NonNull
                public FileVisitResult visitFile(Path file, @NonNull BasicFileAttributes attrs) throws IOException {
                    /* 获取文件最后修改时间 */
                    LocalDateTime lastModified = LocalDateTime.ofInstant(
                            attrs.lastModifiedTime().toInstant(),
                            ZoneId.systemDefault());

                    /* 如果文件超过30天未修改，则删除 */
                    if (lastModified.plusDays(MAX_FILE_AGE_DAYS).isBefore(LocalDateTime.now())) {
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