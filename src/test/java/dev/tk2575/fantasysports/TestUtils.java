package dev.tk2575.fantasysports;

import lombok.*;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TestUtils {
	public static String readTestResourceFileToString(@NonNull String filename) throws IOException {
		if (filename.isBlank()) {
			throw new IllegalArgumentException("filename is a required argument");
		}
		StringBuilder sb = new StringBuilder();
		File file = ResourceUtils.getFile(String.format("src/test/resources/%s",filename));
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		}
		return sb.toString();
	}
}
