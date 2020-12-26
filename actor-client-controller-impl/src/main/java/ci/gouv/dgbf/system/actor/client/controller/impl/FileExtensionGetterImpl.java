package ci.gouv.dgbf.system.actor.client.controller.impl;
/*
import java.io.ByteArrayInputStream;
import java.io.IOException;
*/import java.io.Serializable;
/*
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
*/import org.cyk.utility.__kernel__.file.FileExtensionGetter;
//import org.cyk.utility.__kernel__.log.LogHelper;

@ci.gouv.dgbf.system.actor.server.annotation.System
public class FileExtensionGetterImpl extends FileExtensionGetter.AbstractImpl implements Serializable {
/*
	@Override
	protected String getFromBytes(byte[] bytes) {
		Detector detector = new DefaultDetector();	
		Metadata metadata = new Metadata();
	    MediaType mediaType;
		try {
			mediaType = detector.detect(new ByteArrayInputStream(bytes), metadata);
		} catch (IOException exception) {
			LogHelper.log(exception, getClass());
			return null;
		}
	    return mediaType.getSubtype();
	}
*/	
}