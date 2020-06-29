package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import ci.gouv.dgbf.system.actor.client.controller.entities.ProfilePrivilege;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfilePrivilegeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ProfilePrivilegeListPage extends AbstractEntityListPageContainerManagedImpl<ProfilePrivilege> implements Serializable {

	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = instantiateDataTable();
		@SuppressWarnings("unchecked")
		LazyDataModel<ProfilePrivilege> lazyDataModel = (LazyDataModel<ProfilePrivilege>) dataTable.getValue();
		lazyDataModel.setReadQueryIdentifier(ProfilePrivilegeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_PROFILE_CODE_ASCENDING_BY_PRIVILEGE_CODE_ASCENDING);
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des affectations";
	}
	
	/**/
	
	public static TreeNode instantiateTreeNode(Collection<ProfilePrivilege> profilePrivileges) {
		if(CollectionHelper.isEmpty(profilePrivileges))
			return null;
		TreeNode root = new DefaultTreeNode();		
		//find roots
		Collection<ProfilePrivilege> roots = profilePrivileges.stream().filter(profilePrivilege -> StringHelper.isBlank(profilePrivilege.getPrivilege().getParentIdentifier())).collect(Collectors.toList());
		if(CollectionHelper.isNotEmpty(roots)) {
			roots.forEach(profilePrivilege -> {
				TreeNode node = new DefaultTreeNode(profilePrivilege, root);
				instantiateChildren(node, profilePrivileges);
			});
		}
		return root;
	}
	
	private static void instantiateChildren(TreeNode root,Collection<ProfilePrivilege> profilePrivileges) {
		Collection<ProfilePrivilege> children = profilePrivileges.stream().filter(profilePrivilege -> ((ProfilePrivilege)root.getData()).getPrivilege().getIdentifier().equals(profilePrivilege.getPrivilege().getParentIdentifier()))
				.collect(Collectors.toList());
		if(CollectionHelper.isNotEmpty(children)) {
			children.forEach(child -> {
				TreeNode node = new DefaultTreeNode(child, root);
				instantiateChildren(node, profilePrivileges);
			});
		}
	}
	
	/**/
	
	@SuppressWarnings("unchecked")
	public static DataTable instantiateDataTable(Collection<String> columnsFieldsNames,DataTableListenerImpl listener,LazyDataModelListenerImpl lazyDataModelListener) {
		if(listener == null)
			listener = new DataTableListenerImpl();
		if(columnsFieldsNames == null) {
			columnsFieldsNames = CollectionHelper.listOf(ProfilePrivilege.FIELD_PROFILE,ProfilePrivilege.FIELD_PRIVILEGE);
		}
		
		DataTable dataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,ProfilePrivilege.class
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,columnsFieldsNames,DataTable.FIELD_LISTENER,listener/*,DataTable.FIELD_STICKY_HEADER,Boolean.TRUE*/);
		dataTable.getOrderNumberColumn().setWidth("20");
		
		LazyDataModel<ProfilePrivilege> lazyDataModel = (LazyDataModel<ProfilePrivilege>) dataTable.getValue();
		lazyDataModel.setReaderUsable(Boolean.TRUE);
		if(lazyDataModelListener == null) {
			lazyDataModelListener = new LazyDataModelListenerImpl();
			lazyDataModel.setReadQueryIdentifier(ProfilePrivilegeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_PROFILE_CODE_ASCENDING_BY_PRIVILEGE_CODE_ASCENDING);
		}
		lazyDataModel.setListener(lazyDataModelListener);
		return dataTable;
	}
	
	public static DataTable instantiateDataTable() {
		return instantiateDataTable(null, null, null);
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {

		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
			if(ProfilePrivilege.FIELD_PROFILE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Profile");
			}else if(ProfilePrivilege.FIELD_PRIVILEGE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Privilège");
			}else if(ProfilePrivilege.FIELD_VISIBLE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Visible ?");
				map.put(Column.FIELD_WIDTH, "80");
			}
			return map;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<ProfilePrivilege> implements Serializable {
			
	}
}