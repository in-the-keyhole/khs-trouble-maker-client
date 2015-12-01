# khs-trouble-maker-client 

Servlet that is registered with clients of the [Trouble Maker](https://github.com/in-the-keyhole/khs-trouble-maker) framework used to randomly produce server trouble issues.

Installation
------------

Register this Java Servlet in your web.xml or config


	<servlet>
		 <servlet-name>trouble</servlet-name>
		  <servlet-class>khs.trouble.servlet</servlet-class>
		     <init-param>
	            <param-name>token</param-name>
	            <!-- token should match dashboard token -->
	            <param-value>abc123</param-value>
	        </init-param>
		</servlet>
		<servlet-mapping>
		    <servlet-name>trouble</servlet-name>
		    <url-pattern>/trouble/*</url-pattern>
	</servlet-mapping>
	
	
	
	
