package com.infy.eta.compilers;

import com.infy.eta.databeans.JudgeTestCasesEntity;
import com.infy.eta.utils.DoInTransaction;
import com.infy.eta.utils.TestComparator;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;

import javax.ws.rs.FormParam;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Amit Joshi on 10/3/2015.
 */
public class JavaCompiler {

	private static final Logger logger = Logger.getLogger(JavaCompiler.class);

	private final String    data;
	private final Integer   problemId;
	private final JSONArray compilerOutput;

	public JavaCompiler(String data, Integer problemId) {
		this.data = data;
		this.problemId = problemId;
		compilerOutput = new JSONArray();
	}

	public String invoke() throws IOException, InterruptedException {
		logger.info("Received post request with data " + data);
		StringBuffer buffer = new StringBuffer("");
		String       programFilePath;
		//create a java file Test.java
		File file = createJavaFile(data);
		programFilePath = FilenameUtils.getFullPath(file.getAbsolutePath());
		logger.info("File Path of java file is " + programFilePath);
		//create a batch file to execute compile and run commands
		List<JudgeTestCasesEntity> testCases = fetchTestCasesForProblem(problemId);
		logger.info("Test cases for this problem are: " + testCases.size());
		for (JudgeTestCasesEntity testCase : testCases) {
			HashMap<String, String> object = new HashMap<>();
			object.put("id", String.valueOf(testCase.getId()));
			object.put("problemId", String.valueOf(testCase.getProblemId()));
			object.put("description", testCase.getDescription());
			object.put("input", testCase.getInput());
			object.put("expectedOutput", testCase.getOutput());
			createBatchFile(file, programFilePath, testCase.getInput());
			//execute the batch file
			Runtime r = Runtime.getRuntime();

			Process p = r.exec(programFilePath + "Test.bat"); // execute the compiler script

			boolean waitReturn = p.waitFor(5, TimeUnit.SECONDS);
			if (!waitReturn) {
				p.destroyForcibly();
			}
			logger.info(waitReturn);
			//read output and return it as a response
			readOutput(buffer);
			object.put("actualOutput", buffer.toString().replaceAll("\\u0000", ""));
			//delete the files now
			deleteFiles(programFilePath);

			boolean testResult = new TestComparator().matchTestResults(buffer.toString(), testCase.getOutput());
			object.put("result", String.valueOf(testResult));
			object.put("totalPoints", String.valueOf(testCase.getPoints()));
			object.put("earnedPoints", testResult ? String.valueOf(testCase.getPoints()) : String.valueOf(0));
			compilerOutput.put(object);
		}
		return compilerOutput.toString();
	}

	private File createJavaFile(@FormParam("data") String data) throws IOException {
		logger.info("Creating Test.java with received content");
		File       file   = new File("Test.java");
		FileWriter writer = new FileWriter(file);
		writer.write(data);
		writer.flush();
		writer.close();
		logger.info("Write complete.");
		return file;
	}

	private List<JudgeTestCasesEntity> fetchTestCasesForProblem(Integer problemId) {
		return new DoInTransaction<List<JudgeTestCasesEntity>>() {
			@Override
			protected List<JudgeTestCasesEntity> doWork() {
				return session.createCriteria(JudgeTestCasesEntity.class).add(Restrictions.eq("problemId", problemId)).list();
			}
		}.execute();
	}

	private void createBatchFile(File file, String programFilePath, String input) throws IOException {
		logger.info("Writing Batch file for test input " + input);
		String[] inputs = input.split("\\n");
		logger.info(inputs.length + "" + Arrays.toString(inputs));
		FileWriter writer = new FileWriter("Test.bat");
		writer.write("@echo off\n");
		writer.write("cd " + programFilePath + "\n");
		writer.write("javac " + FilenameUtils.getName(file.getName()) + " > compile.txt \n");

		for (int i = 0; i < inputs.length; i++) {
			if (i == 0) {
				writer.write("( ECHO " + inputs[i] + "\n");
			} else if (i == inputs.length - 1) {
				writer.write("ECHO " + inputs[i] + ")| java " + FilenameUtils.getBaseName(file.getName()));
				for (int j = 1; j <= inputs.length; j++) {
					writer.write(" %" + j + "%");
				}
				writer.write(" > run.txt");
			} else {
				writer.write("ECHO " + inputs[i] + "\n");
			}
		}
		writer.flush();
		writer.close();
		logger.info("Write Complete");
	}

	private void readOutput(StringBuffer buffer) throws IOException {
		logger.info("Reading output");
		FileReader fr = new FileReader("compile.txt");
		readContent(buffer, fr);
		buffer.append("\n");
		fr = new FileReader("run.txt");
		readContent(buffer, fr);
		buffer.append("\n");
		logger.info("Read Complete" + buffer.toString());
	}

	private void deleteFiles(String programFilePath) throws IOException {
		logger.info("Deleting compile.txt");
		Files.deleteIfExists(Paths.get(programFilePath + "compile.txt"));
		logger.info("Deleting run.txt");
		Files.deleteIfExists(Paths.get(programFilePath + "run.txt"));
		logger.info("Deleting Test.bat");
		Files.deleteIfExists(Paths.get(programFilePath + "Test.bat"));
		logger.info("Deleting Test.class");
		Files.deleteIfExists(Paths.get(programFilePath + "Test.class"));
		logger.info("Deleting Test.java");
		Files.deleteIfExists(Paths.get(programFilePath + "Test.java"));
	}

	private void readContent(StringBuffer buffer, FileReader reader) throws IOException {
		char[] a = new char[50];
		reader.read(a); // reads the content to the array
		for (char c : a)
			buffer.append(c); //appends the characters to the buffer one by one
		reader.close();
	}
}
