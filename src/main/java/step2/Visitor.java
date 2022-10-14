package step2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class Visitor extends ASTVisitor {

	private int countClasses;
	private int countMethods;
	private int countPackages;
	private int countInstrMeth;
	private int countArgsMax;
	List<String> allMethodes = new ArrayList<>();
	private String tempClassName;
	private int countLocMethods;
	private CompilationUnit cu;
	private int countAttributs;
	private List<String> packagesNames = new ArrayList<String>();
	private Map<String, List<String>> mapClassesMethods = new HashMap<String, List<String>>();
	private Map<String, List<String>> mapClassesAttributes = new HashMap<String, List<String>>();
	private Map<String, Map<String, Integer>> mapClassesMethodsLoc = new HashMap<String, Map<String, Integer>>();


	public int getCountClasses() {
		return countClasses;
	}

	public void setCountClasses(int countClasses) {
		this.countClasses = countClasses;
	}

	public int getCountMethods() {
		return countMethods;
	}

	public void setCountMethods(int countMethods) {
		this.countMethods = countMethods;
	}

	public int getCountPackages() {
		return countPackages;
	}

	public void setCountPackages(int countPackages) {
		this.countPackages = countPackages;
	}

	public List<String> getPackagesNames() {
		return packagesNames;
	}

	public void setPackagesNames(List<String> packagesNames) {
		this.packagesNames = packagesNames;
	}

	public int getCountLocMethods() {
		return countLocMethods;
	}

	public void setCountLocMethods(int countLocMethods) {
		this.countLocMethods = countLocMethods;
	}

	public CompilationUnit getCu() {
		return cu;
	}

	public void setCu(CompilationUnit cu) {
		this.cu = cu;
	}

	public int getCountAttributs() {
		return countAttributs;
	}

	public void setCountAttributs(int countAttributs) {
		this.countAttributs = countAttributs;
	}

	public Map<String, List<String>> getMapClassesMethods() {
		return mapClassesMethods;
	}

	public void setMapClassesMethods(Map<String, List<String>> mapClassesMethods) {
		this.mapClassesMethods = mapClassesMethods;
	}

	public String getTempClassName() {
		return tempClassName;
	}

	public void setTempClassName(String tempClassName) {
		this.tempClassName = tempClassName;
	}

	public Map<String, List<String>> getMapClassesAttributes() {
		return mapClassesAttributes;
	}

	public void setMapClassesAttributes(Map<String, List<String>> mapClassesAttributes) {
		this.mapClassesAttributes = mapClassesAttributes;
	}

	public Map<String, Map<String, Integer>> getMapClassesMethodsLoc() {
		return mapClassesMethodsLoc;
	}

	public void setMapClassesMethodsLoc(Map<String, Map<String, Integer>> mapClassesMethodsLoc) {
		this.mapClassesMethodsLoc = mapClassesMethodsLoc;
	}

	public int getCountInstrMeth() {
		return countInstrMeth;
	}

	public void setCountInstrMeth(int countInstrMeth) {
		this.countInstrMeth = countInstrMeth;
	}

	public int getCountArgsMax() {
		return countArgsMax;
	}

	public void setCountArgsMax(int countArgsMax) {
		this.countArgsMax = countArgsMax;
	}

	public Visitor() {
		super();
		countClasses = 0;
		countMethods = 0;
		countPackages = 0;
		countLocMethods = 0;
		countAttributs = 0;
		countInstrMeth = 0;
		setCountArgsMax(0);
	}

	public Visitor(boolean tags) {
		super(tags);
	}

	public void addFieldsForClass(String className, FieldDeclaration[] fields) {
		for (FieldDeclaration field : fields) {
			mapClassesAttributes.get(className).add(field.toString());
		}
	}

	@Override
	public void endVisit(MethodDeclaration md) {
		Map<String, Integer> mapMethodsLoc = mapClassesMethodsLoc.get(tempClassName);
		mapMethodsLoc.put(md.getName().toString(), countInstrMeth);

	}

	@Override
	public boolean visit(AssertStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(Block node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(BreakStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(ConstructorInvocation node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(ContinueStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(DoStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(EmptyStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(ExpressionStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(ForStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(IfStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(LabeledStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(MethodDeclaration methodNode) {
		allMethodes.add(methodNode.getName().toString());
		countMethods++;
		mapClassesMethods.get(tempClassName).add(methodNode.getName().toString());
		countInstrMeth = 0;
		Map<String, Integer> mapMethodsLoc = mapClassesMethodsLoc.get(tempClassName);
		mapMethodsLoc.put(methodNode.getName().toString(), countInstrMeth);
		if (countArgsMax < methodNode.parameters().size()) {
			countArgsMax = methodNode.parameters().size();
		}
		return true;
	}

	@Override
	public boolean visit(PackageDeclaration packageNode) {
		if (!packagesNames.contains(packageNode.getName().toString())) {
			countPackages++;
			packagesNames.add(packageNode.getName().toString());
		}
		return true;
	}

	@Override
	public boolean visit(ReturnStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(SuperConstructorInvocation node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(SwitchCase node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(SwitchStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(SynchronizedStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(ThrowStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(TryStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(TypeDeclaration typeNode) {
		tempClassName = typeNode.getName().toString();
		countAttributs += typeNode.getFields().length;
		mapClassesAttributes.put(tempClassName, new ArrayList<String>());
		addFieldsForClass(tempClassName, typeNode.getFields());
		mapClassesMethods.put(tempClassName, new ArrayList<String>());
		countClasses++;
		mapClassesMethodsLoc.put(tempClassName, new HashMap<String, Integer>());
		return true;
	}

	@Override
	public boolean visit(TypeDeclarationStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

	@Override
	public boolean visit(WhileStatement node) {
		countLocMethods++;
		countInstrMeth++;
		return true;
	}

}
