// JavaScript Document
// JavaScript Document
$(function(){
		
		var width=$(".banner").width()
		$(".banner ul li").width(width)
		var height=$(".banner").height()
		$(".banner ul li").height(height)
		$(".banner ul li img").css({width:'100%',height:'100%'})
		var html=$(".banner ul").html();
		var k=0
		var timer
		$('.banner .banner_left').click(function(){
			clearInterval(timer)
			if($('.banner ul').is(':animated')){
				return false;
				}
			k++;
			var len=$(".banner ul li").length;  
			
			if(k>len-1){
				$(".banner ul").append(html);
				var len=$(".banner ul li").length;  
				$(".banner ul").width(width*len);
				}
				
			
			var l=-(k*width);
			changedd(k);
			$('.banner ul').animate({"left":l+"px"},1000)
			})
			
		$('.banner_right').click(function(){
			clearInterval(timer)
			if($('.banner ul').is(':animated')){
				return false;
				}
			k--;
			var len=$(".banner ul li").length;  
			
			if(k<0){
				$(".banner ul").prepend(html);
				var len=$(".banner ul li").length;  
				$(".banner ul").width(width*len);
				$('.banner ul').css({"left":(-2*width)+"px"})
				k=1
				}
				
			
			var l=-(k*width);
			changedd(k);
			$('.banner ul').animate({"left":l+"px"},1000)
			})
	/*定时器开始*/
			timer=setInterval(function(){
			if($('.banner ul').is(':animated')){
				return false;
				}
			k++;
			var len=$(".banner ul li").length;  
			
			if(k>len-1){
				$(".banner ul").append(html);
				var len=$(".banner ul li").length;  
				$(".banner ul").width(width*len);
				}
				
			
			var l=-(k*width);
			changedd(k);
			$('.banner ul').animate({"left":l+"px"},1000)
				
				},1000)
	/*定时器结束*/	
		$('.banner').mouseover(function(){
				clearInterval(timer)
			
			})
		$('.banner').mouseout(function(){
				timer=setInterval(function(){
			if($('.banner ul').is(':animated')){
				return false;
				}
			k++;
			var len=$(".banner ul li").length;  
			
			if(k>len-1){
				$(".banner ul").append(html);
				var len=$(".banner ul li").length;  
				$(".banner ul").width(width*len);
				}
				
			
			var l=-(k*width)
			changedd(k);
			$('.banner ul').animate({"left":l+"px"},1000)

				
				},1000)
			})	
		
		
		$('.banner dd').click(function(){
		    $('.banner dd').removeClass('curt')
			$(this).addClass('curt')
		    k=$(this).index()
			var l=-(k*width);
			
			$('.banner ul').animate({"left":l+"px"},500)
		})
		
		
		
		})
		
		function changedd(i){
		    i=i%4;
			
			$(".banner dd").removeClass("curt");
			$(".banner dd").eq(i).addClass("curt");
			
			}
			$(function(){
		$('.sidebar li a').hover(function(){
			$(this).find('b').css('color','red')
			
			},function(){
				
				$(this).find('b').css('color','#fe0000')
				
				})
		
		})
