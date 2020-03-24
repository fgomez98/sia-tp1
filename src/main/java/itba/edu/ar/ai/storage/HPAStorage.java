package itba.edu.ar.ai.storage;

import itba.edu.ar.ai.Node;
import itba.edu.ar.api.Storage;
import itba.edu.ar.model.State;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HPAStorage implements Storage {

    private PriorityQueue<Node> priorityQueue;
    private Map<State, Node> explored;
    private double w;

    /*
        Si ya lo explore al nodo y con menor costo no agregar a la frontera
        Si dos nodos tienen mismo f(n), elegir el nodo con menor h(n).
     */

    private HPAStorage(double w) {
        this.w = w;
        Comparator<Node> comparator = (n1, n2) -> Double.compare(fn(n1, w), fn(n2, w));
        priorityQueue = new PriorityQueue<>(comparator);
        explored = new HashMap<>();
    }

    private double fn(Node node, double w) {
        return (1 - w) * node.getCost() + w * node.getEval();
    }

    public static HPAStorage getStorage(double w) {
        return new HPAStorage(w);
    }

    @Override
    public Node get() {
        Node node = priorityQueue.poll();
        explored.put(node.getState(), node);
        return node;
    }

    @Override
    public void add(Node node) {
        if (explored.containsKey(node.getState())) {
            double a = fn(explored.get(node.getState()), w);
            double b = fn(node, w);
            if (a < b) {
                return;
            } else if (a == b) {
                if (explored.get(node.getState()).getEval() < node.getEval()) {
                    return;
                }
            }
        }
        priorityQueue.offer(node);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    private static class Function {

        private double heuristic;
        private double fnEvaluation;

        private Function(double heuristic, double fnEvaluation) {
            this.fnEvaluation = fnEvaluation;
            this.heuristic = heuristic;
        }

        public static Function from(double heuristic, double fnEvaluation) {
            return new Function(heuristic, fnEvaluation);
        }

        public double getHeuristic() {
            return heuristic;
        }

        public double getFnEvaluation() {
            return fnEvaluation;
        }
    }
}
