package dad.recursos;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Classe para criar um ficheiro zip e comprimir os ficheiros de um diret�rio.
 * @author D�rio Pereira
 *
 */
public class ZipCompress {
	public static void compress(String dirPath, String name, String destinyPath) {
		final Path sourceDir = Paths.get(dirPath);
		String zipFileName = destinyPath.concat(name);
		try {
			final ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
			Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
					try {
						Path targetFile = sourceDir.relativize(file);
						outputStream.putNextEntry(new ZipEntry(targetFile.toString()));
						byte[] bytes = Files.readAllBytes(file);
						outputStream.write(bytes, 0, bytes.length);
						outputStream.closeEntry();
					} catch (IOException e) {
						Log.getInstance().printLog("Erro ao criar o zip! - " + e.getMessage());
						JOptionPane.showMessageDialog(null, "Ocorreu um erro ao salvar o arquivo zip",
								"ZipCompress - Erro", JOptionPane.OK_OPTION,
								new ImageIcon(getClass().getResource("/FC_SS.jpg")));
						e.printStackTrace();
					}
					return FileVisitResult.CONTINUE;
				}
			});
			outputStream.close();
		} catch (IOException e) {
			Log.getInstance().printLog("Erro ao criar o zip do backup! - " + e.getMessage());
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao salvar a c�pia de seguran�a!",
					"C�pia de Seguran�a - Erro", JOptionPane.OK_OPTION,
					new ImageIcon(ZipCompress.class.getResource("/FC_SS.jpg")));
			e.printStackTrace();
		}
	}
}