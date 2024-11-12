
		  
		  <#list groups>
		  	  <#items as group>
				  <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
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
		    			<p>No hay equipos en la zona</p>
					</#list>
		    	</div>		
		    	</#items>
		</#list>
		
		
		
		
		
		
		
		
		