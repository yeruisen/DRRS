<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!-- <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- <html> -->
 <head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 
<style type="text/css">
*{margin:0;padding:0;list-style-type:none;}
a,img{border:0;}
.demo{width:900px;margin:20px auto;}
.demo h1{font-size:18px;height:28px;font-family:"微软雅黑","宋体";font-weight:normal;color:#3366cc;text-align:center;}
.demo h1 small{font-size:12px;font-style:normal;color:#5e5e5e;margin:0 0 0 20px;}

.thickdiv{display:none;position:fixed;top:0;left:0;z-index:10000001;width:100%;height:100%;background:#000;border:0;filter:alpha(opacity=15);opacity:.15;}
.thickbox{display:none;position:fixed;top:0;left:50%;z-index:10000002;overflow:hidden;padding:0 4px 4px 0;background:url(img/bg_shadow.gif) no-repeat -4px 0;margin-left:-175px;margin-top:180px;}
*html,*html body{background-image:url(about:blank);background-attachment:fixed;}
*html .thickbox{position:absolute;top:expression(eval(document.documentElement.scrollTop));}
.thicktitle{height:27px;padding:0 10px;border:solid #C4C4C4;border-width:1px 1px 0;background:#F3F3F3;line-height:27px;font-family:arial;font-size:14px;font-weight:bold;color:#333;}
.thickclose:link,.thickclose:visited{display:block;position:absolute;z-index:100000;top:7px;right:12px;overflow:hidden;width:15px;height:15px;background:url(img/bg_thickbox.gif) no-repeat 0 -18px;font-size:0;line-height:100px;}
.thickcon{overflow:auto;background:#fff;border:solid #C4C4C4;border-width:1px;padding:10px;}
*html .thickdiv{position:absolute;}
/*preview*/
#preview{width:876px;height:564px;margin:10px auto;border:1px solid #e6e6e6;padding:5px;zoom:1;overflow:hidden;}
#spec-n1{float:left;width:720px;height:540px;border:1px solid #e6e6e6;overflow:hidden;background:#f6f6f6;position:relative;z-index:5;zoom:1;}
#spec-n1 .o-img{width:720px;height:540px;text-align:center;}
#spec-n1 .switch{width:720px;height:540px;position:absolute;z-index:10;top:0;left:0;}
#spec-n1 img{max-width:720px;max-height:540px;_height:384px;}
#spec-n5{position:relative;float:right;width:134px;height:490px;padding:25px 0px;zoom:1}
#spec-list{width:134px;height:490px;overflow:hidden;}
#spec-list ul{height:490px;overflow:hidden;margin:0;}
#spec-list li{width:134px;height:125px;text-align:center;}
#spec-list img{padding:2px;border:1px solid #E8E8E8;}
#spec-list .curr img{padding:1px;border:2px solid #ffd300;}
#spec-n5 .control{position:absolute;width:134px;height:25px;background-repeat:no-repeat;cursor:pointer;}
#spec-top{top:-2px;background:url(img/icoin.png) 0 -30px;}
#spec-top.disabled{background-position:-85px -124px;}
#spec-bottom{bottom:-2px;background:url(img/icoin.png) -139px -30px;}
#spec-bottom.disabled{background-position:-139px -61px;}
#intro{float:left;width:720px;height:25px;line-height:25px;text-align:center;overflow:hidden;font-size:12px;}
#foward{display:block;width:360px;height:540px;float:left;cursor:url(img/foward.ico),pointer;background:#fff;filter:alpha(opacity=0);-moz-opacity:0;opacity:0;}
#foward.disabled,#next.disabled{cursor:default;}
#next{display:block;width:360px;height:540px;float:right;cursor:url(img/next.ico),pointer;background:#fff;filter:alpha(opacity=0);-moz-opacity:0;opacity:0;}
.thickcon{line-height:40px;text-align:center}
</style>

<!--     <base href="<%=basePath%>"/> -->
    
<!--     <title></title> -->
    
<!-- 	<meta http-equiv="pragma" content="no-cache"/> -->
<!-- 	<meta http-equiv="cache-control" content="no-cache"/> -->
<!-- 	<meta http-equiv="expires" content="0"/>     -->
<!-- 	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/> -->
<!-- 	<meta http-equiv="description" content="This is my page"/> -->
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  <%
  	Map<List<String>,Float> result = (Map<List<String>,Float>)session.getAttribute("result");
  	
  	int i = result.size();
  	int j = 0;
  	String first = null;
  	for (java.util.Map.Entry<List<String>, Float> entry : result.entrySet()) {
  		j++;
  		first = entry.getKey().get(2);
  		if (j>0)
  			break;
  	}
  	System.out.println(i);
  
   %>
   <div class="demo">
	
	<div id="preview">
	
		<div id="spec-n1">
			<div class="o-img">
				<img alt="" src="<%=first %>" onerror="this.src='img/none_347.gif'"  />
			</div>
			<div class="switch">
				<a id="foward">&nbsp;</a>
				<a id="next">&nbsp;</a>
			</div>													
		</div><!--spec-n1 end-->
		
		<div id="spec-n5">
			<div id="spec-top" class="control disabled"></div>
			<div id="spec-bottom" class="control"></div>
			<div id="spec-list">
				<ul>
<!-- 				<li class="curr"><img width="128" height="96" name="<%=first %>" alt="宾得（PENTAX） K-5（DA 18-55mm F3.5-5.6 AL WR） 单反套机（黑色）"  src="<%=first %>" /></li> -->
				<%
				Boolean f = true;
				 for (java.util.Map.Entry<List<String>, Float> entry : result.entrySet()) { %>
					<li <%if(f){out.print("class=\"curr\""); f = false;} %>><img width="128" height="96" name="<%=entry.getKey().get(2) %>" alt="宾得（PENTAX） K-5（DA 18-55mm F3.5-5.6 AL WR） 单反套机（黑色）"   src="<%=entry.getKey().get(2) %>"/></li>
				<%} %>														
				</ul>
			</div>
		</div><!--spec-n5 end-->
		
<!-- 		<div id="intro"></div> -->
		
	</div>
	
</div><!--demo end-->
											
	<div class="thickdiv" style="display:none;"></div>
	<div class="thickbox" style="width:272px;height:90px;display:none;">
		<div style="width:250" class="thicktitle">
			<span>提示</span>
		</div>
		<div style="width:250px;height:40px;" id="" class="thickcon">已经到最后一张了！</div>
		<a class="thickclose" href="#">×</a>
	</div>											
											
					
<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>						
<script type="text/javascript">
function closebox(){
	$(".thickdiv,.thickbox").hide();
}
$(".thickclose").bind("click",function(){
	$(".thickdiv,.thickbox").hide();
});

(function(a){a.fn.jdMarquee=function(h,b){if(typeof h=="function"){b=h;h={};}var j=a.extend({deriction:"up",speed:10,auto:false,width:null,height:null,step:1,control:false,_front:null,_back:null,_stop:null,_continue:null,wrapstyle:"",stay:5000,delay:20,dom:"div>ul>li".split(">"),mainTimer:null,subTimer:null,tag:false,convert:false,btn:null,disabled:"disabled",pos:{ojbect:null,clone:null}},h||{});var u=this.find(j.dom[1]);var e=this.find(j.dom[2]);var r;if(j.deriction=="up"||j.deriction=="down"){var l=u.eq(0).outerHeight();var c=j.step*e.eq(0).outerHeight();u.css({width:j.width+"px",overflow:"hidden"});}if(j.deriction=="left"||j.deriction=="right"){var n=e.length*e.eq(0).outerWidth();u.css({width:n+"px",overflow:"hidden"});var c=j.step*e.eq(0).outerWidth();}var o=function(){var s="<div style='position:relative;overflow:hidden;z-index:1;width:"+j.width+"px;height:"+j.height+"px;"+j.wrapstyle+"'></div>";u.css({position:"absolute",left:0,top:0}).wrap(s);j.pos.object=0;r=u.clone();u.after(r);switch(j.deriction){default:case"up":u.css({marginLeft:0,marginTop:0});r.css({marginLeft:0,marginTop:l+"px"});j.pos.clone=l;break;case"down":u.css({marginLeft:0,marginTop:0});r.css({marginLeft:0,marginTop:-l+"px"});j.pos.clone=-l;break;case"left":u.css({marginTop:0,marginLeft:0});r.css({marginTop:0,marginLeft:n+"px"});j.pos.clone=n;break;case"right":u.css({marginTop:0,marginLeft:0});r.css({marginTop:0,marginLeft:-n+"px"});j.pos.clone=-n;break;}if(j.auto){k();u.hover(function(){m(j.mainTimer);},function(){k();});r.hover(function(){m(j.mainTimer);},function(){k();});}if(b){b();}if(j.control){g();}};var k=function(s){m(j.mainTimer);j.stay=s?s:j.stay;j.mainTimer=setInterval(function(){t();},j.stay);};var t=function(){m(j.subTimer);j.subTimer=setInterval(function(){q();},j.delay);};var m=function(s){if(s!=null){clearInterval(s);}};var p=function(s){if(s){a(j._front).unbind("click");a(j._back).unbind("click");a(j._stop).unbind("click");a(j._continue).unbind("click");}else{g();}};var g=function(){if(j._front!=null){a(j._front).click(function(){a(j._front).addClass(j.disabled);p(true);m(j.mainTimer);j.convert=true;j.btn="front";t();if(!j.auto){j.tag=true;}f();});}if(j._back!=null){a(j._back).click(function(){a(j._back).addClass(j.disabled);p(true);m(j.mainTimer);j.convert=true;j.btn="back";t();if(!j.auto){j.tag=true;}f();});}if(j._stop!=null){a(j._stop).click(function(){m(j.mainTimer);});}if(j._continue!=null){a(j._continue).click(function(){k();});}};var f=function(){if(j.tag&&j.convert){j.convert=false;if(j.btn=="front"){if(j.deriction=="down"){j.deriction="up";}if(j.deriction=="right"){j.deriction="left";}}if(j.btn=="back"){if(j.deriction=="up"){j.deriction="down";}if(j.deriction=="left"){j.deriction="right";}}if(j.auto){k();}else{k(4*j.delay);}}};var d=function(w,v,s){if(s){m(j.subTimer);j.pos.object=w;j.pos.clone=v;j.tag=true;}else{j.tag=false;}if(j.tag){if(j.convert){f();}else{if(!j.auto){m(j.mainTimer);}}}if(j.deriction=="up"||j.deriction=="down"){u.css({marginTop:w+"px"});r.css({marginTop:v+"px"});}if(j.deriction=="left"||j.deriction=="right"){u.css({marginLeft:w+"px"});r.css({marginLeft:v+"px"});}};var q=function(){var v=(j.deriction=="up"||j.deriction=="down")?parseInt(u.get(0).style.marginTop):parseInt(u.get(0).style.marginLeft);var w=(j.deriction=="up"||j.deriction=="down")?parseInt(r.get(0).style.marginTop):parseInt(r.get(0).style.marginLeft);var x=Math.max(Math.abs(v-j.pos.object),Math.abs(w-j.pos.clone));var s=Math.ceil((c-x)/j.speed);switch(j.deriction){case"up":if(x==c){d(v,w,true);a(j._front).removeClass(j.disabled);p(false);}else{if(v<=-l){v=w+l;j.pos.object=v;}if(w<=-l){w=v+l;j.pos.clone=w;}d((v-s),(w-s));}break;case"down":if(x==c){d(v,w,true);a(j._back).removeClass(j.disabled);p(false);}else{if(v>=l){v=w-l;j.pos.object=v;}if(w>=l){w=v-l;j.pos.clone=w;}d((v+s),(w+s));}break;case"left":if(x==c){d(v,w,true);a(j._front).removeClass(j.disabled);p(false);}else{if(v<=-n){v=w+n;j.pos.object=v;}if(w<=-n){w=v+n;j.pos.clone=w;}d((v-s),(w-s));}break;case"right":if(x==c){d(v,w,true);a(j._back).removeClass(j.disabled);p(false);}else{if(v>=n){v=w-n;j.pos.object=v;}if(w>=n){w=v-n;j.pos.clone=w;}d((v+s),(w+s));}break;}};if(j.deriction=="up"||j.deriction=="down"){if(l>=j.height&&l>=j.step){o();}}if(j.deriction=="left"||j.deriction=="right"){if(n>=j.width&&n>=j.step){o();}}};})(jQuery);

(function(){

	var a={ 
		obj:$("#spec-list"),
		subobj:$("#spec-n1 img"),
		width:720,
		height:490,
		subheight:540,
		posi:function(){ 
			var h=a.subobj.attr("height");
			if(h<a.subheight&&h>0){
				a.subobj.css({ "margin-top":(a.subheight-h)/2})
			}else{
				a.subobj.css({ "margin-top":0 });
			}
		},
		
		images:function(){ 
			a.obj.find("img").bind("click",function(){ 
				var src=$(this).attr("src");
				var cont = $(this).attr("title");
				$("#intro").html(cont);
				$("#spec-n1 img").attr("src",src.replace("s128x96","s720x540"));
				a.posi();
				
				if($("#spec-list li").hasClass("curr")){
					$("#spec-list .curr").removeClass("curr")
					};
					$(this).parent().addClass("curr");
					var m=a.calculate.swith();   
					  
					if(m[1]==0){ 
						$("#foward").addClass("disabled");
					}else{ 
						if(m[1]+1==m[0]){ 
						$("#next").addClass("disabled");
					}else{
						$("#foward").removeClass("disabled");	
						$("#next").removeClass("disabled");
					}
				}
			})
		},
		
		alpha:function(){ 
			var img=new Image();
			img=$("#spec-n1").find("img").eq(0).get(0);
			var appname = navigator.appName.toLowerCase();
			if(appname.indexOf("netscape") == -1){ 
				if(img.readyState == "complete"){ 
					a.posi();
				}
			}else{ 
				img.onload = function (){ 
					if(img.complete == true){ 
						a.posi();
					}
				}	
			}
		},
	
		calculate:{ 	
			swith:function(){ 
				var p=a.obj.find("li");
				var m=[];
				m[0]=p.length;
				m[1]=p.index($(".curr"));
				return m;	
			},
			roll:function(){ 
				var p=a.obj.find("ul");
				var m=[];
				m[0]=parseInt(p.css("margin-top"));
				m[1]=p.height();
				return m;
			}
		},
		
		swith:function(){ 
			$("#foward").bind("click",function(){ 
				var m=a.calculate.swith();
				var s=a.calculate.roll();
				if(m[1]>0){ 
					$("#next").removeClass("disabled");
					$("#spec-list .curr").removeClass("curr");
					var ob = a.obj.find("li").eq(m[1]-1).find("img");
					var src=ob.attr("name"); 
					var cont = ob.attr("title");
					$("#intro").html(cont);
					$("#spec-n1").find("img").attr("src",src.replace("s128x96","s720x540"));
					a.posi();
					a.obj.find("li").eq(m[1]-1).addClass("curr");
				};
				if(m[1]==1){ 
					$("#foward").addClass("disabled");
				};
				if(m[1]==0){ 
					$(".thickdiv,.thickbox").show();
					setTimeout(function(){ closebox();},1200)
				}
				if(m[1]>2&&s[0]<0){ 
					a.roll.next(1);
				}
			});
			
			$("#next").bind("click",function(){ 
				var m=a.calculate.swith();
				var s=a.calculate.roll();
				if(m[1]<m[0]-1){ 
					$("#foward").removeClass("disabled");
					$("#spec-list .curr").removeClass("curr");
					var ob = a.obj.find("li").eq(m[1]+1).find("img");
					var src= ob.attr("name"); 
					var cont = ob.attr("title");
					$("#intro").html(cont);
					$("#spec-n1").find("img").attr("src",src.replace("s128x96","s720x540"));
					a.posi();
					a.obj.find("li").eq(m[1]+1).addClass("curr");
				};
				if(m[1]+2==m[0]){ 
					$("#next").addClass("disabled")
				};
				if(m[1]+1==m[0]){ 
					$(".thickdiv,.thickbox").show();
					setTimeout(function(){ closebox();},1200)
				};
				if(m[1]>=2&&s[1]+s[0]>490){ 
					a.roll.foward(1);
				}
			})
		},
		
		list:function(){ 
			$("#spec-top").bind("click",function(){ 
				var m=a.calculate.roll();
				if(m[0]<0){ 
					a.roll.next(2);	
				}
			});
			
			$("#spec-bottom").bind("click",function(){ 
				var m=a.calculate.roll();
				if(m[1]+m[0]>490){ 
					a.roll.foward(2);	
				}
			});
		},
		
		roll:{ 
			foward:function(step){ 
				var m=a.calculate.roll();
				a.obj.find("ul").animate({ "marginTop":m[0]-125*step},100);
			},
			next:function(step){ 
				var m=a.calculate.roll();
				a.obj.find("ul").animate({ "marginTop":m[0]+125*step},100)		
			}	
		},
		
		init:function(){ 
			a.obj.jdMarquee({
				deriction:"up",
				width:134,
				height:490,
				step:1,
				speed:4,
				delay:10,
				control:false
			});
		
			a.obj.find("ul").eq(1).remove();
			var h=a.obj.find("li").length;
			a.obj.find("ul").css({ "height":h*125});
			a.images();
			a.swith();
			a.list();
			a.alpha();
		}
	}; 
	
	a.init();
	
})(jQuery)
</script>
</body>
</html>
