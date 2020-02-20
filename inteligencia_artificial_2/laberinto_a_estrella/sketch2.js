function removeFromArray(array, element) {
    for (var i = array.length - 1; i >= 0; i--) {
        if (array[i] == element) {
            array.splice(i, 1);
        }
    }
}

function heuristic(a, b) {
    // var d = dist(a.i, a.j, b.i, b.j);
    var d = abs(a.i - b.i) + abs(a.j - b.j);
    return d;
}
var cols = 6;
var rows = 6;
var grid = new Array(cols);
// Formas para terminar el algoritmo A *
// 1: buscar a traves de los nodos si he llegado al final
// 2: si no tengo nodos por visitar y no he llegado al final
// sin solucion
// openSet lista de nodos que aun necesitan ser evaluados
// el algoritmo finaliza cuando openSet esta vacio
// porque no queda nada por visitar
var openSet = [];
// closedSet lista de nodos evualuados para no ser visitados de nuevo
var closedSet = [];
// inicio
var start;
// fin
var end;
// variales globales para ancho y alto
var w, h;
// camino
var path = [];
// variable para detener cuando no haya solicion
var nosolution = false;
// constructor
// se pasan parametros cuando se dibuja la malla
function Spot(i, j) {
    this.i = i;
    this.j = j;
    this.f = 0;
    this.g = 0;
    this.h = 0;
    // vecinos de este nodo
    this.neighbors = [];
    // de donde vino
    this.previous = undefined;
    // establecer muros
    // this.wall = false;
    // if (random(1) < 0.4) {
    //     this.wall = true;
    // }
    this.show = function(color) {
        // dibujar rectangulo
        fill(color);
        if (this.wall) {
            fill(0);
        }
        noStroke();
        rect(this.i * w, this.j * h, w - 1, h - 1);
    }
    this.addNeighbors = function(grid) {
        var i = this.i;
        var j = this.j;
        // buscar vecinos si
        // 4 vecinos
        if (i < cols - 1) {
            this.neighbors.push(grid[i + 1][j]);
        }
        if (i > 0) {
            this.neighbors.push(grid[i - 1][j]);
        }
        if (j < rows - 1) {
            this.neighbors.push(grid[i][j + 1]);
        }
        if (j > 0) {
            this.neighbors.push(grid[i][j - 1]);
        }
        if (i > 0 && j > 0) {
            this.neighbors.push(grid[i - 1][j - 1]);
        }
        if (i > cols - 1 && j > 0) {
            this.neighbors.push(grid[i - 1][j - 1]);
        }
    }
}

function setup() {
    createCanvas(400, 400);
    console.log('A*')
    // calcular el tamano
    w = width / cols;
    h = height / rows;
    // Creando Array Bidimensional
    for (var i = 0; i < cols; i++) {
        grid[i] = new Array(rows);
    }
    // Hacer los lugares
    // Filas
    for (var i = 0; i < cols; i++) {
        // Columnas
        for (var j = 0; j < rows; j++) {
            // Lugar
            grid[i][j] = new Spot(i, j);
        }
    }
    // buscar vecinos de los lugares
    // Filas
    for (var i = 0; i < cols; i++) {
        // Columnas
        for (var j = 0; j < rows; j++) {
            // Lugar
            grid[i][j].addNeighbors(grid);
        }
    }
    // establecer el nodo inicial
    // en la ezquina superior izquierda de la matriz
    start = grid[5][3];
    // establecer el nodo final
    // en la ezquina inferior derecha de la matriz
    end = grid[0][5];
    // el inicio y el final nunca deberan ser muros
    start.wall = false;
    end.wall = false;
    wall1 = grid[2][1];
    wall1.wall = true;
    wall2 = grid[1][2];
    wall2.wall = true;
    wall3 = grid[1][4];
    wall3.wall = true;
    wall4 = grid[3][4];
    wall4.wall = true;
    wall5 = grid[4][2];
    wall5.wall = true;
    wall6 = grid[4][3];
    wall6.wall = true;
    wall7 = grid[5][4];
    wall7.wall = true;
    openSet.push(start);
    console.log(grid);
}

function draw() {
    if (openSet.length > 0) {
        // buscar camino mas corto
        var winner = 0;
        for (var i = 0; i < openSet.length; i++) {
            if (openSet[i].f < openSet[winner].f) {
                winner = i;
            }
        }
        // actual
        var current = openSet[winner];
        // si el actual es el final
        if (current === end) {
            // detener
            noLoop();
            console.log('Hecho!');
        }
        // quitar el nodo visitado de la lista de nodos por visitar
        // parametros arreglo, posicion
        removeFromArray(openSet, current);
        // agregar a la lista de nodos visitados
        closedSet.push(current);
        // revisar cada vecino
        var neighbors = current.neighbors;
        for (var i = 0; i < neighbors.length; i++) {
            var neighbor = neighbors[i];
            // si es diferente de un nodo visitado
            if (!closedSet.includes(neighbor) && !neighbor.wall) {
                // actualzar distancia en una variable temporal
                var tempG = current.g + 1;
                if (openSet.includes(neighbor)) {
                    if (tempG < neighbor.g) {
                        neighbor.g = tempG;
                    }
                } else {
                    // mejor camino
                    neighbor.g = tempG;
                    openSet.push(neighbor);
                }
                // heuristica
                neighbor.h = heuristic(neighbor, end);
                neighbor.f = neighbor.g + neighbor.h;
                neighbor.previous = current;
            }
        }
    } else {
        // sin solucion
        console.log('Sin solucion');
        nosolution = true;
        noLoop();
    }
    background(0);
    // dibujar lugares en la malla
    for (var i = 0; i < cols; i++) {
        for (var j = 0; j < rows; j++) {
            // color blanco
            grid[i][j].show(color(255));
        }
    }
    //dibujar nodo visitado
    for (var i = 0; i < closedSet.length; i++) {
        // rojo
        closedSet[i].show(color(255, 0, 0));
    }
    // dibujar nodo evaluado
    for (var i = 0; i < openSet.length; i++) {
        // verde
        openSet[i].show(color(0, 255, 0));
    }
    if (!nosolution) {
        // ENCONTRAR EL CAMINO
        path = [];
        var temp = current;
        path.push(temp);
        while (temp.previous) {
            path.push(temp.previous);
            temp = temp.previous;
        }
    }
    // dibujar camino
    for (var i = 0; i < path.length; i++) {
        // azul
        path[i].show(color(0, 0, 255));
    }
}