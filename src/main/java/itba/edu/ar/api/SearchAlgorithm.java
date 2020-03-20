package itba.edu.ar.api;

import itba.edu.ar.ai.BFSStorage;
import itba.edu.ar.ai.DFSStorage;
import itba.edu.ar.ai.HPAGreedy;

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
    },
    GLOBAL_GREEDY {
        @Override
        public Storage getStorage() {
            return HPAGreedy.getStorage(1);
        }
    },
    A_STAR {
        @Override
        public Storage getStorage() {
            return HPAGreedy.getStorage(0.5);
        }
    };

    public abstract Storage getStorage();
}
