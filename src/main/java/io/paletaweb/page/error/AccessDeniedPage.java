package io.paletaweb.page.error;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.http.WebResponse;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketAccessDeniedPage;

import io.paletaweb.page.BasePage;




@MountPath("problem")
@WicketAccessDeniedPage
public class AccessDeniedPage extends BasePage {

	
	private static final long serialVersionUID = 1L;


	protected void setHeaders(final WebResponse response)
	{
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	}
	
	
	public boolean isErrorPage()
	{
		return true;
	}

	
	public boolean isVersioned()
	{
		return false;
	}
	
}
