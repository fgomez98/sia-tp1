package itba.edu.ar.ai.storage;

import itba.edu.ar.ai.Node;
import itba.edu.ar.api.IDStorage;
import itba.edu.ar.api.Storage;
import itba.edu.ar.model.State;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class IDDFSStorage implements Storage, IDStorage {

    /*
        En este stack se almacenan los nodos frontera de la iteracion actual
     */
    private Stack<Node> primaryStack;
    /*
        En este stack funciona como backup de los nodos frontera iniciales
        (los nodos no explotados a limit de profundidad)
        de la proxima iteracion
    */
    private Stack<Node> secondaryStack;
    private Map<State, Integer> explored;
    private int limit;
    private boolean resetTree;

    IDDFSStorage(boolean resetTree) {
        this.primaryStack = new Stack<>();
        this.secondaryStack = new Stack<>();
        explored = new HashMap<>();
        this.limit = 0;
        this.resetTree = resetTree;
    }

    public static IDDFSStorage getStorage(boolean resetTree) {
        return new IDDFSStorage(resetTree);
    }

    /*
        Si ya lo explore al nodo y con menor costo no agregar a la frontera
     */

    @Override
    public void add(Node node) {
        if (explored.containsKey(node.getState()) && explored.get(node.getState()) <= node.getDepth()) {
            return;
        }
        if (node.getDepth() <= limit) {
            primaryStack.push(node);
        }
        if (!resetTree && node.getDepth() == limit) {
            secondaryStack.push(node);
        }
    }

    @Override
    public Node get() {
        Node node = primaryStack.pop();
        explored.put(node.getState(), node.getDepth());
        return node;
    }

    @Override
    public boolean isEmpty() {
        return primaryStack.empty();
    }

    @Override
    public int deepend() {
        limit++;
        primaryStack = secondaryStack;
        secondaryStack = new Stack<>();
        if (resetTree) {
            explored = new HashMap<>();
        }
        return limit;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    protected void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public int frontierSize() {
        return primaryStack.size();
    }

    @Override
    public int exploredSize() {
        return explored.size();
    }
}
