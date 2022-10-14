package step2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class ParserStep2 {

	public static String projectSourcePath;
	public static final String jrePath = "C:\\Users\\SCD UM\\Desktop\\openlogic-openjdk-8u342-b07-windows-64\\jre\\lib\\rt.jar";

	public static void main(String[] args) throws IOException {

		JFileChooser f = new JFileChooser();
		
		//lire le chemin vers le dossier de projet
		f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		f.showSaveDialog(null);
		f.getSelectedFile();
		projectSourcePath = f.getSelectedFile().getAbsolutePath() + "\\src";
		System.out.println("Chemin du projet selectionné:");

		System.out.println(projectSourcePath);
		Traitement t = new Traitement(projectSourcePath);
		t.start();
		t.display();
		t.processGraph();

		System.out.println(t.getGraph());

		t.createGraph();

	}

	// read all java files from specific folder
	public static ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<File>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} else if (fileEntry.getName().contains(".java")) {
				// System.out.println(fileEntry.getName());
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}

	// create AST
	private static CompilationUnit parse(char[] classSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		parser.setBindingsRecovery(true);

		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);

		parser.setUnitName("");

		String[] sources = { projectSourcePath };
		String[] classpath = { jrePath };

		parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);
		parser.setSource(classSource);

		return (CompilationUnit) parser.createAST(null); // create and parse
	}

	// navigate method information
	public static void printMethodInfo(CompilationUnit parse) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);

		for (MethodDeclaration method : visitor.getMethods()) {
			System.out.println("Method name: " + method.getName() + " Return type: " + method.getReturnType2());
		}

	}

	// navigate variables inside method
	public static void printVariableInfo(CompilationUnit parse) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethods()) {

			VariableDeclarationFragmentVisitor visitor2 = new VariableDeclarationFragmentVisitor();
			method.accept(visitor2);

			for (VariableDeclarationFragment variableDeclarationFragment : visitor2.getVariables()) {
				System.out.println("variable name: " + variableDeclarationFragment.getName() + " variable Initializer: "
						+ variableDeclarationFragment.getInitializer());
			}

		}
	}

	// navigate method invocations inside method
	public static void printMethodInvocationInfo(CompilationUnit parse) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethods()) {

			MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
			method.accept(visitor2);

			for (MethodInvocation methodInvocation : visitor2.getMethods()) {
				System.out.println("method " + method.getName() + " invoc method " + methodInvocation.getName());
			}

		}
	}

	private static int countLines;

	public CompilationUnit getCompilationUnit(String filePath) throws FileNotFoundException, IOException {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		char[] fileContent = this.getFileContent(filePath).toCharArray();
		parser.setSource(fileContent);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		return cu;
	}

	public String getFileContent(String filePath) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while (line != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
			line = br.readLine();
			ParserStep2.countLines++;
		}
		br.close();
		return sb.toString();
	}

	public List<String> getFilesPaths(File directory) {

		List<String> filesPaths = new ArrayList<String>();

		for (File file : directory.listFiles()) {
			if (!file.isDirectory()) {
				if (this.isJavaFile(file)) {
					filesPaths.add(file.getAbsolutePath());
				}
			} else {
				filesPaths.addAll(getFilesPaths(file));
			}
		}
		return filesPaths;
	}

	private boolean isJavaFile(File file) {

		final String extentionWanted = ".java";
		int extentionIndex = file.getName().length() - 5;
		int endFileIndex = file.getName().length();
		final String fileExtention = file.getName().substring(extentionIndex, endFileIndex);

		return fileExtention.equals(extentionWanted);
	}

	public static int getCountLines() {
		return countLines;
	}

	public static void setCountLines(int countLines) {
		ParserStep2.countLines = countLines;
	}

}
