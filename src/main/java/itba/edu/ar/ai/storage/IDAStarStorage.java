package itba.edu.ar.ai.storage;

import itba.edu.ar.ai.Node;
import itba.edu.ar.api.IDStorage;
import itba.edu.ar.api.Storage;

public class IDAStarStorage extends IDDFSStorage implements Storage, IDStorage {

    private int nextLimit;

    private IDAStarStorage(boolean resetTree) {
        super(resetTree);
        nextLimit = -1;
    }

    public static IDAStarStorage getStorage(boolean resetTree) {
        return new IDAStarStorage(resetTree);
    }

    @Override
    public void add(Node node) {
        super.add(node);
        int fn = node.getHn();
        nextLimit = (nextLimit == -1 || fn < nextLimit) ? fn : nextLimit;
    }

    @Override
    public int deepend() {
        super.deepend(); /* super.deepend() realiza limit++, no me importa ya que yo dsp lo voy a pisar*/
        setLimit(nextLimit);
        return getLimit();
    }

}


