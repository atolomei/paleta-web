<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="es" lang="es">
  <head>
    <meta name="robots" content="index" />
    <meta name="googlebot" content="index" />

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="icon" href="./images/favicon.gif" type="image/x-icon" />
    <link rel="shortcut icon" href="./images/favicon.ico" type="image/x-icon" />

    <meta name="title" content="${meta.title!"Torneo"}" />
    <meta name="description" content="${meta.description!"Torneo de Paleta"}" />
    <meta name="Keywords" content="${meta.keywords!"paleta"}" />
	<meta name="language" content="${meta.language!"Spanish"}" />

    <meta
      name="viewport"
      content="width=device-width, initial-scale=1.0, minimum-scale=1.0, user-scalable=yes"
    />
    <meta name="robots" content="all" />

    <link rel="stylesheet" type="text/css" href="../../css/kbee.css" />
    <link rel="stylesheet" type="text/css" href="../../css/kbee-1000-1600.css" />


    <link rel="stylesheet" type="text/css" href="../../css/bootstrap-5.3.3-dist/css/bootstrap.css" />
    
    <script src="./js/csi.min.js"></script>

    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@800&display=swap" rel="stylesheet"/>

    
    <!-- ANALYTICS -------------------------------------------------------- -->
    
    <!-- Google tag (gtag.js) -->
      
	<script async src="https://www.googletagmanager.com/gtag/js?id=G-FFGEDJZC3F"></script>
	<script>
	  window.dataLayer = window.dataLayer || [];
	  function gtag(){dataLayer.push(arguments);}
	  gtag('js', new Date());
	
	  gtag('config', 'G-FFGEDJZC3F');
	</script>
  </head>

  <body class="kbee">
  
    <!-- TOP TOOLBAR -------------------------------------------------------- -->

    <section class="image-banner-container bkflor">
      <nav id="toolbar" class="toolbar">
        <div class="odilon-logo">
          <div class="toolbar-item">
            <div style="float: left; width: initial; width: 100%">
              <a href="index.html" style="float: left; display: block; width: 100%">
                <span style="display: inline-block;float: left;font-weight: bold;width: 100%;">
                   CUBA	
                </span>
              </a>
            </div>
          </div>
        </div>

        <div class="main-menu hidden-xs hidden-sm hidden-md">
          <div class="toolbar-item"><a href="./index.html#torneo">El torneo</a></div>
		  <div class="toolbar-item"><a href="./index.html#zonas">Zonas</a></div>
		  <div class="toolbar-item"><a href="./equipos.html">Equipos</a></div>
          <div class="toolbar-item"><a href="./index.html#fixture">Fixture</a></div>
          <div class="toolbar-item"><a href="./index.html#tabla">Tabla</a></div>
          <div class="toolbar-item"><a href="./index.html#contacto">Contacto</a></div>
        </div>
        
        <div class="main-menu hidden-lg">
          <div class="toolbar-item"><a href="./index.html">Portada</a></div>
		  <div class="toolbar-item"><a href="./equipos.html">Equipos</a></div>
        </div>
        
      </nav>

      <!-- TOP TOOLBAR -------------------------------------------------------- -->

      <div id="banner" class="banner">
        <div class="canvas-container" style="background: transparent">
          <div class="text-container" style="float: right">
            <div class="text centered">
              <h2 class="main-banner">Torneo Clausura Categoría B CUBA</h2>
            </div>
          </div>
        </div>
      </div>
    </section>

    
	<nav id="breadcrumb"> 
      	<div class="section">
	    	<div class="section-internal-container">
		    		<div class="section-content" style="min-height: auto;padding: 0.5em 2em;">
		        		<div class="row" style="font-size: 0.875em; text-align:center;";>
					  		<div class="col-lg-12 col-md-12 col-xs-12">
					  			<a class="link" href="index.html">portada</a><span> &gt; </span><span><b></b>equipos</b></span>
					  		</div>
						</div>
		       		</div>
			</div>
		</div>
    </nav>          
          
    
    

	<!-- ALERTAS -------------------------------------------------- -->
	
	<#if alert?has_content>
	<section id="alert" class="section light">
			<div class="section-internal-container">
				<div class="section-content alert-container">
					<div class="row">
						  	<div class="col-lg-12 col-md-12 col-xs-12">
							  		
							  		<div class="alert ${alert.alertClass}" role="alert">
								 		<#if alert.title?has_content>
								 		<h5>${alert.title}</h5>
								 		</#if>	
								 		<#if alert.text?has_content>
								 			${alert.text}
								 		</#if>
								 	</div>
						 	</div>
					</div>
				</div>
			</div>
	</section>
	</#if>		
	
