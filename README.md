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
./sokoban-solver.sh -algorithm=[Algoritmo a utilizar] -level=[Path al nivel] -heuristic=[Heuristica a usar] -out=[Path a carpeta para archivo de salida] -reset-tree[Recomputa el arbol en algorimtos Iterative Deepening] -deadlocks-off[Apaga deteccion de deadlocks] -time[Timo maximo a correr]
```
Son obligatorios: algorithm, level