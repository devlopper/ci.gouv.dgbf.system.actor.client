package ci.gouv.dgbf.system.actor.client.controller.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.test.arquillian.bootablejar.AbstractTest;
import org.cyk.utility.test.arquillian.bootablejar.ArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;

import ci.gouv.dgbf.system.actor.client.controller.entities.ExpenditureNature;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.server.representation.api.ScopeFunctionRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.entities.ScopeFunctionDto;

public class ControllerIT extends AbstractTest {

	@Deployment(name = "server",order = 1)
    public static Archive<?> buildServerArchive() {
		return new ArchiveBuilder().setMode(ArchiveBuilder.Mode.SERVER).setPersistenceEnabled(Boolean.TRUE).setWebConfigEnabled(Boolean.TRUE)
				.addClasses(ServerServletContextListener.class).build();
    }
	
    @Deployment(name = "client",order = 2)
    public static Archive<?> buildClientArchive() {
    	return new ArchiveBuilder().setMode(ArchiveBuilder.Mode.CLIENT).setWebConfigEnabled(Boolean.TRUE)
    			.addPackages("ci.gouv.dgbf.system.actor.client.controller.api").build();
    }
    
    /* Read */
    
    @Test @OperateOnDeployment("client") @InSequence(1)
	public void readMany_expenditureNature() {
    	Collection<ExpenditureNature> collection = EntityReader.getInstance().readMany(ExpenditureNature.class);		
		assertThat(collection).isNotEmpty();
		assertThat(collection.stream().map(x -> x.getCode()).collect(Collectors.toList())).containsExactlyInAnyOrder("1","2","3","4");
	}
    
    @Test @OperateOnDeployment("client") @InSequence(2)
	public void scopeFunctionController_computeAuthorizingOfficerHolderNameByBudgetSpecializationUnitIdentifierByLocalityIdentifier() {
    	String name = __inject__(ScopeFunctionController.class).computeCreditManagerHolderNameByAdministrativeUnitIdentifier("DTI");		
		assertThat(name).isEqualTo("Gestionnaire de crédits Direction des traitements informatiques");
	}
    
    @Test @OperateOnDeployment("client") @InSequence(3)
	public void scopeFunctionController_computeCreditManagerHolderNameByAdministrativeUnitIdentifier() {
    	String name = __inject__(ScopeFunctionController.class).computeAuthorizingOfficerHolderNameByBudgetSpecializationUnitIdentifierByLocalityIdentifier(
				"USB7d152f5a-3bcb-4ba3-a107-b680b6a230b2", "DIMBOKRO");		
		assertThat(name).isEqualTo("Ordonnateur secondaire du Programme Budget a Dimbokro");
	}
    
    @Test @OperateOnDeployment("client") @InSequence(4)
	public void scopeFunctionController_readByScopeIdentifierByFunctionIdentifier() {
    	Collection<ScopeFunction> collection = __inject__(ScopeFunctionController.class)
    			.readByScopeIdentifierByFunctionIdentifier("UA68a6d9e7-420a-4bd9-9c01-12cfdad33fb9", "GC");		
    	assertThat(collection).hasSize(1);
		assertThat(collection.iterator().next().getCode()).isEqualTo("G100762");
	}
    
    @Test @OperateOnDeployment("client") @InSequence(5)
	public void functionController_readHolders() {
    	assertThat(__inject__(FunctionController.class).readHolders().stream().map(Function::getCode).collect(Collectors.toList()))
		.containsExactly("GC","ORD","CF","CPT");
	}
    
    @Test @OperateOnDeployment("client") @InSequence(6)
	public void functionController_readHoldersAndAssistants() {
    	assertThat(__inject__(FunctionController.class).readHoldersAndAssistants().stream().map(Function::getCode).collect(Collectors.toList()))
		.containsExactly("GC","AGC","ORD","AORD","CF","ACF","CPT","ACPT");
	}
    
    /* Transactions */
    
    @Test @OperateOnDeployment("client") @InSequence(7)
	public void scopeFunctionController_createCreditManagerHolder() {
		ScopeFunctionRepresentation.getProxy().save(List.of(new ScopeFunctionDto().setScopeIdentifier("UA68a6d9e7-420a-4bd9-9c01-12cfdad33fb9").setFunctionIdentifier("GC")
				.setCodePrefix("G1")));
		
		Collection<ScopeFunction> collection = __inject__(ScopeFunctionController.class)
    			.readByScopeIdentifierByFunctionIdentifier("UA68a6d9e7-420a-4bd9-9c01-12cfdad33fb9", "GC");	
		assertThat(collection).hasSize(2);
		assertThat(collection.stream().map(x->x.getCode()).collect(Collectors.toList())).containsExactlyInAnyOrder("G100762","G100763");
		assertThat(collection.stream().map(x->x.getName()).collect(Collectors.toList())).containsExactlyInAnyOrder("Gestionnaire de crédits Direction des Affaires Financières - MBPE"
				,"Gestionnaire de crédits Direction des Affaires Financières - MBPE");
	}
    