<!-- EQUIPOS -------------------------------------------------- -->
															
    <section id="jugadores" class="section light">
      <div class="section-internal-container">
        <div class="section-content">
		  <div class="row">
			  <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<h2>Equipos</h2>
			  </div>
		  </div>
		  <div class="row">
			  	<#list teams>
					 	<ul class="group-list col-lg-12 col-md-12 col-xs-12" style="text-align:center;">
								<#items as team>
								<li class="list-item">  
									<h5>${team.name}</h5>
									<#if team.players?has_content>
										<div class="row">		
											<div class="col-lg-12 col-md-12 col-xs-12">
											${team.playersStr}		
											</div>
										</div>
									</#if>
								</li>
								</#items>
							</ul>
					</#list>
		  </div>
         </div>
      </div>
    </section>

    

 	<!-- CONTACTS ---------------------------------- -->

	<#if contacts?has_content>
	<section id="contacto" class="section dark">
      <div class="section-internal-container">
        <div class="section-content">
          <h2>Contacto</h2>
	         	 <#list contacts>
	          	  <div class="row">
					<ul class="list-group col-lg-12 col-md-12 col-xs-12" style="margin-top:2em;">	
			          <#items as contact>
			          <li class="list-item">
				          <div class="row">	
					          <div class="col-lg-12 col-md-12 col-xs-12">
					          <p>
					            ${contact.name!""}<br>
					            ${contact.contactMethod!""}<br>
					          </p>
					          </div>
				          </div>
			          </li>
		          	</#items>
		          </ul>
		          </div>
	          </#list>
        </div>
      </div>
    </section>
    </#if>
  
    
    <!-- ------------------- footer ---------------------------- -->

    <section class="section-footer" id="footer">
      <div
        class="footer"
        style="padding-top: 1em;
          padding-bottom: 1em;
          float: left;
          width: 100%;
          background: #03152b;
          color: white;">
          
          
	            
          
	        <div style="display: block; float: left;">
	          
	          <a class="link"
	            style="color: white"
	            title="Torneo"
	            href="./index.html">
	            	<span style="font-size: 0.8em; padding: 0 1em">Portada</span>
	            </a>
	          
	          <a class="link hidden-xs hidden-sm"
	            style="color: white"
	            title="Tabla"
	            href="#torneo"><span style="font-size: 0.8em; padding: 0 1em">Torneo</span></a>
	            
	            
	          <a class="link hidden-xs hidden-sm"
	            style="color: white"
	            title="Tabla"
	            href="#table"><span style="font-size: 0.8em; padding: 0 1em">Tabla</span></a>
	          
	          <a
	            class="link"
	            style="color: white"
	            title="Equipos"
	            href="./equipos.html"><span style="font-size: 0.8em; padding: 0 1em">Equipos</span></a>
	
	          <a
	            class="link hidden-xs hidden-sm"
	            style="color: white"
	            title="Fixture"
	            href="#fixture"><span style="font-size: 0.8em; padding: 0 1em">Fixture</span></a>
	          <a
	            class="link hidden-xs hidden-sm"
	            style="color: white"
	            title="Contacto"
	            href="#contact"><span style="font-size: 0.8em; padding: 0 1em">Contacto</span></a>
	        </div>
      
  		     <div style="display: block; float: right; font-size:0.7em;">
		      		<span style="color:#cccccc;"> ${dateexported}</span>
		      		<a href="#top" class="link" style="margin-left:2.5em; color: #cccccc;"><span> subir </span> <span>^</span></a>
		     </div>

      </div>
      
      
    </section>
    <!-- ------------------- footer ---------------------------- -->

  </body>
</html>