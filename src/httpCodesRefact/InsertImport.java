package httpCodesRefact;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class InsertImport {
	private static int numOfReplaces = 0;
	static String httpImport = "from six.moves import http_client as http";
	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		//it should read the modified_files.txt file after running Main.java
		List<String> files = FileUtils.readLines(new File(args[0]));

		for (String file : files) {
			System.out.println("-------------------------------------------------------");
			List<String> lines = FileUtils.readLines(new File(file));

			boolean m = true;
			int lineNum = 0;

			while (m) {
				String line = lines.get(lineNum);
				System.out.println(lineNum++ + ". " + line);

				// >30 because of the license things
				if (!line.isEmpty() && lineNum > 30 && !(line.startsWith("import") || line.startsWith("from")))
					m = false;
			}
			System.out.println("-------------------------------------------------------");
			System.out.println("Line?");
			int l = scanner.nextInt();
			lines.add(l, httpImport);
			++numOfReplaces;

			FileUtils.writeLines(new File(file), lines, "\n");
		}

		System.out.println("Finished! Replaces: " + numOfReplaces);

	}

}
