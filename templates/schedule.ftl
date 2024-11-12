<section id="schedule" class="section light">
      <div class="section-internal-container">
        <div class="section-content">
          <div class="row">
			  <div class="col-lg-12 col-md-12 col-xs-12">
			  <h2>Fixture</h2>
			  </div>
		  </div>

			<div class="row">
			  <div class="col-lg-12 col-md-12 col-xs-12">
			  <h4>Clasificación</h4>
			  </div>
		  </div>
		  
		  <div class="row">
			
			<!-- 1 -->
			<div class="col-lg-12 col-md-12 col-xs-12">
			
			<#list matches>
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
			  </div>
		  </div>
		  

  		<div class="row">
			  <div class="col-lg-12 col-md-12 col-xs-12">
			  <h4>Semifinales</h4>
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
							  <th scope="row">21</th>
							  <td>12/12/24</td>              
							  <td>19.00</td>              
							  <td>1ro Zona A</td>              
							  <td>2do Zona B</td>              
							  <td></td>              
							</tr>
							
							<tr>
							  <th scope="row">22</th>
							  <td>12/12/24</td>              
							  <td>19.30</td>              
							  <td>1ro Zona B</td>              
							  <td>2do Zona A</td>              
							  <td></td>              
							</tr>
						
						</tbody>
					</table>
				</div>
			</div>
					

  			<div class="row">
			  <div class="col-lg-12 col-md-12 col-xs-12">
			  <h4>Final</h4>
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
							  <td>12/12/24</td>              
							  <td>20.30</td>              
							  <td>Ganador Semi 1</td>              
							  <td>Ganador Semi 2</td>              
							  <td></td>              
							</tr>
						</tbody>
					</table>
				</div>
			</div>
        </div>
      </div>
    </section>
