package com.gmail.lukacat100.ForgePackageNameReplacer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ForgePackageNameReplacer {
	public static String fileName;
	final static Charset ENCODING = StandardCharsets.UTF_8;

	public static void main(String[] args) {
		String absolutePath = ForgePackageNameReplacer.class.getProtectionDomain().getCodeSource().getLocation()
				.getPath();
		fileName = absolutePath.substring(absolutePath.lastIndexOf("/") + 1, absolutePath.length());
		absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
		absolutePath = absolutePath.replaceAll("%20", " "); // Surely need to do
															// this here
		
		recursive(absolutePath);
	}

	public static void recursive(String initialPath) {
		File f = new File(initialPath);
		for (String s : f.list()) {
			File sf = new File(initialPath + "/" + s);
			if (sf.isDirectory()) {
				recursive(initialPath + "/" + s);
			} else {
				//o(s.substring(s.lastIndexOf(".")+1, s.length()));
				if (s.substring(s.lastIndexOf(".")+1, s.length()).equals("java") && !s.equals(fileName)) {
					packageReplacerForJavaFiles(Paths.get(sf.toURI()));
					o(s);
				}
			}
		}
	}

	public static void packageReplacerForJavaFiles(Path path) {
		List<String> ls = new ArrayList<>();
		try (Scanner scanner = new Scanner(path, ENCODING.name())) {
			while (scanner.hasNextLine()) {
				// process each line in some way
				String s = new String(scanner.nextLine());
				s = s.replaceAll("cpw.mods.fml", "net.minecraftforge.fml");
				ls.add(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
		      for(String line : ls){
		        writer.write(line);
		        writer.newLine();
		      }
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public static void o(String s) {
		System.out.println(s);
	}
}
