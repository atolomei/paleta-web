		
		<div class="row">
			  <div class="col-lg-12 col-md-12 col-xs-12">
			  <h4>${group}</h4>
			  </div>
	 	</div>
	
	<div class="row">
		  
	  <div class="col-lg-12 col-md-12 col-xs-12">
			  <#list tablepositions>
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