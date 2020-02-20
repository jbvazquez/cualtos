#!/usr/bin/env php
<?php

require_once('./websockets.php');

class echoServer extends WebSocketServer {
  //protected $maxBufferSize = 1048576; //1MB... overkill for an echo server, but potentially plausible for other applications.

  private $lista_usuarios = array();
  //funcion que recibe los mensajes de los clientes
  protected function process ($user, $message) {
    // echo           imprime en consola
    // $this->send    manda mensajes a usuarios
    if($message[0] == '{'){
    // Si se ha recibido un archivo que comience con { es un objeto json
      $json_recibido = json_decode($message);
      if(! isset($json_recibido->opcion)){ $this->send($user,'Error!'); }
      //si no se envia ninguna opcion enviar error
      switch ($json_recibido->opcion) {
        //switch para las diferentes opciones
        case 'registrar_usuario':
          //Cuando se ha unido un usario al chat publico
        echo "NUEVO USUARIO: ".$json_recibido->nombre_usuario."\n";
          // MANDAR LISTA DE USUARIOS AL RECIEN CONECTADO
        $array_lista_usuarios = array(
          "opcion" => "lista_usuarios",
          "usuarios" => $this->lista_usuarios,
        );
          //ENVIAR LA LISTA DE LOS USUARIOS QUE ESTAN EN EL CHAT
          //CODIFICANDO EN JSON PARA PROCESAR LA RESPUESTA EN JAVASCRIPT
        $this->send($user,json_encode($array_lista_usuarios));

          // AGRER USUARIO nuevo A lista_usuarios
        $this->lista_usuarios[$user->id] = $json_recibido->nombre_usuario;
        echo json_encode($this->lista_usuarios)."\n\n";
          // MANDAR JSON a todos los usuarios menos al nuevo OPCION -nuevo usuario id # y nombre
        $array_nuevo_usuario = array(
          "opcion" => "nuevo_usuario",
          "id" => $user->id,
          "nombre_usuario" => $json_recibido->nombre_usuario,
        );
          // RECORRER LA LISTA DE LOS USUARIOS CONECTADOS Y ENVIAR LA LISTA A CADA UNO DE ELLOS
        foreach ($this->users as $user_){ 
            // echo $user_->id;
            // SI Y SOLO SI EL id de usuario ES DIFERENTE AL id auxiliar del ciclo
          if($user_->id != $user->id){
            $this->send($user_, json_encode($array_nuevo_usuario));
          }
        }
        break;
        case 'mensaje':
          //Cuando un usuario se envia un mensaje
          //Imprimir en consola
        echo $this->lista_usuarios[$user->id].": ".$json_recibido->mensaje."\n";
          //Variable de codigo html que contiene el mensaje
        $html ='
        <div class="mensaje">
        <div class="avatar-l"><img class="" src="images/avatar.png" alt="avatar"></div>
        <div class="texto-l">           
        <strong>'.$this->lista_usuarios[$user->id].':</strong> '.$json_recibido->mensaje.'
        <small class="text-muted">'.date('H:i A').'</small>
        </div>
        </div>';
        if ($json_recibido->sala_destino=='publica') {
            //Mensaje publico
          $array_mensaje = array(
            "opcion" => "mensaje_publico",
            "mensaje" => $html,
          );
            // ENVIAR A TODOS LOS USUARIOS
          foreach ($this->users as $user_){ 
            $this->send($user_, json_encode($array_mensaje));
          }
        }else{
            //Mensaje privado   
         $array_mensaje = array(
          "opcion" => "mensaje_privado",
          "mensaje" => $html,
        );
            //IMRPIMIR EN CONSOLA
         echo 'DE: '.$this->lista_usuarios[$user->id].' PARA: '.$json_recibido->sala_destino.' '.$json_recibido->mensaje."\n";
            //ENVIAR A REMITENTE
         $this->send($this->users[$user->id], json_encode($array_mensaje));
            //ENVIAR A DESTINATARIO
         $this->send($this->users[$json_recibido->sala_destino], json_encode($array_mensaje));

       }
       break;
       case 'zumbido':
       $array_zumbido = array(
        "opcion" => "zumbido",
      );
            // mandar a todos los conectados excepto al que lo envia
       foreach ($this->users as $user_){
        if($user_->id != $user->id){
                //ENVIAR JSON PARA PROCESAR RESPUESTA EN JAVASCRIPT
          $this->send($user_, json_encode($array_zumbido));
        }
      }
      break;
      default:
          # code...
      break;

    }
  }
  //SI NO ES UN JSON
  else if(strpos($message,'|') !== false){
    $x = explode('|', $message);
      //DEPURACION DE USUARIOS CONECTADOS
      //PARTIR CADENA PARA SEPARAR id de usuario de  | mensaje
    $this->send($this->users[$x[1]],$x[0].' id usuario '.$this->sockets[$user->id]);
      //  $this->send($user,$message.' jalou');
  }
  else{
    //si no mendar mensaje a todos
    foreach ($this->users as $user){
      $this->send($user, $message);
    }
  }
}


protected function connected ($user) {
    // Do nothing: This is just an echo server, there's no need to track the user.
    // However, if we did care about the users, we would probably have a cookie to
    // parse at this step, would be looking them up in permanent storage, etc.
    // echo print_r($this->users, 1);
    // $usuarios = '';
    // foreach ($this->users as $user){ // sending to all connected users
  	// 		$usuarios .= $user->id.'|';
  	// }
    // echo $usuarios;
    // $this->send($user,' ENTRANDO '.$this->sockets[$user->id]);
}

protected function closed ($user) {
    // Do nothing: This is where cleanup would go, in case the user had any sort of
    // open files or other objects associated with them.  This runs after the socket
    // has been closed, so there is no need to clean up the socket itself here.

    // QUITAR USUARIO DE lista_usuarios
    // echo print_r($user,1);
  unset($this->lista_usuarios[$user->id]);

  $array_usuario_exit = array(
    "opcion" => "usuario_exit",
    "id" => $user->id
  );
  // mandar lista de usuarios a todos los clientes cuando un usuario salga
    foreach ($this->users as $user_){ 
      // echo $user_->id;
      if($user_->id != $user->id){
        $this->send($user_, json_encode($array_usuario_exit));
      }
    }
    // echo json_encode($this->$array_usuario_exit)."\n\n";

    // MANDAR JSON a todos los usuarios menos al que se desconecta OPCION -se salió el usuario id #
  }
}

$echo = new echoServer("192.168.116.147","9000");

try {
  $echo->run();
}
catch (Exception $e) {
  $echo->stdout($e->getMessage());
}
