#
# *1      *2      *3     *4     *5         *6                   7          8         9        10
# id,     dia,  hora,  Zona, Local, Visitante, resultado (L, V, E), set1 L-V, set2 L-V, set3 L-V 
# ----------------------------------------------------------------------------------------------
#
#

$clasi

<#list schedule.matchesClasificacion>
<#items as match>
<#if match.daybreak==1>
				  		
</#if>
${match?index+1}, ${match.matchDateStr!"error"}, ${match.matchHourStr!"error"}:${match.matchMinStr!"error"}, <#if match.tournamentGroup?has_content>${match.tournamentGroup.name!"error"}<#else>group missing</#if>, ${match.local.name!"error"}, ${match.visitor.name!"error"}<#if match.result?has_content>, ${match.resultCode}, ${match.setStr}</#if>
</#items>
</#list>

# $semi
# 12/12, 19:00, semi, CUBA A, Ferro B   
# 12/12, 19:30, semi, Gure Echea, GEBA   

# $final
# 12/12, 19:00, final, CUBA A, Ferro B   

# Created: ${dateexported}

