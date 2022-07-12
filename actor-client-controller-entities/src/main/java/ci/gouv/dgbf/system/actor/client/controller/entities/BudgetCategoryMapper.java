package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.BudgetCategoryDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class BudgetCategoryMapper extends AbstractMapperSourceDestinationImpl<BudgetCategory, BudgetCategoryDto> {
	private static final long serialVersionUID = 1L;
    	
}