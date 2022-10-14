package step2;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;

public class Traitement {
	public final static String indentationFormat = "\t";

	private String path;
	private ParserStep2 parser;
	private Visitor visitor;
	private GraphVisitor graphVisitor;

	public Traitement(String path) {
		super();
		this.path = path;
		parser = new ParserStep2();
		visitor = new Visitor();
		graphVisitor = new GraphVisitor();
	}

	public void display() {
		
		System.out.println("Q1 : Nombre de classes de l'application : " + visitor.getCountClasses());
		
		System.out.println("Q2 : Nombre de lignes de code de l'application : " + ParserStep2.getCountLines());
		
		System.out.println("Q3 : Nombre total de méthodes de l'application : " + visitor.getCountMethods());

		System.out.println("Q4 : Nombre total de packages de l'application : " + visitor.getCountPackages());

		System.out.println(
				"Q5 : Nombre moyen de méthodes par classe : " + (visitor.getCountMethods() / visitor.getCountClasses()));

		System.out.println("Q6 : Nombre moyen de lignes de code par méthode : "
				+ (visitor.getCountLocMethods() / visitor.getCountMethods()));

		System.out.println(
				"Q7 : Nombre moyen d'attributs par classe : " + (visitor.getCountAttributs() / visitor.getCountClasses()));

		Map<String, List<String>> mapClassesMethods = visitor.getMapClassesMethods();
		List<String> tenPercentClassesByGtMethods = getTenPercentClassesBygreaterNb(mapClassesMethods);
		System.out.println("Q8 : Les 10% des classes qui possèdent le plus grand nombre de méthodes : ");
		displayList(tenPercentClassesByGtMethods);

		Map<String, List<String>> mapClassesAttributes = visitor.getMapClassesAttributes();
		List<String> tenPercentClassesByGtAtt = getTenPercentClassesBygreaterNb(mapClassesAttributes);
		System.out.println("Q9 : Les 10% des classes qui possèdent le plus grand nombre d'attributs : ");
		displayList(tenPercentClassesByGtAtt);

		Set<String> classesOfTwoCategories = getClassesOfTwoCategories(tenPercentClassesByGtAtt,
				tenPercentClassesByGtMethods);
		System.out.println("Q10 : Les classes qui font partie en même temps des deux catégories précédentes : ");
		displaySet(classesOfTwoCategories); 

		System.out.println("Q11 : Les classes qui possèdent plus de X méthodes : \n*Veuillez insérer la valeur de X :");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		int x = sc.nextInt();
		List<String> classesHavingMoreThanXMeth = getClassesHavingMoreThanXMethods(mapClassesMethods, x);
		displayList(classesHavingMoreThanXMeth);

		System.out.println("Q12 : Les 10% des méthodes qui possèdent le plus grand nombre de lignes de code (par classe) :");
		Map<String, Map<String, Integer>> mapClassesMethodsLoc = visitor.getMapClassesMethodsLoc();
		Map<String, List<String>> tenPersentMethodsHavingMaxLocByClass = getTenPersentMethodsHavingMaxLocByClass(
				mapClassesMethodsLoc);
		displayMap(tenPersentMethodsHavingMaxLocByClass);
		System.out
				.println("Q13 : Le nombre maximal de paramètres par rapport à toutes les méthodes de l'application est de : "
						+ visitor.getCountArgsMax());
		System.out.println("");
		
		System.out.println("Construction du	graphe d’appel de m'application");

	}

	public void displayList(List<String> list) {
		if (list.isEmpty()) {
			System.out.println("Aucun élément à afficher");
		} else {
			for (String elt : list) {
				System.out.println(indentationFormat + "" + elt);
			}
		}
	}

	public void displayMap(Map<String, List<String>> map) {
		if (map.isEmpty()) {
			System.out.println("Aucun élément à afficher");
		} else {
			for (String className : map.keySet()) {
				System.out.println(indentationFormat + "Nom de la classe : " + className);
				for (String methodName : map.get(className)) {
					System.out.println(indentationFormat + indentationFormat + " Nom de la methode : " + methodName);
				}
			}
		}
	}

	public void displayMapMap(Map<String, Map<String, Integer>> map) {
		Map<String, Integer> methodMap;
		if (map.isEmpty()) {
			System.out.println("Aucun élément à afficher");
		} else {
			for (String className : map.keySet()) {
				System.out.println(indentationFormat + "Nom de la classe : " + className);
				methodMap = map.get(className);
				for (String methodName : methodMap.keySet()) {
					System.out.println(indentationFormat + indentationFormat + "Nom de la méthode : " + methodName
							+ "  nbLine : " + methodMap.get(methodName));

				}
			}
		}
	}

	public void displaySet(Set<String> set) {
		if (set.isEmpty()) {
			System.out.println("Aucun élément à afficher");
		} else {
			for (String elt : set) {
				System.out.println(indentationFormat + "" + elt);
			}
		}
	}


	private List<String> getClassesHavingMoreThanXMethods(Map<String, List<String>> mapClassesMethods, int x) {
		List<String> classesChoosed = new ArrayList<String>();
		for (String className : mapClassesMethods.keySet()) {
			if (mapClassesMethods.get(className).size() > x) {
				classesChoosed.add(className);
			}
		}
		return classesChoosed;
	}

	public Set<String> getClassesOfTwoCategories(List<String> tenPercentClassesByGtAtt,
			List<String> tenPercentClassesByGtMeth) {
		Set<String> result = tenPercentClassesByGtAtt.stream().distinct().filter(tenPercentClassesByGtMeth::contains)
				.collect(Collectors.toSet());
		return result;
	}

	public List<String> getJavaFiles() {
		File directory = new File(path);
		return parser.getFilesPaths(directory);
	}

