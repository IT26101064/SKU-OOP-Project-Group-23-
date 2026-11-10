package com.cinereserve.user.repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe file storage helper ensuring safe concurrent read/write operations.
 */
public class FileStorageService {
    private static FileStorageService instance;
    private File dataDirectory;
    private final Map<String, ReentrantReadWriteLock> fileLocks = new ConcurrentHashMap<>();

    private FileStorageService() {
        this.dataDirectory = new File("data");
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }
    }

    public static synchronized FileStorageService getInstance() {
        if (instance == null) {
            instance = new FileStorageService();
        }
        return instance;
    }

    public void setDataDirectory(File dir) {
        this.dataDirectory = dir;
        if (!dataDirectory.exists()) dataDirectory.mkdirs();
    }

    public File getDataDirectory() {
        return dataDirectory;
    }

    private ReentrantReadWriteLock getLock(String filename) {
        return fileLocks.computeIfAbsent(filename, k -> new ReentrantReadWriteLock(true));
    }

    private File getFile(String filename) {
        return new File(dataDirectory, filename);
    }

    public void ensureFileExists(String filename) {
        File file = getFile(filename);
        if (!file.exists()) {
            try {
                if (file.getParentFile() != null && !file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Could not create " + filename + ": " + e.getMessage());
            }
        }
    }

    public List<String> readAllLines(String filename) {
        ReentrantReadWriteLock lock = getLock(filename);
        lock.readLock().lock();
        try {
            File file = getFile(filename);
            if (!file.exists()) return new ArrayList<>();
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) lines.add(line);
                }
            } catch (IOException e) {
                System.err.println("Read error: " + e.getMessage());
            }
            return lines;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean appendLine(String filename, String line) {
        ReentrantReadWriteLock lock = getLock(filename);
        lock.writeLock().lock();
        try {
            File file = getFile(filename);
            ensureFileExists(filename);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
                writer.write(line);
                writer.newLine();
                writer.flush();
                return true;
            } catch (IOException e) {
                return false;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean writeAllLines(String filename, List<String> lines) {
        ReentrantReadWriteLock lock = getLock(filename);
        lock.writeLock().lock();
        try {
            File file = getFile(filename);
            ensureFileExists(filename);
            File tempFile = new File(dataDirectory, filename + ".tmp");
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile, false), StandardCharsets.UTF_8))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.flush();
            }
            try {
                Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (Exception e) {
                Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean updateLine(String filename, String id, String newLine) {
        ReentrantReadWriteLock lock = getLock(filename);
        lock.writeLock().lock();
        try {
            List<String> lines = readAllLines(filename);
            boolean updated = false;
            String prefix = id + "|";
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith(prefix)) {
                    lines.set(i, newLine);
                    updated = true;
                    break;
                }
            }
            if (updated) return writeAllLines(filename, lines);
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean deleteLine(String filename, String id) {
        ReentrantReadWriteLock lock = getLock(filename);
        lock.writeLock().lock();
        try {
            List<String> lines = readAllLines(filename);
            String prefix = id + "|";
            boolean removed = lines.removeIf(line -> line.startsWith(prefix));
            if (removed) return writeAllLines(filename, lines);
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
