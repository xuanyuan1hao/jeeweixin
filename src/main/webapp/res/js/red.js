
/////////////////////////////////////////////////////

//					web·唐明明 			           //

/////////////////////////////////////////////////////


$(document).ready(function() {
	if (window.DeviceMotionEvent){
		var speed = 25;
		var audio = document.getElementById("shakemusic");
		var openAudio = document.getElementById("openmusic");
		var x = t = z = lastX = lastY = lastZ = 0;
		window.addEventListener('devicemotion',
			function () {
				var acceleration = event.accelerationIncludingGravity;
				x = acceleration.x;
				y = acceleration.y;
				if (Math.abs(x - lastX) > speed || Math.abs(y - lastY) > speed) {
					audio.play();
					$('.red-tc').css('display', 'none');
					$('.red-ss').addClass('wobble');
					setTimeout(function(){
						var id=$("#fanId").val();
						var openId=$("#openId").val();
						$.ajax({
							type: "GET",
							url:"/wxapi/my_yaoyiyao_json.html",
							data:"id="+id+"&openId="+openId,// 要提交的表单
							success: function(ret)
							{
								audio.pause();
								openAudio.play();
								$('.red-tc').css('display', 'block');
								$('.red-ss').removeClass('wobble');
								var returnJson=JSON.parse(ret);
								if(returnJson.result==-1){
									alert(returnJson.msg);
								}else if(returnJson.result==-2){
									$("#red-mjh").show();
									$("#red-wzj").hide();
									$("#red-yzj").hide();
									$("#red-refer").hide();
								}else if(returnJson.result==1){
									$("#red-wzj").show();
									$("#red-mjh").hide();
									$("#red-yzj").hide();
									$("#red-refer").hide();
								}else if(returnJson.result==2){
									$("#userGoldCoin").html((Number(returnJson.userYaoyiyaoGoldCoin)).toFixed(2));
									$("#red-yzj").show();
									$("#red-wzj").hide();
									$("#red-mjh").hide();
									$("#red-refer").hide();
								}else if(returnJson.result==-3){
									$("#red-refer").show();
									$("#red-wzj").hide();
									$("#red-mjh").hide();
									$("#red-yzj").hide();
								}
							},
							error: function(error){
								audio.pause();
								openAudio.play();
								$('.red-tc').css('display', 'block');
								$('.red-ss').removeClass('wobble');
								alert(error);
							}
						});
					}, 1500);
				};
				lastX = x;
				lastY = y;
			},false);
	};
	$()
});





