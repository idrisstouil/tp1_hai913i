package step2;

import java.util.ArrayList;
import java.util.HashSet;

public class Method {

	String methodName;
	HashSet<String> calls = new HashSet<>();
	HashSet<String> entries = new HashSet<>();

	public Method(String name, HashSet<String> calls) {
		super();
		this.methodName = name;
		this.calls = calls;
	}

	public Method(String name, ArrayList<String> calls) {
		HashSet<String> r = new HashSet<>();

		for(String ref :calls) {
			r.add(ref);
		}
		this.methodName = name;
		this.calls = r;
	}

	public void addIfNotContained(Method method) {
		if (method.getCalls().contains(this.getMethodName())) {
			this.getEntries().add(method.getMethodName());
		}
	}

	public HashSet<String> getCalls() {
		return calls;
	}

	public HashSet<String> getEntries() {
		return entries;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setCalls(HashSet<String> calls) {
		this.calls = calls;
	}

	public void setEntries(HashSet<String> entries) {
		this.entries = entries;
	}

	public void setName(String name) {
		this.methodName = name;
	}

	public String getMethodWithCallsLinks() {
		StringBuilder res = new StringBuilder("");
		for (String string : calls) {
			res.append(methodName).append("->").append(string).append(" ");
		}
		return res.toString();
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();

		res.append("\n==========================================\n").append("Methodes en entree : ")
		.append(this.getEntries().toString()).append("\n").append("Methode Noeud : ").append(this.getMethodName())
		.append("\nMethodes en sortie : ")
		.append(this.getCalls().toString())
		.append("\n==========================================\n");
		
		return res.toString();
	}

}
