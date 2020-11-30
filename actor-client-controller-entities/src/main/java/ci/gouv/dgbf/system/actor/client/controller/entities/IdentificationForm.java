package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class IdentificationForm extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Collection<IdentificationAttribute> attributs;
	
	/**/
	
	public static Map<String,IdentificationAttribute> computeFieldsNames(IdentificationForm form,Class<?> klass) {
		if(form == null || CollectionHelper.isEmpty(form.getAttributs()) || CollectionHelper.isEmpty(
				ci.gouv.dgbf.system.actor.server.persistence.entities.IdentificationAttribute.CODES_FIELDS_NAMES) || klass == null)
			return null;
		Map<String,IdentificationAttribute> fieldsNames = new LinkedHashMap<>();
		form.getAttributs().forEach(attribut -> {
			for(String codeFieldName : ci.gouv.dgbf.system.actor.server.persistence.entities.IdentificationAttribute.CODES_FIELDS_NAMES) {
				String code = (String) FieldHelper.readStatic(ci.gouv.dgbf.system.actor.server.persistence.entities.IdentificationAttribute.class,codeFieldName);
				if(code.equals(attribut.getCode()))
					fieldsNames.put((String) FieldHelper.readStatic(klass,"FIELD_"+StringUtils.substringAfter(codeFieldName, "CODE_")),attribut);
			}		
		});
		return fieldsNames;
	}
}