package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.__kernel__.string.StringHelper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Privilege extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private PrivilegeType type;
	private String parentIdentifier;
	private Collection<Privilege> children;
	
	public Privilege collectChildren(Collection<Privilege> privileges) {
		if(CollectionHelper.isEmpty(privileges))
			return this;
		if(StringHelper.isNotBlank(identifier))
			children = privileges.stream().filter(privilege -> identifier.equals(privilege.getParentIdentifier())).collect(Collectors.toList());
		return this;
	}
	
	public Boolean isSelectable(Collection<Privilege> selected) {
		if(CollectionHelper.isEmpty(selected))
			return Boolean.TRUE;
		if(!selected.contains(this))
			return Boolean.TRUE;
		if(CollectionHelper.isEmpty(children))
			return Boolean.FALSE;
		for(Privilege child : children)
			if(Boolean.TRUE.equals(child.isSelectable(selected)))
				return Boolean.TRUE;
		return Boolean.FALSE;
	}
	
	/**/
	
	public static void processCollectChildren(Collection<Privilege> privileges) {
		if(CollectionHelper.isEmpty(privileges))
			return;
		privileges.forEach(privilege -> {
			privilege.collectChildren(privileges);
		});
	}
}