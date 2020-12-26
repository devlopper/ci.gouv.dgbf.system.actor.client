package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cyk.utility.client.controller.web.FileServlet;

import ci.gouv.dgbf.system.actor.client.controller.api.RequestController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;

public class FileServletListenerWebImpl extends FileServlet.ListenerImpl implements Serializable {
	
	@Override
	protected byte[] getBytes(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,String classSimpleName, String instanceIdentifier, String type) {
		if(Request.class.getSimpleName().equals(classSimpleName)) {
			if(REQUEST_FILE_TYPE_PHOTO.equals(type))
				return __inject__(RequestController.class).getPhotoByIdentifier(instanceIdentifier);
			if(REQUEST_FILE_TYPE_SIGNATURE.equals(type))
				return __inject__(RequestController.class).getSignatureByIdentifier(instanceIdentifier);
		}
		return super.getBytes(httpServletRequest, httpServletResponse, classSimpleName, instanceIdentifier, type);
	}
	
	/**/
	
	public static final String REQUEST_FILE_TYPE_PHOTO = "photo";
	public static final String REQUEST_FILE_TYPE_SIGNATURE = "signature";
}