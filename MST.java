package sxs180036;

import sxs180036.Graph.Vertex;
import sxs180036.Graph.Edge;
import sxs180036.Graph.GraphAlgorithm;
import sxs180036.Graph.Factory;
import sxs180036.Graph.Timer;

import sxs180036SP9.BinaryHeap.Index;
import sxs180036SP9.BinaryHeap.IndexedHeap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.io.FileNotFoundException;
import java.io.File;

public class MST extends GraphAlgorithm<MST.MSTVertex> {
    String algorithm;
    public long wmst;
    List<Edge> mst;
    Graph forestGraphDFS; 
    
 
    MST(Graph g) {
	super(g, new MSTVertex((Vertex) null));
	forestGraphDFS = g;
    }

    public static class MSTVertex implements Index, Comparable<MSTVertex>, Factory {
    	int state;
    	int component;
    	int d;
    
    MSTVertex(Vertex u) {
		state=0;
		
		
	}

	MSTVertex(MSTVertex u) {  // for prim2
	}

	public MSTVertex make(Vertex u) { return new MSTVertex(u); }

	public void putIndex(int index) { }

	public int getIndex() { return 0; }

	public int compareTo(MSTVertex other) {
	    return 0;
	}
    }

    public long kruskal() {
	algorithm = "Kruskal";
	Edge[] edgeArray = g.getEdgeArray();
        mst = new LinkedList<>();
        wmst = 0;
        return wmst;
    }
	
    public long boruvka() {
	algorithm = "Boruvka";       
	wmst = 0;
	Vertex[] vertexArray = g.getVertexArray();
	Edge[] edgeArray = g.getEdgeArray();
	Graph F = new Graph(vertexArray.length);
	int count = countandLabel(F) ;
	while (count > 1) 
	{
	  addSafeEdges(edgeArray, F, count);
	  count = countandLabel(F);
	}
	Edge[] mstEdges = F.getEdgeArray();
	for(Edge e:mstEdges){
		wmst+= e.getWeight();
	}
	return wmst;
	
    }
    
    private void addSafeEdges(Edge[] edgeArray, Graph h, int count) {
		Edge safe[] = new Edge[count];
		ArrayList<Edge> safeEdges = new ArrayList<>();
			for(Edge e:edgeArray)
			{
				MSTVertex u = get(e.fromVertex());
				MSTVertex v = get(e.toVertex());
				if(u.component!=v.component)
				{
					if(safe[u.component]==null || e.compareTo(safe[u.component])<0)
						safe[u.component]=e;
					if(safe[v.component]==null || e.compareTo(safe[v.component])<0)
						safe[v.component]=e;
				}
			}
			for(Edge j: safe) {
				if(!safeEdges.contains(j))
				{
					 h.addEdge(j.fromVertex().getName() - 1, j.toVertex().getName() - 1, j.getWeight());
				}
				safeEdges.add(j);
			}	
    }

	public int countandLabel(Graph G){
    	int count=0;	
    	Vertex[] vertexArray = G.getVertexArray();
    	forestGraphDFS = G;
    			for (Vertex v : vertexArray)
    				get(v).state=0;
    			for (Vertex v : vertexArray)
    			{
    					if(get(v).state==0)
    					{						
    					label(v, count);
    					count++;    				
    					}
    			}
    			forestGraphDFS = g;
    			return count;
    			}
    
    public void label(Vertex s, int count){//label one component
    	get(s).component=count;
    	get(s).state=1;		
    	for(Graph.Edge e: forestGraphDFS.outEdges(s)){
    		Vertex v = e.otherEnd(s);  		
    		if(get(v).state==0) {    		
    			label(v,count);
    			}   	
    	}
    }
    		

    public long prim2(Vertex s) {
	algorithm = "indexed heaps";
        mst = new LinkedList<>();
	wmst = 0;
	IndexedHeap<MSTVertex> q = new IndexedHeap<>(g.size());
	return wmst;
    }

    public long prim1(Vertex s) {
	algorithm = "PriorityQueue<Edge>";
        mst = new LinkedList<>();
	wmst = 0;
	PriorityQueue<Edge> q = new PriorityQueue<>();
	return wmst;
    }

    public static MST mst(Graph g, Vertex s, int choice) {
	MST m = new MST(g);
	switch(choice) {
	case 0:
	    m.boruvka();
	    break;
	case 1:
	    m.prim1(s);
	    break;
	case 2:
	    m.prim2(s);
	    break;
	case 3:
	    m.kruskal();
	    break;
	default:
	    
	    break;
	}
	return m;
    }

    public static void main(String[] args) throws FileNotFoundException {
    Scanner in;
    File inputFile = new File("C:\\Users\\singi\\Downloads\\SP9 test cases\\mst-10k-30k-1085305.txt");
    in = new Scanner(inputFile);
	
	int choice = 0;  // prim3
//        if (args.length == 0 || args[0].equals("-")) {
//            in = new Scanner(System.in);
//        } else {
//            File inputFile = new File(args[0]);
//            in = new Scanner(inputFile);
//        }

	if (args.length > 1) { choice = Integer.parseInt(args[1]); }

	Graph g = Graph.readGraph(in);
        Vertex s = g.getVertex(1);

	Timer timer = new Timer();
	MST m = mst(g, s, choice);
	System.out.println(m.algorithm + "\n" + m.wmst);
	System.out.println(timer.end());
    }
}