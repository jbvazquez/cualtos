/* Variable global que almacena el nombre de usuario */
var nombre_usuario = ''; 
var historial_salas={};
var sala_actual='publica';

$(document).ready(function() {
  if (nombre_usuario.length<=0) {
    $('#modalUsuario').modal('show');
  }else if (nombre_usuario.length>0) {
    $('#etiquetaUsuario').val(nombre_usuario);
    console.log(nombre_usuario);
  }
});
function crearSala(idsala){
  historial_salas[idsala]={};
  historial_salas[idsala]["mensajes"] = [];
}

function cambiarSala(idsala){
  sala_actual = idsala;
  console.log(idsala);
  $('#log').html('');
}

function guardarUsuario(){
  nombre_usuario =$('#nombreUsuario').val();
  //nombre_usuario.length<=0 && 
  if (nombre_usuario.length<=0 || nombre_usuario.length<4) {
    $('#errorModal').html('<div id="errorUsuario" class="alert alert-danger alert-dismissible fade show" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>Ingrese un nombre de usuario de al menos 4 caracteres</div>');
    return;
  }else{
    $('#modalUsuario').modal('hide');
    init();
    setTimeout(function(){
     send_json(nombre_usuario);
   }, 200);
  }
}
function scrollBottom(){
  $('#log').scrollTop($('#log')[0].scrollHeight);
}

var socket;

function init(nombre_usuario) {
  var host = "ws://192.168.116.147:9000/echobot"; // SET THIS TO YOUR SERVER
  //var host = "ws://192.168.2.137:9000/echobot"; // SET THIS TO YOUR SERVER
  try {
    socket = new WebSocket(host);
    //log('WebSocket - status '+socket.readyState);
    socket.onopen    = function(msg) {
    //  log("Welcome - status "+this.readyState);
    crearSala('publica');
      };
    socket.onmessage = function(msg) {
                // alert(msg.data[0])
                if(msg.data[0] == '{'){
                  // alert(msg.data)
                  var json = JSON.parse(msg.data);
                  switch (json.opcion) {
                    case 'lista_usuarios':
                      for(var i in json.usuarios){
                        //$("#lista_usuarios").append('<li class="list-group-item" id="'+i+'">'+json.usuarios[i]+'</li>');
                        $("#lista_usuarios").append('<a href="javascript:;" onclick="cambiarSala(\''+i+'\');" id="'+i+'" class="list-group-item list-group-item-action" >'+json.usuarios[i]+'</a>');
                        crearSala(i);
                      }
                    break;
                    case 'nuevo_usuario':
                      //$("#lista_usuarios").append('<li class="list-group-item" id="'+json.id+'">'+json.nombre_usuario+'</li>');
                      $("#lista_usuarios").append('<a href="javascript:;" onclick="cambiarSala(\''+json.id+'\');" id="'+json.id+'" class="list-group-item list-group-item-action">'+json.nombre_usuario+'</a>');
                      crearSala(json.id);
                    break;
                    case 'usuario_exit':
                      $("#"+json.id).remove();
                    break;
                    case 'mensaje_publico':
                      console.log(json.mensaje);
                      $("#log").append(json.mensaje);
                      historial_salas.publica.mensajes.push(json.mensaje);
                      console.log(historial_salas);
                      scrollBottom();
                    break;
                    case 'mensaje_privado':
                      console.log(json.mensaje);
                      $("#log").append(json.mensaje);
                      //salas[json.remitente].mensajes.push(json.mensaje);
                      console.log(historial_salas);
                      scrollBottom();
                    break;
                    case 'zumbido':
                     console.log(json.opcion);
                     zumbido();
                    break;
                    default:

                  }
                  // alert(json);
                }
                //log("Received: "+msg.data);
                console.log("Received: "+msg.data);
               };
    socket.onclose   = function(msg) {
                 log("Disconnected - status "+this.readyState);
               };
  }
  catch(ex){
    log(ex);
  }
}


function send_json(nombre_usuario){
  var json = {};
  json["opcion"] = 'registrar_usuario';
  json["nombre_usuario"] = nombre_usuario;
  json = JSON.stringify(json);
  try {
    socket.send(json);
  } catch(ex) {
    log(ex);
  }
}// /send_json

function send_json2(){
  var mensaje = $('#txtMensajePublico').val();
  if(mensaje.length<=0) {
    alert("El mensaje no puede estar vacio");
    return;
  }
  var json = {};
  json["opcion"] = 'mensaje';
  json["sala_destino"] = sala_actual;
  json["mensaje"] = mensaje;
  json = JSON.stringify(json);
  $('#txtMensajePublico').val('');
  try {
    socket.send(json);
  } catch(ex) {
    log(ex);
  }
}// /send_json

function quit(){
  if (socket != null) {
    log("Goodbye!");
    socket.close();
    socket=null;
  }
}

function reconnect() {
  quit();
  init();
  setTimeout(function(){
    send_json(nombre_usuario);
  }, 200);
}

function log(msg){
  $("#log").append("<br>"+msg);
  scrollBottom();
}

function zumbido(){
  $.playSound("sounds/surprise-on-a-spring.mp3");
  //$( "#log" ).effect( "shake" );
  $( "body" ).effect( "shake" );

}
function enviar_zumbido(){
  var json = {};
  json["opcion"] = 'zumbido';
  json = JSON.stringify(json);
  try {
    socket.send(json);
  } catch(ex) {
    log(ex);
  }
}