package step2;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

public class GraphVisitor extends ASTVisitor {

	CompilationUnit cu;
	List<MethodDeclaration> methodsDeclaration = new ArrayList<>();
	List<MethodInvocation> methodsInvocation = new ArrayList<>();
	List<SuperMethodInvocation> superMethodsInvocation = new ArrayList<>();
	ArrayList<Method> methodsHavingReferences = new ArrayList<>();

	public void calculateGraph() {
		calculateAllMethods();
		calculateReferences();
	}
	
	private void calculateAllMethods() {
		for (int i = 0; i < methodsDeclaration.size(); i++) {
			// init
			ArrayList<String> references = new ArrayList<>();
			GraphVisitor graphVisitor2 = new GraphVisitor();
			graphVisitor2.setCu(cu);
			// calcul
			MethodDeclaration method = methodsDeclaration.get(i);
			method.accept(graphVisitor2);
			String methodName = method.getName().toString();
			if (!methodContainedInCollection(methodName, methodsHavingReferences)) {
				references = graphVisitor2.getMethodsCalls();
				Method methodToAdd = new Method(methodName, references);
				methodsHavingReferences.add(methodToAdd);
			}
		}
	}

	private boolean methodContainedInCollection(String methodName, ArrayList<Method> methodsWithReferences2) {

		for (int i = 0; i < methodsWithReferences2.size(); i++) {
			if (methodsWithReferences2.get(i).getMethodName().equals(methodName)) {
				return true;
			}
		}
		return false;
	}

	private void calculateReferences() {
		for (int i = 0; i < this.methodsHavingReferences.size(); i++) {
			for (int j = 0; j < this.methodsHavingReferences.size(); j++) {
				if (i != j)
					this.methodsHavingReferences.get(i).addIfNotContained(this.methodsHavingReferences.get(j));
			}
		}
	}

	private ArrayList<String> getMethodsCalls() {
		ArrayList<String> res = new ArrayList<>();
		String methodName;
		for (MethodInvocation methodsInvocation : methodsInvocation) {
			methodName = methodsInvocation.getName().toString();
			if (!res.contains(methodName)) {
				res.add(methodName);
			}
		}
		return res;
	}

	public void setCu(CompilationUnit ast) {
		cu = ast;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		if (node.isConstructor()) {
			return false;
		}
		methodsDeclaration.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(MethodInvocation node) {
		methodsInvocation.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SuperMethodInvocation node) {
		superMethodsInvocation.add(node);
		return super.visit(node);
	}

	public String getGraph() {
		StringBuilder res = new StringBuilder("");
		for (Method method : methodsHavingReferences) {
			res.append(method);
		}
		if (res.toString().equals("")) {
			res.append("Aucun appel de methodes .");
		}
		return res.toString();
	}



}
