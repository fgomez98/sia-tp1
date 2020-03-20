package itba.edu.ar.api;

import itba.edu.ar.ai.BFSStorage;
import itba.edu.ar.ai.DFSStorage;

public enum SearchAlgorithm {

    DFS {
        @Override
        public Storage getStorage() {
            return DFSStorage.getStorage();
        }
    },
    BFS {
        @Override
        public Storage getStorage() {
            return BFSStorage.getStorage();
        }
    };

    public abstract Storage getStorage();
}
