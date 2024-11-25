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
    <meta name="Keywords" content="${meta.keywords!"paleta, torneo"}" />
	<meta name="language" content="${meta.language!"Spanish"}" />

    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, user-scalable=yes"/>
    
    <meta name="robots" content="all" />

    <link rel="stylesheet" type="text/css" href="../../css/kbee.css" />
    <link rel="stylesheet" type="text/css" href="../../css/kbee-1000-1600.css" />

    <link rel="stylesheet" type="text/css" href="../../css/bootstrap-5.3.3-dist/css/bootstrap.css" />
    
    <script src="./js/csi.min.js"></script>

    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@800&display=swap" rel="stylesheet"/>

    <!-- ANALYTICS -->
    
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

      <!-- TOP TOOLBAR -------------------------------------------- -->

      <div id="banner" class="banner">
        <div class="canvas-container" style="background: transparent">
          <div class="text-container" style="float: right">
            <div class="text centered">
              <h2 class="main-banner">${banner!"Torneo Clausura Categoría B"}</h2>
            </div>
          </div>
        </div>
      </div>
    </section>

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
								 			<#noautoesc>
								 				${alert.text}
								 			</#noautoesc>								 			
								 		</#if>
								 	</div>
						 	</div>
					</div>
				</div>
			</div>
	</section>
	</#if>			 
	
	<!-- INFO -------------------------------------------------------- -->
				 
	<section id="torneo" class="section light">
						<div class="section-internal-container">
							<div class="section-content">
								
								<#if info?has_content>
								 			<#noautoesc>
								 				${info}
								 			</#noautoesc>								 			
							    </#if>
								
								<h2>El Torneo</h2>
								<p>Torneo Clausura Categoría B<br/>
								<a href="https://maps.app.goo.gl/MTw1qqN5H3UgHXPR8" target="_blank">CUBA Palermo</a>
								</p>
								<p>Se juega en Palermo la última semana de Noviembre y primera de Diciembre de 2024.
								</p>
								<h4>Clasificación</h4>
								<p>
								11 equipos en dos zonas de 5 y 6, cada equipo juega contra los otros de su zona.<br/>
								Partidos a 25 puntos, sin alargue.<br/>
								Clasifican 2 primeros de cada zona, desempate por diferencia de sets, diferencia de tantos, tantos a favor, sorteo.<br/>
								</p>
							</div>
						</div>
						
	</section>
		
    <!-- ZONAS -------------------------------------------------------- -->
    
    <section id="zonas" class="section dark">
        <div class="section-internal-container">
	        <div class="section-content">
			  <div class="row">
				  <div class="col-lg-12 col-md-12 col-xs-12">
				  <h2>Zonas</h2>
				  </div>
			  </div>
			  <div class="row">
						<#list groups>
					  	  	<#items as group>
								  <div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
									<h4>${group.name}</h4>
									<#list group.teams>
										<table class="table table-bordered table-hover table-responsive">
										  <thead class="table-dark">
											<tr>
											  <th scope="col">#</th>
											  <th scope="col">Equipo</th>
											</tr>
										  </thead>
											<tbody>
											<#items as team>
												<tr>
												  <th scope="row">${team?index+1}</th>
												  <td>${team.name}</td>              
												</tr>
											</#items>
											</tbody>
										</table>
									<#else>
						    			<p>No hay partidos en la zona</p>
									</#list>
						    		</div>		
					    	</#items>
						</#list>
			    </div>
			    
			    <div class="row">
			    	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-top:1em;">
			    		<p>
			    			<a href="equipos.html" title="Equipos">Equipos</a>
			    		</p>
			    	</div>
			    </div>
			    
	         </div>
      </div>
    </section>

    <!-- -------------------------------------------------------- -->
    
    
    
    
    
    <!-- -------------------------------------------------------- -->

	
	<section id="fixture" class="section light">
      <div class="section-internal-container">
        <div class="section-content">
          <div class="row">
			  <div class="col-lg-12 col-md-12 col-xs-12">
			  <h2>Fixture</h2>
			  </div>
		  </div>

			<div class="row">
			  <div class="col-lg-12 col-md-12 col-xs-12">
			  <h3 style="margin:0.5em 0;">Clasificación</h3>
			  </div>
		  </div>
		  
		  <#if rawSchedule=="yes">
			  <!-- Match x Group ------------------------------------------------ -->
			  <div class="row">
							<#list groups>
						  	  	<#items as group>
									  <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
										<h4>${group.name}</h4>
										<#list group.matches>
											<table class="table table-bordered table-hover table-responsive">
											  <thead class="table-dark">
												<tr>
												  <th scope="col">#</th>
						  						  <th scope="col" title="Equipo 1">Eq. 1</th>
												  <th scope="col" title="Equipo 2">Eq. 2</th>
												  <th scope="col" title="Resultado">Res.</th>
												</tr>
											  </thead>
												<tbody>
												<#items as match>
													<tr>
													  <th scope="row">${match?index+1}</th>
													  <td>${match.local.name!"error"}</td>              
													  <td>${match.visitor.name!"error"}</td>              
													  <td>
													  <#if match.result?has_content>
													  		${match.setStr}
													  </#if>
													  </td>              
													</tr>
												</#items>
												</tbody>
											</table>
										<#else>
							    			<p>No hay partidos en la zona</p>
										</#list>
							    		</div>		
						    	</#items>
							</#list>
				    </div>		  
		  </#if>
		  
		  <#if calendarSchedule=="yes">
		  <!-- ------------------------------------------------------------ -->
		  <div class="row">			
			<div class="col-lg-12 col-md-12 col-xs-12">
			<#list schedule.matchesClasificacion>
					<table class="table table-bordered table-hover">
					<thead class="table-dark">
						<tr>
						  <th scope="col">#</th>
						  <th scope="col">Día</th>
						  <th scope="col">Hora</th>
						  <th scope="col" title="Equipo 1">Eq. 1</th>
						  <th scope="col" title="Equipo 2">Eq. 2</th>
						  <th scope="col" title="Resultado">Res.</th>
						</tr>
					</thead>
					
					<tbody>
						<#items as match>
					  		<#if match.daybreak==1>
					  			<tr class="separator">
						  			<th class=separator" scope="row"></th>
						  			<td></td>              
						  			<td></td>              
						  			<td></td>              
						  			<td></td>              
						  			<td></td>              
								</tr>
							</#if>
																			
							<tr>
							  <th scope="row">${match?index+1}</th>
							  <td>${match.matchDateStr!"error"}</td>              
							  <td>${match.matchHourStr!"error"}:${match.matchMinStr!"error"}</td>              
							  <td>${match.local.name!"error"}</td>              
							  <td>${match.visitor.name!"error"}</td>              
							  <td>
							  <#if match.result?has_content>
							  		${match.setStr}
							  </#if>
							  </td>              
							</tr>
						</#items>
					</tbody>
				</table>
				</#list>
			</div>
		  </div>
		  </#if>
		  <!-- ------------------------------------------------------------ -->
		  
		  

  		 <div class="row">
			  <div class="col-lg-12 col-md-12 col-xs-12">
			  <h3 style="margin:0.5em 0;" id="semis">Semifinales</h3>
			  </div>
		  </div>
		  
		  
		    <div class="row">
			<div class="col-lg-12 col-md-12 col-xs-12">
					<#if schedule.matchesSemifinal??>
						<#list schedule.matchesSemifinal>
							<table class="table table-bordered table-hover">
								<thead class="table-dark">
									<tr>
									  <th scope="col">#</th>
									  <th scope="col">Día</th>
									  <th scope="col">Hora</th>
									  <th scope="col" title="Equipo 1">Eq. 1</th>
								  	  <th scope="col" title="Equipo 2">Eq. 2</th>
								  	  <th scope="col" title="Resultado">Res.</th>
									</tr>
								</thead>
								<tbody>
									<#items as match>
										<tr>
										  <th scope="row">${match?index+21}</th>
										  <td>${match.matchdate}</td>              
										  <td>${match.matchhour}</td>              
										  <td>${match.local.name}</td>              
										  <td>${match.visitor.name}</td>              
										  <td>
										  <#if match.result?has_content>
										  		${match.setStr}
										  </#if>
										  </td>              
										</tr>
									</#items>
								</tbody>
							</table>
						</#list>
					<#else>
							<table class="table table-bordered table-hover">
								<thead class="table-dark">
									<tr>
									  <th scope="col">#</th>
									  <th scope="col">Día</th>
									  <th scope="col">Hora</th>
									  <th scope="col" title="Equipo 1">Eq. 1</th>
								  	  <th scope="col" title="Equipo 2">Eq. 2</th>
								  	  <th scope="col" title="Resultado">Res.</th>
									</tr>
								</thead>
								<tbody>
									<tr>
									  <th scope="row">21</th>
									  <td>12/12</td>              
									  <td>19.00</td>              
									  <td>1ro Zona A</td>              
									  <td>2do Zona B</td>              
									  <td></td>              
									</tr>
									<tr>
									  <th scope="row">22</th>
									  <td>12/12</td>              
									  <td>19.30</td>              
									  <td>1ro Zona B</td>              
									  <td>2do Zona A</td>              
									  <td></td>              
									</tr>
								</tbody>
							</table>
					</#if>
				</div>
			</div>
					
  			<div class="row">
			  <div class="col-lg-12 col-md-12 col-xs-12">
			  <h3 style="margin:0.5em 0;" id="final">Final</h3>
			  </div>
		  </div>
		  
		    <div class="row">
			<div class="col-lg-12 col-md-12 col-xs-12">
					<table class="table table-bordered table-hover">
						<thead class="table-dark">
							<tr>
							  <th scope="col">#</th>
							  <th scope="col">Día</th>
							  <th scope="col">Hora</th>
							  <th scope="col" title="Equipo 1">Eq. 1</th>
						  	  <th scope="col" title="Equipo 2">Eq. 2</th>
						  	  <th scope="col" title="Resultado">Res.</th>
							</tr>
						</thead>
						
						<tbody>
							<tr>
							  <th scope="row">23</th>
							  <td>12/12</td>              
							  <td>20.30</td>      

							  <#if schedule.matchFinal??>			          
									<td>${schedule.matchFinal.local.name!"Gan. semi 1"}</td>              
									<td>${schedule.matchFinal.visitor.name!"Gan. semi 2"}</td>
									<td>							  	  
									  	<#if schedule.matchFinal.result?has_content>
											  		${schedule.matchFinal.setStr}
										</#if>
									</td>
								<#else>  
									<td>Gan. semi 1</td>              
								  	<td>Gan. semi 2</td>
								  	<td></td>   
								</#if>        
							</tr>
						</tbody>
					</table>
				</div>
			</div>

						
			<#if torneo.winner?has_content>						
				<div class="row">
				  <div class="col-lg-12 col-md-12 col-xs-12">
					  		<div class="alert alert-success" role="alert" style="margin:2em 0;">
						 		<p>Felicitaciones al equipo de <b>${torneo.winner.name}</b> <br/>campeón de la Copa Viamonte 2024.</p>
						 	</div>
				 </div>
			 </#if>
		  </div>
	    </div>
      </div>
    </section>

	<!-- TABLA -------------------------------------------------- -->

    <section id="tabla" class="section dark">

      	<div class="section-internal-container">
	        <div class="section-content">

				<div class="row">
					  <div class="col-lg-12 col-md-12 col-xs-12">
						  <h2>Tabla</h2>
					  </div>
				</div>

		<#list grouptables>
		  <#items as grouptable>
			 <div class="row">
			  <div class="col-lg-12 col-md-12 col-xs-12">
			  <h4>${grouptable.tournamentGroup.name}</h4>
			  </div>
	 	     </div>
	
			<div class="row">
				  
			  <div class="col-lg-12 col-md-12 col-xs-12">
					  <#list grouptable.table>
					  <table class="table table-responsive table-bordered table-hover">
								<thead class="table-dark">
									<tr>
									  <th scope="col">#</th>
									  <th scope="col">Equipo</th>
									  <th scope="col" class="center">Pts</th>							  
									  <th class="hidden-sm hidden-md hidden-lg" scope="col" title="Jugados | Ganados | Perdidos">J | G | P</th>							  
									  <th class="hidden-xs center" scope="col" title="Jugados">Jug</th>
									  <th class="hidden-xs center" scope="col" title="Gandos">Gan</th>
									  <th class="hidden-xs center" scope="col" title="Perdidos">Per</th>
									  
									  <th class="hidden-sm hidden-md hidden-lg" scope="col" title="Sets ganados | Sets Perdidos | Diferencia Sets">Sets ( F | C | Dif )</th>
									  <th class="hidden-xs center" scope="col" title="Sets ganados">SG</th>
									  <th class="hidden-xs center" scope="col" title="Sets perdidos">SP</th>
									  <th class="hidden-xs center" scope="col" title="Diferencia Sets">SDif</th>
		
		  							  <th class="hidden-sm hidden-md hidden-lg" scope="col" title="Tantos a favor | Tantos en contra | Diferencia tantos">Tantos ( F | C | Dif )</th>
									  <th class="hidden-xs center" scope="col" title="Tantos a favor">TF</th>
									  <th class="hidden-xs center" scope="col" title="Tantos en contra">TC</th>
									  <th class="hidden-xs center" scope="col" title="Diferencia tantos">TDif</th>
									</tr>
								</thead>
								
								<tbody>
								
								<#items as tablePosition>
								
									<tr>
									  <th scope="row">${tablePosition?index+1}</th>
									  
									  <td>${tablePosition.team.name}</td>
									                
									  <td class="integer">${tablePosition.puntos}</td>              
									  							  
									  <td class="hidden-sm hidden-md hidden-lg" scope="col">${tablePosition.partidosJugados} | ${tablePosition.partidosGanados} | ${tablePosition.partidosPerdidos} </td>
									  <td class="hidden-xs integer" scope="col">${tablePosition.partidosJugados}</td>              
									  <td class="hidden-xs integer" scope="col">${tablePosition.partidosGanados}</td>              
									  <td class="hidden-xs integer" scope="col">${tablePosition.partidosPerdidos}</td>              
		
									  <td class="hidden-sm hidden-md hidden-lg" scope="col">${tablePosition.setGanados} | ${tablePosition.setPerdidos} | ${tablePosition.difSets} </td>
									  <td class="hidden-xs integer" scope="col">${tablePosition.setGanados}</td>              
									  <td class="hidden-xs integer" scope="col">${tablePosition.setPerdidos}</td>              
									  <td class="hidden-xs integer" scope="col">${tablePosition.difSets}</td>              
		
									  <td class="hidden-sm hidden-md hidden-lg" scope="col">${tablePosition.tantosFavor} | ${tablePosition.tantosContra} | ${tablePosition.difTantos} </td>
									  <td class="hidden-xs integer" scope="col">${tablePosition.tantosFavor}</td>              
									  <td class="hidden-xs integer" scope="col">${tablePosition.tantosContra}</td>              
									  <td class="hidden-xs integer" scope="col">${tablePosition.difTantos}</td>              
									  
									</tr>
								</#items>
									
								</tbody>
							</table>
							
							<#else>
		    					<p>No hay equipos en la tabla</p>
							</#list>
							
					  </div>
				</div>			
				 
				 </#items>
				 
				</#list>
			</div>
		</div>
	</section>


    
   <!-- CONTACTS ---------------------------------- -->

	<#if contacts?has_content>
	<section id="contacto" class="section light">
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