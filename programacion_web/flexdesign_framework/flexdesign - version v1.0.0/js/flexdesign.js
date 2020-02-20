$(document).ready(function(){
	/* Los elementos que sean div dentro de una seccion no tendran padding */
	$("section:has(.parallax)").css("padding", "0");
	/* Scroll */
	$("#button").addClass("scrollTop-autohidden");
	
	$(window).scroll(function(){
		if ($(this).scrollTop() > 100) {
			$('.scrollToTop').fadeIn();
			$("#button").removeClass("scrollTop-autohidden");
		} else {
			$('.scrollToTop').fadeOut();
		}
	});
	$('.scrollToTop').click(function(){
		$('html, body').animate({scrollTop : 0},800);
		return false;
	});
	
});


/* Funcion para mostrar el aside del lado derecho si el mouse esta cerca del borde derecho */
$( document ).on( "mousemove", function( event ) {
	//console.log('width: '+width);
	//console.log( "pageX: " + event.pageX + ", pageY: " + event.pageY );
	if (width>600) {
		if (event.pageX >=width-20) {
			$('aside').fadeIn();
		}else {
			if ( event.pageX <=width-250)
			{ 
			//console.log('orilla derecha');mouseleave!!!!!
			$('aside').fadeOut();
		} 
	}
}
});
/* Funcion para hacer funcional la barra de navegacion responsiva */

var width = $(window).width();
$(window).on('resize', function(){
	if($(this).width() != width){
		width = $(this).width();
			//console.log(width);    
			if (width>600) {
				$('.navbar-collapse').show();
						//console.log("mostrar barra");
					}else if(width<600){
						$('.navbar-collapse').hide();
					}
				}
			});
/* Efecto Toogle Menu responsivo */
$( ".navbar-toggle" ).click(function() {
	$('.navbar-collapse').fadeToggle( "slow");
});

/* Cerrar Alertas */
$( ".closebtn" ).click(function() {
	this.parentElement.style.display = 'none';
});

jQuery.fn.selectText = function(){
	this.find('input').each(function() {
		if($(this).prev().length == 0 || !$(this).prev().hasClass('p_copy')) { 
			$('<p class="p_copy" style="position: absolute; z-index: -1;"></p>').insertBefore($(this));
		}
		$(this).prev().html($(this).val());
	});
	var doc = document;
	var element = this[0];
    //console.log(this, element);
    if (doc.body.createTextRange) {
    	var range = document.body.createTextRange();
    	range.moveToElementText(element);
    	range.select();
    } else if (window.getSelection) {
    	var selection = window.getSelection();        
    	var range = document.createRange();
    	range.selectNodeContents(element);
    	selection.removeAllRanges();
    	selection.addRange(range);
    }
};
$('.select-text').on('click', function(e) {
	var selector = $(this).data('selector');
	$(selector).selectText();
	e.preventDefault();
});
