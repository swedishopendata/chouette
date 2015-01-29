package mobi.chouette.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.extern.log4j.Log4j;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;

@Log4j
public class CompressUtils {

	public static void uncompress(String filename, String path)
			throws IOException, ArchiveException {
		ArchiveInputStream in = new ArchiveStreamFactory()
				.createArchiveInputStream(new FileInputStream(
						new File(filename)));

		ArchiveEntry entry = null;
		while ((entry = in.getNextEntry()) != null) {

			File file = new File(path, entry.getName());
			file.getParentFile().mkdirs();
			if (!entry.isDirectory()) {
				OutputStream out = new FileOutputStream(file);
				IOUtils.copy(in, out);
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}
		}
	}

	public static void compress(String path, String filename)
			throws IOException {

		ZipArchiveOutputStream zout = new ZipArchiveOutputStream(
				Files.newOutputStream(Paths.get(filename)));

		Path dir = Paths.get(path);
		DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
		for (Path file : stream) {

			String name = file.getName(file.getNameCount() - 1).toString();
			long size = Files.size(file);

			ZipArchiveEntry entry = new ZipArchiveEntry(name);
			entry.setSize(size);
			InputStream in = Files.newInputStream(file);

			zout.putArchiveEntry(entry);
			IOUtils.copy(in, zout);
			zout.closeArchiveEntry();
			IOUtils.closeQuietly(in);

		}
		IOUtils.closeQuietly(zout);

	}
}
