package itba.edu.ar.api;

import itba.edu.ar.ai.DFSStorage;

public enum SearchAlgorithm {

    DFS {
        @Override
        public Storage getStorage() {
            return DFSStorage.get();
        }
    };

    public abstract Storage getStorage();
}
