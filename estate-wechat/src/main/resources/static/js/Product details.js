// JavaScript Document
$(document).ready(function(e) {
    $('.big li').click(function(){
		$('.big li').removeClass('curt');
		$(this).addClass('curt');
		var url=$(this).find('img').attr('src');
		$('.big').find('img').eq(0).attr('src',url);
		
		})
});