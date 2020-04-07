# sia-tp1
Métodos de búsqueda Desinformados e Informados

### Instrucciones
Situarse en la raiz y ejecutar los comandos segun corresponda

#### Compilacion
```
mvn clean install
```

#### Ejecución
Otorgar permisos de ejecución en caso de ser necesario
```
chmod 777 ./sokoban-solver.sh
``` 
Para correr el generador de soluciones ejecutar el siguiente comando con los parametros deseados 
```
./sokoban-solver.sh -algorithm=[Algoritmo] -level=[Path] -heuristic=[Heuristica] -out=[Path] -reset-tree[false|true] -deadlocks-off[false|true] -time=[Timepo]
```

Posibles parametros:

 * -algorithm [DFS | BFS | GLOBAL_GREEDY | A_STAR | IDDFS | IDA_STAR]  : Algoritmo con el cual se realizara la busqueda, requerido         
 * -level                                 : Archivo con la descripcion del nivel, requerido
 * -heuristic [MANHATTAN | MANHATTAN_OPT | POINT_POSITION_OPT | POINT_POSITION | PYTHAGORAS | GREEDY_ASSIGNMENT]  : Heuristica a usar
 * -out                                   : Directorio donde se guardara en un archivo .txt los resultados (default: ./)
 * -reset-tree                            : Reseteo del arbol en algoritmos Iterative Deepening (default: false)
 * -time                                  : Tiempo limite a correr en segundos
 * -deadlocks-off                         : Apaga chequeo de deadlocks (default: false)
 
 #### Ejemplos Oportunos
 ###### Ejemplo 1
 ```
 ./sokoban-solver.sh -algorithm=DFS -level=./Demo Levels/Level 1
 ```
 ###### Ejemplo 2
 ```
 ./sokoban-solver.sh -algorithm=A_STAR -level=./Demo Levels/Level 2 -heuristic=MANHATTAN
```
 