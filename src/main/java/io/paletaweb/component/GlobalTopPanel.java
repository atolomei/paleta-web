package io.paletaweb.component;

import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import io.wktui.nav.menu.DropDownMenu;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavBar;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.menu.SeparatorMenuItem;
import wktui.base.BasePanel;
import wktui.base.LabelPanel;
import wktui.base.ModelPanel;


public class GlobalTopPanel<T> extends ModelPanel<T> {

	private static final long serialVersionUID = 1L;

	public GlobalTopPanel(String id) {
		this(id, null);
	}
	
	public GlobalTopPanel(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		NavBar<Void> nav = new NavBar<Void>("navbar");
		
		
		LabelPanel logo = new LabelPanel("item", new Model<String>("[   P  ]"));
		
		nav.addNoCollapseLeft(logo);
		
		nav.addCollapse(getTorneoMenu());
		nav.addCollapse(getClubesMenu());
		nav.addCollapse(getAdminMenu());
		nav.addCollapse(getAcercaMenu());
		
		
		
		
		add(nav);
		
		
	}
	

	/**
	 * 
	 * 
	 * @return
	 */
	private NavDropDownMenu<Void> getTorneoMenu() {
		
		NavDropDownMenu<Void> menu = new NavDropDownMenu<Void>("item", null, new Model<String>("Torneo Metropolitano"));

		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {

					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/torneo/info"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Información general");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});


		
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {

					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/torneo/fixture"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Fixture");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});
		

		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {

					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/torneo/tabla"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Tabla de posiciones");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Void> getItem(String id) {
				return new  SeparatorMenuItem(id);
			}
		});
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {

					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/torneo/reglamento"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Reglamento");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});

		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {

					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/torneo/inscripcion"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Inscripcion");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});


		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Void> getItem(String id) {
				return new  SeparatorMenuItem(id);
			}
		});

		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {

					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/clubes"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Equipos");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {

					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/clubes"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Jueces");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});


		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Void> getItem(String id) {
				return new  SeparatorMenuItem(id);
			}
		});

		

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {

					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/clubes"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Carga de Formulario de Juego");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});

		
		return menu;
	}
	
	
 


		private  NavDropDownMenu<Void> getClubesMenu() {
			
			NavDropDownMenu<Void> menu = new NavDropDownMenu<Void>("item", null, new Model<String>("Clubes y Jugadores"));
			
			
			
			menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<Void> getItem(String id) {

					return new  LinkMenuItem<Void>(id) {

						private static final long serialVersionUID = 1L;
						
						@Override
						public void onClick() {
							setResponsePage(new RedirectPage("/clubes"));
						}

						@Override
						public IModel<String> getLabel() {
							return new Model<String>("Clubes");
						}

						@Override
						public String getBeforeClick() {
							return null;
						}
					};
				}
			});

			
			menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<Void> getItem(String id) {

					return new  LinkMenuItem<Void>(id) {

						private static final long serialVersionUID = 1L;
						
						@Override
						public void onClick() {
							setResponsePage(new RedirectPage("/clubes"));
						}

						@Override
						public IModel<String> getLabel() {
							return new Model<String>("Jugadores");
						}

						@Override
						public String getBeforeClick() {
							return null;
						}
					};
				}
			});


			menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<Void> getItem(String id) {

					return new  LinkMenuItem<Void>(id) {

						private static final long serialVersionUID = 1L;
						
						@Override
						public void onClick() {
							setResponsePage(new RedirectPage("http://google.com"));
						}

						@Override
						public IModel<String> getLabel() {
							return new Model<String>("Jueces");
						}

						@Override
						public String getBeforeClick() {
							return null;
						}
					};
				}
			});

			
		return menu;
	}
	
	
	
	private  NavDropDownMenu<Void> getAdminMenu() {
		NavDropDownMenu<Void> menu = new NavDropDownMenu<Void>("item", null, new Model<String>("Administración"));


		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Void> getItem(String id) {
				return new  LinkMenuItem<Void>(id) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/categorias"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Categorías");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});

		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Void> getItem(String id) {
				return new  LinkMenuItem<Void>(id) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/clubes"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Clubes");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});

		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Void> getItem(String id) {
				return new  LinkMenuItem<Void>(id) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/clubes"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Localidades");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});

		

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Void> getItem(String id) {
				return new  LinkMenuItem<Void>(id) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/clubes"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Personas");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});


		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Void> getItem(String id) {
				return new  LinkMenuItem<Void>(id) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/clubes"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Jugadores");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});



		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Void> getItem(String id) {
				return new  LinkMenuItem<Void>(id) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/clubes"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Jueces");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Void> getItem(String id) {
				return new  LinkMenuItem<Void>(id) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/torneos"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Torneos");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});


		
		return menu;
	}
	
	private  NavDropDownMenu<Void> getAcercaMenu() {

		NavDropDownMenu<Void> menu = new NavDropDownMenu<Void>("item", null, new Model<String>("Acerca"));
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {

					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/about"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Acerca de Paleta");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});


		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {

					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/contacto"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Contáctenos");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});

		
		return menu;
	}
	
	
	
	

}
