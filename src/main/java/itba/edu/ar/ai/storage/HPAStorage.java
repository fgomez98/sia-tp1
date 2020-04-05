package itba.edu.ar.ai.storage;

import itba.edu.ar.ai.Benchmarking;
import itba.edu.ar.ai.Node;
import itba.edu.ar.api.Storage;
import itba.edu.ar.model.State;

import java.util.*;

public class HPAStorage implements Storage {

    private Map<State, Node> frontier; // contains en O(1)
    private PriorityQueue<Node> priorityQueue;
    private Map<State, Node> explored;
    private double w;

    /*
        Si ya lo explore al nodo y con menor costo no agregar a la frontera
        Si dos nodos tienen mismo f(n), elegir el nodo con menor h(n).
     */

    private HPAStorage(double w) {
        this.w = w;
        priorityQueue = new PriorityQueue<>(this::nodeCompare);
        frontier = new HashMap<>();
        explored = new HashMap<>();
    }

    private double fn(Node node, double w) {
        return (1 - w) * node.getGn() + w * node.getHn();
    }

    private int nodeCompare(Node a, Node b) {
        int ret = Double.compare(fn(a, w), fn(b, w));
        if (ret == 0) {
            return a.getHn() - b.getHn();
        }
        return ret;
    }

    public static HPAStorage getStorage(double w) {
        return new HPAStorage(w);
    }

    @Override
    public Node get() {
        Node node = priorityQueue.poll();
        frontier.remove(node.getState());
        explored.put(node.getState(), node);
        return node;
    }

    @Override
    public void add(Node node) {
        if (!explored.containsKey(node.getState()) && !frontier.containsKey(node.getState())) {
            Benchmarking.nodesFrontier++;
            priorityQueue.offer(node);
            frontier.put(node.getState(), node);
        } else if (frontier.containsKey(node.getState())) {
            Node nodeInFrontier = frontier.get(node.getState());
            double frontierFn = fn(nodeInFrontier, w);
            double currentFn = fn(node, w);
            if (currentFn < frontierFn || (currentFn == frontierFn) && node.getHn() < nodeInFrontier.getHn()) {
                priorityQueue.remove(nodeInFrontier);
                priorityQueue.offer(node);
                frontier.put(node.getState(), node);
            }
        } else if (explored.containsKey(node.getState())) {
            Node nodeInExplored = explored.get(node.getState());
            double exploredFn = fn(nodeInExplored, w);
            double currentFn = fn(node, w);
            if (currentFn < exploredFn || (currentFn == exploredFn) && node.getHn() < nodeInExplored.getHn()) {
                priorityQueue.offer(node);
                frontier.put(node.getState(), node);
            }
        }

        /*
        if (explored.containsKey(node.getState())) {
            double a = fn(explored.get(node.getState()), w);
            double b = fn(node, w);
            if (a < b || (a == b && explored.get(node.getState()).getHn() <= node.getHn())) {
                return;
            }
        }
        Benchmarking.nodesFrontier++;
        priorityQueue.offer(node);
        */
    }

    @Override
    public boolean isEmpty() {
        return priorityQueue.isEmpty();
    }
}