    @Test @OperateOnDeployment("client") @InSequence(8)
	public void scopeFunctionController_createCreditManagerHolder_customName() {
		ScopeFunctionRepresentation.getProxy().save(List.of(new ScopeFunctionDto().setScopeIdentifier("UA68a6d9e7-420a-4bd9-9c01-12cfdad33fb9").setFunctionIdentifier("GC")
				.setCodePrefix("G1").setName("This is my name")));
		
		Collection<ScopeFunction> collection = __inject__(ScopeFunctionController.class)
    			.readByScopeIdentifierByFunctionIdentifier("UA68a6d9e7-420a-4bd9-9c01-12cfdad33fb9", "GC");	
		assertThat(collection).hasSize(3);
		assertThat(collection.stream().map(x->x.getCode()).collect(Collectors.toList())).containsExactlyInAnyOrder("G100762","G100763","G100764");
		assertThat(collection.stream().map(x->x.getName()).collect(Collectors.toList())).containsExactlyInAnyOrder("Gestionnaire de crédits Direction des Affaires Financières - MBPE"
				,"Gestionnaire de crédits Direction des Affaires Financières - MBPE","This is my name");
	}
    
    @Test @OperateOnDeployment("client") @InSequence(9)
	public void scopeFunctionController_createAuthorizingOfficerHolder() {
		ScopeFunctionRepresentation.getProxy().save(List.of(new ScopeFunctionDto().setScopeIdentifier("1").setFunctionIdentifier("ORD")
				.setLocalityIdentifier("DIMBOKRO").setCodePrefix("O3")));
		
		Collection<ScopeFunction> collection = __inject__(ScopeFunctionController.class)
    			.readByScopeIdentifierByFunctionIdentifier("1", "ORD");	
		assertThat(collection).hasSize(1);
		assertThat(collection.stream().map(x->x.getCode()).collect(Collectors.toList())).containsExactlyInAnyOrder("O300001");
		assertThat(collection.stream().map(x->x.getName()).collect(Collectors.toList())).containsExactlyInAnyOrder("Ordonnateur secondaire du Programme Budget a Dimbokro");
	}
    
    @Test @OperateOnDeployment("client") @InSequence(10)
	public void scopeFunctionController_createAuthorizingOfficerHolder_USB() {
		ScopeFunctionRepresentation.getProxy().save(List.of(new ScopeFunctionDto().setScopeIdentifier("USB7d152f5a-3bcb-4ba3-a107-b680b6a230b2").setFunctionIdentifier("ORD")
				.setLocalityIdentifier("BOUNA").setCodePrefix("O3")));
		
		Collection<ScopeFunction> collection = __inject__(ScopeFunctionController.class)
    			.readByScopeIdentifierByFunctionIdentifier("USB7d152f5a-3bcb-4ba3-a107-b680b6a230b2", "ORD");	
		assertThat(collection).hasSize(3);
		assertThat(collection.stream().map(x->x.getCode()).collect(Collectors.toList())).containsExactlyInAnyOrder("O402500","O502501","O300002");
		assertThat(collection.stream().map(x->x.getName()).collect(Collectors.toList())).containsExactlyInAnyOrder("Ordonnateur des dépenses centralisées de solde"
				,"Ordonnateur des crédits centralisés de patrimoine","Ordonnateur secondaire du Programme Budget a Bouna");
	}
    
    @Test @OperateOnDeployment("client") @InSequence(11)
	public void scopeFunctionController_createAuthorizingOfficerHolder_USB_customName() {
		ScopeFunctionRepresentation.getProxy().save(List.of(new ScopeFunctionDto().setScopeIdentifier("USB7d152f5a-3bcb-4ba3-a107-b680b6a230b2").setFunctionIdentifier("ORD")
				.setLocalityIdentifier("BOUNA").setCodePrefix("O3").setName("Ordonnateur secondaire du Programme Budget de la zone de Bouna")));
		
		Collection<ScopeFunction> collection = __inject__(ScopeFunctionController.class)
    			.readByScopeIdentifierByFunctionIdentifier("USB7d152f5a-3bcb-4ba3-a107-b680b6a230b2", "ORD");	
		assertThat(collection).hasSize(4);
		assertThat(collection.stream().map(x->x.getCode()).collect(Collectors.toList())).containsExactlyInAnyOrder("O402500","O502501","O300002","O300003");
		assertThat(collection.stream().map(x->x.getName()).collect(Collectors.toList())).containsExactlyInAnyOrder("Ordonnateur des dépenses centralisées de solde"
				,"Ordonnateur des crédits centralisés de patrimoine","Ordonnateur secondaire du Programme Budget a Bouna"
				,"Ordonnateur secondaire du Programme Budget de la zone de Bouna");
	}
}