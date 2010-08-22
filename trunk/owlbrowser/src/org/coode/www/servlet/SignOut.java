package org.coode.www.servlet;

import org.coode.html.OWLHTMLKit;
import org.coode.html.doclet.Doclet;
import org.coode.html.doclet.HTMLDoclet;
import org.coode.html.impl.OWLHTMLConstants;
import org.coode.html.impl.OWLHTMLParam;
import org.coode.html.page.HTMLPage;
import org.coode.html.page.OWLDocPage;
import org.coode.www.exception.OntServerException;
import org.coode.www.exception.RedirectException;
import org.coode.www.mngr.SessionManager;

import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;

/**
 * Author: Nick Drummond<br>
 * nick.drummond@cs.manchester.ac.uk<br>
 * http://www.cs.man.ac.uk/~drummond<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Jun 29, 2007<br><br>
 * <p/>
 * code made available under Mozilla Public License (http://www.mozilla.org/MPL/MPL-1.1.html)<br>
 * copyright 2006, The University of Manchester<br>
 */
public class SignOut extends AbstractOntologyServerServlet {

    @Override
    protected Doclet handleXMLRequest(Map<OWLHTMLParam, String> params, OWLHTMLKit kit, URL servletURL) throws OntServerException {
        final String confirm = params.get(OWLHTMLParam.confirm);
        final String result;
        if (confirm != null && Boolean.parseBoolean(confirm)){
            HttpSession session = getSession();
            SessionManager.closeSession(session);
            result = "<quit result=\"true\"/>";
        }
        else{
            result = "<quit result=\"false\"/>";
        }

        return new Doclet(){
            public void renderAll(URL pageURL, PrintWriter out) {
                out.println("<?xml version=\"1.0\" encoding=\"" + OWLHTMLConstants.DEFAULT_ENCODING + "\" ?>");
                out.println(result);
            }
        };
    }

    @Override
    protected HTMLPage handleHTMLPageRequest(Map<OWLHTMLParam, String> params, OWLHTMLKit kit, URL pageURL) throws OntServerException {
        final String confirm = params.get(OWLHTMLParam.confirm);

        if (confirm == null){
            OWLDocPage page = new OWLDocPage(kit);
            StringBuilder sb = new StringBuilder();
            sb.append("<p>This will clear all ontologies you are browsing.");
            sb.append(" All permalinks you have bookmarked or sent to friends will continue to work.</p>");
            sb.append("<p>Are you sure you wish to quit?</p>");
            sb.append("<p><a href='");
            sb.append(pageURL);
            sb.append("?confirm=true'>Yes</a>");
            sb.append(" <a href='");
            sb.append(pageURL);
            sb.append("?confirm=false'>No</a></p>");

            page.addMessage(sb.toString());

            return page;
        }
        else{
            final URL baseURL = kit.getBaseURL();
            if (Boolean.parseBoolean(confirm)){
                HttpSession session = getSession();
                SessionManager.closeSession(session);
            }
            throw new RedirectException(baseURL);
        }
    }

    @Override
    protected HTMLDoclet handleHTMLFragmentRequest(Map<OWLHTMLParam, String> params, OWLHTMLKit kit, URL pageURL) throws OntServerException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
