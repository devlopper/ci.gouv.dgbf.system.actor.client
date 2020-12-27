package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.cyk.utility.client.controller.web.jsf.FileServlet;

import ci.gouv.dgbf.system.actor.client.controller.api.RequestController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;

public class FileServletListenerImpl extends FileServlet.Listener.AbstractImpl implements Serializable {
	
	@Override
	protected byte[] getBytes(HttpServletRequest httpServletRequest,String classSimpleName, String instanceIdentifier, String type) {
		if(Request.class.getSimpleName().equals(classSimpleName)) {
			if(REQUEST_FILE_TYPE_PHOTO.equals(type))
				return __inject__(RequestController.class).getPhotoByIdentifier(instanceIdentifier);
			if(REQUEST_FILE_TYPE_ACT_OF_APPOINTMENT.equals(type))
				return __inject__(RequestController.class).getActOfAppointmentByIdentifier(instanceIdentifier);
			if(REQUEST_FILE_TYPE_SIGNATURE.equals(type))
				return __inject__(RequestController.class).getSignatureByIdentifier(instanceIdentifier);
			if(REQUEST_FILE_TYPE_SIGNED_REQUEST_SHEET.equals(type))
				return __inject__(RequestController.class).getSignedRequestSheetByIdentifier(instanceIdentifier);
		}
		return super.getBytes(httpServletRequest, classSimpleName, instanceIdentifier, type);
	}
	
	/**/
	
	public static final String REQUEST_FILE_TYPE_PHOTO = "photo";
	public static final String REQUEST_FILE_TYPE_ACT_OF_APPOINTMENT = "actofappointment";
	public static final String REQUEST_FILE_TYPE_SIGNATURE = "signature";
	public static final String REQUEST_FILE_TYPE_SIGNED_REQUEST_SHEET = "signedrequestsheet";
}