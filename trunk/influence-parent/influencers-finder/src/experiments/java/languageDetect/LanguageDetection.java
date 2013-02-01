package languageDetect;

import java.io.File;
import java.net.URISyntaxException;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;


public class LanguageDetection {
	
	public static void main(String[] args) throws LangDetectException {
//		
//		Detector detector = DetectorFactory.create();
//		DetectorFactory.loadProfile("/home/godzy/Dropbox/universita/tesi/lib/langdetect-09-13-2011/profiles");
//		detector.append("che bello questo tweet mi piace proprio tanto");
//		detector.detect();
//	}
		
		
		//LanguageDetection detector = new LanguageDetection();
		
		//detector.init("/home/godzy/Dropbox/universita/tesi/lib/langdetect-09-13-2011/profiles");
		DetectorFactory.loadProfile("/home/godzy/Dropbox/universita/tesi/lib/langdetect-09-13-2011/profiles.sm");
		Detector detector = DetectorFactory.create();
		detector.append("che bello questo tweet mi piace proprio tanto");
		System.out.println(detector.detect());
	}
	

//	public void init(String profileDirectory) throws LangDetectException {
//		DetectorFactory.loadProfile(profileDirectory);
//	}
//	public String detect(String text) throws LangDetectException {
//		Detector detector = DetectorFactory.create();
//	    detector.append(text);
//	    return detector.detect();
//	}
//	public ArrayList<Language> detectLangs(String text) throws LangDetectException {
//	    Detector detector = DetectorFactory.create();
//	    detector.append(text);
//	    return detector.getProbabilities();
//	}

}
