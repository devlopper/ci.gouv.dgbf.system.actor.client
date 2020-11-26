package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class MyAccountIndexPage extends AbstractLoggedInUserPage implements MyAccountTheme, Serializable {

	@Override
	protected String __getWindowTitleValue__() {
		return "Module de gestion de votre compte";
	}
	
	/**/
	
	public static Layout buildProfileLayout(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G);
		return Layout.build(arguments);
	}
	
	public static Layout buildProfileLayout(Object...objects) {
		return buildProfileLayout(MapHelper.instantiate(objects));
	}
}