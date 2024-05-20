package io.paletaweb.page.torneo;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wicketstuff.annotation.mount.MountPath;

import io.paleta.model.TablaPosicion;

import io.paletaweb.component.GlobalFooterPanel;
import io.paletaweb.component.GlobalTopPanel;
import io.paletaweb.component.MainHeaderPanel;
import io.paletaweb.page.BasePage;



/**
 * Club 
 * Localidad
 * Club
 * Categoria
 * Persona
 * Jugador
 * 
 */
@MountPath("tabla")
public class PaletaWebTablaPage extends BasePage {
	
	private static final long serialVersionUID = 1L;

	public class PosicionesProvider extends SortableDataProvider<TablaPosicion, Integer> {
						
		private static final long serialVersionUID = 1L;
		
		public Iterator<TablaPosicion> iterator(long first, long count) {
			ArrayList<TablaPosicion> iteration = new ArrayList<TablaPosicion>();
			Iterator<TablaPosicion> iterator =getPosiciones().listIterator((int)first);
			int i = 0;
			while (i++<count) {
				iteration.add(iterator.next());
			}
			return iteration.iterator();
		}	
		
		public IModel<TablaPosicion> model(TablaPosicion object) {
			return new Model<TablaPosicion>(object);
		}
		public long size() {
			return getPosiciones().size();
		}
	}

	
	
	public PaletaWebTablaPage() {
	}
	
	
	public List<TablaPosicion> getPosiciones() {
		
		List<TablaPosicion> list = new ArrayList<TablaPosicion>();
		
		TablaPosicion e1 = new TablaPosicion();
		e1.equipo="CUBA";
		e1.tantosFavor=233;
		e1.tantosContra=122;
		e1.difTantos=e1.tantosFavor-e1.tantosContra;
		e1.partidosJugados=7;
		e1.puntos=14;
		
		TablaPosicion e2 = new TablaPosicion();
		e2.equipo="Belgrano Social";
		e2.tantosFavor=211;
		e2.tantosContra=145;
		e2.difTantos=e2.tantosFavor-e2.tantosContra;
		e2.partidosJugados=7;
		e2.puntos=12;
		
		TablaPosicion e3 = new TablaPosicion();
		e3.equipo="Chicago B";
		e3.tantosFavor=162;
		e3.tantosContra=165;
		e3.difTantos=e2.tantosFavor-e3.tantosContra;
		e3.partidosJugados=7;
		e3.puntos=10;
		
		TablaPosicion e4 = new TablaPosicion();
		e4.equipo="Gure Echea";
		e4.tantosFavor=142;
		e4.tantosContra=189;
		e4.difTantos=e2.tantosFavor-e3.tantosContra;
		e4.partidosJugados=7;
		e4.puntos=6;
		
		TablaPosicion e5 = new TablaPosicion();
		e5.equipo="Navarro";
		e5.tantosFavor=142;
		e5.tantosContra=189;
		e5.difTantos=e5.tantosFavor-e5.tantosContra;
		e5.partidosJugados=7;
		e5.puntos=4;
		
		
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		list.add(e5);
		
		return list;
	}


	@Override
	public void onInitialize() {
		super.onInitialize();
		
		add(new GlobalTopPanel<Void>("top-panel"));
		add(new MainHeaderPanel<Void>("main-header-panel", null, new Model<String>("Clubes")));
		add(new GlobalFooterPanel<Void>("footer-panel"));
		
		// ServiceLocator.getInstance().getApplicationContext().getBean();

		DataTable<TablaPosicion, Integer> table = new DataTable<TablaPosicion, Integer>("posiciones", getColumns(), new PosicionesProvider(), 40);
		table.add(new AttributeModifier("class", "table table-bordered table-hover"));
		table.getBody().add(new AttributeModifier("class", "body"));
		
		
		table.addTopToolbar(new AjaxFallbackHeadersToolbar<Integer>(table, (PosicionesProvider) table.getDataProvider()));
		
		
		add(table);
		
	}

	
	
	protected List<IColumn<TablaPosicion, Integer>> getColumns() {
		
		 List<IColumn<TablaPosicion, Integer>> columns = new ArrayList<>();

		 columns.add(new PropertyColumn<TablaPosicion, Integer>(new Model<String>("Equipo"), "equipo"));
		 columns.add(new PropertyColumn<TablaPosicion, Integer>(new Model<String>("Puntos"), "puntos"));
		 
		 columns.add(new PropertyColumn<TablaPosicion, Integer>(new Model<String>("Tantos a Favor"), "tantosFavor"));
		 columns.add(new PropertyColumn<TablaPosicion, Integer>(new Model<String>("Tantos en Contra"), "tantosContra"));
		 columns.add(new PropertyColumn<TablaPosicion, Integer>(new Model<String>("Dif tantos"), "difTantos"));
		 
		 
		 columns.add(new PropertyColumn<TablaPosicion, Integer>(new Model<String>("Jugados"), "partidosJugados"));
		 columns.add(new PropertyColumn<TablaPosicion, Integer>(new Model<String>("Ganados"), "partidosGanados"));
		 columns.add(new PropertyColumn<TablaPosicion, Integer>(new Model<String>("Perdidos"), "partidosPerdidos"));
		 

		 
		 columns.add(new PropertyColumn<TablaPosicion, Integer>(new Model<String>("Set Ganados"), "partidosJugados"));
		 columns.add(new PropertyColumn<TablaPosicion, Integer>(new Model<String>("Set Perdidos"), "partidosGanados"));
		 columns.add(new PropertyColumn<TablaPosicion, Integer>(new Model<String>("Dif Sets"), "partidosPerdidos"));

		 
		 columns.add(new PropertyColumn<TablaPosicion, Integer>(new Model<String>("Desc."), "partidosGanados"));
		 columns.add(new PropertyColumn<TablaPosicion, Integer>(new Model<String>("Reprog."), "partidosPerdidos"));

		 
		 return columns;
		
	}

	
}
