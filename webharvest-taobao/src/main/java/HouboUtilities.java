import org.webharvest.runtime.DynamicScopeContext;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by bobguo on 14-7-26.
 */
public class HouboUtilities {

    private DynamicScopeContext context;

    public HouboUtilities(DynamicScopeContext context) {
        this.context = context;
    }

    public String escapeUrl(Object s) {
        if(s!=null) {
            try {
                return URLEncoder.encode(s.toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                //
                return s.toString();
            }
        }
        else {
            return null;
        }
    }
}
