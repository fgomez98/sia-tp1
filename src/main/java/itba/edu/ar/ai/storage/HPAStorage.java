package itba.edu.ar.ai.storage;

import itba.edu.ar.api.Cost;
import itba.edu.ar.ai.InformedNode;
import itba.edu.ar.ai.Node;
import itba.edu.ar.api.Storage;
import itba.edu.ar.model.State;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HPAStorage implements Storage {

    private PriorityQueue<Node> priorityQueue;
    private Map<State, Double> explored;
    private double w;

    /*
        TODO: Si ya lo explore al nodo y con menor costo no agregar a la frontera
        TODO: Si dos nodos tienen mismo f(n), elegir el nodo con menor h(n). -> la funcion de costo es cte
     */

    private HPAStorage(double w) {
        this.w = w;
        Comparator<Node> comparator = (n1, n2) -> Double.compare(fn(n1, w), fn(n2, w));
        priorityQueue = new PriorityQueue<>(comparator);
        explored = new HashMap<>();
    }

    private double fn(Node node, double w) {
        return (1 - w) * Cost.getCost(node) + w * ((InformedNode) node).getEvaluation();
    }

    public static HPAStorage getStorage(double w) {
        return new HPAStorage(w);
    }

    @Override
    public Node get() {
        Node node = priorityQueue.poll();
        explored.put(node.getState(), fn(node, w));
        return node;
    }

    @Override
    public void add(Node node) {
        if (explored.containsKey(node.getState()) && explored.get(node.getState()) <= fn(node, w)) {
            return;
        }
        priorityQueue.offer(node);

    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
