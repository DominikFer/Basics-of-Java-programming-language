package hr.fer.zemris.java.tecaj_6;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

public class Lister2 {

	public static void main(String[] args) throws IOException {
		Path p = Paths.get("C:/wamp/www/php_vjestina");
		
		Radnik radnik = new Radnik();
		Files.walkFileTree(p, radnik);
		System.out.println("Broj .txt datoteka je " + radnik.getBrojTxtDatoteka());
		System.out.println("Ukupna veličina u KB je " + radnik.getUkupnaVelicina()/1024);
	}

	static class Radnik implements FileVisitor<Path> {
		
		private long ukupnaVelicina;
		private int brojTxtDatoteka;
		
		public long getUkupnaVelicina() {
			return ukupnaVelicina;
		}

		public int getBrojTxtDatoteka() {
			return brojTxtDatoteka;
		}

		private int razina;
		
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
			razina--;
			
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attra) throws IOException {
			if(razina == 0) {
				System.out.println(dir.toFile().getAbsolutePath());
			} else {
				System.out.println(String.format("%" + (razina*2) + "s", "") + dir.toFile().getName());
			}
			
			razina++;
			
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes arg1) throws IOException {
			String fileName = file.toFile().getName();
			System.out.println(String.format("%" + (razina*2) + "s", "") + fileName);
			
			ukupnaVelicina += Files.size(file);
			if(fileName.endsWith(".txt")) {
				brojTxtDatoteka++;
			}
			
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path arg0, IOException e) throws IOException {
			return FileVisitResult.CONTINUE;
		}
		
	}
}
