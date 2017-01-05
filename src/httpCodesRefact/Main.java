package httpCodesRefact;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.FileUtils;

// Default way to put the import is:
//		from six.moves import http_client as http

public class Main {
	private static int numOfReplaces = 0;
	/**
	 * Environment should be shown on asking.
	 * 4 means 4 lines before and 4 lines after the found line.
	 */

	static int lineEnvironment = 4;

	static Scanner scanner = new Scanner(System.in);

	static String[][] replaces = {
			{ "100", "http.CONTINUE" },
			{ "101", "http.SWITCHING_PROTOCOLS" },
			{ "200", "http.OK" },
			{ "201", "http.CREATED" },
			{ "202", "http.ACCEPTED" },
			{ "203", "http.NON_AUTHORITATIVE_INFORMATION" },
			{ "204", "http.NO_CONTENT" },
			{ "205", "http.RESET_CONTENT" },
			{ "206", "http.PARTIAL_CONTENT" },
			{ "300", "http.MULTIPLE_CHOICES" },
			{ "301", "http.MOVED_PERMANENTLY" },
			{ "302", "http.FOUND" },
			{ "303", "http.SEE_OTHER" },
			{ "304", "http.NOT_MODIFIED" },
			{ "305", "http.USE_PROXY" },
			{ "307", "http.TEMPORARY_REDIRECT" },
			{ "400", "http.BAD_REQUEST" },
			{ "401", "http.UNAUTHORIZED" },
			{ "403", "http.FORBIDDEN" },
			{ "404", "http.NOT_FOUND" },
			{ "405", "http.METHOD_NOT_ALLOWED" },
			{ "406", "http.NOT_ACCEPTABLE" },
			{ "407", "http.PROXY_AUTHENTICATION_REQUIRED" },
			{ "408", "http.REQUEST_TIMEOUT" },
			{ "409", "http.CONFLICT" },
			{ "410", "http.GONE" },
			{ "411", "http.LENGTH_REQUIRED" },
			{ "412", "http.PRECONDITION_FAILED" },
			{ "413", "http.REQUEST_ENTITY_TOO_LARGE" },
			{ "414", "http.REQUEST_URI_TOO_LONG" },
			{ "415", "http.UNSUPPORTED_MEDIA_TYPE" },
			{ "416", "http.REQUESTED_RANGE_NOT_SATISFIABLE" },
			{ "417", "http.EXPECTATION_FAILED" },
			{ "500", "http.INTERNAL_SERVER_ERROR" },
			{ "501", "http.NOT_IMPLEMENTED" },
			{ "502", "http.BAD_GATEWAY" },
			{ "503", "http.SERVICE_UNAVAILABLE" },
			{ "504", "http.GATEWAY_TIMEOUT" },
			{ "505", "http.HTTPVERSION_NOT_SUPPORTED" } };

	static int counter = 0;
	static Set<String> modifiedFiles = new HashSet<>();
	static boolean modified = false;

	public static void main3(String[] args) throws IOException {
		if (args.length < 1) {
			System.err.println("Add an argument!");
			System.exit(1);
		}

		List<File> filesInDir = (List<File>) FileUtils.listFiles(new File(args[0]), new String[] { "py" }, true);


		for (File current : filesInDir) {
			List<String> lines = FileUtils.readLines(current);
			List<String> newLines = new ArrayList<String>();

			for (int i = 0; i < lines.size(); i++) {
				newLines.add(refactor(lines, i, current.toString()));
			}

			FileUtils.writeLines(current, newLines, "\n");

			if (modified) {
				modifiedFiles.add(current.getAbsolutePath());
			}
			counter = 0;
			modified = false;
		}

		System.out.println("Finished! Total replaces: " + numOfReplaces);

		System.out.println("Files modified " + modifiedFiles);
		FileUtils.writeLines(new File("modified_files.txt"), modifiedFiles);
	}


	/**
	 * This method decides to refactor and does the refactoring for a given line.
	 * @param lines Collection containing the line
	 * @param lineNum which line should be refactord
	 * @param fileName file name
	 * @return  refactored line if we should refactor
	 * 			and the original line otherwise
	 * @throws IOException who cares
	 */
	private static String refactor(List<String> lines, int lineNum, String fileName) throws IOException {
		String line = lines.get(lineNum);

		// Not in a comment... at least not in a single line comment
		// And we can match to 3 numbers with boundaries (it wont match for 2017
		// and variable_300 and so on)
		if (!line.trim().startsWith("#") && line.matches(".*\\b\\d{3}\\b.*")) {
			for (String[] replacement : replaces) {
				String code = replacement[0]; // code
				String rep = replacement[1]; // constant name

				if (line.contains(code)) {
					numOfReplaces++;
					System.out.println("----------------- " + fileName + " ------------------------");
					for (int i = lineNum - lineEnvironment; i != lineNum + lineEnvironment + 1; i++) {
						if (i == lineNum) {
							System.out.println("X. " + lines.get(i));
						} else {
							if (i < lines.size()) // we dont like index out of bounds exception
								System.out.println(i + ". " + lines.get(i));
						}
					}

					System.out.println("-----------------------------------------");
					System.out.println("Replace code with constant (n for no, anything else for yes) ?");

					char a = scanner.next().toLowerCase().charAt(0);
					if (a == 'n') {
						modified = true;
						return line;
					} else {
						line = line.replace(code, rep);
					}

					return line;
				}
			}

		}

		return line;
	}
}