	public List<String> getTenPercentClassesBygreaterNb(Map<String, List<String>> map) {
		List<String> classesChoosed = new ArrayList<String>();
		SortedMap<Integer, String> tempSortedMap = new TreeMap<Integer, String>();
		String[] sortedClassesTempArray;
		int nbClasses = map.keySet().size();
		int nbClassesWanted = ((10 * nbClasses) / 100) + 1;
		for (String className : map.keySet()) {
			tempSortedMap.put(map.get(className).size(), className);
		}
		sortedClassesTempArray = tempSortedMap.values().toArray(new String[0]);

		for (int i = sortedClassesTempArray.length - 1; i > (sortedClassesTempArray.length - nbClassesWanted
				- 1); i--) {
			classesChoosed.add(sortedClassesTempArray[i]);
		}
		return classesChoosed;
	}

	private Map<String, List<String>> getTenPersentMethodsHavingMaxLocByClass(
			Map<String, Map<String, Integer>> mapClassesMethodsLoc) {

		Map<String, List<String>> classAndMethodChoosed = new HashMap<String, List<String>>();

		SortedMap<Integer, String> tempSortedMap;

		Map<String, Integer> tempMapMethodLoc;

		String[] sortedMethodLocTempArray;

		List<String> tempListMethodChoosed;

		int tenPersentMethNb = 0;

		for (String ClassName : mapClassesMethodsLoc.keySet()) {
			tempMapMethodLoc = mapClassesMethodsLoc.get(ClassName);
			tenPersentMethNb = (int) ((tempMapMethodLoc.keySet().size() * 0.1) + 1);
			tempSortedMap = new TreeMap<Integer, String>();
			for (String methodName : tempMapMethodLoc.keySet()) {
				tempSortedMap.put(tempMapMethodLoc.get(methodName), methodName);
			}
			sortedMethodLocTempArray = tempSortedMap.values().toArray(new String[0]);

			if (sortedMethodLocTempArray.length > 0) {

				tempListMethodChoosed = new ArrayList<String>();
				for (int i = sortedMethodLocTempArray.length - 1; i > (sortedMethodLocTempArray.length
						- tenPersentMethNb - 1); i--) {
					if (i > 0 && i < sortedMethodLocTempArray.length - 1)
						tempListMethodChoosed.add(sortedMethodLocTempArray[i]);
					else {
						tempListMethodChoosed.add(sortedMethodLocTempArray[i]);
					}

				}
				classAndMethodChoosed.put(ClassName, tempListMethodChoosed);
			}

		}
		return classAndMethodChoosed;
	}

	public void start() throws FileNotFoundException, IOException {
		List<String> javaFilesPaths = this.getJavaFiles();
		CompilationUnit ast;

		for (String filePath : javaFilesPaths) {
			ast = parser.getCompilationUnit(filePath);
			visitor.setCu(ast);
			ast.accept(visitor);
		}
	}


	public String returnListAsString(List<String> list) {
		StringBuilder sb = new StringBuilder();
		if (list.isEmpty()) {
			sb.append("Aucun élément à afficher");
		} else {
			for (String elt : list) {
				sb.append(indentationFormat + "" + elt);
			}
		}
		return sb.toString();
	}



	public String returnSetAsString(Set<String> set) {
		StringBuilder sb = new StringBuilder();
		if (set.isEmpty()) {
			sb.append("Aucun élément à afficher");
		} else {
			for (String elt : set) {
				sb.append(indentationFormat + "" + elt);
			}
		}
		return sb.toString();
	}



	public void processGraph() throws FileNotFoundException, IOException {
		List<String> javaFilesPaths = this.getJavaFiles();
		CompilationUnit ast;

		for (String filePath : javaFilesPaths) {
			ast = parser.getCompilationUnit(filePath);
			graphVisitor.setCu(ast);
			ast.accept(graphVisitor);
			graphVisitor.calculateGraph();
		}

	}

	ArrayList<Method> methodsHavingReferences = new ArrayList<>();

	public String getGraph() {
		return graphVisitor.getGraph();
	}

	public void createGraph() throws IOException {

		File imgFile = new File("src/main/resources/graph.png");
		imgFile.createNewFile();

		DefaultDirectedGraph<String, DefaultEdge> directedGraph = new DefaultDirectedGraph<>(DefaultEdge.class);

		for (String methode : visitor.allMethodes) {
			directedGraph.addVertex(methode);
		}

		for (Method method : graphVisitor.methodsHavingReferences) {
			for (String sortie : method.calls) {
				if (visitor.allMethodes.contains(sortie))
					directedGraph.addEdge(method.getMethodName().toString(), sortie);

			}

			for (String entree : method.entries) {
				if (visitor.allMethodes.contains(entree))
					directedGraph.addEdge(entree, method.getMethodName().toString());

			}
		}

		JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(directedGraph);
		mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
		layout.execute(graphAdapter.getDefaultParent());

		BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 1, Color.WHITE, true, null);
		File imgFile1 = new File("src/main/resources/graph.png");
		ImageIO.write(image, "PNG", imgFile1);
		System.out.println("Image créer : src/main/resources/graph.png");
		displayImageGraph() ;
	}
	
	void displayImageGraph() throws IOException{
		File file = new File("src/main/resources/graph.png");
        BufferedImage bufferedImage = ImageIO.read(file);
        ImageIcon imageIcon = new ImageIcon(bufferedImage);

        JFrame jFrame = new JFrame();

        jFrame.setLayout(new FlowLayout());
        
        jFrame.setSize(800, 600);
        JLabel jLabel = new JLabel();

        jLabel.setIcon(imageIcon);
        jFrame.add(jLabel);
        jFrame.setVisible(true);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
	}
	


}
