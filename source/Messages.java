import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import Graphic.ScrollPanel;

@SuppressWarnings ( "serial" )
public class Messages extends ScrollPanel {

	public Messages () {
		// Create the message panel that is scrollable and set the default properties
        super ( 500, 420, BoxLayout.Y_AXIS );
        super.setPosition ( 0, 80 );
        super.getContentPanel ().setBorder (
        	BorderFactory.createEmptyBorder ( 0, 0, 15, 0 )
        );
	}

}