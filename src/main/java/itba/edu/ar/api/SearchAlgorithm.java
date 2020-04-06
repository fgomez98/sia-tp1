package itba.edu.ar.api;

import itba.edu.ar.ai.storage.BFSStorage;
import itba.edu.ar.ai.storage.DFSStorage;
import itba.edu.ar.ai.storage.HPAStorage;
import itba.edu.ar.ai.storage.IDDFSStorage;

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
    }, GLOBAL_GREEDY {
        @Override
        public Storage getStorage() {
            return HPAStorage.getStorage(1);
        }
    },
    A_STAR {
        @Override
        public Storage getStorage() {
            return HPAStorage.getStorage(0.5);
        }
    }, IDDFS {
        @Override
        public Storage getStorage() {
            return IDDFSStorage.getStorage(resetTree);
        }
    }, IDA_STAR {
        @Override
        public Storage getStorage() {
            return IDDFSStorage.getStorage(resetTree);
        }
    };

    public static boolean resetTree = false;

    public abstract Storage getStorage();
}
