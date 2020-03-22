package itba.edu.ar.ai.storage;

import itba.edu.ar.ai.Node;
import itba.edu.ar.api.IDStorage;
import itba.edu.ar.api.Storage;

public class IDAStarStorage extends IDDFSStorage implements Storage, IDStorage {

    private int nextLimit;

    private IDAStarStorage() {
        super();
    }

    public static IDAStarStorage getStorage() {
        return new IDAStarStorage();
    }

    @Override
    public void add(Node node) {
        super.add(node);
        int fn = node.getEval();
        nextLimit = (nextLimit == -1 || fn < nextLimit) ? fn : nextLimit;
    }

    @Override
    public int deepend() {
        super.deepend(); /* super.deepend() realiza limit++, no me importa ya que yo dsp lo voy a pisar*/
        setLimit(nextLimit);
        return getLimit();
    }

}


